package com.think.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.think.util.GsonUtil;

/**
 * 数据参数工具类
 * 
 * @author veion
 *
 */
public final class DataMap {
	/**
	 * 具体参数存储容器
	 */
	private Map<String, Object> map = new HashMap<>();

	/**
	 * 默认构造函数
	 */
	public DataMap() {

	}

	/**
	 * 通过Json数据构建参数,ps:只支持简单的json数据格式，不支持json数据嵌套
	 * 
	 * @param json
	 *            json数据
	 */
	public DataMap(String json) {
		this(GsonUtil.toMap(json));
	}

	/**
	 * 通过键值对方式构建: "userName", "peter", "age": 22, "height": 1.78
	 * 
	 * @param params
	 *            构建的参数集合
	 */
	public DataMap(Object... params) {
		Object[] array = Objects.requireNonNull(params);
		int len = array.length;
		int tmpIndex = 0;
		for (int i = 0; i < len; i += 2) {
			Object key = array[i];
			tmpIndex = i + 1;
			Object value = array[tmpIndex];
			if (key != null && value != null) {
				map.put(String.valueOf(key), value);
			}
		}
	}

	public DataMap(Map<String, Object> params) {
		map.putAll(params);
	}

	public DataMap put(String key, Object value) {
		map.put(key, value);
		return this;
	}

	public String getString(String key, String defaultValue) {
		String value = getString(key);
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}

	public String getString(String key) {
		Object value = map.get(key);
		if (value != null) {
			return String.valueOf(value);
		}
		return null;
	}

	public Integer getInteger(String key) {
		return Integer.valueOf(getString(key));
	}

	public int getInt(String key) {
		return getInteger(key).intValue();
	}

	public Long getLong(String key) {
		return Long.parseLong(getString(key));
	}

	public Double getDouble(String key) {
		return Double.parseDouble(getString(key));
	}

	public Float getFloat(String key) {
		return Float.parseFloat(getString(key));
	}

	public Boolean getBoolean(String key) {
		return Boolean.parseBoolean(getString(key));
	}

	@SuppressWarnings("unchecked")
	public List<String> getStringList(String key) {
		List<String> resultList = new ArrayList<>();
		Object value = this.map.get(key);
		if (value != null) {
			if (value instanceof List) {
				resultList = (List<String>) value;
			}
		}
		return resultList;
	}

	public List<Integer> getIntList(String key) {
		List<Integer> resultList = new ArrayList<>();
		getStringList(key).stream().forEach(s -> resultList.add(Integer.parseInt(s)));
		return resultList;
	}

	public List<Long> getLongList(String key) {
		List<Long> resultList = new ArrayList<>();
		getStringList(key).stream().forEach(s -> resultList.add(Long.parseLong(s)));
		return resultList;
	}

	public List<Double> getDoubleList(String key) {
		List<Double> resultList = new ArrayList<>();
		getStringList(key).stream().forEach(s -> resultList.add(Double.parseDouble(s)));
		return resultList;
	}

	public Object remove(String key) {
		return this.map.remove(key);
	}

	public boolean containsKey(String key) {
		return this.map.containsKey(key);
	}

	public boolean containsValue(String value) {
		return this.map.containsValue(value);
	}

	/**
	 * 转换成Map集合对象返回
	 * 
	 * @return
	 */
	public Map<String, Object> toMap() {
		return map;
	}

	/**
	 * 转换成JSON字符串返回
	 * 
	 * @return
	 */
	public String toJson() {
		return GsonUtil.toJson(map);
	}

	/**
	 * 转换成实体类返回
	 * 
	 * @param type
	 *            要转换的类型
	 * @return
	 */
	public <T> T toBean(Class<T> type) {
		return GsonUtil.fromJson(toJson(), type);
	}
}
