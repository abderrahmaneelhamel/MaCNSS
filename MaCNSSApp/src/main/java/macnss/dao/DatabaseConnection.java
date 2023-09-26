package macnss.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;

    private DatabaseConnection() {
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/macnss", "postgres", "najoua"
            );
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Rethrow the exception for error handling
        }
    }
}
