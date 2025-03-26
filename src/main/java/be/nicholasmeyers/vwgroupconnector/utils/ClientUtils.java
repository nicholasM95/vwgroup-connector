package be.nicholasmeyers.vwgroupconnector.utils;

import java.util.HashMap;
import java.util.Map;

public class ClientUtils {
    private ClientUtils() {
    }

    public static Map<String, String> getClientConfig() {
        Map<String, String> connect = new HashMap<>();
        connect.put("client_id", "7f045eee-7003-4379-9968-9355ed2adb06@apps_vw-dilab_com");
        connect.put("scope", "openid profile address cars email birthdate badge mbb phone driversLicense nationalIdentifier dealers mileage profession vin");
        connect.put("token_types", "code id_token");
        connect.put("redirect_uri", "myskoda://redirect/login/");
        return connect;
    }

}
