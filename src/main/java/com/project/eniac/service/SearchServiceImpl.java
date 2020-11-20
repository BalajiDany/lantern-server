package com.project.eniac.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.eniac.entity.MainSearchEntity;
import com.project.eniac.entity.ResultEntity.GeneralSearchResultEntity;
import com.project.eniac.service.spec.SearchService;

@Service
public class SearchServiceImpl implements SearchService {

	@Override
	public List<GeneralSearchResultEntity> searchAll(MainSearchEntity searchEntity) {
		return null;
	}

}
