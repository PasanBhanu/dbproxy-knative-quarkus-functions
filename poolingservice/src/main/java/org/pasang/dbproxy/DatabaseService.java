package org.pasang.dbproxy;

import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.SQLException;

@ApplicationScoped
public class DatabaseService {

    @Inject
    @DataSource("db1")
    AgroalDataSource db1DataSource;

    @Inject
    @DataSource("db2")
    AgroalDataSource db2DataSource;

    public Connection getConnection(String dbIdentifier) throws SQLException {
        if ("db1".equalsIgnoreCase(dbIdentifier)) {
            return db1DataSource.getConnection();
        } else if ("db2".equalsIgnoreCase(dbIdentifier)) {
            return db2DataSource.getConnection();
        } else {
            throw new IllegalArgumentException("Unknown Database: " + dbIdentifier);
        }
    }
}
