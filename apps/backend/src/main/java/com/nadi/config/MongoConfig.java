package com.nadi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB configuration
 * Enables geospatial indexes and repositories
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.nadi")
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "nadi";
    }

    // TODO: Add custom MongoDB configuration if needed
    // TODO: Configure geospatial indexes
    // TODO: Add custom converters if needed
}
