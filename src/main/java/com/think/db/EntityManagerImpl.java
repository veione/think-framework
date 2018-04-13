package com.think.db;

import com.think.util.ClassScanner;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;

import static com.think.db.Constant.DEFAULT_LOAD_LIMIT;

public class EntityManagerImpl implements EntityManager {
    private final BasicDataSource dataSource = new BasicDataSource();
    private final QueryRunner runner = new QueryRunner(dataSource);
    private final Map<String, EntityDescription> entityMap = new HashMap<>();
    private final Map<String, String> tableNameMap = new HashMap<>();
    private AtomicBoolean running = new AtomicBoolean(false);

    public EntityManagerImpl() {
        registerDriver();
    }

    /**
     * 注册驱动实例化数据源
     */
    public void registerDriver() {
        dataSource.setUrl("jdbc:mysql://localhost:3306/awesome?useUnicode=true&characterEncoding=UTF-8&useSSL=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setJmxName("think-mysql-db");
        dataSource.setDefaultQueryTimeout(3000);
        dataSource.setMaxTotal(Runtime.getRuntime().availableProcessors());
        dataSource.setMaxIdle(6000);
        dataSource.setMaxWaitMillis(6000);

        Set<Class<?>> classSet = ClassScanner.getClasses("com.think.db", f -> f.isAnnotationPresent(Table.class));
        classSet.stream().forEach(c -> {
            Table table = c.getAnnotation(Table.class);
            if (table != null) {
                EntityDescription description = new EntityDescription();
                description.parse(c);
                entityMap.put(table.table(), description);
                tableNameMap.put(c.getName(), table.table());
            }
        });
        running.compareAndSet(false, true);
    }

    @Override
    public <T> T load(Object clzz) {
        String tableName = tableNameMap.get(clzz.getClass().getName());
        checkTable(tableName);
        EntityDescription description = entityMap.get(tableName);
        Map<String, Object> paramsMap = description.getReadWriteProperties(clzz);
        SQLBuilder builder = new SQLBuilder();
        builder.select().from(tableName).where(paramsMap.keySet().toArray());
        builder.print();

        String sql = builder.getSQL();
        System.out.println("sql = [" + sql + "]");
        Object t = null;
        try {
            t = runner.query(sql, new BeanHandler<>(clzz.getClass(), new EntityRowProcesor()), paramsMap.values().toArray());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (T) t;
    }

    private void checkTable(String tableName) {
        if (tableName == null) {
            throw new IllegalArgumentException("Invalid table " + tableName);
        }
    }

    public <T> List<T> load(Object object, Map<String, Object> wheres, int limit) {
        List<T> results = new ArrayList<>();
        EntityDescription description = new EntityDescription();
        description.parse(object);

        SQLBuilder builder = new SQLBuilder();
        builder.select().from(description.getTableName()).where(wheres.keySet().toArray()).limit(limit);

        System.out.println("object = [" + object + "], wheres = [" + wheres + ", sql = [" + builder.getSQL() + "]]");

        try {
            results = (List<T>) runner.query(builder.getSQL(), new BeanListHandler<>(description.getEntityClass(), new EntityRowProcesor()), wheres.values().toArray());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    public <T> List<T> load(Object object, Map<String, Object> wheres) {
        return load(object, wheres, DEFAULT_LOAD_LIMIT);
    }

    @Override
    public int insert(Object object) {
        Objects.requireNonNull(object, "Object can't be null");

        String tableName = tableNameMap.get(object.getClass().getName());
        EntityDescription description = entityMap.get(tableName);
        Map<String, Object> paramsMap = description.getReadWriteProperties(object);

        SQLBuilder builder = new SQLBuilder();
        Object[] posParams = paramsMap.keySet().toArray();
        Object[] values = paramsMap.values().toArray();

        builder.insert(tableName).values(posParams);

        System.out.println("sql = [" + builder.getSQL() + "]");

        try {
            Object count = runner.insert(builder.getSQL(), new MapHandler(), values);
            System.out.println("count = [" + count + "]");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int update(Object object) {
        Objects.requireNonNull(object, "Object can't be null");
        String tableName = tableNameMap.get(object.getClass().getName());
        EntityDescription description = entityMap.get(tableName);
        Map<String, Object> paramsMap = description.getReadWriteProperties(object);

        SQLBuilder builder = new SQLBuilder();
        builder.update(description.getTableName()).set(paramsMap.keySet().toArray()).where("id");
        builder.print();

        Object[] params = Arrays.copyOf(paramsMap.values().toArray(), paramsMap.values().toArray().length + 1);
        params[params.length - 1] = paramsMap.get("id");
        return execute(builder.getSQL(), params);
    }

    @Override
    public int delete(Object object) {
        int result = 0;
        Objects.requireNonNull(object, "Object can't be null");
        String tableName = tableNameMap.get(object.getClass().getName());
        EntityDescription description = entityMap.get(tableName);
        Map<String, Object> paramsMap = description.getReadWriteProperties(object);

        SQLBuilder builder = new SQLBuilder();
        builder.delete(description.getTableName()).where(paramsMap);

        builder.print();

        try {
            runner.update(builder.getSQL(), paramsMap.values().toArray());
            result = 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private int execute(String sql, Object... params) {
        int result = 0;
        try {
            runner.update(sql, params);
            result = 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void shutdown() {
        if (this.dataSource != null && running.get()) {
            try {
                running.compareAndSet(true, false);
                this.dataSource.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
