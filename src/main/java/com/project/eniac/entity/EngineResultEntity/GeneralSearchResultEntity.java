package com.project.eniac.entity.EngineResultEntity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeneralSearchResultEntity {

	private String title;

	private String url;

	private String content;

}
