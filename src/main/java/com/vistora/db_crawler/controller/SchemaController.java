package com.vistora.db_crawler.controller;

import com.vistora.db_crawler.config.ConfigLoader;
import com.vistora.db_crawler.model.TableMetadata;
import org.springframework.http.ResponseEntity;
import com.vistora.db_crawler.service.*;
import org.springframework.web.bind.annotation.*;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SchemaController {
    private final ConfigLoader configLoader;
    private final MetadataExtractor metadataExtractor;
    private final ModelGenerator modelGenerator;
    private final DataSource dataSource;

    public SchemaController(
        ConfigLoader configLoader,
        MetadataExtractor metadataExtractor,
        ModelGenerator modelGenerator,
        DataSource dataSource
    ) {
        this.configLoader = configLoader;
        this.metadataExtractor = metadataExtractor;
        this.modelGenerator = modelGenerator;
        this.dataSource = dataSource;
    }

    @GetMapping("/metadata")
    public List<TableMetadata> getMetadata() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            return metadataExtractor.extractMetadata(conn);
        }
    }

    @PostMapping("/generate-models")
    public ResponseEntity<String> generateModels() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            List<TableMetadata> tables = metadataExtractor.extractMetadata(conn);
            modelGenerator.generateModels(tables, configLoader.getOutputDir());
            return ResponseEntity.ok("Models generated in " + configLoader.getOutputDir());
        }
    }
}