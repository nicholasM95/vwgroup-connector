package be.nicholasmeyers.vwgroupconnector.exception;

public class LoginException extends RuntimeException {
    public LoginException(String message, String loginUrl) {
        super(message + " " + loginUrl);
    }
}
