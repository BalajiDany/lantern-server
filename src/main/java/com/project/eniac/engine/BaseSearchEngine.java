package com.project.eniac.engine;

import org.apache.http.client.methods.HttpGet;

import com.project.eniac.entity.MainSearchEntity;
import com.project.eniac.types.EngineCategory;

public interface BaseSearchEngine<T> {

	HttpGet getRequest(MainSearchEntity mainSearchEntity);

	T getResponse(String reponse);
	
	String getEngineName();
	
	EngineCategory getEngineCategory();
}
