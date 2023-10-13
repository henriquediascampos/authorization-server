package com.br.authorizationserver.core.liquibase;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import com.br.authorizationserver.core.Exceptions.DriverNotCompatibleException;
import com.br.authorizationserver.core.Exceptions.PropertyNotFoundException;
import com.zaxxer.hikari.HikariDataSource;

import liquibase.integration.spring.SpringLiquibase;

@Configuration
public class LiquibaseConfiguration extends SchemaConfig {

    @Bean
    @LiquibaseDataSource
    public HikariDataSource liquibaseDataSource(DataSourceProperties dataSourceProperties)
            throws SQLException, DriverNotCompatibleException, PropertyNotFoundException {

        HikariDataSource inicialDateSource = createDataSource(dataSourceProperties);
        dataBaseType(inicialDateSource);

        if (!StringUtils.hasLength(schema)) {
            throw new PropertyNotFoundException(String.format(
                    "Propertie '%s' not found, defined in application.properties file a default schema ( %s )",
                    PropertyNotFoundException.attention("default-schema"),
                    PropertyNotFoundException
                            .solution("add em application.properties file: default-schema:schemaName")));
        }

        if (inicialDateSource.getUsername().equalsIgnoreCase(schema)) {
            return inicialDateSource;
        }

        createShemaDefault(inicialDateSource);
        inicialDateSource.close();
        HikariDataSource ds = createDataSource(dataSourceProperties);
        if (databaseType.isOracle()) {
            ds.setUsername(schema);
        }
        if (!databaseType.isH2()) {
            ds.setSchema(schema);
        }
        return ds;
    }

    @Bean
    public SpringLiquibase liquibase(HikariDataSource dataSource) throws SQLException {
        final SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:liquibase/liquibase.yml");
        liquibase.setDataSource(dataSource);

        liquibase.setDefaultSchema(schema);
        liquibase.setChangeLogParameters(generateProperties());

        return liquibase;
    }

    private Map<String, String> generateProperties() {
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("schema", schema);
        parameters.put("checkSchema",
                databaseType.isOracle() ? " 1=1" : String.format(" table_schema = '%s'", (schema)));
        return parameters;
    }

    private HikariDataSource createDataSource(DataSourceProperties dataSourceProperties) {
        HikariDataSource ds = dataSourceProperties
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
        ds.setPoolName(schema);
        ds.setMaximumPoolSize(5);
        return ds;
    }
}
