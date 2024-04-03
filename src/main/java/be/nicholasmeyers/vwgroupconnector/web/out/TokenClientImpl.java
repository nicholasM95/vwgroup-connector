package be.nicholasmeyers.vwgroupconnector.web.out;

import be.nicholasmeyers.vwgroupconnector.resource.TokenInfo;
import be.nicholasmeyers.vwgroupconnector.resource.Tokens;
import be.nicholasmeyers.vwgroupconnector.service.TokenService;
import be.nicholasmeyers.vwgroupconnector.web.out.resource.TokenWebResponseResource;
import feign.Feign;
import feign.form.FormEncoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

public class TokenClientImpl implements TokenService {
    private final TokenClient tokenClient;

    public TokenClientImpl() {
        this.tokenClient = Feign.builder()
                .encoder(new FormEncoder(new JacksonEncoder()))
                .decoder(new JacksonDecoder())
                .target(TokenClient.class, "https://tokenrefreshservice.apps.emea.vwapps.io");
    }

    @Override
    public Tokens getTokens(TokenInfo tokenInfo) {
        TokenWebResponseResource webResponseResource = tokenClient.getTokens(tokenInfo.getAuthCode(), tokenInfo.getIdToken(), "skoda");
        return new Tokens(webResponseResource.getAccessToken(), webResponseResource.getIdToken());
    }
}
