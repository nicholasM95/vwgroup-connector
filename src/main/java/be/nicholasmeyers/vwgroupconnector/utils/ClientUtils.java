package be.nicholasmeyers.vwgroupconnector.utils;

import java.util.HashMap;
import java.util.Map;

public class ClientUtils {
    private ClientUtils() {
    }

    public static Map<String, String> getClientConfig(String client) {
        if (getClients().containsKey(client)) {
            return getClients().get(client);
        }
        return getClients().get("connect");
    }

    private static Map<String, Map<String, String>> getClients() {
        Map<String, String> connect = new HashMap<>();
        connect.put("client_id", "7f045eee-7003-4379-9968-9355ed2adb06@apps_vw-dilab_com");
        connect.put("scope", "openid profile address cars email birthdate badge mbb phone driversLicense dealers profession vin");
        connect.put("token_types", "code id_token");

        Map<String, String> vwg = new HashMap<>();
        vwg.put("client_id", "7f045eee-7003-4379-9968-9355ed2adb06@apps_vw-dilab_com");
        vwg.put("scope", "openid profile address cars email birthdate badge mbb phone driversLicense dealers profession vin");
        vwg.put("token_types", "code id_token");

        Map<String, String> skoda = new HashMap<>();
        skoda.put("client_id", "f9a2359a-b776-46d9-bd0c-db1904343117@apps_vw-dilab_com");
        skoda.put("scope", "openid mbb profile");
        skoda.put("token_types", "code id_token");

        Map<String, String> smartlink = new HashMap<>();
        smartlink.put("client_id", "72f9d29d-aa2b-40c1-bebe-4c7683681d4c@apps_vw-dilab_com");
        smartlink.put("scope", "openid dealers profile email cars address");
        smartlink.put("token_types", "code id_token");

        Map<String, Map<String, String>> clients = new HashMap<>();
        clients.put("connect", connect);
        clients.put("vwg", vwg);
        clients.put("skoda", skoda);
        clients.put("smartlink", smartlink);

        return clients;
    }


}
