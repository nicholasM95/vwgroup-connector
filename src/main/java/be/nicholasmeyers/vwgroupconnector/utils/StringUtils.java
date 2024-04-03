package be.nicholasmeyers.vwgroupconnector.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class StringUtils {

    private StringUtils() {
    }

    public static String getResponseBodyAsString(InputStream inputStream) {
        try {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }
    }

    public static String getHmacFromString(String input) {
        String hmac = input.substring(input.indexOf("hmac") + 7);
        hmac = hmac.substring(0, hmac.indexOf("\""));
        return hmac;
    }

    public static String getCsrfFromString(String input) {
        String csrf = input.substring(input.indexOf("csrf_token") + 13);
        csrf = csrf.substring(0, csrf.indexOf("'"));
        return csrf;
    }
}
