package be.nicholasmeyers.vwgroupconnector.resource;

public class TokenInfo {
    private final String authCode;
    private final String redirectUri;
    private final String verifier;

    public TokenInfo(String authCode, String redirectUri, String verifier) {
        this.authCode = authCode;
        this.redirectUri = redirectUri;
        this.verifier = verifier;
    }

    public String getAuthCode() {
        return authCode;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getVerifier() {
        return verifier;
    }
}
