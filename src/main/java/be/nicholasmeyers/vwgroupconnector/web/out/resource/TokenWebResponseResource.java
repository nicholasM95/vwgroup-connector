package be.nicholasmeyers.vwgroupconnector.web.out.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenWebResponseResource {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("id_token")
    private String idToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
