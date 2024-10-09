package com.tbank.edu.hw8.сontroller;

import com.tbank.edu.hw8.exception.CurrencyNotFoundException;
import com.tbank.edu.hw8.exception.ServiceUnavailableException;
import com.tbank.edu.hw8.model.ConvertRequest;
import com.tbank.edu.hw8.model.ConvertResponse;
import com.tbank.edu.hw8.model.RateResponse;
import com.tbank.edu.hw8.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Operation(summary = "Получение курса валюты по её коду")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Курс валюты успешно получен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RateResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некорректный код валюты",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Валюта не найдена в данных ЦБ",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/rates/{code}")
    public RateResponse getRate(@PathVariable String code) {
        try {
            if (code == null || code.trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Не указан код валюты");
            }
            double rate = currencyService.getRateByCode(code);
            return new RateResponse(code, rate);
        } catch (CurrencyNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @Operation(summary = "Конвертация валюты")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная конвертация",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConvertResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Одна из валют не найдена",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "503", description = "Сервис ЦБ недоступен",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/convert")
    public ConvertResponse convertCurrency(@RequestBody ConvertRequest request) {
        try {
            if (request.getFromCurrency() == null || request.getFromCurrency().trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Не указана исходная валюта");
            }

            if (request.getToCurrency() == null || request.getToCurrency().trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Не указана валюта назначения");
            }

            if (request.getAmount() == null || request.getAmount() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Сумма должна быть больше 0");
            }

            double convertedAmount = currencyService.convert(
                    request.getFromCurrency(),
                    request.getToCurrency(),
                    request.getAmount()
            );

            return new ConvertResponse(
                    request.getFromCurrency(),
                    request.getToCurrency(),
                    convertedAmount
            );
        } catch (CurrencyNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (ServiceUnavailableException ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());

        }
    }
}