package be.nicholasmeyers.vwgroupconnector.web.out;

import be.nicholasmeyers.vwgroupconnector.resource.AuthorizationInfo;
import be.nicholasmeyers.vwgroupconnector.resource.ConsentInfo;
import be.nicholasmeyers.vwgroupconnector.resource.FinalAuthorizationInfo;
import be.nicholasmeyers.vwgroupconnector.resource.OpenidConfiguration;
import be.nicholasmeyers.vwgroupconnector.resource.SsoLogin;
import be.nicholasmeyers.vwgroupconnector.resource.StartAuthorization;
import be.nicholasmeyers.vwgroupconnector.resource.SuccessInfo;
import be.nicholasmeyers.vwgroupconnector.resource.TokenInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static be.nicholasmeyers.vwgroupconnector.util.UserUtils.getEmail;

public class IdentityClientImplTest {
    private static final String CLIENT = "7f045eee-7003-4379-9968-9355ed2adb06@apps_vw-dilab_com";
    private IdentityClientImpl identityClient;
    private IdentityClientHelper identityClientHelper;

    @BeforeEach
    public void setup() {
        identityClient = new IdentityClientImpl();
        identityClientHelper = new IdentityClientHelper(identityClient);
    }

    @Test
    public void getConfiguration() {
        OpenidConfiguration configuration = identityClient.getConfiguration();
        Assertions.assertEquals("https://identity.vwgroup.io", configuration.getIssuer());
        Assertions.assertEquals("https://identity.vwgroup.io/oidc/v1/authorize", configuration.getAuthorizationEndpoint());
    }

    @Test
    public void getAuthorizationEndpoint() {
        StartAuthorization startAuthorization = identityClientHelper.getStartAuthorization();
        Assertions.assertNotNull(startAuthorization.getCookie());
        Assertions.assertNotNull(startAuthorization.getRelayState());
    }

    @Test
    public void getAuthorizationInfo() {
        AuthorizationInfo authorizationInfo = identityClientHelper.getAuthorizationInfo(identityClientHelper.getStartAuthorization());
        Assertions.assertNotNull(authorizationInfo.getCookie());
        Assertions.assertNotNull(authorizationInfo.getHmac());
        Assertions.assertNotNull(authorizationInfo.getCsrf());
    }

    @Test
    public void postEmail() {
        StartAuthorization startAuthorization = identityClientHelper.getStartAuthorization();
        identityClient.postEmail(CLIENT, startAuthorization, identityClientHelper.getAuthorizationInfo(startAuthorization), getEmail());
    }

    @Test
    public void getFinalAuthorizationInfo() {
        StartAuthorization startAuthorization = identityClientHelper.getStartAuthorization();
        AuthorizationInfo authorizationInfo = identityClientHelper.getAuthorizationInfo(startAuthorization);
        identityClient.postEmail(CLIENT, startAuthorization, authorizationInfo, getEmail());

        FinalAuthorizationInfo finalAuthorizationInfo = identityClientHelper.getFinalAuthorizationInfo(startAuthorization, authorizationInfo);
        Assertions.assertNotNull(finalAuthorizationInfo.getHmac());
        Assertions.assertNotNull(finalAuthorizationInfo.getCsrf());
    }

    @Test
    public void postEmailPassword() {
        StartAuthorization startAuthorization = identityClientHelper.getStartAuthorization();
        AuthorizationInfo authorizationInfo = identityClientHelper.getAuthorizationInfo(startAuthorization);
        identityClient.postEmail(CLIENT, startAuthorization, authorizationInfo, getEmail());

        FinalAuthorizationInfo finalAuthorizationInfo = identityClientHelper.getFinalAuthorizationInfo(startAuthorization, authorizationInfo);

        SsoLogin ssoLogin = identityClientHelper.postEmailPassword(startAuthorization, authorizationInfo, finalAuthorizationInfo);
        Assertions.assertNotNull(ssoLogin.getUser());
        Assertions.assertNotNull(ssoLogin.getHmac());
    }

    @Test
    public void ssoLogin() {
        StartAuthorization startAuthorization = identityClientHelper.getStartAuthorization();
        AuthorizationInfo authorizationInfo = identityClientHelper.getAuthorizationInfo(startAuthorization);
        identityClient.postEmail(CLIENT, startAuthorization, authorizationInfo, getEmail());

        FinalAuthorizationInfo finalAuthorizationInfo = identityClientHelper.getFinalAuthorizationInfo(startAuthorization, authorizationInfo);

        SsoLogin ssoLogin = identityClientHelper.postEmailPassword(startAuthorization, authorizationInfo, finalAuthorizationInfo);
        ConsentInfo consentInfo = identityClientHelper.ssoLogin(startAuthorization, authorizationInfo, ssoLogin);
        Assertions.assertNotNull(consentInfo.getHmac());
    }

    @Test
    public void checkConsent() {
        StartAuthorization startAuthorization = identityClientHelper.getStartAuthorization();
        AuthorizationInfo authorizationInfo = identityClientHelper.getAuthorizationInfo(startAuthorization);
        identityClient.postEmail(CLIENT, startAuthorization, authorizationInfo, getEmail());

        FinalAuthorizationInfo finalAuthorizationInfo = identityClientHelper.getFinalAuthorizationInfo(startAuthorization, authorizationInfo);

        SsoLogin ssoLogin = identityClientHelper.postEmailPassword(startAuthorization, authorizationInfo, finalAuthorizationInfo);
        ConsentInfo consentInfo = identityClientHelper.ssoLogin(startAuthorization, authorizationInfo, ssoLogin);
        SuccessInfo successInfo = identityClientHelper.checkConsent(startAuthorization, authorizationInfo, ssoLogin, consentInfo);
        Assertions.assertNotNull(successInfo.getHmac());
    }

    @Test
    public void handleSuccess() {
        StartAuthorization startAuthorization = identityClientHelper.getStartAuthorization();
        AuthorizationInfo authorizationInfo = identityClientHelper.getAuthorizationInfo(startAuthorization);
        identityClient.postEmail(CLIENT, startAuthorization, authorizationInfo, getEmail());

        FinalAuthorizationInfo finalAuthorizationInfo = identityClientHelper.getFinalAuthorizationInfo(startAuthorization, authorizationInfo);

        SsoLogin ssoLogin = identityClientHelper.postEmailPassword(startAuthorization, authorizationInfo, finalAuthorizationInfo);
        ConsentInfo consentInfo = identityClientHelper.ssoLogin(startAuthorization, authorizationInfo, ssoLogin);
        SuccessInfo successInfo = identityClientHelper.checkConsent(startAuthorization, authorizationInfo, ssoLogin, consentInfo);

        TokenInfo tokenInfo = identityClientHelper.handleSuccess(startAuthorization, authorizationInfo, ssoLogin, successInfo);
        Assertions.assertNotNull(tokenInfo.getAuthCode());
        Assertions.assertNotNull(tokenInfo.getRedirectUri());
        Assertions.assertNotNull(tokenInfo.getVerifier());
    }
}
