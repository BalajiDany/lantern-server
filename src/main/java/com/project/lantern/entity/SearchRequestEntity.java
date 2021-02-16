package com.project.lantern.entity;

import lombok.Data;

@Data
public class SearchRequestEntity {

    private String query;

    private String language;

    private String location;

    private int pageNo;

    private long startFrom;

    private long offset;

}
