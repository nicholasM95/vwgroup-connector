package be.nicholasmeyers.vwgroupconnector.resource;

public class Tokens {
    private final String accessToken;
    private final String idToken;
    private final String refreshToken;

    public Tokens(String accessToken, String idToken, String refreshToken) {
        this.accessToken = accessToken;
        this.idToken = idToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
