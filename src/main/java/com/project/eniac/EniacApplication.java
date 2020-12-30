package com.project.eniac;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

//Run this command
// mvn clean install eclipse:clean eclipse:eclipse -DdownloadJavadocs=true
// To test the each Search Engine run the corresponding Test files.

// API
// http://localhost:8080/eniac/api/v1/search/general?query=spring%20boot
// http://localhost:8080/eniac/api/v1/search/torrent?query=spring%20boot
// http://localhost:8080/eniac/api/v1/search/video?query=spring%20boot

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
        for (String profile : environment.getActiveProfiles()) profileBuilder.append(profile);

        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("Unable to find host name, using 'localhost' as fallback");
        }

        String localServer = "http://localhost:" + serverPort + contextPath;
        log.info("---------------------------------------------------------");
        log.info("Application   : {}", applicationName);
        log.info("Server        : {}", localServer);
        log.info("External      : http://{}:{}{}", hostAddress, serverPort, contextPath);
        log.info("Profile(s)    : {}", profileBuilder.toString());
        log.info("---------------------------------------------------------");

        Map<RequestMappingInfo, HandlerMethod> handlerMethodsMap = context.getBean(RequestMappingHandlerMapping.class)
                .getHandlerMethods();

        log.info("All Registered End point(s) - local");
        for (Map.Entry<RequestMappingInfo, HandlerMethod> handlerMethod : handlerMethodsMap.entrySet()) {
            RequestMappingInfo requestMappingInfo = handlerMethod.getKey();
            RequestMethodsRequestCondition requestMethods = requestMappingInfo.getMethodsCondition();
            for (String endpoints : requestMappingInfo.getPatternsCondition().getPatterns()) {
                log.info("{} {}{}", requestMethods, localServer, endpoints);
            }
        }

    }

}
