package be.nicholasmeyers.vwgroupconnector.resource;

public class AuthorizationInfo {
    private final String cookie;
    private final String hmac;
    private final String csrf;

    public AuthorizationInfo(String cookie, String hmac, String csrf) {
        this.cookie = cookie;
        this.hmac = hmac;
        this.csrf = csrf;
    }

    public String getCookie() {
        return cookie;
    }

    public String getHmac() {
        return hmac;
    }

    public String getCsrf() {
        return csrf;
    }
}
