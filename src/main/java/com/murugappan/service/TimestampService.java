package com.murugappan.service;

import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TimestampService {

    public Map<String, Object> processTimestamp(String date) {
        DateTimeFormatter utcFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'")
                .withZone(ZoneId.of("UTC"));
        Map<String, Object> response = new HashMap<>();

        try {
            if (date.matches("\\d+")) {
                long timestamp = Long.parseLong(date);
                Instant instant = Instant.ofEpochMilli(timestamp);
                response.put("unix", timestamp);
                response.put("utc", utcFormatter.format(instant));
                return response;
            }
            try {
                LocalDate localDate = LocalDate.parse(date);
                Instant instant = localDate.atStartOfDay(ZoneId.of("UTC")).toInstant();
                response.put("unix", instant.toEpochMilli());
                response.put("utc", utcFormatter.format(instant));
                return response;
            } catch (Exception e) {
                try {
                    SimpleDateFormat naturalDateFormat = new SimpleDateFormat("dd MMMM yyyy, zzz");
                    Date parsedDate = naturalDateFormat.parse(date);
                    Instant instant = parsedDate.toInstant();
                    response.put("unix", instant.toEpochMilli());
                    response.put("utc", utcFormatter.format(instant));
                    return response;
                } catch (ParseException ex) {
                    throw new IllegalArgumentException("Invalid Date");
                }
            }
        } catch (Exception e) {
            response.put("error", "Invalid Date");
            return response;
        }
    }

    public Map<String, Object> getCurrentTimestamp() {
        DateTimeFormatter utcFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'")
                .withZone(ZoneId.of("UTC"));
        Map<String, Object> response = new HashMap<>();

        Instant now = Instant.now();
        response.put("unix", now.toEpochMilli());
        response.put("utc", utcFormatter.format(now));

        return response;
    }
}
