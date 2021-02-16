package com.project.lantern.entity.EngineResultEntity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TorrentSearchResultEntity {

    private String torrentName;

    private String torrentSize;

    private String torrentUrl;

    private String magneticLink;

    private String uploadedDate;

    private String category;

    private int seeders;

    private int leechers;

}
