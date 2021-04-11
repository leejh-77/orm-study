package test.orm.model;

import test.orm.Database;
import test.orm.serialize.IOAdapter;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class ORMRepository<T extends ORMEntity> {

    private static HashMap<Class, String> typeMap = new HashMap<>();

    static {
        typeMap.put(String.class, "TEXT");

        typeMap.put(boolean.class, "INTEGER");
        typeMap.put(byte.class, "INTEGER");
        typeMap.put(short.class, "INTEGER");
        typeMap.put(int.class, "INTEGER");
        typeMap.put(float.class, "INTEGER");
        typeMap.put(double.class, "INTEGER");
        typeMap.put(long.class, "INTEGER");

        typeMap.put(Boolean.class, "INTEGER");
        typeMap.put(Byte.class, "INTEGER");
        typeMap.put(Short.class, "INTEGER");
        typeMap.put(Integer.class, "INTEGER");
        typeMap.put(Float.class, "INTEGER");
        typeMap.put(Double.class, "INTEGER");
        typeMap.put(Long.class, "INTEGER");
    }

    private Field idField;
    private Field[] ormFields;

    private PreparedStatement selectStmt;
    private PreparedStatement selectAllStmt;
    private PreparedStatement insertStmt;
    private PreparedStatement updateStmt;
    private PreparedStatement deleteStmt;

    private Database database;

    public ORMRepository(Database database) {
        try {
            this.database = database;

            this.makeORMFields();
            this.createTable();

            this.makeSelectQuery();
            this.makeInsertQuery();
            this.makeUpdateQuery();
            this.makeDeleteQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void makeORMFields() {
        ArrayList list = new ArrayList();

        Class c = this.getORMClass();
        while (ORMEntity.class.isAssignableFrom(c)) {
            Field[] fields = c.getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);
                if (f.getName().equals("id")) {
                    this.idField = f;
                }
                else {
                    list.add(f);
                }
            }
            c = c.getSuperclass();
        }
        this.ormFields = (Field[]) list.toArray(new Field[list.size()]);
    }


    private void makeSelectQuery() throws SQLException {
        Connection conn = this.database.getConnection();
        String sql = "SELECT * FROM " + this.getORMClass().getSimpleName();
        this.selectAllStmt = conn.prepareStatement(sql);
        this.selectStmt = conn.prepareStatement(sql + " WHERE id = ?");
    }

    private void makeUpdateQuery() throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append(this.getORMClass().getSimpleName());
        sb.append(" SET ");

        for (Field f : this.ormFields) {
            String name = f.getName();
            sb.append(name);
            sb.append(" = ?,");
        }
        sb.setLength(sb.length() - 1);
        sb.append(" WHERE id = ?");

        Connection conn = this.database.getConnection();
        this.updateStmt = conn.prepareStatement(sb.toString());
    }

    private void makeDeleteQuery() throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(this.getORMClass().getSimpleName());
        sb.append(" WHERE id = ?");

        Connection conn = this.database.getConnection();
        this.deleteStmt = conn.prepareStatement(sb.toString());
    }


    private void makeInsertQuery() throws SQLException {
        Class<T> ormClass = this.getORMClass();

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(ormClass.getSimpleName());
        sb.append(" (");

        for (Field f : this.ormFields) {
            sb.append(f.getName());
            sb.append(",");
        }
        sb.setLength(sb.length() - 1);
        sb.append(") VALUES (");

        for (Field f : this.ormFields) {
            sb.append("?,");
        }
        sb.setLength(sb.length() - 1);
        sb.append(")");

        String query = sb.toString();

        Connection conn = this.database.getConnection();
        this.insertStmt = conn.prepareStatement(query);
    }

    abstract Class<T> getORMClass();

    private void createTable() throws SQLException {
        Class<T> ormClass = this.getORMClass();

        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ");
        sb.append(ormClass.getSimpleName());
        sb.append(" (id INTEGER PRIMARY KEY AUTOINCREMENT,");

        for (Field field : this.ormFields) {
            String name = field.getName();
            sb.append(name);
            sb.append(" ");

            Class c = field.getType();
            sb.append(typeMap.get(c));
            sb.append(",");
        }
        sb.setLength(sb.length() - 1);
        sb.append(")");

        String query = sb.toString();

        Connection conn = this.database.getConnection();
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(query);
    }

    public long save(T entity) {
        try {
            if (entity.getId() == null) {
                return this.addEntity(entity);
            }
            this.updateEntity(entity);
            return entity.getId();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private long addEntity(T entity) throws Exception {
        PreparedStatement stmt = this.insertStmt;
        this.setArguments(stmt, entity);
        stmt.executeUpdate();
        long id = stmt.getGeneratedKeys().getLong(1);
        return id;
    }

    private void setArguments(PreparedStatement stmt, T entity) throws Exception {
        JDBCWriter writer = new JDBCWriter(stmt);
        for (Field field : this.ormFields) {
            Object v = field.get(entity);
            Class c = field.getType();
            IOAdapter adapter = IOAdapter.getAdapter(c);
            adapter.write(v, writer);

            writer.incrementIndex();
        }
    }

    private void updateEntity(T entity) throws Exception {
        PreparedStatement stmt = this.updateStmt;
        this.setArguments(stmt, entity);
        stmt.setLong(this.ormFields.length + 1, entity.getId());
        int result = stmt.executeUpdate();

        if (result == 0) {
            throw new RuntimeException();
        }
    }

    public T findById(long id) {
        try {
            this.selectStmt.setLong(1, id);
            ResultSet result = this.selectStmt.executeQuery();
            List<T> entities = this.makeEntities(result);
            if (entities.size() == 0) {
                return null;
            }
            return entities.get(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<T> findAll() {
        ResultSet result = null;
        try {
            result = this.selectAllStmt.executeQuery();
            return this.makeEntities(result);
        } catch (Exception throwables) {
            throw new RuntimeException();
        } finally {
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    private List<T> makeEntities(ResultSet result) throws Exception {
        ArrayList<T> list = new ArrayList<>();
        while (result.next()) {
            T entity = this.getORMClass().newInstance();

            long id = result.getLong(1);
            this.idField.set(entity, id);

            JDBCReader reader = new JDBCReader(result);
            reader.incrementIndex();

            for (Field f : this.ormFields) {
                Class c = f.getType();
                IOAdapter adapter = IOAdapter.getAdapter(c);
                Object v = adapter.read(reader);;
                f.set(entity, v);

                reader.incrementIndex();
            }
            list.add(entity);
        }
        return list;
    }

    public void delete(long id) {
        try {
            this.deleteStmt.setLong(1, id);
            this.deleteStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<T> findByQuery(String where) {
        try {
            String sql = "SELECT * FROM " + this.getORMClass().getSimpleName() + "WHERE " + where;
            Statement stmt = this.database.getConnection().createStatement();
            ResultSet result = stmt.executeQuery(sql);
            return this.makeEntities(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
