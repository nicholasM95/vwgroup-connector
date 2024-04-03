package be.nicholasmeyers.vwgroupconnector.service;

import be.nicholasmeyers.vwgroupconnector.resource.*;
import be.nicholasmeyers.vwgroupconnector.web.out.IdentityClientImpl;
import be.nicholasmeyers.vwgroupconnector.web.out.TokenClientImpl;
import be.nicholasmeyers.vwgroupconnector.web.out.VWGClientImpl;

import java.util.Map;

import static be.nicholasmeyers.vwgroupconnector.utils.ClientUtils.getClientConfig;

public class ConnectorService {
    private final IdentityService identityService;
    private final TokenService tokenService;
    private final VWGService vwgService;

    public ConnectorService() {
        this.identityService = new IdentityClientImpl();
        this.tokenService = new TokenClientImpl();
        this.vwgService = new VWGClientImpl();
    }

    public Tokens getTokens(Client client, String email, String password) {
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
            return new Tokens(vwgService.getAccessToken(idToken), idToken);
        } else {
            return tokenService.getTokens(tokenInfo);
        }
    }


}
