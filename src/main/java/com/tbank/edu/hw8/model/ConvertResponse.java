package com.tbank.edu.hw8.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConvertResponse {
    private String fromCurrency;
    private String toCurrency;
    private double convertedAmount;
}
