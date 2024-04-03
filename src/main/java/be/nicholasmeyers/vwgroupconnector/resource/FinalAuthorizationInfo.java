package be.nicholasmeyers.vwgroupconnector.resource;

public class FinalAuthorizationInfo {
    private final String hmac;
    private final String csrf;

    public FinalAuthorizationInfo(String hmac, String csrf) {
        this.hmac = hmac;
        this.csrf = csrf;
    }

    public String getHmac() {
        return hmac;
    }

    public String getCsrf() {
        return csrf;
    }
}
