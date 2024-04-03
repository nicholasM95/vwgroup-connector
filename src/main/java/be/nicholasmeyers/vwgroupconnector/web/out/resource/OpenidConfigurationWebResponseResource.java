package be.nicholasmeyers.vwgroupconnector.web.out.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OpenidConfigurationWebResponseResource {
    @JsonProperty("issuer")
    private String issuer;
    @JsonProperty("authorization_endpoint")
    private String authorizationEndpoint;

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getAuthorizationEndpoint() {
        return authorizationEndpoint;
    }

    public void setAuthorizationEndpoint(String authorizationEndpoint) {
        this.authorizationEndpoint = authorizationEndpoint;
    }
}
