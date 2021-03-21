package test.orm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.orm.model.ORMTables;
import test.orm.model.PostRepository;
import test.orm.model.User;
import test.orm.model.UserRepository;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.ReflectPermission;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ORMTests {

    @BeforeEach
    public void deleteDB() {
        String dir = System.getProperty("user.dir");
        String path = dir + "/test_db";
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void createAndSelectUser() {
        UserRepository repo = ORMTables.User;

        User user = new User();
        user.setEmailAddress("jongheok@gmail.com");
        user.setPassword("password!@#$$");
        user.setUsername("Lee JongHyeok");

        long id = repo.save(user);

        assertTrue(id > 0);

        User created = repo.findById(id);

        assertEquals(user.getEmailAddress(), created.getEmailAddress());
        assertEquals(user.getPassword(), created.getPassword());
    }

    @Test
    public void selectAllUsers() {
        UserRepository repo = ORMTables.User;

        for (int i = 0; i < 4; i++) {
            User user = new User();
            user.setEmailAddress("jongheok@gmail.com");
            user.setPassword("password!@#$$");
            user.setUsername("Lee JongHyeok");
            repo.save(user);
        }

        List<User> users = repo.findAll();

        assertEquals(4, users.size());
    }

    @Test
    public void updateUser() {
        UserRepository repo = ORMTables.User;

        User user = new User();
        user.setEmailAddress("jongheok@gmail.com");
        user.setPassword("password!@#$$");
        user.setUsername("Lee JongHyeok");

        long id = repo.save(user);

        user = repo.findById(id);
        user.setEmailAddress("jonghoon@gmail.com");
        repo.save(user);

        User updated = repo.findById(id);

        assertEquals(user.getEmailAddress(), updated.getEmailAddress());
    }

    @Test
    public void deleteUser() {
        UserRepository repo = ORMTables.User;

        User user = new User();
        user.setEmailAddress("jongheok@gmail.com");
        user.setPassword("password!@#$$");
        user.setUsername("Lee JongHyeok");

        long id = repo.save(user);

        assertTrue(id > 0);

        repo.delete(id);

        user = repo.findById(id);

        assertNull(user);
    }

    @Test
    public void createUserTable() throws SQLException {
        UserRepository repo = ORMTables.User;

        Connection connection = Database.getInstance().getConnection();
        Statement stmt = connection.createStatement();
        ResultSet result = stmt.executeQuery("SELECT * FROM sqlite_master where tbl_name = 'User';");

        boolean success = result.next();
        assertTrue(success);
    }

    @Test
    public void createPostTable() throws SQLException {
        PostRepository repo = ORMTables.Post;

        Connection connection = Database.getInstance().getConnection();
        Statement stmt = connection.createStatement();
        ResultSet result = stmt.executeQuery("SELECT * FROM sqlite_master where tbl_name = 'Post';");

        boolean success = result.next();
        assertTrue(success);
    }

    @Test
    public void createUser() throws SQLException {
        UserRepository repo = ORMTables.User;

        User user = new User();
        user.setEmailAddress("jonghyeok@gmail.com");
        user.setPassword("password!@#4");
        user.setUsername("Lee Jonghyeok");

        long id = repo.save(user);
        assertTrue(id > 0);
    }
}