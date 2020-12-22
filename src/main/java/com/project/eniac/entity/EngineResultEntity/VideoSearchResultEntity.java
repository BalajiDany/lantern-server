package com.project.eniac.entity.EngineResultEntity;

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

    private String thumbnailUrl;

}
