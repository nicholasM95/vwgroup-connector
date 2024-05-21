package be.nicholasmeyers.vwgroupconnector.service;

import be.nicholasmeyers.vwgroupconnector.resource.*;
import be.nicholasmeyers.vwgroupconnector.web.out.IdentityClientImpl;
import be.nicholasmeyers.vwgroupconnector.web.out.TokenClientImpl;
import be.nicholasmeyers.vwgroupconnector.web.out.VWGClientImpl;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.JwtClaims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static be.nicholasmeyers.vwgroupconnector.utils.ClientUtils.getClientConfig;

public class ConnectorService {
    private static final Logger log = LoggerFactory.getLogger(ConnectorService.class);

    private final IdentityService identityService;
    private final TokenService tokenService;
    private final VWGService vwgService;
    private final Map<Client, Tokens> cache;

    public ConnectorService() {
        this.identityService = new IdentityClientImpl();
        this.tokenService = new TokenClientImpl();
        this.vwgService = new VWGClientImpl();
        this.cache = new HashMap<>();
    }

    public Tokens getTokens(Client client, String email, String password) {
        if (cache.containsKey(client) && areTokensFromCacheOk(cache.get(client))) {
            log.info("Get tokens from cache.");
            return cache.get(client);
        }
        log.info("Get new tokens from vw group.");
        Map<String, String> clientConfig = getClientConfig(client.name().toLowerCase());
        String redirectUri = "skodaconnect://oidc.login/";
        String clientId = clientConfig.get("client_id");
        String scope = clientConfig.get("scope");
        String responseType = clientConfig.get("token_types");

        StartAuthorization startAuthorization = identityService.getAuthorizationEndpoint(redirectUri, clientId, scope, responseType);
        AuthorizationInfo authorizationInfo = identityService.getAuthorizationInfo(clientId, startAuthorization);
        identityService.postEmail(clientId, startAuthorization, authorizationInfo, email);
        FinalAuthorizationInfo finalAuthorizationInfo = identityService.getFinalAuthorizationInfo(clientId, startAuthorization, authorizationInfo);
        SsoLogin ssoLogin = identityService.postEmailPassword(clientId, startAuthorization, authorizationInfo, finalAuthorizationInfo, email, password);
        ConsentInfo consentInfo = identityService.ssoLogin(clientId, startAuthorization, authorizationInfo, ssoLogin);
        SuccessInfo successInfo = identityService.checkConsent(clientId, startAuthorization, authorizationInfo, consentInfo, scope, ssoLogin.getUser());
        TokenInfo tokenInfo = identityService.handleSuccess(clientId, startAuthorization, authorizationInfo, successInfo, scope, ssoLogin.getUser());

        if (Client.VWG.equals(client)) {
            String idToken = tokenService.getTokens(tokenInfo).getIdToken();
            Tokens tokens = new Tokens(vwgService.getAccessToken(idToken), idToken);
            cache.put(client, tokens);
            return tokens;
        } else {
            Tokens tokens = tokenService.getTokens(tokenInfo);
            cache.put(client, tokens);
            return tokens;
        }
    }

    private boolean areTokensFromCacheOk(Tokens tokens) {
        try {
            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                    .setSkipAllValidators()
                    .setDisableRequireSignature()
                    .setSkipSignatureVerification()
                    .build();

            JwtClaims jwtClaims = jwtConsumer.processToClaims(tokens.getAccessToken());

            return jwtClaims.getExpirationTime().isAfter(NumericDate.now());

        } catch (InvalidJwtException | MalformedClaimException e) {
           return false;
        }
    }


}
