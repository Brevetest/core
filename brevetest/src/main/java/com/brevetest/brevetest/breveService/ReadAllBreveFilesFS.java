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
                            config.get("expected_status_code"),
                            config.get("expected_status_message")
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
