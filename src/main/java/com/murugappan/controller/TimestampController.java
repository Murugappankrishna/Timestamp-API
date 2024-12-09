package com.murugappan.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.*;
import java.time.format.*;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Allow all origins
public class TimestampController {

    @GetMapping("/{date}")
    public ResponseEntity<?> timeStamp(@PathVariable(required = false) String date) {
        DateTimeFormatter utcFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'")
                .withZone(ZoneOffset.UTC);

        try {
            Map<String, Object> response = new HashMap<>();

            if (date == null || date.isEmpty()) {
                // Handle empty date -> current time
                Instant now = Instant.now();
                response.put("unix", now.toEpochMilli());
                response.put("utc", utcFormatter.format(now));
                return ResponseEntity.ok(response);
            }

            // Handle numeric timestamps
            if (date.matches("\\d+")) {
                long timestamp = Long.parseLong(date);
                Instant instant = Instant.ofEpochMilli(timestamp);
                response.put("unix", timestamp);
                response.put("utc", utcFormatter.format(instant));
                return ResponseEntity.ok(response);
            }

            // Handle natural language dates and other formats
            try {
                // Parse using ZonedDateTime for flexible formats
                ZonedDateTime parsedDate = ZonedDateTime.parse(date, DateTimeFormatter.ofPattern("dd MMMM yyyy[, zzzz]")
                        .withZone(ZoneId.of("GMT")));
                response.put("unix", parsedDate.toInstant().toEpochMilli());
                response.put("utc", utcFormatter.format(parsedDate.toInstant()));
                return ResponseEntity.ok(response);
            } catch (DateTimeParseException ignored) {
                // Try with LocalDate
                try {
                    LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
                    Instant instant = localDate.atStartOfDay(ZoneOffset.UTC).toInstant();
                    response.put("unix", instant.toEpochMilli());
                    response.put("utc", utcFormatter.format(instant));
                    return ResponseEntity.ok(response);
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Invalid Date");
                }
            }
        } catch (Exception e) {
            // Handle invalid date
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid Date");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
