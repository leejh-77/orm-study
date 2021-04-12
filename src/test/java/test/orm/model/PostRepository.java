package test.orm.model;

import test.orm.Database;

import java.util.List;

public class PostRepository extends ORMRepository<Post> {

    public PostRepository(Database database) {
        super(database);
    }

    @Override
    Class<Post> getORMClass() {
        return Post.class;
    }

    public List<Post> findByUserId(Long id) {
        return super.findByQuery("accountId = " + id);
    }
}
