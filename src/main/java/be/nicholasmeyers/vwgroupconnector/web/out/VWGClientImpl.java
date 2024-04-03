package be.nicholasmeyers.vwgroupconnector.web.out;

import be.nicholasmeyers.vwgroupconnector.service.VWGService;
import be.nicholasmeyers.vwgroupconnector.web.out.resource.VWGAuthorizationWebRequestResource;
import feign.Feign;
import feign.form.FormEncoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

import java.util.HashMap;
import java.util.Map;

public class VWGClientImpl implements VWGService {
    private final VWGClient vwgClient;

    public VWGClientImpl() {
        this.vwgClient = Feign.builder()
                .encoder(new FormEncoder(new JacksonEncoder()))
                .decoder(new JacksonDecoder())
                .target(VWGClient.class, "https://mbboauth-1d.prd.ece.vwg-connect.com");
    }

    @Override
    public String getAccessToken(String idToken) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("X-App-Version", "5.2.7");
        headers.put("X-App-Name", "cz.skodaauto.connect");
        headers.put("X-Client-Id", "fef89b3d-a6e0-4525-91eb-a9436e6e469a");

        VWGAuthorizationWebRequestResource resource = new VWGAuthorizationWebRequestResource();
        resource.setToken(idToken);
        resource.setScope("sc2:fal");
        resource.setGrant_type("id_token");

        return vwgClient.getAccessToken(resource, headers).getAccessToken();
    }
}
