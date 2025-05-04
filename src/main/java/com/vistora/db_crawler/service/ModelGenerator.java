package com.vistora.db_crawler.service;

import com.vistora.db_crawler.model.*;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModelGenerator {
    public void generateModels(List<TableMetadata> tables, String outputDir) {
        new File(outputDir).mkdirs();

        for (TableMetadata table : tables) {
            if (isJoinTable(table)) {
                continue;
            }

            String className = sanitizeClassName(table.getName());
            File file = new File(outputDir + "/" + className + ".java");

            try (FileWriter writer = new FileWriter(file)) {
                writer.write("import lombok.Data;\n");
                writer.write("import javax.persistence.*;\n");
                writer.write("import java.util.List;\n\n");
                
                writer.write("@Data\n@Entity\n@Table(name = \"" + table.getName() + "\")\n");
                writer.write("public class " + className + " {\n\n");

                writer.write("    @Id\n");
                writer.write("    @GeneratedValue(strategy = GenerationType.IDENTITY)\n");
                ColumnMetadata idColumn = table.getColumns().stream()
                    .filter(ColumnMetadata::isPrimaryKey)
                    .findFirst()
                    .orElse(null);
                if (idColumn != null) {
                    writer.write("    private " + mapToJavaType(idColumn.getType()) + " " + idColumn.getName() + ";\n\n");
                }

                for (ColumnMetadata column : table.getColumns()) {
                    if (!column.isPrimaryKey()) {
                        writer.write("    @Column(name = \"" + column.getName() + "\")\n");
                        writer.write("    private " + mapToJavaType(column.getType()) + " " + column.getName() + ";\n");
                    }
                }

                for (ForeignKeyMetadata fk : table.getForeignKeys()) {
                    String targetClass = sanitizeClassName(fk.getTargetTable());
                    String fieldName = sanitizeFieldName(fk.getTargetTable());

                    if (isOneToMany(table, fk)) {
                        writer.write("\n    @OneToMany(mappedBy = \"" + getMappedByProperty(table, fk) + "\")\n");
                        writer.write("    private List<" + targetClass + "> " + fieldName + "List;\n");
                    } else {
                        writer.write("\n    @ManyToOne\n");
                        writer.write("    @JoinColumn(name = \"" + fk.getSourceColumn() + "\")\n");
                        writer.write("    private " + targetClass + " " + fieldName + ";\n");
                    }
                }

                generateManyToManyRelationships(writer, table, tables);
                writer.write("}\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void generateManyToManyRelationships(FileWriter writer, TableMetadata table, List<TableMetadata> allTables) throws Exception {
        for (TableMetadata joinTable : allTables.stream()
                .filter(this::isJoinTable)
                .collect(Collectors.toList())) {
            
            List<ForeignKeyMetadata> fks = joinTable.getForeignKeys();
            if (fks.size() == 2) {
                ForeignKeyMetadata fk1 = fks.get(0);
                ForeignKeyMetadata fk2 = fks.get(1);

                if (fk1.getTargetTable().equals(table.getName())) {
                    String targetClass = sanitizeClassName(fk2.getTargetTable());
                    writer.write("\n    @ManyToMany\n");
                    writer.write("    @JoinTable(\n");
                    writer.write("        name = \"" + joinTable.getName() + "\",\n");
                    writer.write("        joinColumns = @JoinColumn(name = \"" + fk1.getSourceColumn() + "\"),\n");
                    writer.write("        inverseJoinColumns = @JoinColumn(name = \"" + fk2.getSourceColumn() + "\")\n");
                    writer.write("    )\n");
                    writer.write("    private List<" + targetClass + "> " + sanitizeFieldName(targetClass) + "List;\n");
                }
            }
        }
    }

    private boolean isJoinTable(TableMetadata table) {
        return table.getForeignKeys().size() == 2 && table.getColumns().stream().allMatch(c -> c.isPrimaryKey() || table.getForeignKeys().stream().anyMatch(fk -> fk.getSourceColumn().equals(c.getName())));
    }

    private boolean isOneToMany(TableMetadata table, ForeignKeyMetadata fk) {
        return !table.getColumns().stream()
            .filter(c -> c.getName().equals(fk.getSourceColumn()))
            .findFirst()
            .map(ColumnMetadata::isPrimaryKey)
            .orElse(false);
    }

    private String getMappedByProperty(TableMetadata sourceTable, ForeignKeyMetadata fk) {
        return sanitizeFieldName(sourceTable.getName());
    }

    private String sanitizeFieldName(String name) {
        return name.substring(0, 1).toLowerCase() + name.substring(1).replaceAll("_", "");
    }

    private String sanitizeClassName(String tableName) {
        return tableName.substring(0, 1).toUpperCase() + tableName.substring(1).toLowerCase();
    }

    private String mapToJavaType(String sqlType) {
        switch (sqlType.toUpperCase()) {
            case "VARCHAR": return "String";
            case "INT": return "Integer";
            case "DATETIME": return "LocalDateTime";
            case "BOOLEAN": return "Boolean";
            default: return "Object";
        }
    }
}