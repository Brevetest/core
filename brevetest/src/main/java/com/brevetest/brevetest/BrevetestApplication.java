package com.brevetest.brevetest;

import com.brevetest.brevetest.breveService.ReadAllBreveFilesFS;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BrevetestApplication implements CommandLineRunner {

	private final ReadAllBreveFilesFS breveReader = new ReadAllBreveFilesFS();

	public static void main(String[] args) {
		SpringApplication.run(BrevetestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		breveReader.runBreve();
	}
}
