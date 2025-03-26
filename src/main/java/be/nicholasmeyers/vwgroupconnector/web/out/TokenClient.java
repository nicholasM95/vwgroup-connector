package be.nicholasmeyers.vwgroupconnector.web.out;

import be.nicholasmeyers.vwgroupconnector.web.out.resource.CodeExchangeWebRequestResource;
import be.nicholasmeyers.vwgroupconnector.web.out.resource.TokenWebResponseResource;
import feign.Headers;
import feign.RequestLine;

public interface TokenClient {
    @RequestLine("POST /api/v1/authentication/exchange-authorization-code?tokenType=CONNECT")
    @Headers("Content-Type: application/json")
    TokenWebResponseResource getTokens(CodeExchangeWebRequestResource codeExchangeWebRequestResource);
}
