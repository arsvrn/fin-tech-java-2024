package com.tbank.edu.hw8.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.tbank.edu.hw8.exception.CurrencyNotFoundException;
import com.tbank.edu.hw8.exception.ServiceUnavailableException;
import com.tbank.edu.hw8.model.ValCurs;
import com.tbank.edu.hw8.model.Valute;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Slf4j
public class CurrencyService {

    @Value("${cbr.api.url}")
    private String cbrUrl;

    @Value("${cbr.api.date_format}")
    private String dateFormat;

    private final RestClient restClient;
    private final XmlMapper xmlMapper;

    public CurrencyService() {
        restClient = RestClient.create();
        xmlMapper = new XmlMapper();
    }

    private String getCurrentDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(dateFormat));
    }

    @Cacheable(value = "currencyRates", key = "#date", unless = "#result == null", cacheManager = "cacheManager")
    @CircuitBreaker(name = "currencyService", fallbackMethod = "fallbackGetCurrencyRates")
    public ValCurs getCurrencyRates(String date) {
        if (date == null || date.isEmpty()) {
            date = getCurrentDate();
        }
        String url = String.format("%s?date_req=%s", cbrUrl, date);
        String xmlResponse;
        try {
            xmlResponse = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(String.class);

            return xmlMapper.readValue(xmlResponse, ValCurs.class);
        } catch (JsonProcessingException e) {
            log.error("Ошибка парсинга XML");
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error("Сервис ЦБ недоступен");
            throw new ServiceUnavailableException("Сервис ЦБ недоступен");
        }
    }

    public double getRateByCode(String code) {
        ValCurs valCurs = getCurrencyRates("");

        Optional<Valute> valuteOptional = valCurs.getValuteList().stream()
                .filter(valute -> valute.getCharCode().equalsIgnoreCase(code))
                .findFirst();

        if (code.equals("RUB")) {
            return 1;
        }
        if (valuteOptional.isPresent()) {
            String value = valuteOptional.get().getValue().replace(",", ".");
            return Double.parseDouble(value);
        } else {
            log.error("Валюта с кодом " + code + " не найдена");
            throw new CurrencyNotFoundException("Валюта с кодом " + code + " не найдена");
        }
    }

    public double convert(String fromCurrency, String toCurrency, double amount) {
        if (amount <= 0) {
            log.error("Сумма должна быть больше 0");
            throw new IllegalArgumentException("Сумма должна быть больше 0");
        }

        double fromRate = getRateByCode(fromCurrency);
        double toRate = getRateByCode(toCurrency);

        if (fromRate <= 0 || toRate <= 0) {
            log.error("Недопустимый курс валют для конвертации");
            throw new IllegalArgumentException("Недопустимый курс валют для конвертации");
        }

        return amount * (fromRate / toRate);
    }
}