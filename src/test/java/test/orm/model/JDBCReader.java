package test.orm.model;

import test.orm.serialize.DataReader;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCReader implements DataReader {

    private ResultSet set;
    private int index;

    public JDBCReader(ResultSet set) {
        this.set = set;
        this.index = 1;
    }

    public void incrementIndex() {
        this.index++;
    }

    @Override
    public String readString() {
        try {
            return this.set.getString(this.index);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long readLong() {
        try {
            return this.set.getLong(this.index);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
