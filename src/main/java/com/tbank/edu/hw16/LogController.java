package com.tbank.edu.hw16;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LogController {
    private final LogGenerator generator;

    @GetMapping("/generate")
    public ResponseEntity test(@RequestParam(name = "count", defaultValue = "0") Integer count) {
        log.info("Test request received with count: {}", count);
        generator.generate(count);
        return ResponseEntity.ok("Success!");
    }

    @GetMapping("/log")
    public String log(@RequestParam(name = "customField", defaultValue = "customField") String customField) {
        MDC.put("customField", customField);
        return "Logged with custom field: " + customField;
    }
}
