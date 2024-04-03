package be.nicholasmeyers.vwgroupconnector.web.out;

import be.nicholasmeyers.vwgroupconnector.web.out.resource.VWGAuthorizationWebRequestResource;
import be.nicholasmeyers.vwgroupconnector.web.out.resource.VWGAuthorizationWebResponseResource;
import feign.HeaderMap;
import feign.QueryMap;
import feign.RequestLine;

import java.util.Map;

public interface VWGClient {

    @RequestLine("POST /mbbcoauth/mobile/oauth2/v1/token")
    VWGAuthorizationWebResponseResource getAccessToken(@QueryMap VWGAuthorizationWebRequestResource resource, @HeaderMap Map<String, String> headers);
}
