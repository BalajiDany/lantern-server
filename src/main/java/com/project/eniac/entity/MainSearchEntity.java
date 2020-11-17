package com.project.eniac.entity;

import lombok.Data;

@Data
public class MainSearchEntity {

	private String query;

	private String language;

	private String location;

	private long startFrom;

	private long offset;

}
