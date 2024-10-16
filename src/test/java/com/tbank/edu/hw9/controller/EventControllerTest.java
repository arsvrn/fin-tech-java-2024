package com.tbank.edu.hw9.controller;

import com.tbank.edu.hw8.service.CurrencyService;
import com.tbank.edu.hw9.model.DateRange;
import com.tbank.edu.hw9.model.Event;
import com.tbank.edu.hw9.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@WebFluxTest(EventController.class)
class EventControllerTest {

    @Mock
    private EventService eventService;

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private EventController eventController;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToController(eventController).build();
    }

    @Test
    void getEvents_Success() {
        when(currencyService.convert(any(), any(), any())).thenReturn(BigDecimal.valueOf(1000));
        when(eventService.getEvents(any(), any())).thenReturn(Flux.just(createEvent("Test Event")));
        when(eventService.filterEventsByBudgetAndDate(any(), any(), any(), any())).thenReturn(Flux.just(createEvent("Test Event")));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/events")
                        .queryParam("budget", 1000)
                        .queryParam("currency", "RUB")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].title").isEqualTo("Test Event");
    }

    @Test
    void getEvents_ErrorHandling() {
        when(currencyService.convert(any(), any(), any())).thenThrow(new RuntimeException("Conversion Error"));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/events")
                        .queryParam("budget", 1000)
                        .queryParam("currency", "RUB")
                        .build())
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Conversion Error");
    }

    private Event createEvent(String title) {
        Event event = new Event();
        event.setTitle(title);
        event.setPrice("1000 рублей");
        event.setFree(false);
        event.setDates(List.of(new DateRange(Instant.now(), Instant.now().plusSeconds(3600))));
        return event;
    }
}