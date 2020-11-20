package com.project.eniac.entity.ResultEntity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TorrentSearchResultEntity {

	String torrentName;

	String torrentSize;

	String torrentUrl;

	String magneticLink;

	String uploadedDate;

	String category;

	int seeders;

	int leechers;

}
