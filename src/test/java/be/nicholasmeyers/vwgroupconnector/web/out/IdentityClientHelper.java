package be.nicholasmeyers.vwgroupconnector.web.out;

import be.nicholasmeyers.vwgroupconnector.resource.*;

public class IdentityClientHelper {

    private static final String CLIENT = "7f045eee-7003-4379-9968-9355ed2adb06@apps_vw-dilab_com";
    private static final String EMAIL = "";
    private static final String PASSWORD = "";
    private final IdentityClientImpl identityClient;

    public IdentityClientHelper(IdentityClientImpl identityClient) {
        this.identityClient = identityClient;
    }

    public StartAuthorization getStartAuthorization() {
        String redirectUri = "skodaconnect://oidc.login/";
        String scope = "openid profile address cars email birthdate badge mbb phone driversLicense dealers profession vin";
        String responseType = "code id_token";
        return identityClient.getAuthorizationEndpoint(redirectUri, CLIENT, scope, responseType);
    }

    public AuthorizationInfo getAuthorizationInfo(StartAuthorization startAuthorization) {
        return identityClient.getAuthorizationInfo(CLIENT, startAuthorization);
    }

    public void postEmail(StartAuthorization startAuthorization, AuthorizationInfo authorizationInfo) {
        identityClient.postEmail(CLIENT, startAuthorization, authorizationInfo, EMAIL);
    }

    public FinalAuthorizationInfo getFinalAuthorizationInfo(StartAuthorization startAuthorization, AuthorizationInfo authorizationInfo) {
        return identityClient.getFinalAuthorizationInfo(CLIENT, startAuthorization, authorizationInfo);
    }

    public SsoLogin postEmailPassword(StartAuthorization startAuthorization, AuthorizationInfo authorizationInfo, FinalAuthorizationInfo finalAuthorizationInfo) {
        return  identityClient.postEmailPassword(CLIENT, startAuthorization, authorizationInfo, finalAuthorizationInfo, EMAIL, PASSWORD);
    }

    public ConsentInfo ssoLogin(StartAuthorization startAuthorization, AuthorizationInfo authorizationInfo, SsoLogin ssoLogin) {
        return identityClient.ssoLogin(CLIENT, startAuthorization, authorizationInfo, ssoLogin);
    }

    public SuccessInfo checkConsent(StartAuthorization startAuthorization, AuthorizationInfo authorizationInfo, SsoLogin ssoLogin, ConsentInfo consentInfo) {
        return identityClient.checkConsent(CLIENT, startAuthorization, authorizationInfo, consentInfo, "openid profile address cars email birthdate badge mbb phone driversLicense dealers profession vin", ssoLogin.getUser());
    }

    public TokenInfo handleSuccess(StartAuthorization startAuthorization, AuthorizationInfo authorizationInfo, SsoLogin ssoLogin, SuccessInfo successInfo) {
        return identityClient.handleSuccess(CLIENT, startAuthorization, authorizationInfo, successInfo, "openid profile address cars email birthdate badge mbb phone driversLicense dealers profession vin", ssoLogin.getUser());
    }
}
