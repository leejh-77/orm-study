package test.orm;

import org.junit.jupiter.api.Test;
import test.orm.model.User;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class Unittest {

    @Test
    public void reflection() throws IllegalAccessException {
        Field[] fields = User.class.getDeclaredFields();

        assertEquals(4, fields.length);

        User user = new User();

        Field emailField = null;
        for (Field f : fields) {
            if (f.getName().equals("emailAddress")) {
                emailField = f;
                break;
            }
        }
        String jonghyoek = "jonghyeok@gmail.com";
        emailField.setAccessible(true);
        emailField.set(user, jonghyoek);

        String emailAddress = (String) emailField.get(user);
        assertEquals(jonghyoek, emailAddress);
    }
}
