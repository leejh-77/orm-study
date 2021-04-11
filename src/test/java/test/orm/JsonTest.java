package test.orm;

import org.junit.jupiter.api.Test;
import test.orm.json.JsonReader;
import test.orm.json.JsonWriter;
import test.orm.model.RfcEmailAddress;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {

    @Test
    public void testSerialize() {
        RfcEmailAddress addr = new RfcEmailAddress();
        addr.setId(29383);
        addr.setEmail("jonghoon.lee@gmail.com");
        addr.setName("Jonghoon Lee");

        JsonWriter writer = new JsonWriter();
        writer.write(addr);
        String json = writer.jsonString();

        JsonReader reader = new JsonReader(json);
        RfcEmailAddress obj = reader.readObject(RfcEmailAddress.class);

        assertEquals(addr.getId(), obj.getId());
        assertEquals(addr.getEmail(), obj.getEmail());
        assertEquals(addr.getName(), obj.getName());
    }
}
