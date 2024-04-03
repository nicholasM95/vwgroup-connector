package be.nicholasmeyers.vwgroupconnector.resource;

public class StartAuthorization {
    private final String cookie;
    private final String relayState;

    public StartAuthorization(String cookie, String relayState) {
        this.cookie = cookie;
        this.relayState = relayState;
    }

    public String getCookie() {
        return cookie;
    }

    public String getRelayState() {
        return relayState;
    }
}
