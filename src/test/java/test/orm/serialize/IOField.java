package test.orm.serialize;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class IOField {

    static final HashMap<Class, Field[]> fields = new HashMap<>();

    public static Field[] getFields(Class c) {
        Field[] fs = fields.get(c);
        if (fs == null) {
            fs = makeFields(c);
            fields.put(c, fs);
        }
        return fs;
    }

    private static Field[] makeFields(Class c) {
        ArrayList<Field> list = new ArrayList<>();
        Field[] fs = c.getDeclaredFields();
        for (Field f : fs) {
            f.setAccessible(true);
            list.add(f);
        }
        return list.toArray(new Field[list.size()]);
    }
}
