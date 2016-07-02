package com.example.spring.boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

/**
 * Created by pc on 2016/6/29.
 */
@Configuration
@Profile("production")
public class DataSourceConfig {

    @Bean
    public DataSource productionDataSource() {

        EmbeddedDatabase dataSource =
                new EmbeddedDatabaseBuilder()
                        .setType(EmbeddedDatabaseType.H2)
                        .addScript("productSchema.sql")
                        .ignoreFailedDrops(false)
                        .build();
        return dataSource;
    }

}
