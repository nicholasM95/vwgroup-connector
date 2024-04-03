package be.nicholasmeyers.vwgroupconnector.resource;

public class ConsentInfo {
    private final String hmac;

    public ConsentInfo(String hmac) {
        this.hmac = hmac;
    }

    public String getHmac() {
        return hmac;
    }
}
