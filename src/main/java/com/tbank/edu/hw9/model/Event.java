package com.tbank.edu.hw9.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("is_free")
    private boolean isFree;
    @JsonProperty("favorites_count")
    private Integer favoritesCount;
    @JsonProperty("short_title")
    private String shortTitle;
}
