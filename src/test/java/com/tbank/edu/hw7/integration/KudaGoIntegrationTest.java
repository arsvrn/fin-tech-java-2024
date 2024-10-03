package com.tbank.edu.hw7.integration;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class KudaGoIntegrationTest {

    @Container
    public static GenericContainer<?> wiremock = new GenericContainer<>("rodolpheche/wiremock:2.27.2")
            .withExposedPorts(8080);

    private static String wireMockUrl;

    @BeforeAll
    public static void setup() {
        Integer port = wiremock.getMappedPort(8080);
        wireMockUrl = "http://localhost:" + port;

        WireMock.configureFor("localhost", port);

        WireMock.stubFor(get(WireMock.urlEqualTo("/public-api/v1.4/place-categories"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"id\":123,\"slug\":\"airports\",\"name\":\"Airports\"}]")));

        WireMock.stubFor(get(WireMock.urlEqualTo("/public-api/v1.4/locations"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"slug\":\"msk\",\"name\":\"Moscow\"}]")));
    }

    @AfterAll
    public static void tearDown() {
        wiremock.stop();
    }

    @Test
    public void testPlaceCategories() {
        String response = getFromKudaGo("/public-api/v1.4/place-categories");

        assertEquals("[{\"id\":123,\"slug\":\"airports\",\"name\":\"Airports\"}]", response);
    }

    @Test
    public void testLocations() {
        String response = getFromKudaGo("/public-api/v1.4/locations");

        assertEquals("[{\"slug\":\"msk\",\"name\":\"Moscow\"}]", response);
    }

    private String getFromKudaGo(String endpoint) {
        try (java.util.Scanner s = new java.util.Scanner(new java.net.URL(wireMockUrl + endpoint).openStream())) {
            return s.useDelimiter("\\A").hasNext() ? s.next() : "";
        } catch (Exception e) {
            throw new RuntimeException("Ошибка обращения к API: " + e.getMessage(), e);
        }
    }
}
