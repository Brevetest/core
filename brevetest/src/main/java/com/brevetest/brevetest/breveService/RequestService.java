package com.brevetest.brevetest.breveService;

import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class RequestService {

    private final RestTemplate restTemplate = new RestTemplate();

    public void executePost(String token, String postUrl, String postJson,
                            String expectedStatusCode, String expectedStatusMessage) {
        executeRequest(HttpMethod.POST, token, postUrl, postJson, expectedStatusCode, expectedStatusMessage, null);
    }

    public void executePut(String token, String putUrl, String putJson,
                           String expectedStatusCode, String expectedStatusMessage) {
        executeRequest(HttpMethod.PUT, token, putUrl, putJson, expectedStatusCode, expectedStatusMessage, null);
    }

    public void executeGet(String token, String getUrl,
                           String expectedStatusCode, String expectedStatusMessage,
                           String expectedResponseContains) {
        executeRequest(HttpMethod.GET, token, getUrl, null, expectedStatusCode, expectedStatusMessage, expectedResponseContains);
    }

    public void executeDelete(String token, String deleteUrl, String deleteJson,
                              String expectedStatusCode, String expectedStatusMessage,
                              String expectedResponseContains) {
        executeRequest(HttpMethod.DELETE, token, deleteUrl, deleteJson, expectedStatusCode, expectedStatusMessage, expectedResponseContains);
    }

    private void executeRequest(HttpMethod method, String token, String url, String json,
                                String expectedStatusCode, String expectedStatusMessage,
                                String expectedResponseContains) {

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