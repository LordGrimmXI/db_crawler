package com.vistora.db_crawler.model;

import lombok.Data;

@Data
public class IndexMetadata {
    private String name;
    private String columnName;
    private boolean isUnique;
}