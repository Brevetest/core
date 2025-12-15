package com.brevetest.brevetest.breveService;

import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class RequestService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ResponseAssertionService assertionService = new ResponseAssertionService();

    public void executePost(String token, String postUrl, String postJson,
                            String expectedStatusCode, String expectedStatusMessage,
                            List<String> assertions) {
        executeRequest(HttpMethod.POST, token, postUrl, postJson, expectedStatusCode, expectedStatusMessage, null, assertions);
    }

    public void executePut(String token, String putUrl, String putJson,
                           String expectedStatusCode, String expectedStatusMessage,
                           List<String> assertions) {
        executeRequest(HttpMethod.PUT, token, putUrl, putJson, expectedStatusCode, expectedStatusMessage, null, assertions);
    }

    public void executeGet(String token, String getUrl,
                           String expectedStatusCode, String expectedStatusMessage,
                           String expectedResponseContains,
                           List<String> assertions) {
        executeRequest(HttpMethod.GET, token, getUrl, null, expectedStatusCode, expectedStatusMessage, expectedResponseContains, assertions);
    }

    public void executeDelete(String token, String deleteUrl, String deleteJson,
                              String expectedStatusCode, String expectedStatusMessage,
                              String expectedResponseContains,
                              List<String> assertions) {
        executeRequest(HttpMethod.DELETE, token, deleteUrl, deleteJson, expectedStatusCode, expectedStatusMessage, expectedResponseContains, assertions);
    }

    private void executeRequest(HttpMethod method, String token, String url, String json,
                                String expectedStatusCode, String expectedStatusMessage,
                                String expectedResponseContains, List<String> assertions) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<String> request;
        if (json != null && !json.isEmpty()) {
            request = new HttpEntity<>(json, headers);
        } else {
            request = new HttpEntity<>(headers);
        }

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, method, request, String.class);
            validateResponse(response, expectedStatusCode, expectedStatusMessage, expectedResponseContains);
            System.out.println("Response Body: " + response.getBody());

            // Execute response assertions
            if (assertions != null && !assertions.isEmpty()) {
                System.out.println("--- Validating response assertions ---");
                assertionService.validate(response.getBody(), assertions);
            }
        } catch (HttpClientErrorException e) {
            System.out.println("Request Failed!");
            System.out.println("Status Code: " + e.getStatusCode().value());
            System.out.println("Response: " + e.getResponseBodyAsString());
            throw e;
        }
    }

    // TODO: this method should move to a new validation class file
    private void validateResponse(ResponseEntity<String> response,
                                  String expectedStatusCode, String expectedStatusMessage,
                                  String expectedResponseContains) {

        int actualCode = response.getStatusCode().value();
        String actualMessage = response.getStatusCode().toString();
        String responseBody = response.getBody();

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

        if (expectedResponseContains != null && !expectedResponseContains.isEmpty()) {
            if (responseBody == null || !responseBody.toLowerCase().contains(expectedResponseContains.toLowerCase())) {
                throw new AssertionError("Response body does not contain: " + expectedResponseContains);
            }
            System.out.println("✓ Response contains: " + expectedResponseContains);
        }
    }
}