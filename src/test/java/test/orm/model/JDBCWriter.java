package test.orm.model;

import test.orm.serialize.DataWriter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBCWriter implements DataWriter {

    private PreparedStatement stmt;
    private int index;

    public JDBCWriter(PreparedStatement stmt) {
        this.stmt = stmt;
        this.index = 1;
    }

    public void incrementIndex() {
        this.index++;
    }

    @Override
    public void write(String v) {
        try {
            this.stmt.setString(index, v);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void write(long v) {
        try {
            this.stmt.setLong(index, v);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void write(Long v) {

    }

    @Override
    public void write(Object v) {

    }

    @Override
    public void writeNull() {

    }

    @Override
    public DataWriter beginWriter() {
        return null;
    }

    @Override
    public void close() {

    }
}
