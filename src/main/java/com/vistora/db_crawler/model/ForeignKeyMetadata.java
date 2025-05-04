package com.vistora.db_crawler.model;

import lombok.Data;

@Data
public class ForeignKeyMetadata {
    private String constraintName;
    private String sourceTable;
    private String sourceColumn;
    private String targetTable;
    private String targetColumn;
}