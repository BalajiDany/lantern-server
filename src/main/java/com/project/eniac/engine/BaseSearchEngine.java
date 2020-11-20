package com.project.eniac.engine;

import org.apache.http.client.methods.HttpGet;

import com.project.eniac.entity.MainSearchEntity;
import com.project.eniac.entity.ResultEntity.SearchResultEntity;
import com.project.eniac.types.EngineType;

public interface BaseSearchEngine<T> {

	HttpGet getRequest(MainSearchEntity mainSearchEntity);

	SearchResultEntity<T> getResponse(String response);
	
	String getEngineName();
	
	EngineType getEngineType();
}
