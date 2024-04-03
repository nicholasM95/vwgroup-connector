package be.nicholasmeyers.vwgroupconnector.service;

import be.nicholasmeyers.vwgroupconnector.resource.TokenInfo;
import be.nicholasmeyers.vwgroupconnector.resource.Tokens;

public interface TokenService {
    Tokens getTokens(TokenInfo tokenInfo);
}
