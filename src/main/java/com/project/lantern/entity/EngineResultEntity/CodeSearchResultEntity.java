package com.project.lantern.entity.EngineResultEntity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CodeSearchResultEntity {

    private String title;

    private String url;

    private String content;

}
