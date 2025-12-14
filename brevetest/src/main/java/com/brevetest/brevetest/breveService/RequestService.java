package com.brevetest.brevetest.breveService;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class RequestService {

    private final RestTemplate restTemplate = new RestTemplate();

    public void executePost(String token, String postUrl, String postJson,
                            String expectedStatusCode, String expectedStatusMessage) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        String jsonBody = (postJson == null || postJson.isEmpty()) ? "{}" : postJson;
        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(postUrl, request, String.class);
            validateResponse(response, expectedStatusCode, expectedStatusMessage);
            System.out.println("Response Body: " + response.getBody());
        } catch (HttpClientErrorException e) {
            System.out.println("Request Failed!");
            System.out.println("Status Code: " + e.getStatusCode().value());
            System.out.println("Response: " + e.getResponseBodyAsString());
            throw e;
        }
    }

    private void validateResponse(ResponseEntity<String> response,
                                  String expectedStatusCode, String expectedStatusMessage) {

        int actualCode = response.getStatusCode().value();
        String actualMessage = response.getStatusCode().toString();

        System.out.println("Status Code: " + actualCode);
        System.out.println("Status Message: " + actualMessage);

        if (expectedStatusCode != null && !expectedStatusCode.isEmpty()) {
            int expected = Integer.parseInt(expectedStatusCode);
            if (actualCode != expected) {
                throw new AssertionError("Status code mismatch! Expected: " + expected + ", Actual: " + actualCode);
            }
            System.out.println("✓ Status code matched: " + expected);
        }

        if (expectedStatusMessage != null && !expectedStatusMessage.isEmpty()) {
            if (!actualMessage.toLowerCase().contains(expectedStatusMessage.toLowerCase())) {
                throw new AssertionError("Status message mismatch! Expected to contain: " + expectedStatusMessage + ", Actual: " + actualMessage);
            }
            System.out.println("✓ Status message matched: " + expectedStatusMessage);
        }
    }
}