package test.orm.serialize;

public interface DataWriter {

    void write(String v);

    void write(long v);

    void write(Long v);

    void write(Object v);

    void writeNull();

    DataWriter beginWriter();

    void close();
}
