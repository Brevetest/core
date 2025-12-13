package com.brevetest.brevetest;

import com.brevetest.brevetest.breveService.ReadAllBreveFilesFS;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BrevetestApplication implements CommandLineRunner {

	private final ReadAllBreveFilesFS breveReader;

	public BrevetestApplication(ReadAllBreveFilesFS breveReader) {
		this.breveReader = breveReader;
	}

	public static void main(String[] args) {
		SpringApplication.run(BrevetestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		breveReader.runBreve();
	}
}
