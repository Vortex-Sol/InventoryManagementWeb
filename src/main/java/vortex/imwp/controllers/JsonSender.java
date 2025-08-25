package vortex.imwp.controllers;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

public class JsonSender {

    public ResponseEntity<String> hard() {
        RestTemplate restTemplate = new RestTemplate();

        // Hardcoded JSON as a map
        Map<String, Object> json = Map.of(
                "lines", List.of(
                        Map.of("na", "Towar 1", "il", 1.0, "vt", 1, "pr", 12356),
                        Map.of("na", "Towar 2", "il", 1.0, "vt", 0, "pr", 34567)
                ),
                "summary", Map.of(
                        "to", 46923,
                        "fp", 46923
                ),
                "payments", List.of(
                        Map.of("ty", 0, "wa", 40000, "na", "Gotówka", "re", false),
                        Map.of("ty", 2, "wa", 6923, "na", "Visa ... ... 0456", "re", false)
                )
        );

        // Headers + entity
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(json, headers);

        // Correct URL format
        String url = "http://127.0.0.1:3050/paragon";
        return restTemplate.postForEntity(url, request, String.class);
    }
}
