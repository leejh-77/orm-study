package test.orm.model;

import test.orm.Database;

import java.util.HashMap;

public class UserRepository extends ORMRepository<User> {

    public UserRepository(Database database) {
        super(database);
    }

    @Override
    Class<User> getORMClass() {
        return User.class;
    }
}
