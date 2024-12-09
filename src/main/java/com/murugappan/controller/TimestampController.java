package com.murugappan.controller;

import com.murugappan.dto.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static java.time.Instant.*;

@RestController
@RequestMapping("/api")
public class TimestampController {

    @GetMapping("/{date}")
    public ResponseEntity<?> timeStamp(@PathVariable(required = false) String date) {
        ResponseDto response = new ResponseDto();
        DateTimeFormatter utcFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'")
                .withZone(java.time.ZoneOffset.UTC);

        try {
            if (date == null || date.isEmpty()) {
                Instant now = now();
                response.setUnix(now.toEpochMilli());
                response.setUtc(utcFormatter.format(now));
                return ResponseEntity.ok(response);
            }

            if (date.matches("\\d+")) {
                long timestamp = Long.parseLong(date);
                Instant instant = ofEpochMilli(timestamp);
                response.setUnix(timestamp);
                response.setUtc(utcFormatter.format(instant));
                return ResponseEntity.ok(response);
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate parsedDate = LocalDate.parse(date, formatter);
            response.setUnix(parsedDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000);
            response.setUtc(utcFormatter.format(parsedDate.atStartOfDay(ZoneOffset.UTC)));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid Date");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
