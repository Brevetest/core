package com.brevetest.brevetest.breveService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class ReadAllBreveFilesFS {

    @Autowired
    private AuthService authService;

    @Autowired
    private RequestService requestService;

    public void runBreve() throws IOException {
        Path breveDir = Paths.get("src/test/breve");

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(breveDir, "*.breve")) {
            for (Path file : stream) {
                System.out.println("=== Processing: " + file.getFileName() + " ===");

                Map<String, String> config = parseBreveFile(file);

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
                            config.get("post_expected_status_message")
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
                            config.get("put_expected_status_message")
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
                            config.get("get_expected_response_contains")
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
                            config.get("delete_expected_response_contains")
                    );
                }

                System.out.println("=== Completed: " + file.getFileName() + " ===\n");
            }
        }
    }

    private Map<String, String> parseBreveFile(Path file) throws IOException {
        Map<String, String> config = new HashMap<>();
        for (String line : Files.readAllLines(file)) {
            String[] parts = line.split("=", 2);
            if (parts.length == 2) {
                config.put(parts[0].trim(), parts[1].trim());
            }
        }
        return config;
    }
}
