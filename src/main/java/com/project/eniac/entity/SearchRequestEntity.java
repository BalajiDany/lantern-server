package com.project.eniac.entity;

import lombok.Data;

@Data
public class SearchRequestEntity {

	private String query;

	private String language;

	private String location;

	private long startFrom;

	private long offset;

}
