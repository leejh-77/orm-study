package test.orm.json;

import test.orm.serialize.DataWriter;
import test.orm.serialize.IOAdapter;
import test.orm.serialize.IOField;

import java.lang.reflect.Field;

public class JsonWriter implements DataWriter {

    private JsonWriter parent;

    private StringBuilder sb;
    private boolean isKey;

    public JsonWriter() {
        this.sb = new StringBuilder();
        this.sb.append("{");
        this.isKey = true;
    }

    @Override
    public void write(String v) {
        if (!this.isKey) {
            v = "\"" + v + "\"";
        }
        this.writeString(v);
    }

    @Override
    public void write(long v) {
        this.writeString(String.valueOf(v));
    }

    private void writeString(String v) {
        if (isKey) {
            this.sb.append("\"");
            this.sb.append(v);
            this.sb.append("\"");
            this.sb.append(":");
        }
        else {
            this.sb.append(v);
            this.sb.append(",");
        }
        this.isKey = !this.isKey;
    }

    @Override
    public void write(Long v) {
        this.write((long)v);
    }

    @Override
    public void write(Object obj) {
        try {
            Field[] fs = IOField.getFields(obj.getClass());
            for (Field field : fs) {
                Object v = field.get(obj);
                this.write(field.getName());
                IOAdapter adapter = IOAdapter.getAdapter(field.getType());
                adapter.write(v, this);
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {

        }
    }

    @Override
    public void writeNull() {
        this.writeString("null");
    }

    @Override
    public DataWriter beginWriter() {
        JsonWriter writer = new JsonWriter();
        writer.parent = this;
        return writer;
    }

    @Override
    public void close() {
        this.parent.writeString(this.jsonString());
    }

    public String jsonString() {
        this.sb.setLength(this.sb.length() - 1);
        this.sb.append("}");
        return this.sb.toString();
    }
}
