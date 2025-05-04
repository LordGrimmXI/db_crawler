package com.vistora.db_crawler.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.vistora.db_crawler.model.*;

@Slf4j
@Service
public class MetadataExtractor {
    public List<TableMetadata> extractMetadata(Connection connection) throws SQLException {
        log.info("Extracting metadata...");
        DatabaseMetaData meta = connection.getMetaData();
        List<TableMetadata> tables = new ArrayList<>();

        try (ResultSet rs = meta.getTables(null, null, "%", new String[]{"TABLE"})) {
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                log.info("Found table: {}", tableName);
                TableMetadata table = new TableMetadata();
                table.setName(tableName);
                table.setColumns(extractColumns(meta, tableName));
                table.setForeignKeys(extractForeignKeys(meta, tableName));
                table.setIndexes(extractIndexes(meta, tableName));
                tables.add(table);
            }
        }
        return tables;
    }

    private List<ColumnMetadata> extractColumns(DatabaseMetaData meta, String tableName) throws SQLException {
        List<ColumnMetadata> columns = new ArrayList<>();
        try (ResultSet rs = meta.getColumns(null, null, tableName, null)) {
            while (rs.next()) {
                ColumnMetadata column = new ColumnMetadata();
                column.setName(rs.getString("COLUMN_NAME"));
                column.setType(rs.getString("TYPE_NAME"));
                column.setNullable("YES".equals(rs.getString("IS_NULLABLE")));
                columns.add(column);
            }
        }
    
        try (ResultSet rs = meta.getPrimaryKeys(null, null, tableName)) {
            while (rs.next()) {
                String pkColumn = rs.getString("COLUMN_NAME");
                columns.stream()
                    .filter(c -> c.getName().equals(pkColumn))
                    .forEach(c -> c.setPrimaryKey(true));
            }
        }
        return columns;
    }

    private List<ForeignKeyMetadata> extractForeignKeys(DatabaseMetaData meta, String tableName) throws SQLException {
        List<ForeignKeyMetadata> fks = new ArrayList<>();
        try (ResultSet rs = meta.getImportedKeys(null, null, tableName)) {
            while (rs.next()) {
                ForeignKeyMetadata fk = new ForeignKeyMetadata();
                fk.setConstraintName(rs.getString("FK_NAME"));
                fk.setSourceTable(tableName);
                fk.setSourceColumn(rs.getString("FKCOLUMN_NAME"));
                fk.setTargetTable(rs.getString("PKTABLE_NAME"));
                fk.setTargetColumn(rs.getString("PKCOLUMN_NAME"));
                fks.add(fk);
            }
        }
        return fks;
    }

    private List<IndexMetadata> extractIndexes(DatabaseMetaData meta, String tableName) throws SQLException {
        List<IndexMetadata> indexes = new ArrayList<>();
        
        try (ResultSet rs = meta.getIndexInfo(null, null, tableName, false, false)) {
            while (rs.next()) {
                String indexName = rs.getString("INDEX_NAME");

                if ("PRIMARY".equalsIgnoreCase(indexName)) continue;

                IndexMetadata index = new IndexMetadata();
                index.setName(indexName);
                index.setColumnName(rs.getString("COLUMN_NAME"));
                index.setUnique(!rs.getBoolean("NON_UNIQUE"));
                indexes.add(index);
            }
        }
        return indexes;
    }
}