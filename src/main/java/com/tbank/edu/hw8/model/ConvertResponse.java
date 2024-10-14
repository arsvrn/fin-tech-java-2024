package com.tbank.edu.hw8.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConvertResponse {
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal convertedAmount;
}
