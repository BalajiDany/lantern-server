package com.project.eniac.engine.google.service;

public interface GoogleDomainService {

	public String getDomainByLocation(String location);

	public String getDefaultDomain();
	
	public String getRandomDomain();

}
