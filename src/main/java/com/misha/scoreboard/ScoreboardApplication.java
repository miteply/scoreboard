package com.misha.scoreboard;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
public class ScoreboardApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(ScoreboardApplication.class, args);
		openHomePage();
	}

	// TODO to implement event listener after app is run, open web browser
	private static void openHomePage() throws IOException {
		Runtime rt = Runtime.getRuntime();
		rt.exec("rundll32 url.dll,FileProtocolHandler " + "http://localhost:8080");
	}
}