package com.project.eniac.utils;

import java.util.List;

import com.project.eniac.entity.EngineResultEntity.GeneralSearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.SearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.TorrentSearchResultEntity;
import com.project.eniac.entity.EngineResultEntity.VideoSearchResultEntity;
import com.project.eniac.types.EngineResultType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Printer {

	public static void printGeneralSearchResultEntity(SearchResultEntity<GeneralSearchResultEntity> entity) {
		printBaseAndGetResult(entity);
		if (entity.getEngineResultType() == EngineResultType.FOUND_SEARCH_RESULT) {
			List<GeneralSearchResultEntity> searchResultList = entity.getSearchResult();
			log.info("Result Count  : " + searchResultList.size());

			int KEY_LENGTH = 12;
			for (GeneralSearchResultEntity searchResult : searchResultList) {
				log.info("");
				log.info(fixedLengthString("Title : ", KEY_LENGTH) + searchResult.getTitle());
				log.info(fixedLengthString("URL : ", KEY_LENGTH) + searchResult.getUrl());
				log.info(fixedLengthString("Content : ", KEY_LENGTH) + searchResult.getContent());
			}
		}
	}

	public static void printTorrentSearchResultEntity(SearchResultEntity<TorrentSearchResultEntity> entity) {
		printBaseAndGetResult(entity);
		if (entity.getEngineResultType() == EngineResultType.FOUND_SEARCH_RESULT) {
			List<TorrentSearchResultEntity> searchResultList = entity.getSearchResult();
			log.info("Result Count  : " + searchResultList.size());

			int KEY_LENGTH = 16;
			for (TorrentSearchResultEntity searchResult : searchResultList) {
				log.info("");
				log.info(fixedLengthString("Torrent Name : ", KEY_LENGTH)+ searchResult.getTorrentName());
				log.info(fixedLengthString("Torrent Size : ", KEY_LENGTH) + searchResult.getTorrentSize());
				log.info(fixedLengthString("Category : ", KEY_LENGTH) + searchResult.getCategory());
				log.info(fixedLengthString("Torrent URL : ", KEY_LENGTH) + searchResult.getTorrentUrl());
				log.info(fixedLengthString("Magnetic Link : ", KEY_LENGTH) + searchResult.getMagneticLink());
				log.info(fixedLengthString("Upload Date : ", KEY_LENGTH) + searchResult.getUploadedDate());
				log.info(fixedLengthString("Seeders : ", KEY_LENGTH) + searchResult.getSeeders());
				log.info(fixedLengthString("Leechers : ", KEY_LENGTH) + searchResult.getLeechers());
			}
		}
	}

	public static void printVideoSearchResultEntity(SearchResultEntity<VideoSearchResultEntity> entity) {
		printBaseAndGetResult(entity);
		if (entity.getEngineResultType() == EngineResultType.FOUND_SEARCH_RESULT) {
			List<VideoSearchResultEntity> searchResultList = entity.getSearchResult();
			log.info("Result Count  : " + searchResultList.size());

			int KEY_LENGTH = 16;
			for (VideoSearchResultEntity searchResult : searchResultList) {
				log.info("");
				log.info(fixedLengthString("Title : ", KEY_LENGTH)+ searchResult.getTitle());
				log.info(fixedLengthString("Content : ", KEY_LENGTH) + searchResult.getContent());
				log.info(fixedLengthString("URL : ", KEY_LENGTH) + searchResult.getUrl());
				log.info(fixedLengthString("Upload Date : ", KEY_LENGTH) + searchResult.getUploadedDate());
				log.info(fixedLengthString("Duration : ", KEY_LENGTH) + searchResult.getDuration());
				log.info(fixedLengthString("Thumbnail : ", KEY_LENGTH) + searchResult.getThumbnailUrl());
			}
		}
	}

	private static <T> void printBaseAndGetResult(SearchResultEntity<T> entity) {
		log.info("");
		log.info("Engine Name   : " + entity.getEngineName());
		log.info("Engine Type   : " + entity.getEngineType());
		log.info("Engine Result : " + entity.getEngineResultType());
	}

	public static String fixedLengthString(String string, int length) {
	    return String.format("%1$"+length+ "s", string);
	}

}
