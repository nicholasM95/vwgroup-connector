package be.nicholasmeyers.vwgroupconnector.resource;

public class TokenInfo {
    private final String authCode;
    private final String idToken;

    public TokenInfo(String authCode, String idToken) {
        this.authCode = authCode;
        this.idToken = idToken;
    }

    public String getAuthCode() {
        return authCode;
    }

    public String getIdToken() {
        return idToken;
    }
}
