package be.nicholasmeyers.vwgroupconnector.resource;

public class Tokens {
    private final String accessToken;
    private final String idToken;

    public Tokens(String accessToken, String idToken) {
        this.accessToken = accessToken;
        this.idToken = idToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getIdToken() {
        return idToken;
    }
}
