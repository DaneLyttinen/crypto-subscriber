package service;

import org.junit.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class IndicatorResourceIT {

    private static final String WEB_SERVICE_URI = "http://localhost:10000/services/technical_indicators";
    private Client client;

    @Before
    public void setUp() {

        client = ClientBuilder.newClient();
    }

    /**
     * Kills the client after every test, to get rid of any leftover cookies.
     */
    @After
    public void tearDown() {
        client.close();
        client = null;
    }

    @Test
    public void testGetTechnicalIndicator() {
        Response indicator = client.target(WEB_SERVICE_URI+"/bollingerBands").request().get();
        assertEquals(200,indicator.getStatus() );
    }
}
