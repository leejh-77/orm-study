package test.orm.json;

import test.orm.serialize.DataReader;
import test.orm.serialize.IOAdapter;
import test.orm.serialize.IOField;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutionException;

public class JsonReader implements DataReader {

    private CharScanner scanner;

    private boolean isKey;

    public JsonReader(String jsonStr) {
        this.scanner = new CharScanner(jsonStr);
        this.isKey = true;

        char c = this.scanner.read();
        assert c == '{';
    }

    @Override
    public String readString() {
        String v = this.read();
        v = v.substring(1, v.length() - 1);
        return v;
    }

    @Override
    public long readLong() {
        String v = this.read();
        return Long.parseLong(v);
    }

    public <T> T readObject(Class<T> c) {
        Field[] fields = IOField.getFields(c);
        try {
            T obj = c.newInstance();

            int count = fields.length;
            while (true) {
                String key = this.read();
                for (Field field : fields) {
                    if (field.getName().equals(key)) {
                        IOAdapter adapter = IOAdapter.getAdapter(field.getType());
                        Object v = adapter.read(this);
                        field.set(obj, v);
                        break;
                    }
                }
                if (--count == 0) {
                    break;
                }
            }
            return obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String read() {
        String v;
        if (this.isKey) {
            char c = this.scanner.read();
            assert c == '\"';
            v = this.scanner.readUntil('\"');
        }
        else {
            char c = this.scanner.read();
            assert c == ':';
            v = this.scanner.readUntil(',', '}');
        }
        this.isKey = !this.isKey;
        return v;
    }
}
