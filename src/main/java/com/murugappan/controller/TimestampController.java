package com.murugappan.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
            // Handle Unix timestamps
            if (date.matches("\\d+")) {
                System.out.println("Line 26: " + date);
                long timestamp = Long.parseLong(date);
                Instant instant = Instant.ofEpochMilli(timestamp);
                response.put("unix", timestamp);
                response.put("utc", utcFormatter.format(instant));
                return ResponseEntity.ok(response);
            }

            // Handle ISO-8601 dates (yyyy-MM-dd)
            try {
                System.out.println("Line 35: " + date);
                LocalDate localDate = LocalDate.parse(date);
                Instant instant = localDate.atStartOfDay(ZoneId.of("UTC")).toInstant();
                response.put("unix", instant.toEpochMilli());
                response.put("utc", utcFormatter.format(instant));
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                // Attempt to parse natural language date
                System.out.println("Line 43: " + date);
                try {
                    SimpleDateFormat naturalDateFormat = new SimpleDateFormat("dd MMMM yyyy, zzz");
                    Date parsedDate = naturalDateFormat.parse(date);
                    Instant instant = parsedDate.toInstant();
                    response.put("unix", instant.toEpochMilli());
                    response.put("utc", utcFormatter.format(instant));
                    return ResponseEntity.ok(response);
                } catch (ParseException ex) {
                    throw new IllegalArgumentException("Invalid Date");
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid Date: " + date);
            response.put("error", "Invalid Date");
            return ResponseEntity.ok().body(response);
        }
    }
    @GetMapping
    public ResponseEntity<?> currentTimeStamp() {
        DateTimeFormatter utcFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'")
                .withZone(ZoneId.of("UTC"));
        Map<String, Object> response = new HashMap<>();

        Instant now = Instant.now();
        response.put("unix", now.toEpochMilli());
        response.put("utc", utcFormatter.format(now));

        return ResponseEntity.ok(response);
    }
}
