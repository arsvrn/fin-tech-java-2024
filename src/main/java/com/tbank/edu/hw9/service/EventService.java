package com.tbank.edu.hw9.service;

import com.tbank.edu.hw9.exception.EventServiceException;
import com.tbank.edu.hw9.model.DateRange;
import com.tbank.edu.hw9.model.Event;
import com.tbank.edu.hw9.model.EventResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventService {

    private final WebClient webClient;
    @Value("${kudago.events.api.url}")
    private String EVENTS_API_URL;

    public EventService() {
        this.webClient = WebClient.create();
    }

    public Flux<Event> getEvents(String actualSince, String actualUntil) {
        String url = EVENTS_API_URL + "?fields=title,short_title,dates,description,price,is_free" +
                "&actual_since=" + actualSince + "&actual_until=" + actualUntil;

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToFlux(EventResponse.class)
                .flatMapIterable(EventResponse::getResults);
    }


    public Flux<Event> filterEventsByBudgetAndDate(Flux<Event> events, BigDecimal budgetInRub, Instant dateFrom, Instant dateTo) {
        log.info("Фильтрация событий по бюджету: {}, и диапазону дат: от {} до {}", budgetInRub, dateFrom, dateTo);

        return events
                .concatMap(event -> {
                    List<DateRange> filteredDates = event.getDates().stream()
                            .filter(dateRange -> isDateInRange(dateRange, dateFrom, dateTo))
                            .collect(Collectors.toList());

                    event.setDates(filteredDates);

                    return Mono.just(event)
                            .filter(e -> !e.getDates().isEmpty())
                            .filter(e -> {
                                if (e.isFree()) {
                                    return true;
                                }
                                if (e.getPrice() == null || e.getPrice().isEmpty()) {
                                    return false;
                                }
                                BigDecimal minPrice = extractMinPrice(e.getPrice());
                                return minPrice != null && minPrice.compareTo(budgetInRub) <= 0;
                            })
                            .doOnNext(e -> log.info("Событие прошло фильтрацию: {}", e.getTitle()));
                })
                .onErrorResume(Throwable.class, ex -> {
                    log.error("Ошибка при фильтрации событий", ex);
                    return Flux.error(new EventServiceException("Ошибка при фильтрации событий", ex));
                });
    }

    private boolean isDateInRange(DateRange dateRange, Instant dateFrom, Instant dateTo) {
        return (dateRange.getStart().isAfter(dateFrom) || dateRange.getStart().equals(dateFrom)) &&
                (dateRange.getEnd().isBefore(dateTo) || dateRange.getEnd().equals(dateTo));
    }

    private BigDecimal extractMinPrice(String price) {
        if (price == null || price.isEmpty()) {
            return null;
        }


        Pattern pattern = Pattern.compile("(\\d+)");
        Matcher matcher = pattern.matcher(price);
        if (matcher.find()) {
            try {
                return new BigDecimal(matcher.group(1));
            } catch (NumberFormatException e) {
                log.error("Ошибка при разборе цены: {}", price, e);
                throw new EventServiceException("Ошибка при извлечении минимальной цены", e);
            }
        }

        return null;
    }
}

