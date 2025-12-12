package com.brevetest.brevetest.breveService;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;

@Component
public class ReadAllBreveFilesFS {

    public void runBreve() throws IOException {
        Path breveDir = Paths.get("src/test/breve");

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(breveDir, "*.breve")) {
            for (Path file : stream) {
                System.out.println("=== Reading: " + file.getFileName() + " ===");
                Files.readAllLines(file).forEach(System.out::println);
            }
        }
    }
}
