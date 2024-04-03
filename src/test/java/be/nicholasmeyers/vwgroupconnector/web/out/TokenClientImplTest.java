package be.nicholasmeyers.vwgroupconnector.web.out;

import be.nicholasmeyers.vwgroupconnector.resource.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TokenClientImplTest {
    private TokenClientImpl tokenClient;
    private IdentityClientHelper identityClientHelper;

    @BeforeEach
    public void setup() {
        tokenClient = new TokenClientImpl();
        identityClientHelper = new IdentityClientHelper(new IdentityClientImpl());
    }

    @Test
    public void getToken() {
        Tokens tokens = tokenClient.getTokens(getTokenInfo());
        Assertions.assertNotNull(tokens.getAccessToken());
        Assertions.assertNotNull(tokens.getIdToken());
    }

    private TokenInfo getTokenInfo() {
        StartAuthorization startAuthorization = identityClientHelper.getStartAuthorization();
        AuthorizationInfo authorizationInfo = identityClientHelper.getAuthorizationInfo(startAuthorization);
        identityClientHelper.postEmail(startAuthorization, authorizationInfo);

        FinalAuthorizationInfo finalAuthorizationInfo = identityClientHelper.getFinalAuthorizationInfo(startAuthorization, authorizationInfo);

        SsoLogin ssoLogin = identityClientHelper.postEmailPassword(startAuthorization, authorizationInfo, finalAuthorizationInfo);
        ConsentInfo consentInfo = identityClientHelper.ssoLogin(startAuthorization, authorizationInfo, ssoLogin);
        SuccessInfo successInfo = identityClientHelper.checkConsent(startAuthorization, authorizationInfo, ssoLogin, consentInfo);

        return identityClientHelper.handleSuccess(startAuthorization, authorizationInfo, ssoLogin, successInfo);
    }
}
