package be.nicholasmeyers.vwgroupconnector.web.out.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VWGAuthorizationWebRequestResource {
    @JsonProperty("grant_type")
    private String grant_type;
    @JsonProperty("scope")
    private String scope;
    @JsonProperty("token")
    private String token;

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
