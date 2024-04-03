package be.nicholasmeyers.vwgroupconnector.web.out;

import be.nicholasmeyers.vwgroupconnector.web.out.resource.OpenidConfigurationWebResponseResource;
import be.nicholasmeyers.vwgroupconnector.web.out.resource.PostEmailPasswordWebRequestResource;
import be.nicholasmeyers.vwgroupconnector.web.out.resource.PostEmailWebRequestResource;
import feign.*;

import java.util.Map;

public interface IdentityClient {

    @RequestLine("GET /.well-known/openid-configuration")
    OpenidConfigurationWebResponseResource getConfiguration();

    @RequestLine("GET /oidc/v1/authorize")
    Response getAuthorizationEndpoint(@QueryMap Map<String, String> query);

    @RequestLine("GET /signin-service/v1/signin/{client}")
    Response getAuthorizationPage(@HeaderMap Map<String, String> headers, @QueryMap Map<String, String> query, @Param("client") String client);

    @RequestLine("POST /signin-service/v1/{client}/login/identifier")
    Response postEmail(@QueryMap PostEmailWebRequestResource resource, @HeaderMap Map<String, String> headers, @Param("client") String client);

    @RequestLine("GET /signin-service/v1/{client}/login/authenticate")
    Response getFinalAuthenticationPage(@HeaderMap Map<String, String> headers, @QueryMap Map<String, String> query, @Param("client") String client);

    @RequestLine("POST /signin-service/v1/{client}/login/authenticate")
    Response postEmailPassword(@QueryMap PostEmailPasswordWebRequestResource resource, @HeaderMap Map<String, String> headers, @Param("client") String client);

    @RequestLine("GET /oidc/v1/oauth/sso")
    Response ssoLogin(@HeaderMap Map<String, String> headers, @QueryMap Map<String, String> query);

    @RequestLine("GET /signin-service/v1/consent/users/{user}/{client}")
    Response checkConsent(@HeaderMap Map<String, String> headers, @QueryMap Map<String, String> query, @Param("user") String user, @Param("client") String client);

    @RequestLine("GET /oidc/v1/oauth/client/callback/success")
    Response handleSuccess(@HeaderMap Map<String, String> headers, @QueryMap Map<String, String> query);
}
