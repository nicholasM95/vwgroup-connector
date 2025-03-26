package be.nicholasmeyers.vwgroupconnector.web.out;

import be.nicholasmeyers.vwgroupconnector.exception.LoginException;
import be.nicholasmeyers.vwgroupconnector.resource.*;
import be.nicholasmeyers.vwgroupconnector.service.IdentityService;
import be.nicholasmeyers.vwgroupconnector.web.out.resource.OpenidConfigurationWebResponseResource;
import be.nicholasmeyers.vwgroupconnector.web.out.resource.PostEmailPasswordWebRequestResource;
import be.nicholasmeyers.vwgroupconnector.web.out.resource.PostEmailWebRequestResource;
import feign.Feign;
import feign.Request;
import feign.Response;
import feign.form.FormEncoder;
import feign.jackson.JacksonDecoder;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static be.nicholasmeyers.vwgroupconnector.utils.HeaderUtils.getCookieHeader;
import static be.nicholasmeyers.vwgroupconnector.utils.HeaderUtils.getLocationHeader;
import static be.nicholasmeyers.vwgroupconnector.utils.StringUtils.getHmacFromString;
import static be.nicholasmeyers.vwgroupconnector.utils.StringUtils.getCsrfFromString;
import static be.nicholasmeyers.vwgroupconnector.utils.StringUtils.getResponseBodyAsString;

public class IdentityClientImpl implements IdentityService {

    private final IdentityClient identityClient;

    public IdentityClientImpl() {
        Request.Options defaultOpts = new Request.Options();
        this.identityClient = Feign.builder()
                .encoder(new FormEncoder())
                .decoder(new JacksonDecoder())
                .options(new Request.Options(defaultOpts.connectTimeoutMillis(),
                        defaultOpts.connectTimeoutUnit(),
                        defaultOpts.readTimeout(),
                        defaultOpts.connectTimeoutUnit(),
                        false))
                .target(IdentityClient.class, "https://identity.vwgroup.io");
    }

    @Override
    public OpenidConfiguration getConfiguration() {
        OpenidConfigurationWebResponseResource response = identityClient.getConfiguration();
        return new OpenidConfiguration(response.getIssuer(), response.getAuthorizationEndpoint());
    }

    @Override
    public StartAuthorization getAuthorizationEndpoint(String redirectUri, String client, String scope, String responseType) {
        String codeVerifier = generateCodeVerifier();
        String codeChallenge = "";

        try {
            codeChallenge = generateCodeChallenge(codeVerifier);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        Map<String, String> query = new HashMap<>();
        query.put("redirect_uri", redirectUri);
        query.put("client_id", client);
        query.put("scope", scope);
        query.put("response_type", responseType);
        query.put("nonce", getNonce());
        query.put("code_challenge", codeChallenge);
        query.put("code_challenge_method", "s256");
        query.put("prompt", "login");

        Response response = identityClient.getAuthorizationEndpoint(query);
        response.close();
        String location = getLocationHeader(response.headers());
        String cookie = getCookieHeader(response.headers());
        cookie = cookie.substring(0, cookie.indexOf(";"));
        String relayState = location.substring(location.indexOf("?relayState") + 12);
        return new StartAuthorization(cookie, relayState, location, redirectUri, codeVerifier);
    }

    @Override
    public AuthorizationInfo getAuthorizationInfo(String client, StartAuthorization startAuthorization) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", startAuthorization.getCookie());

        Map<String, String> query = new HashMap<>();
        query.put("relayState", startAuthorization.getRelayState());

        Response response = identityClient.getAuthorizationPage(headers, query, client);
        String responseBody = getResponseBody(response.body());
        response.close();

        String cookie = getCookieHeader(response.headers());
        cookie = cookie.substring(0, cookie.indexOf(";"));
        String hmac = getHmacFromString(responseBody);
        String csrf = getCsrfFromString(responseBody);
        return new AuthorizationInfo(cookie, hmac, csrf);
    }

    @Override
    public void postEmail(String client, StartAuthorization startAuthorization, AuthorizationInfo authorizationInfo, String email) {
        PostEmailWebRequestResource resource = new PostEmailWebRequestResource();
        resource.setEmail(email);
        resource.setRelayState(startAuthorization.getRelayState());
        resource.setHmac(authorizationInfo.getHmac());
        resource.set_csrf(authorizationInfo.getCsrf());

        Map<String, String> headers = new HashMap<>();
        //headers.put("Cookie", authorizationInfo.getCookie());
        headers.put("Cookie", startAuthorization.getCookie() + "; " + authorizationInfo.getCookie());
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        //headers.put("User-Agent", "okhttp/3.14.9");
        //headers.put("Connection", "keep-alive");
        identityClient.postEmail(resource, headers, client);
    }

    @Override
    public FinalAuthorizationInfo getFinalAuthorizationInfo(String client, StartAuthorization startAuthorization, AuthorizationInfo authorizationInfo) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", startAuthorization.getCookie() + "; " + authorizationInfo.getCookie());

        Map<String, String> query = new HashMap<>();
        query.put("relayState", startAuthorization.getRelayState());

        Response response = identityClient.getFinalAuthenticationPage(headers, query, client);
        String responseBody = getResponseBody(response.body());
        response.close();
        if (response.status() == 303) {
            throw new LoginException("Failed to log in (can't get the final authorization info). Please log in manually to a browser to authorize and accept the terms and conditions.", startAuthorization.getLoginUrl());
        }

        String hmac = getHmacFromString(responseBody);
        String csrf = getCsrfFromString(responseBody);
        return new FinalAuthorizationInfo(hmac, csrf);
    }

    @Override
    public SsoLogin postEmailPassword(String client, StartAuthorization startAuthorization, AuthorizationInfo authorizationInfo, FinalAuthorizationInfo finalAuthorizationInfo, String email, String password) {
        PostEmailPasswordWebRequestResource resource = new PostEmailPasswordWebRequestResource();
        resource.setEmail(email);
        resource.setPassword(password);
        resource.setRelayState(startAuthorization.getRelayState());
        resource.setHmac(finalAuthorizationInfo.getHmac());
        resource.set_csrf(finalAuthorizationInfo.getCsrf());

        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", startAuthorization.getCookie() + "; " + authorizationInfo.getCookie());
        Response response = identityClient.postEmailPassword(resource, headers, client);
        response.close();
        if (response.status() == 303) {
            throw new LoginException("Failed to log in (can't get the final authorization info). Please log in manually to a browser to authorize and accept the terms and conditions.", startAuthorization.getLoginUrl());
        }

        String location = getLocationHeader(response.headers());
        String user = location.substring(location.indexOf("userId") + 7);
        user = user.substring(0, user.indexOf("&"));
        String hmac = location.substring(location.indexOf("HMAC") + 5);

        return new SsoLogin(user, hmac);
    }

    @Override
    public ConsentInfo ssoLogin(String client, StartAuthorization startAuthorization, AuthorizationInfo authorizationInfo, SsoLogin ssoLogin) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", startAuthorization.getCookie() + "; " + authorizationInfo.getCookie());

        Map<String, String> query = new HashMap<>();
        query.put("clientId", client);
        query.put("relayState", startAuthorization.getRelayState());
        query.put("userId", ssoLogin.getUser());
        query.put("HMAC", ssoLogin.getHmac());

        Response response = identityClient.ssoLogin(headers, query);
        response.close();
        if (response.status() == 400) {
            throw new LoginException("Failed to log in (can't get the final authorization info). Please log in manually to a browser to authorize and accept the terms and conditions.", startAuthorization.getLoginUrl());
        }

        String location = getLocationHeader(response.headers());
        String hmac = location.substring(location.indexOf("hmac") + 5);
        return new ConsentInfo(hmac);
    }

    @Override
    public SuccessInfo checkConsent(String client, StartAuthorization startAuthorization, AuthorizationInfo authorizationInfo, ConsentInfo consentInfo, String scope, String user) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", startAuthorization.getCookie() + "; " + authorizationInfo.getCookie());

        Map<String, String> query = new HashMap<>();
        query.put("scopes", scope);
        query.put("relayState", startAuthorization.getRelayState());
        query.put("callback", "https://identity.vwgroup.io/oidc/v1/oauth/client/callback");
        query.put("hmac", consentInfo.getHmac());

        Response response = identityClient.checkConsent(headers, query, user, client);
        response.close();
        String location = getLocationHeader(response.headers());
        String hmac = location.substring(location.indexOf("hmac") + 5);
        return new SuccessInfo(hmac);
    }

    @Override
    public TokenInfo handleSuccess(String client, StartAuthorization startAuthorization, AuthorizationInfo authorizationInfo, SuccessInfo successInfo, String scope, String user) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", startAuthorization.getCookie() + "; " + authorizationInfo.getCookie());

        Map<String, String> query = new HashMap<>();
        query.put("user_id", user);
        query.put("client_id", client);
        query.put("scopes", scope);
        query.put("consentedScopes", scope);
        query.put("relayState", startAuthorization.getRelayState());
        query.put("hmac", successInfo.getHmac());

        Response response = identityClient.handleSuccess(headers, query);
        response.close();

        String location = getLocationHeader(response.headers());

        String authCode = location.substring(location.indexOf("code") + 5);
        authCode = authCode.substring(0, authCode.indexOf("&token_type"));

        String idToken = location.substring(location.indexOf("id_token") + 9);

        return new TokenInfo(authCode, startAuthorization.getRedirectUrl(), startAuthorization.getCodeVerifier());
    }

    private String getNonce() {
        String dateTimeString = Long.toString(new Date().getTime());
        byte[] nonceByte = dateTimeString.getBytes();
        return Base64.encodeBase64String(nonceByte);
    }

    private String getResponseBody(Response.Body body) {
        try {
            return getResponseBodyAsString(body.asInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String generateCodeVerifier() {
        SecureRandom random = new SecureRandom();
        byte[] codeVerifier = new byte[32];
        random.nextBytes(codeVerifier);
        return base64UrlEncode(codeVerifier);
    }

    private static String generateCodeChallenge(String codeVerifier) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(codeVerifier.getBytes(StandardCharsets.UTF_8));
        return base64UrlEncode(hash);
    }

    private static String base64UrlEncode(byte[] input) {
        return java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(input);
    }
}
