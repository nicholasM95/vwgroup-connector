package be.nicholasmeyers.vwgroupconnector.service;

import be.nicholasmeyers.vwgroupconnector.resource.*;

public interface IdentityService {

    OpenidConfiguration getConfiguration();

    StartAuthorization getAuthorizationEndpoint(String redirectUri, String client, String scope, String responseType);

    AuthorizationInfo getAuthorizationInfo(String client, StartAuthorization startAuthorization);

    void postEmail(String client, StartAuthorization startAuthorization, AuthorizationInfo authorizationInfo, String email);

    FinalAuthorizationInfo getFinalAuthorizationInfo(String client, StartAuthorization startAuthorization, AuthorizationInfo authorizationInfo);

    SsoLogin postEmailPassword(String client, StartAuthorization startAuthorization, AuthorizationInfo authorizationInfo, FinalAuthorizationInfo finalAuthorizationInfo, String email, String password);

    ConsentInfo ssoLogin(String client, StartAuthorization startAuthorization, AuthorizationInfo authorizationInfo, SsoLogin ssoLogin);

    SuccessInfo checkConsent(String client, StartAuthorization startAuthorization, AuthorizationInfo authorizationInfo, ConsentInfo consentInfo, String scope, String user);

    TokenInfo handleSuccess(String client, StartAuthorization startAuthorization, AuthorizationInfo authorizationInfo, SuccessInfo successInfo, String scope, String user);
}
