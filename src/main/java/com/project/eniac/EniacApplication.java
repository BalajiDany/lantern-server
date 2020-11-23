package com.project.eniac;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EniacApplication {

	// Run this command
	// mvn clean install eclipse:clean eclipse:eclipse -DdownloadJavadocs=true
	// To test the each Search Engine run the corresponding Test files.

	// API
	// http://localhost:8080/general?query=spring%20boot
	// http://localhost:8080/torrent?query=spring%20boot
	// http://localhost:8080/video?query=spring%20boot
	public static void main(String... args) throws ClientProtocolException, IOException, URISyntaxException {
		SpringApplication.run(EniacApplication.class, args);
	}

}
