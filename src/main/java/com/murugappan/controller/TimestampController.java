package com.murugappan.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Allow all origins
public class TimestampController {

    @GetMapping("/{date}")
    public ResponseEntity<?> timeStamp(@PathVariable String date) {
        DateTimeFormatter utcFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'")
                .withZone(ZoneId.of("UTC"));
        Map<String, Object> response = new HashMap<>();

        try {
            if (date.matches("\\d+")) {
                System.out.println( "Line 26"+date);
                long timestamp = Long.parseLong(date);
                Instant instant = Instant.ofEpochMilli(timestamp);
                response.put("unix", timestamp);
                response.put("utc", utcFormatter.format(instant));
                return ResponseEntity.ok(response);
            }

            try {
                System.out.println( "Line 35"+date);
                LocalDate localDate = LocalDate.parse(date);
                Instant instant = localDate.atStartOfDay(ZoneId.of("UTC")).toInstant();
                response.put("unix", instant.toEpochMilli());
                response.put("utc", utcFormatter.format(instant));
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                try {
                    System.out.println( "Line 43"+date);
                    Instant instant = Instant.parse(date);
                    response.put("unix", instant.toEpochMilli());
                    response.put("utc", utcFormatter.format(instant));
                    return ResponseEntity.ok(response);
                } catch (Exception ex) {
                    throw new IllegalArgumentException("Invalid Date");
                }
            }
        } catch (Exception e) {
            response.put("error", "Invalid Date");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
