package com.think.core;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.think.common.DataMap;

public class DataMapTest {

	public static void main(String[] args) {
		DataMap map = new DataMap("userName", "peter", "age", 22, "height", 1.78, "sm", null);
		String userName = map.getString("userName");
		int age = map.getInt("age");
		float height = map.getFloat("height");
		System.out.println(userName);
		System.out.println(age);
		System.out.println(height);
		String sm1 = map.getString("sm");
		String sm2 = map.getString("sm", "sm");
		System.out.println(sm1);
		System.out.println(sm2);

		String json = map.toJson();
		System.out.println(json);
		Map<String, Object> result = map.toMap();
		System.out.println(result);
		Student stu = map.toBean(Student.class);
		System.out.println(stu);

		String jsonData = "{\"userName\":\"peter\",\"age\":20,\"height\":1.78}";
		DataMap dataMap2 = new DataMap(jsonData);
		System.out.println(dataMap2.toMap());

		List<String> stringList = Arrays.asList("Hello", "World");
		DataMap dataMap3 = new DataMap();
		dataMap3.put("stringList", stringList);
		System.out.println(dataMap3.getStringList("stringList"));
	}

	static class Student {
		String userName;
		int age;
		float height;

		@Override
		public String toString() {
			return "Student [userName=" + userName + ", age=" + age + ", height=" + height + "]";
		}
	}
}
