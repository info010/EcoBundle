package org.info_0.ecobundle.util;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import org.info_0.ecobundle.EcoBundle;

public class Database {
    private Connection connection;

    public void connect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Failed to load SQLite JDBC class", ex);
        }

        File database = new File(EcoBundle.getInstance().getDataFolder(), "database.db");

        try {
            database.getParentFile().mkdirs();
            database.createNewFile();
        } catch (IOException e) {
            EcoBundle.getInstance().getLogger().log(Level.SEVERE, "File write error: database.db");
        }

        connection = DriverManager.getConnection("jdbc:sqlite:" + database);
    }

    public void setup() throws SQLException {
        try (Statement s = connection.createStatement()) {
            s.executeUpdate("CREATE TABLE IF NOT EXISTS economy (" +
                    "`uuid` varchar(36) NOT NULL, `player_name` varchar(16) NOT NULL, `balance` double(1000) NOT NULL, PRIMARY KEY (`uuid`));");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isOpen() {
        if (connection == null) {
            return false;
        }

        try {
            return !connection.isClosed();
        } catch (SQLException exception) {
            return false;
        }
    }

    public void report(SQLException exception) {
        EcoBundle.getInstance().getLogger().log(Level.SEVERE, "Unhandled exception: " + exception.getMessage(), exception);
    }
}
