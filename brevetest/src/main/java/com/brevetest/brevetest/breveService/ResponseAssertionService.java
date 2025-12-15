package com.brevetest.brevetest.breveService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class ResponseAssertionService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Validates response body against a list of assertions.
     * Each assertion is in format: "response.field.path == \"expected_value\""
     *
     * @param responseBody The JSON response body as a string
     * @param assertions List of assertion strings
     * @throws AssertionError if any assertion fails (fail-fast)
     */
    public void validate(String responseBody, List<String> assertions) {
        if (assertions == null || assertions.isEmpty()) {
            return;
        }

        if (responseBody == null || responseBody.trim().isEmpty()) {
            throw new AssertionError("Response body is empty, cannot validate assertions");
        }

        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(responseBody);
        } catch (Exception e) {
            throw new AssertionError("Response is not valid JSON: " + e.getMessage());
        }

        for (String assertion : assertions) {
            validateSingleAssertion(rootNode, assertion);
        }
    }

    private void validateSingleAssertion(JsonNode rootNode, String assertion) {
        // Parse: "response.field.path == \"expected_value\""
        String[] parts = assertion.split("==", 2);
        if (parts.length != 2) {
            throw new AssertionError("Invalid assertion format: " + assertion + "\nExpected: response.field == \"value\"");
        }

        String fieldPath = parts[0].trim();
        String expectedValue = parts[1].trim();

        // Remove "response." prefix
        if (fieldPath.startsWith("response.")) {
            fieldPath = fieldPath.substring("response.".length());
        }

        // Remove quotes from expected value
        if (expectedValue.startsWith("\"") && expectedValue.endsWith("\"")) {
            expectedValue = expectedValue.substring(1, expectedValue.length() - 1);
        }

        // Navigate to the field using dot notation
        String actualValue = getFieldValue(rootNode, fieldPath);

        // Compare
        if (!expectedValue.equals(actualValue)) {
            throw new AssertionError(
                    "\n✗ Assertion failed: response." + fieldPath +
                    "\n  Expected: \"" + expectedValue + "\"" +
                    "\n  Actual:   \"" + actualValue + "\""
            );
        }

        System.out.println("✓ response." + fieldPath + " == \"" + expectedValue + "\"");
    }

    private String getFieldValue(JsonNode node, String fieldPath) {
        String[] pathParts = fieldPath.split("\\.");
        JsonNode currentNode = node;

        for (String part : pathParts) {
            if (currentNode == null || currentNode.isMissingNode()) {
                throw new AssertionError("Field '" + fieldPath + "' not found in response");
            }
            currentNode = currentNode.get(part);
        }

        if (currentNode == null || currentNode.isMissingNode()) {
            throw new AssertionError("Field '" + fieldPath + "' not found in response");
        }

        // Convert to string representation
        if (currentNode.isTextual()) {
            return currentNode.asText();
        } else if (currentNode.isNumber()) {
            return currentNode.asText();
        } else if (currentNode.isBoolean()) {
            return currentNode.asText();
        } else if (currentNode.isNull()) {
            return "null";
        } else {
            return currentNode.toString();
        }
    }
}
