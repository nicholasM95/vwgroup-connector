package be.nicholasmeyers.vwgroupconnector.resource;

public class SsoLogin {
    private final String user;
    private final String hmac;

    public SsoLogin(String user, String hmac) {
        this.user = user;
        this.hmac = hmac;
    }

    public String getUser() {
        return user;
    }

    public String getHmac() {
        return hmac;
    }
}
