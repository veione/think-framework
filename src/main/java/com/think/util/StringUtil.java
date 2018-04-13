package com.think.util;

import com.think.db.*;
import com.think.db.BaseEntity.Mark;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    /**
     * 下划线转驼峰法
     *
     * @param line       源字符串
     * @param smallCamel 大小驼峰,是否为小驼峰
     * @return 转换后的字符串
     */
    public static String underline2Camel(String line, boolean smallCamel) {
        if (line == null || "".equals(line)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("([A-Za-z\\d]+)(_)?");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(smallCamel && matcher.start() == 0 ? Character.toLowerCase(word.charAt(0)) : Character.toUpperCase(word.charAt(0)));
            int index = word.lastIndexOf('_');
            if (index > 0) {
                sb.append(word.substring(1, index).toLowerCase());
            } else {
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    public static String camel2Underline(String line) {
        return camel2Underline(line, true);
    }

    /**
     * 驼峰法转下划线
     *
     * @param line 源字符串
     * @return 转换后的字符串
     */
    public static String camel2Underline(String line, boolean isUpper) {
        if (line == null || "".equals(line)) {
            return "";
        }
        line = String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            if (isUpper) {
                sb.append(word.toUpperCase());
            } else {
                sb.append(word);
            }
            sb.append(matcher.end() == line.length() ? "" : "_");
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        /*String line = "user_name";
        String camel = underline2Camel(line, true);
        System.out.println(camel);
        System.out.println(camel2Underline(camel));

        User user = new User();
        user.mark(Mark.UPDATE);


        EntityDescription description = new EntityDescription();
        description.parse(User.class);
        System.out.println("description = [" + description + "]");*/


        EntityManager manager = ServerContext.getEntityManager();
        User myUser = new User();
        myUser.setId(9527);
        myUser.setAge(21);
        User u = manager.load(myUser);
        u.mark(Mark.UPDATE);
        System.out.println("User = [" + u + "]");


        User user1 = new User();
        user1.setAge(21);
        user1.setId(9530);
        user1.setName("Python");
        user1.setNickName("Jim");
        //manager.insert(user1);

        //manager.delete(user1);


        //manager.update(user1);

        User user2 = manager.load(user1);
        System.out.println("user2 = [" + user2 + "]");

        Map<String, Object> wheres = new HashMap<>();
        wheres.put("age", 1);
        List<User> users = manager.load(User.class, wheres, 2);
        System.out.println("users = [" + users + "]");

        manager.shutdown();
    }
}
