package be.nicholasmeyers.vwgroupconnector.resource;

public class OpenidConfiguration {
    private final String issuer;
    private final String authorizationEndpoint;

    public OpenidConfiguration(String issuer, String authorizationEndpoint) {
        this.issuer = issuer;
        this.authorizationEndpoint = authorizationEndpoint;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getAuthorizationEndpoint() {
        return authorizationEndpoint;
    }
}
