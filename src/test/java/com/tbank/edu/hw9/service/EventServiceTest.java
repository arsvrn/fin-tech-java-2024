package com.tbank.edu.hw9.service;

import com.tbank.edu.hw9.exception.EventApiException;
import com.tbank.edu.hw9.model.DateRange;
import com.tbank.edu.hw9.model.Event;
import com.tbank.edu.hw9.model.EventResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.*;

class EventServiceTest {

    @Mock
    private WebClient webClient;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getEvents_Success() {
        // Step 1: Mocking WebClient components
        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        // Step 2: Mock the WebClient flow
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(EventResponse.class)).thenReturn(Flux.just(createEventResponse()));

        // Call the service
        Flux<Event> eventsFlux = eventService.getEvents("1609459200", "1612137600");

        // Verify the result
        StepVerifier.create(eventsFlux)
                .expectNextMatches(event -> event.getTitle().equals("Test Event"))
                .verifyComplete();

        verify(webClient, times(1)).get();
    }

    @Test
    void getEvents_ApiError() {
        // Step 1: Mocking WebClient components
        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        // Step 2: Mock the WebClient flow to throw an error
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(EventResponse.class)).thenThrow(WebClientResponseException.class);

        Flux<Event> eventsFlux = eventService.getEvents("1609459200", "1612137600");

        StepVerifier.create(eventsFlux)
                .expectError(EventApiException.class)
                .verify();

        verify(webClient, times(1)).get();
    }

    // Helper methods to create test data
    private EventResponse createEventResponse() {
        EventResponse response = new EventResponse();
        response.setResults(List.of(createEvent("Test Event", BigDecimal.valueOf(500), false)));
        return response;
    }

    private Event createEvent(String title, BigDecimal price, boolean isFree) {
        Event event = new Event();
        event.setTitle(title);
        event.setPrice(price != null ? price + " рублей" : "");
        event.setFree(isFree);
        event.setDates(List.of(new DateRange(Instant.now(), Instant.now().plusSeconds(3600))));
        return event;
    }
}