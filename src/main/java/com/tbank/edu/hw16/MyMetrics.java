package com.tbank.edu.hw16;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class MyMetrics {

    private final Counter myCounter;

    public MyMetrics(MeterRegistry meterRegistry) {
        this.myCounter = meterRegistry.counter("my_custom_metric");
    }

    public void increment() {
        myCounter.increment();
    }
}
