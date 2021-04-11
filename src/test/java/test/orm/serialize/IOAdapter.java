package test.orm.serialize;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public abstract class IOAdapter<T> {

    private static final HashMap<Class<?>, IOAdapter<?>> adapters = new HashMap<>();

    public abstract void write(T v, DataWriter writer);

    public abstract T read(DataReader reader);

    static {
        adapters.put(String.class, new StringAdapter());

        LongAdapter _long = new LongAdapter();
        adapters.put(Long.class, _long);
        adapters.put(long.class, _long);

        adapters.put(Object.class, new ObjectAdapter());
    }

    public static IOAdapter getAdapter(Class c) {
        IOAdapter<?> adapter = adapters.get(c);
        while (adapter == null) {
            c = c.getSuperclass();
            adapter = adapters.get(c);
        }
        return adapter;
    }

    public static void registerAdapter(Class c, IOAdapter adapter) {
        adapters.put(c, adapter);
    }

    static final class StringAdapter extends IOAdapter<String> {

        @Override
        public void write(String v, DataWriter writer) {
            writer.write(v);
        }

        @Override
        public String read(DataReader reader) {
            return reader.readString();
        }
    }

    static final class LongAdapter extends IOAdapter<Long> {

        @Override
        public void write(Long v, DataWriter writer) {
            writer.write(v);
        }

        @Override
        public Long read(DataReader reader) {
            return reader.readLong();
        }
    }

    static final class ObjectAdapter extends IOAdapter<Object> {

        @Override
        public void write(Object v, DataWriter writer) {
            if (v == null) {
                writer.writeNull();
                return;
            }
            DataWriter wr2 = writer.beginWriter();
            wr2.write(v);
            wr2.close();
        }

        @Override
        public Object read(DataReader reader) {
//            return reader.readObject();
            return null;
        }
    }
}
