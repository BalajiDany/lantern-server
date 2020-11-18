package com.project.eniac.engine.google.service;

public interface GoogleLanguageService {
	
	String getValidLanguage(String sampleLanguage);
	
	String getDefaultLanguage();
	
	String getRandomLanguage();
}
