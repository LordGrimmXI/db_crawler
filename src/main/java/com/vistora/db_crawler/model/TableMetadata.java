package com.vistora.db_crawler.model;

import lombok.Data;
import java.util.List;

@Data
public class TableMetadata {
    private String name;
    private List<ColumnMetadata> columns;
    private List<ForeignKeyMetadata> foreignKeys;
    private List<IndexMetadata> indexes;
    private String relationshipType;
}