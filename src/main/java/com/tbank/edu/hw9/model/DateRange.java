package com.tbank.edu.hw9.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
public class DateRange {
    private long start;
    private long end;

    public DateRange(Instant now, Instant end) {
        this.start = now.getEpochSecond();
        this.end = end.getEpochSecond();
    }

    public Instant getStart() {
        return Instant.ofEpochSecond(start);
    }

    public void setStart(long start) {
        this.start = start;
    }

    public Instant getEnd() {
        return Instant.ofEpochSecond(end);
    }

    public void setEnd(long end) {
        this.end = end;
    }
}
