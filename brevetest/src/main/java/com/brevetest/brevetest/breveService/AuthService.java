package com.brevetest.brevetest.breveService;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AuthService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String authenticate(String username, String password, String authUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = Map.of("username", username, "password", password);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(authUrl, request, Map.class);

        System.out.println("Auth Status: " + response.getStatusCode().value());

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return (String) response.getBody().get("accessToken");
        }
        throw new RuntimeException("Authentication failed: " + response.getStatusCode());
    }
}