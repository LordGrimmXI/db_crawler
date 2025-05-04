package com.vistora.db_crawler.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConfigLoader {
    @Value("${spring.datasource.url}") 
    private String url;
    
    @Value("${spring.datasource.username}")
    private String username;
    
    @Value("${spring.datasource.password}")
    private String password;
    
    @Value("${app.outputDir:generated-models}")
    private String outputDir;

    public String getUrl() { return url; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getOutputDir() { return outputDir; }
}