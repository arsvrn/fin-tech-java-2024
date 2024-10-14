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
public class EventResponse {
    private int count;
    private String next;
    private String previous;
    private List<Event> results;

}
