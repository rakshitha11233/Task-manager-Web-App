package util;


import java.sql.Connection;
import java.sql.Statement;

public class SchemaInitializer {
    public static void initialize() {
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement()) {

            // Create users table
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                         "username VARCHAR(100) PRIMARY KEY," +
                         "password VARCHAR(255) NOT NULL)");

            // Create tasks table
            stmt.execute("CREATE TABLE IF NOT EXISTS tasks (" +
                         "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                         "owner VARCHAR(100)," +
                         "title VARCHAR(255)," +
                         "description TEXT," +
                         "due_date DATE," +
                         "completed BOOLEAN," +
                         "FOREIGN KEY (owner) REFERENCES users(username))");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

