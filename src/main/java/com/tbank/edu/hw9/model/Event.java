package com.tbank.edu.hw9.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    private List<DateRange> dates;
    private String title;
    private String description;
    private String price;
    private boolean isFree;
    private String shortTitle;

}
