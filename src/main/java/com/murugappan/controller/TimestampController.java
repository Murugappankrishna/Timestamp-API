package com.murugappan.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api")
public class TimestampController {

    @GetMapping("/{date}")
    public ResponseEntity<?> timeStamp(@PathVariable(required = false) String date) {
        try {
            Map<String, Object> response = new HashMap<>();
            SimpleDateFormat utcFormatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'");
            utcFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));

            if (date == null || date.isEmpty()) {
                Date now = new Date();
                response.put("unix", now.getTime());
                response.put("utc", utcFormatter.format(now));
                return ResponseEntity.ok(response);
            }

            if (date.matches("\\d+")) {
                // Handle numeric timestamp
                long timestamp = Long.parseLong(date);
                Date dateObj = new Date(timestamp);
                response.put("unix", timestamp);
                response.put("utc", utcFormatter.format(dateObj));
                return ResponseEntity.ok(response);
            }
            SimpleDateFormat inputFormatter = new SimpleDateFormat("dd MMMM yyyy, z");
            Date parsedDate;
            try {
                parsedDate = inputFormatter.parse(date);
            } catch (ParseException ex) {
                inputFormatter = new SimpleDateFormat("dd MMMM yyyy");
                parsedDate = inputFormatter.parse(date);
            }
            response.put("unix", parsedDate.getTime());
            response.put("utc", utcFormatter.format(parsedDate));
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid Date");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
