package com.project.eniac.engine;

import com.project.eniac.entity.ResultEntity.TorrentSearchResultEntity;
import com.project.eniac.types.EngineType;

public abstract class TorrentSearchEngine extends BaseSearchEngine<TorrentSearchResultEntity> {

	@Override
	public EngineType getEngineType() {
		return EngineType.TORRENT;
	}

}
