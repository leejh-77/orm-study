package test.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static Database instance;

    private Connection connection;

    static {
        try {
            instance = new Database();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public static Database getInstance() {
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private Database() throws Exception {
        Class.forName("org.sqlite.JDBC");
        this.connection = DriverManager.getConnection("jdbc:sqlite:test_db");
    }
}
