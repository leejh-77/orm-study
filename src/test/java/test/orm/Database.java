package test.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Database {

    private static Database instance;

    // Thread Local
    private static ThreadLocal<Connection> localConnection = new ThreadLocal<>();

    static {
        try {
            instance = new Database();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Database getInstance() {
        return instance;
    }

    public Connection getConnection() {
        Connection conn = localConnection.get();
        if (conn == null) {
            try {
                conn = this.openConnection();
                localConnection.set(conn);
            } catch (SQLException throwables) {
                throw new RuntimeException(throwables);
            }
        }
        return conn;
    }

    private Database() throws Exception {
        Class.forName("org.sqlite.JDBC");
    }

    private Connection openConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:test_db");
    }

    public interface Transaction {
        void execute();
    }

    public void executeInTransaction(Transaction executor) {
//        Tr tr;
//        try {
//            tr.beinTransaction();
//            executor.execute();
//            tr.commit();
//        }
//        catch (Exception e) {
//            tr.rollbak();
//        }
    }
}
