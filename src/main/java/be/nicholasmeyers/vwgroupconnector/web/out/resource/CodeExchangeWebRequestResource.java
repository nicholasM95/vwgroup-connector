package be.nicholasmeyers.vwgroupconnector.web.out.resource;

public class CodeExchangeWebRequestResource {
    private String code;
    private String redirectUri;
    private String verifier;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getVerifier() {
        return verifier;
    }

    public void setVerifier(String verifier) {
        this.verifier = verifier;
    }
}
