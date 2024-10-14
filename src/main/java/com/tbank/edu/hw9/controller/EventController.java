package com.tbank.edu.hw9.controller;

import com.tbank.edu.hw8.service.CurrencyService;
import com.tbank.edu.hw9.model.Event;
import com.tbank.edu.hw9.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/events")
    @com.tbank.edu.hw5.aspect.LogExecutionTime
    public CompletableFuture<List<Event>> getEvents(
            @RequestParam(name = "budget") BigDecimal budget,
            @RequestParam(name = "currency") String currency,
            @RequestParam(name = "dateFrom", required = false) String dateFrom,
            @RequestParam(name = "dateTo", required = false) String dateTo) {

        Instant from = (dateFrom != null) ? LocalDate.parse(dateFrom, DateTimeFormatter.ISO_DATE).atStartOfDay().toInstant(java.time.ZoneOffset.UTC)
                : LocalDate.now().with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)).atStartOfDay().toInstant(java.time.ZoneOffset.UTC);
        Instant to = (dateTo != null) ? LocalDate.parse(dateTo, DateTimeFormatter.ISO_DATE).atStartOfDay().toInstant(java.time.ZoneOffset.UTC)
                : LocalDate.now().with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY)).atStartOfDay().toInstant(java.time.ZoneOffset.UTC);

        CompletableFuture<List<Event>> eventsFuture = eventService.getEvents(String.valueOf(from.getEpochSecond()), String.valueOf(to.getEpochSecond()));
        CompletableFuture<BigDecimal> budgetInRubFuture = CompletableFuture.supplyAsync(() -> currencyService.convert(currency, "RUB", budget));

        return eventsFuture.thenCombine(budgetInRubFuture, (events, budgetInRub) ->
                eventService.filterEventsByBudgetAndDate(events, budgetInRub, from, to)
        );
    }
}
