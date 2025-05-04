package com.vistora.db_crawler.model;

import lombok.Data;

@Data
public class ColumnMetadata {
    private String name;
    private String type;
    private boolean isNullable;
    private boolean isPrimaryKey;
}