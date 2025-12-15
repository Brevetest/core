package com.brevetest.brevetest.breveService;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadAllBreveFilesFS {


    private AuthService authService = new AuthService();


    private RequestService requestService = new RequestService();

    public void runBreve() throws IOException {
        Path breveDir = Paths.get("src/test/breve");

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(breveDir, "*.breve")) {
            for (Path file : stream) {
                System.out.println("=== Processing: " + file.getFileName() + " ===");

                BreveFileContent content = parseBreveFile(file);
                Map<String, String> config = content.config;
                List<String> assertions = content.assertions;

                // Phase 1: Authenticate
                String token = authService.authenticate(
                        config.get("username"),
                        config.get("password"),
                        config.get("auth_url")
                );
                System.out.println("Token acquired: " + token.substring(0, 20) + "...");

                // Phase 2: Execute POST request (if post_url provided)
                String postUrl = config.get("post_url");
                if (postUrl != null && !postUrl.isEmpty()) {
                    System.out.println("--- Executing POST request ---");
                    requestService.executePost(
                            token,
                            postUrl,
                            config.get("post_json"),
                            config.get("post_expected_status_code"),
                            config.get("post_expected_status_message"),
                            assertions
                    );
                }

                // Phase 3: Execute PUT request (if put_url provided)
                String putUrl = config.get("put_url");
                if (putUrl != null && !putUrl.isEmpty()) {
                    System.out.println("--- Executing PUT request ---");
                    requestService.executePut(
                            token,
                            putUrl,
                            config.get("put_json"),
                            config.get("put_expected_status_code"),
                            config.get("put_expected_status_message"),
                            assertions
                    );
                }

                // Phase 4: Execute GET request (if get_url provided)
                String getUrl = config.get("get_url");
                if (getUrl != null && !getUrl.isEmpty()) {
                    System.out.println("--- Executing GET request ---");
                    requestService.executeGet(
                            token,
                            getUrl,
                            config.get("get_expected_status_code"),
                            config.get("get_expected_status_message"),
                            config.get("get_expected_response_contains"),
                            assertions
                    );
                }

                // Phase 5: Execute DELETE request (if delete_url provided)
                String deleteUrl = config.get("delete_url");
                if (deleteUrl != null && !deleteUrl.isEmpty()) {
                    System.out.println("--- Executing DELETE request ---");
                    requestService.executeDelete(
                            token,
                            deleteUrl,
                            config.get("delete_json"),
                            config.get("delete_expected_status_code"),
                            config.get("delete_expected_status_message"),
                            config.get("delete_expected_response_contains"),
                            assertions
                    );
                }

                System.out.println("=== Completed: " + file.getFileName() + " ===\n");
            }
        }
    }

    private BreveFileContent parseBreveFile(Path file) throws IOException {
        Map<String, String> config = new HashMap<>();
        List<String> assertions = new ArrayList<>();

        for (String line : Files.readAllLines(file)) {
            String trimmedLine = line.trim();

            // Skip empty lines
            if (trimmedLine.isEmpty()) {
                continue;
            }

            // Check if it's a response assertion
            if (trimmedLine.startsWith("response.")) {
                assertions.add(trimmedLine);
            } else {
                // Regular key=value config
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    config.put(parts[0].trim(), parts[1].trim());
                }
            }
        }

        return new BreveFileContent(config, assertions);
    }

    /**
     * Holds parsed content from a .breve file
     */
    private static class BreveFileContent {
        final Map<String, String> config;
        final List<String> assertions;

        BreveFileContent(Map<String, String> config, List<String> assertions) {
            this.config = config;
            this.assertions = assertions;
        }
    }
}
