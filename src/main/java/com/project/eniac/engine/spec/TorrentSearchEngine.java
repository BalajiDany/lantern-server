package com.project.eniac.engine.spec;

import com.project.eniac.entity.EngineResultEntity.TorrentSearchResultEntity;
import com.project.eniac.types.EngineType;

public abstract class TorrentSearchEngine extends BaseSearchEngine<TorrentSearchResultEntity> {

	@Override
	public EngineType getEngineType() {
		return EngineType.TORRENT;
	}

}
