package com.think.db;

import com.think.util.StringUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.think.db.Constant.ALL;
import static com.think.db.Constant.AND;
import static com.think.db.Constant.DELETE;
import static com.think.db.Constant.DESC;
import static com.think.db.Constant.EQUAL;
import static com.think.db.Constant.FROM;
import static com.think.db.Constant.INSERT;
import static com.think.db.Constant.INTO;
import static com.think.db.Constant.LEFT_BRACKET;
import static com.think.db.Constant.LIMIT;
import static com.think.db.Constant.ORDER_BY;
import static com.think.db.Constant.QUERY;
import static com.think.db.Constant.RIGHT_BRACKET;
import static com.think.db.Constant.SET;
import static com.think.db.Constant.SPACE;
import static com.think.db.Constant.SYNBOL_COMMA;
import static com.think.db.Constant.SYNBOL_QUESTION;
import static com.think.db.Constant.UPDATE;
import static com.think.db.Constant.VALUES;
import static com.think.db.Constant.WHERE;

/**
 * SQL语句构建工具类
 *
 * @author veione
 */
public final class SQLBuilder {
    private StringBuilder builder = new StringBuilder();

    // select * from t_user where id = 100

    public static void main(String[] args) {
        SQLBuilder builder = new SQLBuilder();
        // "id", "name", "password", "nickname", "age"
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("id", 9527);
        paramsMap.put("name", "peter");
        paramsMap.put("password", "123");
        paramsMap.put("nickName", "凌星");
        paramsMap.put("age", "21");
        builder.select(paramsMap.keySet().toArray()).from("t_user").where(paramsMap.keySet().toArray()).orderBy("id", "name").desc();
        builder.print();


        // insert into t_user values(x,x,x,x,x);
        SQLBuilder builder2 = new SQLBuilder();
        // Arrays.asList("1000", "peter", "21")
        //builder2.insert("t_user").placeholder("id", "name", "age");
        builder2.insert("t_user").values("id", "name", "age");
        builder2.print();

        SQLBuilder builder3 = new SQLBuilder();
        builder3.delete("t_user").where(paramsMap.keySet().toArray());
        builder3.print();

        SQLBuilder builder4 = new SQLBuilder();
        builder4.update("t_user").set(paramsMap.keySet().toArray());
        builder4.print();
    }

    public SQLBuilder set(Object... params) {
        builder.append(SPACE).append(SET).append(SPACE);
        if (params.length > 1) {
            Arrays.stream(params).forEach(o -> builder.append(StringUtil.camel2Underline(o.toString())).append(EQUAL.trim()).append(SYNBOL_QUESTION.trim())
                    .append(SYNBOL_COMMA));
            removeInvalidChar(SYNBOL_COMMA);
        } else {
            Arrays.stream(params).forEach(o -> builder.append(o).append(EQUAL.trim()).append(SYNBOL_QUESTION.trim()));
        }
        return this;
    }

    public SQLBuilder update(String tableName) {
        builder.append(UPDATE).append(SPACE).append(tableName);
        return this;
    }

    public SQLBuilder delete(String tableName) {
        builder.append(DELETE).append(SPACE).append(FROM).append(SPACE).append(tableName);
        return this;
    }

    /**
     * 指定执行参数对应值集合
     *
     * @param placeholders 占位符
     * @return
     */
    public SQLBuilder values(Object... placeholders) {
        builder.append(LEFT_BRACKET);
        Arrays.stream(placeholders).forEach(o -> builder.append(StringUtil.camel2Underline(o.toString())).append(SYNBOL_COMMA));
        removeInvalidChar(SYNBOL_COMMA);
        builder.append(RIGHT_BRACKET).append(SPACE);

        builder.append(VALUES).append(LEFT_BRACKET);
        for (int i = 0; i < placeholders.length; i++) {
            builder.append(SYNBOL_QUESTION).append(SYNBOL_COMMA);
        }
        removeInvalidChar(SYNBOL_COMMA);
        builder.append(RIGHT_BRACKET);
        return this;
    }

    /**
     * 指定插入的表名
     *
     * @param tableName 表名
     * @return
     */
    public SQLBuilder insert(String tableName) {
        return insert(tableName, new Object());
    }

    /**
     * 执行插入时指定表名以及字段
     *
     * @param tableName 表名
     * @param fields    字段集合
     * @return
     */
    public SQLBuilder insert(String tableName, Object... fields) {
        builder.append(INSERT).append(SPACE).append(INTO).append(SPACE).append(tableName);
        if (fields != null && fields.length > 0) {
            placeholder(fields);
        }
        return this;
    }

    /**
     * 指定需要查询的参数集合
     *
     * @param fields 查询的参数集合
     * @return
     */
    public SQLBuilder placeholder(Object... fields) {
        return values(fields);
    }

    public SQLBuilder where(Object... data) {
        builder.append(SPACE).append(WHERE);
        if (data.length > 1) {
            Arrays.stream(data).forEach(o -> builder.append(SPACE).append(StringUtil.camel2Underline(o.toString())).append(EQUAL.trim())
                    .append(SYNBOL_QUESTION).append(SPACE)
                    .append(AND));
            removeInvalidChar(AND);
        } else {
            Arrays.stream(data).forEach(o -> builder.append(SPACE).append(o.toString()).append(EQUAL)
                    .append(SYNBOL_QUESTION));
        }
        return this;
    }

    public SQLBuilder limit(int limit) {
        builder.append(SPACE).append(LIMIT).append(SPACE).append(limit);
        return this;
    }

    public SQLBuilder orderBy(Object... fields) {
        builder.append(SPACE).append(ORDER_BY);
        Arrays.stream(fields).forEach(f -> builder.append(SPACE).append(f).append(SYNBOL_COMMA));
        removeInvalidChar(SYNBOL_COMMA);
        return this;
    }

    public SQLBuilder desc() {
        builder.append(SPACE).append(DESC);
        return this;
    }

    /**
     * 指定查询来自哪个表
     *
     * @param tableName 表名
     * @return
     */
    public SQLBuilder from(String tableName) {
        builder.append(SPACE).append(FROM).append(SPACE).append(tableName);
        return this;
    }

    /**
     * 默认查询所有的表结构字段
     *
     * @return
     */
    public SQLBuilder select() {
        builder.append(QUERY).append(SPACE).append(ALL);
        return this;
    }

    /**
     * 指定查询的字段列表
     *
     * @param fields 查询的字段列表
     * @return
     */
    public SQLBuilder select(Object... fields) {
        builder.append(QUERY);
        Arrays.stream(fields).forEach(f -> builder.append(SPACE).append(f).append(SYNBOL_COMMA));
        removeInvalidChar(SYNBOL_COMMA);
        return this;
    }

    /**
     * 移除无效的字符串
     *
     * @param str 指定移除的字符串
     */
    private void removeInvalidChar(String str) {
        int len = str.length();
        builder.trimToSize();
        builder.replace(builder.length() - len, builder.length(), "");
    }

    /**
     * 打印拼接的SQL语句字符串
     */
    public void print() {
        System.out.println(builder.toString());
    }

    /**
     * 获取SQL语句
     *
     * @return SQL语句
     */
    public String getSQL() {
        return builder.toString();
    }
}
