package com.brevetest.brevetest;

import com.brevetest.brevetest.breveService.ReadAllBreveFilesFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class BrevetestApplication implements CommandLineRunner {

	@Autowired
	private ReadAllBreveFilesFS readAllBreveFilesFS;

	public static void main(String[] args) {
		SpringApplication.run(BrevetestApplication.class, args);
	}

	@Override
	public void run(String... args) throws IOException {
		readAllBreveFilesFS.runBreve();
	}
}
