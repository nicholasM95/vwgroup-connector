package be.nicholasmeyers.vwgroupconnector.resource;

public class StartAuthorization {
    private final String cookie;
    private final String relayState;
    private final String loginUrl;

    public StartAuthorization(String cookie, String relayState, String loginUrl) {
        this.cookie = cookie;
        this.relayState = relayState;
        this.loginUrl = loginUrl;
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
}
