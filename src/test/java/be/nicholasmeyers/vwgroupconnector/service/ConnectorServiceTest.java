package be.nicholasmeyers.vwgroupconnector.service;

import be.nicholasmeyers.vwgroupconnector.resource.Client;
import be.nicholasmeyers.vwgroupconnector.resource.Tokens;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ConnectorServiceTest {

    private ConnectorService connectorService;

    @BeforeEach
    public void setup() {
        connectorService = new ConnectorService();
    }

    @Test
    public void testConnect() {
        Tokens tokens = connectorService.getTokens(Client.CONNECT, "", "");
        Assertions.assertNotNull(tokens.getAccessToken());
        Assertions.assertNotNull(tokens.getIdToken());
    }

    @Test
    public void testSkoda() {
        Tokens tokens = connectorService.getTokens(Client.SKODA, "", "");
        Assertions.assertNotNull(tokens.getAccessToken());
        Assertions.assertNotNull(tokens.getIdToken());
    }

    @Test
    public void testSmartlink() {
        Tokens tokens = connectorService.getTokens(Client.SMARTLINK, "", "");
        Assertions.assertNotNull(tokens.getAccessToken());
        Assertions.assertNotNull(tokens.getIdToken());
    }

    @Test
    public void testVWG() {
        Tokens tokens = connectorService.getTokens(Client.VWG, "", "");
        Assertions.assertNotNull(tokens.getAccessToken());
        Assertions.assertNotNull(tokens.getIdToken());
    }
}
