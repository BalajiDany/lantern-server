package com.project.eniac;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import lombok.extern.slf4j.Slf4j;

//Run this command
// mvn clean install eclipse:clean eclipse:eclipse -DdownloadJavadocs=true
// To test the each Search Engine run the corresponding Test files.

// API
// http://localhost:8080/eniac/api/v1/general?query=spring%20boot
// http://localhost:8080/eniac/api/v1/torrent?query=spring%20boot
// http://localhost:8080/eniac/api/v1/video?query=spring%20boot

@Slf4j
@SpringBootApplication
public class EniacApplication {

	public static void main(String... args) {
		ConfigurableApplicationContext context = SpringApplication.run(EniacApplication.class, args);
		logApplicationStartup(context);
	}

	private static void logApplicationStartup(ApplicationContext context) {
		Environment environment = context.getEnvironment();

		String hostAddress = "localhost";
		String serverPort = environment.getProperty("server.port");
		String applicationName = environment.getProperty("spring.application.name");
		String contextPath = environment.getProperty("server.servlet.context-path");
		StringBuilder profileBuilder = new StringBuilder();

		if (StringUtils.isBlank(contextPath)) contextPath = "/";
		for (String profile: environment.getActiveProfiles()) profileBuilder.append(profile);

		try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			log.warn("Unable to find host name, using 'localhost' as fallback");
		}

		log.info("---------------------------------------------------------");
		log.info("Application   : {}", applicationName);
		log.info("Server        : http://localhost:{}{}", serverPort, contextPath);
		log.info("External      : http://{}:{}{}", hostAddress, serverPort, contextPath);
		log.info("Profile(s)    : {}", profileBuilder.toString());
		log.info("---------------------------------------------------------");
	}

}
