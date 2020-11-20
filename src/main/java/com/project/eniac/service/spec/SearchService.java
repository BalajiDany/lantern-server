package com.project.eniac.service.spec;

import java.util.List;

import com.project.eniac.entity.MainSearchEntity;
import com.project.eniac.entity.ResultEntity.GeneralSearchResultEntity;

public interface SearchService {
	
	List<GeneralSearchResultEntity> searchAll(MainSearchEntity searchEntity);

}
