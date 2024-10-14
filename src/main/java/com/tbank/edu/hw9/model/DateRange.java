package com.tbank.edu.hw9.model;

import java.time.Instant;

public class DateRange {
    private long start;
    private long end;

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
