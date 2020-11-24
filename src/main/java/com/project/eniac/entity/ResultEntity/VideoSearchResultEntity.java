package com.project.eniac.entity.ResultEntity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoSearchResultEntity {

	private String title;

	private String content;

	private String url;

	private String uploadedDate;

	private String duration;

	private String thumpnailUrl;

}