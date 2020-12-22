package com.project.eniac.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:eniac.properties")
public class CustomPropertySourceConfiguration {

}
