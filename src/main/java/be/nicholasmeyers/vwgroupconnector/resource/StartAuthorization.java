package be.nicholasmeyers.vwgroupconnector.resource;

public class StartAuthorization {
    private final String cookie;
    private final String relayState;
    private final String loginUrl;
    private final String redirectUrl;
    private final String codeVerifier;

    public StartAuthorization(String cookie, String relayState, String loginUrl, String redirectUrl, String codeVerifier) {
        this.cookie = cookie;
        this.relayState = relayState;
        this.loginUrl = loginUrl;
        this.redirectUrl = redirectUrl;
        this.codeVerifier = codeVerifier;
    }

    public String getCookie() {
        return cookie;
    }

    public String getRelayState() {
        return relayState;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public String getCodeVerifier() {
        return codeVerifier;
    }
}
