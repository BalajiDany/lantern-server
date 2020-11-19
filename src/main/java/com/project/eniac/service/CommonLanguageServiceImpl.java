package com.project.eniac.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.project.eniac.service.spec.CommonLanguageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CommonLanguageServiceImpl implements CommonLanguageService {

	private static final Map<String, String> SUPPORTED_LANGUAGE = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("en", "English");
			put("de", "German");
			put("ru", "Russian");
			put("es", "Spanish");
			put("fr", "French");
			put("ja", "Japanese");
			put("pt", "Portuguese");
			put("it", "Italian");
			put("fa", "Persian");
			put("zh", "Chinese"); // TODO confirm the Chinese code
		}
	};

	@Value("${project.eniac.configuration.language:en}")
	private String defaultLanguage;

	@PostConstruct
	public void init() {
		// To ensure configured language is correct.
		boolean invalidLanguage = !this.isValidLanguage(defaultLanguage);
		if (invalidLanguage) {
			log.error("Default Language mentioned in properties is not supported - {}", defaultLanguage);
			this.defaultLanguage = "en";
		}
		log.info("Enabling default language - {}", SUPPORTED_LANGUAGE.get(this.defaultLanguage));
	}

	@Override
	public String getDefaultLanguage() {
		return this.defaultLanguage;
	}

	@Override
	public boolean isValidLanguage(String sampleLanguage) {
		return SUPPORTED_LANGUAGE.containsKey(sampleLanguage);
	}

	@Override
	public String getSupportedLanguage(String sampleLanguage) {
		return this.isValidLanguage(sampleLanguage) ? sampleLanguage : this.getDefaultLanguage();
	}

	@Override
	public Map<String, String> getAllSupportedLanguage() {
		return SUPPORTED_LANGUAGE;
	}

}
