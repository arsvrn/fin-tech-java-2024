package com.tbank.edu.hw9.service;

import com.tbank.edu.hw9.model.Event;
import com.tbank.edu.hw9.model.EventResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class EventServiceRateLimitingTest {

    @Mock
    private WebClient webClient;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    void setUp() {
        this.eventService = new EventService(2);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRateLimiting() throws InterruptedException {
        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(EventResponse.class)).thenReturn(Flux.empty());

        Flux<Event> flux1 = eventService.getEvents("1609459200", "1612137600");
        Flux<Event> flux2 = eventService.getEvents("1609459200", "1612137600");
        Flux<Event> flux3 = eventService.getEvents("1609459200", "1612137600");

        StepVerifier.create(Flux.merge(flux1, flux2, flux3))
                .expectComplete()
                .verify();

        Semaphore semaphore = new Semaphore(2);
        boolean acquired1 = semaphore.tryAcquire(1, TimeUnit.SECONDS);
        boolean acquired2 = semaphore.tryAcquire(1, TimeUnit.SECONDS);
        assertTrue(acquired1 && acquired2);

        verify(webClient, times(3)).get();
    }
}
