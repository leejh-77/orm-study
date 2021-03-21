package test.orm.model;

import test.orm.Database;

public interface ORMTables {

    Database database = Database.getInstance();

    UserRepository User = new UserRepository(database);

    PostRepository Post = new PostRepository(database);

}
