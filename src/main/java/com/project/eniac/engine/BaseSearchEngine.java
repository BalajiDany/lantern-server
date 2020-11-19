package com.project.eniac.engine;

import java.util.List;

import org.apache.http.client.methods.HttpGet;

import com.project.eniac.entity.MainSearchEntity;
import com.project.eniac.types.EngineCategory;

public interface BaseSearchEngine<T> {

	HttpGet getRequest(MainSearchEntity mainSearchEntity);

	List<T> getResponse(String reponse);
	
	String getEngineName();
	
	EngineCategory getEngineCategory();
}
