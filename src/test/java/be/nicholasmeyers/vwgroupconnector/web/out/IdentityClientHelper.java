package be.nicholasmeyers.vwgroupconnector.web.out;

import be.nicholasmeyers.vwgroupconnector.resource.AuthorizationInfo;
import be.nicholasmeyers.vwgroupconnector.resource.ConsentInfo;
import be.nicholasmeyers.vwgroupconnector.resource.FinalAuthorizationInfo;
import be.nicholasmeyers.vwgroupconnector.resource.SsoLogin;
import be.nicholasmeyers.vwgroupconnector.resource.StartAuthorization;
import be.nicholasmeyers.vwgroupconnector.resource.SuccessInfo;
import be.nicholasmeyers.vwgroupconnector.resource.TokenInfo;

import static be.nicholasmeyers.vwgroupconnector.util.UserUtils.getEmail;
import static be.nicholasmeyers.vwgroupconnector.util.UserUtils.getPassword;

public class IdentityClientHelper {

    private static final String CLIENT = "7f045eee-7003-4379-9968-9355ed2adb06@apps_vw-dilab_com";
    private final IdentityClientImpl identityClient;

    public IdentityClientHelper(IdentityClientImpl identityClient) {
        this.identityClient = identityClient;
    }

    public StartAuthorization getStartAuthorization() {
        String redirectUri = "myskoda://redirect/login/";
        String scope = "openid profile address cars email birthdate badge mbb phone driversLicense nationalIdentifier dealers mileage profession vin";
        String responseType = "code id_token";
        return identityClient.getAuthorizationEndpoint(redirectUri, CLIENT, scope, responseType);
    }

    public AuthorizationInfo getAuthorizationInfo(StartAuthorization startAuthorization) {
        return identityClient.getAuthorizationInfo(CLIENT, startAuthorization);
    }

    public void postEmail(StartAuthorization startAuthorization, AuthorizationInfo authorizationInfo) {
        identityClient.postEmail(CLIENT, startAuthorization, authorizationInfo, getEmail());
    }

    public FinalAuthorizationInfo getFinalAuthorizationInfo(StartAuthorization startAuthorization, AuthorizationInfo authorizationInfo) {
        return identityClient.getFinalAuthorizationInfo(CLIENT, startAuthorization, authorizationInfo);
    }

    public SsoLogin postEmailPassword(StartAuthorization startAuthorization, AuthorizationInfo authorizationInfo, FinalAuthorizationInfo finalAuthorizationInfo) {
        return identityClient.postEmailPassword(CLIENT, startAuthorization, authorizationInfo, finalAuthorizationInfo, getEmail(), getPassword());
    }

    public ConsentInfo ssoLogin(StartAuthorization startAuthorization, AuthorizationInfo authorizationInfo, SsoLogin ssoLogin) {
        return identityClient.ssoLogin(CLIENT, startAuthorization, authorizationInfo, ssoLogin);
    }

    public SuccessInfo checkConsent(StartAuthorization startAuthorization, AuthorizationInfo authorizationInfo, SsoLogin ssoLogin, ConsentInfo consentInfo) {
        return identityClient.checkConsent(CLIENT, startAuthorization, authorizationInfo, consentInfo, "openid profile address cars email birthdate badge mbb phone driversLicense nationalIdentifier dealers mileage profession vin", ssoLogin.getUser());
    }

    public TokenInfo handleSuccess(StartAuthorization startAuthorization, AuthorizationInfo authorizationInfo, SsoLogin ssoLogin, SuccessInfo successInfo) {
        return identityClient.handleSuccess(CLIENT, startAuthorization, authorizationInfo, successInfo, "openid profile address cars email birthdate badge mbb phone driversLicense nationalIdentifier dealers mileage profession vin", ssoLogin.getUser());
    }
}
