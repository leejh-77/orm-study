package test.orm.model;

import test.orm.Database;

public class PostRepository extends ORMRepository<Post> {

    public PostRepository(Database database) {
        super(database);
    }

    @Override
    Class<Post> getORMClass() {
        return Post.class;
    }
}
