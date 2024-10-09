package com.tbank.edu.hw8.сontroller;

import com.tbank.edu.hw8.exception.CurrencyNotFoundException;
import com.tbank.edu.hw8.exception.ServiceUnavailableException;
import com.tbank.edu.hw8.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CurrencyController.class)
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyService currencyService;

    @Test
    void testGetRateWithValidCurrency() throws Exception {
        Mockito.when(currencyService.getRateByCode("USD")).thenReturn(75.0);

        mockMvc.perform(get("/currencies/rates/USD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.rate").value(75.0));
    }

    @Test
    void testGetRateWithInvalidCurrency() throws Exception {
        doThrow(new CurrencyNotFoundException("Валюта не найдена")).when(currencyService).getRateByCode(anyString());

        mockMvc.perform(get("/currencies/rates/XYZ"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("Валюта не найдена"));
    }

    @Test
    void testGetRateWithEmptyCurrency() throws Exception {
        mockMvc.perform(get("/currencies/rates/"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testConvertCurrencyWithValidRequest() throws Exception {
        Mockito.when(currencyService.convert("USD", "RUB", 100.0)).thenReturn(7500.0);

        String requestBody = """
                    {
                        "fromCurrency": "USD",
                        "toCurrency": "RUB",
                        "amount": 100.0
                    }
                """;

        mockMvc.perform(post("/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fromCurrency").value("USD"))
                .andExpect(jsonPath("$.toCurrency").value("RUB"))
                .andExpect(jsonPath("$.convertedAmount").value(7500.0));
    }

    @Test
    void testConvertCurrencyWithNegativeAmount() throws Exception {
        doThrow(new IllegalArgumentException("Сумма должна быть больше 0")).when(currencyService)
                .convert("USD", "RUB", -100.0);

        String requestBody = """
                    {
                        "fromCurrency": "USD",
                        "toCurrency": "RUB",
                        "amount": -100.0
                    }
                """;

        mockMvc.perform(post("/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("Сумма должна быть больше 0"));
    }

    @Test
    void testConvertCurrencyWithNonExistingCurrency() throws Exception {
        doThrow(new CurrencyNotFoundException("Валюта не найдена")).when(currencyService)
                .convert("XYZ", "RUB", 100.0);

        String requestBody = """
                    {
                        "fromCurrency": "XYZ",
                        "toCurrency": "RUB",
                        "amount": 100.0
                    }
                """;

        mockMvc.perform(post("/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("Валюта не найдена"));
    }

    @Test
    void testServiceUnavailable() throws Exception {
        doThrow(new ServiceUnavailableException("Сервис ЦБ недоступен"))
                .when(currencyService).getRateByCode("USD");

        mockMvc.perform(get("/currencies/rates/USD"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.code").value(503))
                .andExpect(jsonPath("$.message").value("Сервис ЦБ недоступен"));
    }
}