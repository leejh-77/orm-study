package test.orm;

import org.junit.jupiter.api.Test;
import org.sqlite.SQLiteConfig;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseTests {

    @Test
    public void testConnection() throws Exception{
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:test_db");

        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery("pragma user_version");
        int version = result.getInt(1);

        assertEquals(0, version);
    }
}
