package com.madadipouya.sample.spark;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class DbInitializer {

    private static final String INSERT_QUERY = "INSERT INTO books(title, description, author, year, edition, publisher) " +
            "VALUES(?, ?, ?, ?, ?, ?)";

    private PropertyLoader propertyLoader;

    private Connection connection;

    public DbInitializer(PropertyLoader propertyLoader) {
        this.propertyLoader = propertyLoader;
        this.connection = init();
    }

    public Connection init() {
        try {
            return DriverManager.getConnection(propertyLoader.getJdbcConnectionString(),
                    propertyLoader.getDatabaseUserName(), propertyLoader.getDatabasePassword());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertTestData(int size) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_QUERY)) {
            for (int i = 0; i < size; i++) {
                int j = 0;
                ps.setString(++j, UUID.randomUUID().toString()); // title
                ps.setString(++j, UUID.randomUUID().toString()); // description
                ps.setString(++j, UUID.randomUUID().toString()); // author
                ps.setInt(++j, ThreadLocalRandom.current().nextInt(1930, 2020)); // year
                ps.setInt(++j, ThreadLocalRandom.current().nextInt(1, 30)); // edition
                ps.setString(++j, UUID.randomUUID().toString()); // publisher
                ps.addBatch();
                if (i % 1000 == 0) {
                    ps.executeBatch();
                    System.out.println("Batch inserted!");
                }
            }
            ps.executeBatch();
            System.out.println("Batch inserted!");
        } catch (SQLException sqlException) {
            throw sqlException;
        }
        System.out.println(String.format("Finished inserting %d records", size));
        connection.setAutoCommit(true);
    }
}