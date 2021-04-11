package test.orm.model;

import test.orm.serialize.DataReader;
import test.orm.serialize.DataWriter;
import test.orm.serialize.IOAdapter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RfcEmailAddress {

    private long id;
    private String email;
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    static
    {
        IOAdapter.registerAdapter(RfcEmailAddress.class, new IOAdapter<RfcEmailAddress>() {
            @Override
            public void write(RfcEmailAddress v, DataWriter writer) {
                String email = v.getEmail();
                String name = v.getName();

                String encoded = String.format("\"%s\" <%s>", name, email);
                writer.write(encoded);
            }

            @Override
            public RfcEmailAddress read(DataReader reader) {
                String v = reader.readString();
                String[] arr = v.split(" ");
                String name = arr[0];
                String email = arr[1];

                name = name.substring(1, name.length() - 1);
                email = email.substring(1, email.length() - 1);

                RfcEmailAddress addr = new RfcEmailAddress();
                addr.setEmail(email);
                addr.setName(name);
                return addr;
            }
        });
    }
}
