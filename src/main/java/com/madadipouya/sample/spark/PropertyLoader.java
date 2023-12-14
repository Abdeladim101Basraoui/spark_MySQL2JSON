package com.madadipouya.sample.spark;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class PropertyLoader {

    private static final String APPLICATION_PROPERTIES_FILE_NAME = "application.properties";

    private static final String JDBC_CONNECTION_URI = "jdbc:mysql://%s:%s/%s?rewriteBatchedStatements=true";

    private Properties properties;

    public PropertyLoader() {
        try {
            properties = new Properties();
            properties.load(Objects.requireNonNull(
                    DbInitializer.class.getClassLoader().getResourceAsStream(APPLICATION_PROPERTIES_FILE_NAME)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDatabaseConnectionUri() {
        return properties.getProperty("jdbc.db.uri");
    }

    public String getDatabaseConnectionPort() {
        return properties.getProperty("jdbc.db.port");
    }

    public String getDatabaseName() {
        return properties.getProperty("jdbc.db.name");
    }

    public String getDatabaseUserName() {
        return properties.getProperty("jdbc.db.username");
    }

    public String getDatabasePassword() {
        return properties.getProperty("jdbc.db.password");
    }

    public String getJdbcConnectionString() {
        return String.format(JDBC_CONNECTION_URI, getDatabaseConnectionUri(), getDatabaseConnectionPort(), getDatabaseName());
    }
}
