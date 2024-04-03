package be.nicholasmeyers.vwgroupconnector.utils;

import java.util.Collection;
import java.util.Map;

public class HeaderUtils {
    private HeaderUtils() {
    }

    public static String getLocationHeader(Map<String, Collection<String>> headers) {
        if (headers.containsKey("location")) {
            return headers.get("location").stream().findFirst().orElse("");
        }
        return "";
    }

    public static String getCookieHeader(Map<String, Collection<String>> headers) {
        if (headers.containsKey("set-cookie")) {
            return headers.get("set-cookie").stream().findFirst().orElse("");
        }
        return "";
    }
}
