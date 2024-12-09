package com.murugappan.controller;

import com.murugappan.service.TimestampService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Allow all origins
public class TimestampController {

    private final TimestampService timestampService;

    public TimestampController(TimestampService timestampService) {
        this.timestampService = timestampService;
    }

    @GetMapping("/{date}")
    public ResponseEntity<?> timeStamp(@PathVariable String date) {
        Map<String, Object> response = timestampService.processTimestamp(date);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> currentTimeStamp() {
        Map<String, Object> response = timestampService.getCurrentTimestamp();
        return ResponseEntity.ok(response);
    }
}
