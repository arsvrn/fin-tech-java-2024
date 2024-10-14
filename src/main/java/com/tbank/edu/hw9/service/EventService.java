package com.tbank.edu.hw9.service;

import com.tbank.edu.hw9.model.DateRange;
import com.tbank.edu.hw9.model.Event;
import com.tbank.edu.hw9.model.EventResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final RestClient restClient;
    @Value("${kudago.events.api.url}")
    private String EVENTS_API_URL;

    public EventService() {
        restClient = RestClient.create();
    }

    @Async
    public CompletableFuture<List<Event>> getEvents(String actualSince, String actualUntil) {
        String url = EVENTS_API_URL + "?fields=title,short_title,dates,description,price,is_free" + "&actual_since=" + actualSince + "&actual_until=" + actualUntil;

        EventResponse response = restClient.get()
                .uri(url)
                .retrieve()
                .body(EventResponse.class);

        return CompletableFuture.completedFuture(response.getResults());
    }

    public List<Event> filterEventsByBudgetAndDate(List<Event> events, BigDecimal budgetInRub, Instant dateFrom, Instant dateTo) {
        return events.stream()
                .map(event -> {
                    List<DateRange> filteredDates = event.getDates().stream()
                            .filter(dateRange -> isDateInRange(dateRange, dateFrom, dateTo))
                            .collect(Collectors.toList());
                    event.setDates(filteredDates);

                    return event;
                })
                .filter(event -> !event.getDates().isEmpty())
                .filter(event -> {
                    if (event.isFree()) {
                        return true;
                    }
                    if (event.getPrice() == null || event.getPrice().isEmpty()) {
                        return false;
                    }
                    BigDecimal minPrice = extractMinPriceOrNull(event.getPrice());
                    return minPrice != null && minPrice.compareTo(budgetInRub) <= 0;
                })
                .collect(Collectors.toList());
    }

    private BigDecimal extractMinPriceOrNull(String price) {
        if (price == null || price.isEmpty()) {
            return null;
        }

        Pattern pattern = Pattern.compile("(\\d+)");
        Matcher matcher = pattern.matcher(price);
        if (matcher.find()) {
            try {
                return new BigDecimal(matcher.group(1));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private boolean isDateInRange(DateRange dateRange, Instant dateFrom, Instant dateTo) {
        return (dateRange.getStart().isAfter(dateFrom) || dateRange.getStart().equals(dateFrom)) &&
                (dateRange.getEnd().isBefore(dateTo) || dateRange.getEnd().equals(dateTo));
    }
}

