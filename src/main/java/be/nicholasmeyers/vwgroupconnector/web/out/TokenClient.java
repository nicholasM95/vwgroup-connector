package be.nicholasmeyers.vwgroupconnector.web.out;

import be.nicholasmeyers.vwgroupconnector.web.out.resource.TokenWebResponseResource;
import feign.*;

public interface TokenClient {
    @RequestLine("POST /exchangeAuthCode")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    TokenWebResponseResource getTokens(@Param("auth_code") String authCode, @Param("id_token") String idToken, @Param("brand") String brand);
}
