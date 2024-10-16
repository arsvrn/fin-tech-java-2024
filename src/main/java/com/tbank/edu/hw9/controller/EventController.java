package com.tbank.edu.hw9.controller;

import com.tbank.edu.hw8.service.CurrencyService;
import com.tbank.edu.hw9.exception.EventServiceException;
import com.tbank.edu.hw9.model.Event;
import com.tbank.edu.hw9.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;

@Slf4j
@RestController
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/events")
    @com.tbank.edu.hw5.aspect.LogExecutionTime
    public Flux<Event> getEvents(
            @RequestParam(name = "budget") BigDecimal budget,
            @RequestParam(name = "currency") String currency,
            @RequestParam(name = "dateFrom", required = false) String dateFrom,
            @RequestParam(name = "dateTo", required = false) String dateTo) {
        Instant from = (dateFrom != null) ? LocalDate.parse(dateFrom, DateTimeFormatter.ISO_DATE).atStartOfDay().toInstant(java.time.ZoneOffset.UTC)
                : LocalDate.now().with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)).atStartOfDay().toInstant(java.time.ZoneOffset.UTC);
        Instant to = (dateTo != null) ? LocalDate.parse(dateTo, DateTimeFormatter.ISO_DATE).atStartOfDay().toInstant(java.time.ZoneOffset.UTC)
                : LocalDate.now().with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY)).atStartOfDay().toInstant(java.time.ZoneOffset.UTC);

        Mono<BigDecimal> budgetInRubMono = Mono.fromCallable(() -> currencyService.convert(currency, "RUB", budget));
        Flux<Event> eventsFlux = eventService.getEvents(String.valueOf(from.getEpochSecond()), String.valueOf(to.getEpochSecond()));

        return Mono.zip(budgetInRubMono, Mono.just(eventsFlux))
                .flatMapMany(tuple -> eventService.filterEventsByBudgetAndDate(tuple.getT2(), tuple.getT1(), from, to))
                .sort(Comparator.comparing(Event::getFavoritesCount).reversed())
                .onErrorResume(EventServiceException.class, ex -> {
                    log.error("Ошибка при обработке событий: {}", ex.getMessage());
                    return Flux.error(ex);
                });
    }

    @ExceptionHandler(EventServiceException.class)
    public Mono<String> handleEventServiceException(EventServiceException ex) {
        log.error("Обработано исключение: {}", ex.getMessage());
        return Mono.just("Ошибка при обработке событий: " + ex.getMessage());
    }
}
