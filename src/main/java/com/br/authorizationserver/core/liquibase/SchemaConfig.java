package com.br.authorizationserver.core.liquibase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.br.authorizationserver.core.Exceptions.DriverNotCompatibleException;
import com.br.authorizationserver.core.Exceptions.SchemaCreationFailureException;
import com.zaxxer.hikari.HikariDataSource;

import org.springframework.beans.factory.annotation.Value;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SchemaConfig {
    @Value("${default-schema:}")
    protected String schema;
    @Value("${spring.datasource.driverClassName:}")
    protected String drive;
    @Value("${spring.jpa.database-platform:}")
    protected String platform;
    @Value("${spring.datasource.password:}")
    protected String passwordDataBase;
    
    protected EDataBaseType databaseType;
    


    protected void dataBaseType(final HikariDataSource dataSource) throws DriverNotCompatibleException {
        this.databaseType = EDataBaseType.of(dataSource.getDriverClassName());
    }

    protected void createShemaDefault(HikariDataSource dataSource) throws SQLException {
        log.info("Creating default schema if it doesn't exist...");
        if (databaseType.isH2() || databaseType.isPostgres()) {
            createPostgresSchema(dataSource);
        } else if (databaseType.isOracle()) {
            createOracleSchema(dataSource);
        }
    }

    private void createOracleSchema(HikariDataSource dataSource) throws SQLException {
        Boolean schemaNotExists = false;
        final Connection conn = dataSource.getConnection();

        try {
            log.info("checking schema exists...");
            ResultSet result = conn.prepareStatement(String
                    .format("select count(*) as exist from dba_users WHERE username = '%s'", schema.toUpperCase()))
                    .executeQuery();
            result.next();
            schemaNotExists = result.getInt("exist") == 0;
        } catch (SQLException e) {
            throw new SchemaCreationFailureException(
                    "Possibly your user does not have permission to select in the table DBA_USERS", e);
        }

        try {
            if (schemaNotExists) {
                log.info("creating schema " + schema);
                String sqlschema = String.format("CREATE USER %s IDENTIFIED BY %s", schema, passwordDataBase);
                String sqlgrant = String.format("GRANT ALL PRIVILEGES TO %s", schema);

                conn.prepareStatement(sqlschema).execute();
                conn.prepareStatement(sqlgrant).execute();
            }
        } catch (SQLException e) {
            throw new SchemaCreationFailureException(
                    "Possibly your user does not have permission to create a schema, use a user with permission to start this system.",
                    e);
        }
        conn.close();
    }

    private void createPostgresSchema(HikariDataSource dataSource) throws SQLException {
        final Connection conn = dataSource.getConnection();
        conn.prepareStatement(String.format("CREATE SCHEMA IF NOT EXISTS %s", schema)).execute();
        ResultSet result = conn.prepareStatement("SELECT * FROM information_schema.schemata").executeQuery();
        while (result.next()) {
            String schemaNotExists = result.getString("schema_name");
            log.info(schemaNotExists);
        }
        if (databaseType.isH2()) {
            conn.commit();
        }
        conn.close();
    }

}
