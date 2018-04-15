package com.think.common.json;

import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;

/**
 * A representation of a <a href="http://json.org/">JSON</a> object in Java.
 * <p>
 * Unlike some other languages Java does not have a native understanding of JSON. To enable JSON to be used easily
 * in Vert.x code we use this class to encapsulate the notion of a JSON object.
 *
 * The implementation adheres to the <a href="http://rfc-editor.org/rfc/rfc7493.txt">RFC-7493</a> to support Temporal
 * data types as well as binary data.
 * <p>
 * Please see the documentation for more information.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class JsonObject implements Iterable<Map.Entry<String, Object>> {

    private Map<String, Object> map;

    /**
     * 创建一个新的空的实例
     */
    public JsonObject() {
        map = new LinkedHashMap<>();
    }

    /**
     * 创建一个来自JSON字符串的实例
     */
    public JsonObject(String json) {
        fromJson(json);
    }

    /**
     * 创建一个来自Map集合对象，这个Map不是复制的
     *
     * @param map 来自创建的Map实例对象
     */
    public JsonObject(Map<String, Object> map) {
        this.map = map;
    }

    /**
     * Create a JsonObject from the fields of a Java object.
     * Faster than calling `new JsonObject(Json.encode(obj))`.
     *
     * @param obj The object to convert to a JsonObject.
     * @throws IllegalArgumentException if conversion fails due to an incompatible type.
     */
    public static JsonObject mapFrom(Object obj) {
        return new JsonObject((Map<String, Object>) Json.mapper.convertValue(obj, Map.class));
    }

    /**
     * Instantiate a Java object from a JsonObject.
     * Faster than calling `Json.decodeValue(Json.encode(jsonObject), type)`.
     *
     * @param type The type to instantiate from the JsonObject.
     * @throws IllegalArgumentException if the type cannot be instantiated.
     */
    public <T> T mapTO(Class<T> type) {
        return Json.mapper.convertValue(map, type);
    }

    /**
     * 根据指定的key获取字符串值
     *
     * @param key 返回值的key
     * @return 返回key关联的value或者null
     * @throws ClassCastException 如果关联的value不是字符串
     */
    public String getString(String key) {
        Objects.requireNonNull(key);
        CharSequence cs = (CharSequence) map.get(key);
        return cs == null ? null : cs.toString();
    }

    /**
     * 根据指定的key获取一个整型数据值
     *
     * @param key 返回值的key
     * @return 返回关联key的值或者null
     * @throws ClassCastException 如果value不是一个整型数据
     */
    public Integer getInteger(String key) {
        Objects.requireNonNull(key);
        Number number = (Number) map.get(key);
        if (number == null) {
            return null;
        } else if (number instanceof Integer) {
            return (Integer) number; // 避免不必要的装箱/拆箱
        } else {
            return number.intValue();
        }
    }

    /**
     * 根据指定的key获取一个Long类型数据
     *
     * @param key 返回值的key
     * @return 给定key关联的value或者null
     * @throws ClassCastException 如果value不是一个Long类型数据
     */
    public Long getLong(String key) {
        Objects.requireNonNull(key);
        Number number = (Number) map.get(key);
        if (number == null) {
            return null;
        } else if (number instanceof Long) {
            return (Long) number; // 避免不必要的装箱/拆箱
        } else {
            return number.longValue();
        }
    }

    /**
     * 根据指定的key获取一个Float类型数据
     *
     * @param key 关联value的key
     * @return 给定key关联的value或者null
     * @throws ClassCastException 如果value不是一个Float数据类型
     */
    public Float getFloat(String key) {
        Objects.requireNonNull(key);
        Number number = (Number) map.get(key);
        if (number == null) {
            return null;
        } else if (number instanceof Float) {
            return (Float) number; //避免不必要的装箱/拆箱
        } else {
            return number.floatValue();
        }
    }

    /**
     * 根据指定的key获取一个Double类型数据
     *
     * @param key 关联value的key
     * @return 给定key关联的value或者null
     * @throws ClassCastException 如果value不是一个Double数据类型
     */
    public Double getDouble(String key) {
        Objects.requireNonNull(key);
        Number number = (Number) map.get(key);
        if (number == null) {
            return null;
        } else if (number instanceof Double) {
            return (Double) number; //避免不必要的装箱/拆箱
        } else {
            return number.doubleValue();
        }
    }

    /**
     * 根据指定的key获取关联的布尔值
     *
     * @param key 关联value的key
     * @return 给定key关联的value或者null
     * @throws ClassCastException 如果value不是一个布尔类型值
     */
    public Boolean getBoolean(String key) {
        Objects.requireNonNull(key);
        return (Boolean) map.get(key);
    }

    /**
     * 根据指定的key获取JsonObject对象
     *
     * @param key 关联value的key
     * @return 给定key关联的value或者null
     * @throws ClassCastException 如果value不是一个JsonObject值
     */
    public JsonObject getJsonObject(String key) {
        Objects.requireNonNull(key);
        Object value = map.get(key);
        if (value instanceof Map) {
            value = new JsonObject((Map) value);
        }
        return (JsonObject) value;
    }

    /**
     * 根据指定的key获取List对象
     *
     * @param key 关联value的key
     * @return 给定key关联的value或者null
     * @throws ClassCastException 如果value不是一个List值
     */
    public JsonArray getJsonArray(String key) {
        Objects.requireNonNull(key);
        Object value = map.get(key);
        if (value instanceof List) {
            return new JsonArray((List) value);
        }
        return null;
    }

    /**
     * 根据指定的key获取二进制字节数据
     *
     * @param key 关联value的key
     * @return 给定key关联的value或者null
     * @throws ClassCastException       如果value不是String字符串数据
     * @throws IllegalArgumentException 如果字符串值不是一个合法的Base64编码值
     */
    public byte[] getBinary(String key) {
        Objects.requireNonNull(key);
        String encoded = (String) map.get(key);
        return encoded == null ? null : Base64.getDecoder().decode(encoded);
    }

    /**
     * 根据指定的key获取Instant对象数据
     *
     * @param key 关联value的key
     * @return 给定key关联的value或者null
     * @throws ClassCastException                      如果value不是String字符串数据
     * @throws java.time.format.DateTimeParseException 如果字符串值不是一个合法的ISO 8601编码值
     */
    public Instant getInstant(String key) {
        Objects.requireNonNull(key);
        String encoded = (String) map.get(key);
        return encoded == null ? null : Instant.from(ISO_INSTANT.parse(encoded));
    }

    /**
     * 根据指定的key获取Object对象数据
     *
     * @param key 关联value的key
     * @return 给定key关联的value
     */
    public Object getValue(String key) {
        Objects.requireNonNull(key);
        Object value = map.get(key);
        if (value instanceof Map) {
            value = new JsonObject((Map) value);
        } else if (value instanceof List) {
            value = (List) value;
        }
        return value;
    }

    /**
     * 和 {@link #getString(String)} 一样，但是如果关联的value为空则使用指定的默认值
     *
     * @param key 查找的key
     * @param def 如果关联的value为空将会使用默认值代替
     * @return 关联的值或者当值为空的时候使用默认值{@code def}来代替返回值
     */
    public String getString(String key, String def) {
        Objects.requireNonNull(key);
        CharSequence cs = (CharSequence) map.get(key);
        return cs != null || map.containsKey(key) ? cs == null ? null : cs.toString() : def;
    }

    /**
     * Like {@link #getInteger(String)} but specifying a default value to return if there is no entry.
     *
     * @param key the key to lookup
     * @param def the default value to use if the entry is not present
     * @return the value or {@code def} if no entry present
     */
    public Integer getInteger(String key, Integer def) {
        Objects.requireNonNull(key);
        Number val = (Number) map.get(key);
        if (val == null) {
            if (map.containsKey(key)) {
                return null;
            } else {
                return def;
            }
        } else if (val instanceof Integer) {
            return (Integer) val;  // Avoids unnecessary unbox/box
        } else {
            return val.intValue();
        }
    }

    /**
     * Like {@link #getLong(String)} but specifying a default value to return if there is no entry.
     *
     * @param key the key to lookup
     * @param def the default value to use if the entry is not present
     * @return the value or {@code def} if no entry present
     */
    public Long getLong(String key, Long def) {
        Objects.requireNonNull(key);
        Number val = (Number) map.get(key);
        if (val == null) {
            if (map.containsKey(key)) {
                return null;
            } else {
                return def;
            }
        } else if (val instanceof Long) {
            return (Long) val;  // Avoids unnecessary unbox/box
        } else {
            return val.longValue();
        }
    }

    /**
     * Like {@link #getDouble(String)} but specifying a default value to return if there is no entry.
     *
     * @param key the key to lookup
     * @param def the default value to use if the entry is not present
     * @return the value or {@code def} if no entry present
     */
    public Double getDouble(String key, Double def) {
        Objects.requireNonNull(key);
        Number val = (Number) map.get(key);
        if (val == null) {
            if (map.containsKey(key)) {
                return null;
            } else {
                return def;
            }
        } else if (val instanceof Double) {
            return (Double) val;  // Avoids unnecessary unbox/box
        } else {
            return val.doubleValue();
        }
    }

    /**
     * Like {@link #getFloat(String)} but specifying a default value to return if there is no entry.
     *
     * @param key the key to lookup
     * @param def the default value to use if the entry is not present
     * @return the value or {@code def} if no entry present
     */
    public Float getFloat(String key, Float def) {
        Objects.requireNonNull(key);
        Number val = (Number) map.get(key);
        if (val == null) {
            if (map.containsKey(key)) {
                return null;
            } else {
                return def;
            }
        } else if (val instanceof Float) {
            return (Float) val;  // Avoids unnecessary unbox/box
        } else {
            return val.floatValue();
        }
    }

    /**
     * Like {@link #getBoolean(String)} but specifying a default value to return if there is no entry.
     *
     * @param key the key to lookup
     * @param def the default value to use if the entry is not present
     * @return the value or {@code def} if no entry present
     */
    public Boolean getBoolean(String key, Boolean def) {
        Objects.requireNonNull(key);
        Object val = map.get(key);
        return val != null || map.containsKey(key) ? (Boolean) val : def;
    }

    /**
     * Like {@link #getJsonObject(String)} but specifying a default value to return if there is no entry.
     *
     * @param key the key to lookup
     * @param def the default value to use if the entry is not present
     * @return the value or {@code def} if no entry present
     */
    public JsonObject JsonObject(String key, JsonObject def) {
        JsonObject val = getJsonObject(key);
        return val != null || map.containsKey(key) ? val : def;
    }

    /**
     * Like {@link #getJsonArray(String)} but specifying a default value to return if there is no entry.
     *
     * @param key the key to lookup
     * @param def the default value to use if the entry is not present
     * @return the value or {@code def} if no entry present
     */
    public JsonArray getJsonArray(String key, JsonArray def) {
        JsonArray val = getJsonArray(key);
        return val != null || map.containsKey(key) ? val : def;
    }

    /**
     * Like {@link #getBinary(String)} but specifying a default value to return if there is no entry.
     *
     * @param key the key to lookup
     * @param def the default value to use if the entry is not present
     * @return the value or {@code def} if no entry present
     */
    public byte[] getBinary(String key, byte[] def) {
        Objects.requireNonNull(key);
        Object val = map.get(key);
        return val != null || map.containsKey(key) ? (val == null ? null : Base64.getDecoder().decode((String) val)) : def;
    }

    /**
     * Like {@link #getInstant(String)} but specifying a default value to return if there is no entry.
     *
     * @param key the key to lookup
     * @param def the default value to use if the entry is not present
     * @return the value or {@code def} if no entry present
     */
    public Instant getInstant(String key, Instant def) {
        Objects.requireNonNull(key);
        Object val = map.get(key);
        return val != null || map.containsKey(key) ?
                (val == null ? null : Instant.from(ISO_INSTANT.parse((String) val))) : def;
    }

    /**
     * Like {@link #getValue(String)} but specifying a default value to return if there is no entry.
     *
     * @param key the key to lookup
     * @param def the default value to use if the entry is not present
     * @return the value or {@code def} if no entry present
     */
    public Object getValue(String key, Object def) {
        Objects.requireNonNull(key);
        Object val = getValue(key);
        return val != null || map.containsKey(key) ? val : def;
    }

    /**
     * Does the JSON object contain the specified key?
     *
     * @param key the key
     * @return true if it contains the key, false if not.
     */
    public boolean containsKey(String key) {
        Objects.requireNonNull(key);
        return map.containsKey(key);
    }

    /**
     * Return the set of field names in the JSON objects
     *
     * @return the set of field names
     */
    public Set<String> fieldNames() {
        return map.keySet();
    }

    /**
     * Put an Enum into the JSON object with the specified key.
     * <p>
     * JSON has no concept of encoding Enums, so the Enum will be converted to a String using the {@link Enum#name}
     * method and the value put as a String.
     *
     * @param key   the key
     * @param value the value
     * @return a reference to this, so the API can be used fluently
     */
    public JsonObject put(String key, Enum value) {
        Objects.requireNonNull(key);
        map.put(key, value == null ? null : value.name());
        return this;
    }

    /**
     * Put an CharSequence into the JSON object with the specified key.
     *
     * @param key   the key
     * @param value the value
     * @return a reference to this, so the API can be used fluently
     */
    public JsonObject put(String key, CharSequence value) {
        Objects.requireNonNull(key);
        map.put(key, value == null ? null : value.toString());
        return this;
    }

    /**
     * Put a String into the JSON object with the specified key.
     *
     * @param key   the key
     * @param value the value
     * @return a reference to this, so the API can be used fluently
     */
    public JsonObject put(String key, String value) {
        Objects.requireNonNull(key);
        map.put(key, value);
        return this;
    }

    /**
     * Put an Integer into the JSON object with the specified key.
     *
     * @param key   the key
     * @param value the value
     * @return a reference to this, so the API can be used fluently
     */
    public JsonObject put(String key, Integer value) {
        Objects.requireNonNull(key);
        map.put(key, value);
        return this;
    }

    /**
     * Put a Long into the JSON object with the specified key.
     *
     * @param key   the key
     * @param value the value
     * @return a reference to this, so the API can be used fluently
     */
    public JsonObject put(String key, Long value) {
        Objects.requireNonNull(key);
        map.put(key, value);
        return this;
    }

    /**
     * Put a Double into the JSON object with the specified key.
     *
     * @param key   the key
     * @param value the value
     * @return a reference to this, so the API can be used fluently
     */
    public JsonObject put(String key, Double value) {
        Objects.requireNonNull(key);
        map.put(key, value);
        return this;
    }

    /**
     * Put a Float into the JSON object with the specified key.
     *
     * @param key   the key
     * @param value the value
     * @return a reference to this, so the API can be used fluently
     */
    public JsonObject put(String key, Float value) {
        Objects.requireNonNull(key);
        map.put(key, value);
        return this;
    }

    /**
     * Put a Boolean into the JSON object with the specified key.
     *
     * @param key   the key
     * @param value the value
     * @return a reference to this, so the API can be used fluently
     */
    public JsonObject put(String key, Boolean value) {
        Objects.requireNonNull(key);
        map.put(key, value);
        return this;
    }

    /**
     * Put a null value into the JSON object with the specified key.
     *
     * @param key the key
     * @return a reference to this, so the API can be used fluently
     */
    public JsonObject putNull(String key) {
        Objects.requireNonNull(key);
        map.put(key, null);
        return this;
    }

    /**
     * Put another JSON object into the JSON object with the specified key.
     *
     * @param key   the key
     * @param value the value
     * @return a reference to this, so the API can be used fluently
     */
    public JsonObject put(String key, JsonObject value) {
        Objects.requireNonNull(key);
        map.put(key, value);
        return this;
    }

    /**
     * Put a JSON array into the JSON object with the specified key.
     *
     * @param key   the key
     * @param value the value
     * @return a reference to this, so the API can be used fluently
     */
    public JsonObject put(String key, List value) {
        Objects.requireNonNull(key);
        map.put(key, value);
        return this;
    }

    /**
     * Put a byte[] into the JSON object with the specified key.
     * <p>
     * JSON extension RFC7493, binary will first be Base64 encoded before being put as a String.
     *
     * @param key   the key
     * @param value the value
     * @return a reference to this, so the API can be used fluently
     */
    public JsonObject put(String key, byte[] value) {
        Objects.requireNonNull(key);
        map.put(key, value == null ? null : Base64.getEncoder().encodeToString(value));
        return this;
    }

    /**
     * Put a Instant into the JSON object with the specified key.
     * <p>
     * JSON extension RFC7493, instant will first be encoded to ISO 8601 date and time
     * String such as "2017-04-03T10:25:41Z".
     *
     * @param key   the key
     * @param value the value
     * @return a reference to this, so the API can be used fluently
     */
    public JsonObject put(String key, Instant value) {
        Objects.requireNonNull(key);
        map.put(key, value == null ? null : ISO_INSTANT.format(value));
        return this;
    }

    /**
     * Put an Object into the JSON object with the specified key.
     *
     * @param key   the key
     * @param value the value
     * @return a reference to this, so the API can be used fluently
     */
    public JsonObject put(String key, Object value) {
        Objects.requireNonNull(key);
        value = Json.checkAndCopy(value, false);
        map.put(key, value);
        return this;
    }

    /**
     * Remove an entry from this object.
     *
     * @param key the key
     * @return the value that was removed, or null if none
     */
    public Object remove(String key) {
        return map.remove(key);
    }

    /**
     * Merge in another JSON object.
     * <p>
     * This is the equivalent of putting all the entries of the other JSON object into this object. This is not a deep
     * merge, entries containing (sub) JSON objects will be replaced entirely.
     *
     * @param other the other JSON object
     * @return a reference to this, so the API can be used fluently
     */
    public JsonObject mergeIn(JsonObject other) {
        return mergeIn(other, false);
    }

    /**
     * Merge in another JSON object.
     * A deep merge (recursive) matches (sub) JSON objects in the existing tree and replaces all
     * matching entries. JsonArrays are treated like any other entry, i.e. replaced entirely.
     *
     * @param other the other JSON object
     * @param deep  if true, a deep merge is performed
     * @return a reference to this, so the API can be used fluently
     */
    public JsonObject mergeIn(JsonObject other, boolean deep) {
        return mergeIn(other, deep ? Integer.MAX_VALUE : 1);
    }

    /**
     * Merge in another JSON object.
     * The merge is deep (recursive) to the specified level. If depth is 0, no merge is performed,
     * if depth is greater than the depth of one of the objects, a full deep merge is performed.
     *
     * @param other the other JSON object
     * @param depth depth of merge
     * @return a reference to this, so the API can be used fluently
     */
    @SuppressWarnings("unchecked")
    public JsonObject mergeIn(JsonObject other, int depth) {
        if (depth < 1) {
            return this;
        }
        if (depth == 1) {
            map.putAll(other.map);
            return this;
        }
        for (Map.Entry<String, Object> e : other.map.entrySet()) {
            if (e.getValue() == null) {
                map.put(e.getKey(), null);
            } else {
                map.merge(e.getKey(), e.getValue(), (oldVal, newVal) -> {
                    if (oldVal instanceof Map) {
                        oldVal = new JsonObject((Map) oldVal);
                    }
                    if (newVal instanceof Map) {
                        newVal = new JsonObject((Map) newVal);
                    }
                    if (oldVal instanceof JsonObject && newVal instanceof JsonObject) {
                        return ((JsonObject) oldVal).mergeIn((JsonObject) newVal, depth - 1);
                    }
                    return newVal;
                });
            }
        }
        return this;
    }

    /**
     * Encode this JSON object as a string.
     *
     * @return the string encoding.
     */
    public String encode() {
        return Json.encode(map);
    }

    /**
     * Encode this JSON object a a string, with whitespace to make the object easier to read by a human, or other
     * sentient organism.
     *
     * @return the pretty string encoding.
     */
    public String encodePrettily() {
        return Json.encodePrettily(map);
    }

    /**
     * 解析来自json字符串的数据
     *
     * @param json json字符串
     */
    public void fromJson(String json) {
        map = Json.decodeValue(json, Map.class);
    }

    /**
     * Copy the JSON object
     *
     * @return a copy of the object
     */
    public JsonObject copy() {
        Map<String, Object> copiedMap;
        if (map instanceof LinkedHashMap) {
            copiedMap = new LinkedHashMap<>(map.size());
        } else {
            copiedMap = new HashMap<>(map.size());
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            value = Json.checkAndCopy(value, true);
            copiedMap.put(entry.getKey(), value);
        }
        return new JsonObject(copiedMap);
    }


    @Override
    public Iterator<Map.Entry<String, Object>> iterator() {
        return new Iter(map.entrySet().iterator());
    }

    /**
     * Get the underlying {@code Map} as is.
     *
     * This map may contain values that are not the types returned by the {@code JsonObject}.
     *
     * @return the underlying Map.
     */
    public Map<String, Object> getMap() {
        return map;
    }

    /**
     * Get a stream of the entries in the JSON object.
     *
     * @return a stream of the entries.
     */
    public Stream<Map.Entry<String, Object>> stream() {
        return Json.asStream(iterator());
    }

    /**
     * Get the number of entries in the JSON object
     *
     * @return the number of entries
     */
    public int size() {
        return map.size();
    }

    /**
     * Remove all the entries in this JSON object
     */
    public JsonObject clear() {
        map.clear();
        return this;
    }

    /**
     * Is this object entry?
     *
     * @return true if it has zero entries, false if not.
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public String toString() {
        return encode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        return objectEquals(map, o);
    }

    static boolean objectEquals(Map<?, ?> m1, Object o2) {
        Map<?, ?> m2;
        if (o2 instanceof JsonObject) {
            m2 = ((JsonObject) o2).map;
        } else if (o2 instanceof Map<?, ?>) {
            m2 = (Map<?, ?>) o2;
        } else {
            return false;
        }
        if (m1.size() != m2.size())
            return false;
        for (Map.Entry<?, ?> entry : m1.entrySet()) {
            Object val = entry.getValue();
            if (val == null) {
                if (m2.get(entry.getKey()) != null) {
                    return false;
                }
            } else {
                if (!equals(entry.getValue(), m2.get(entry.getKey()))) {
                    return false;
                }
            }
        }
        return true;
    }

    static boolean equals(Object o1, Object o2) {
        if (o1 == o2)
            return true;
        if (o1 instanceof JsonObject) {
            return objectEquals(((JsonObject) o1).map, o2);
        }
        if (o1 instanceof Map<?, ?>) {
            return objectEquals((Map<?, ?>) o1, o2);
        }
        if (o1 instanceof JsonArray) {
            return JsonArray.arrayEquals(((JsonArray) o1).getList(), o2);
        }
        if (o1 instanceof List<?>) {
            return JsonArray.arrayEquals((List<?>) o1, o2);
        }
        if (o1 instanceof Number && o2 instanceof Number && o1.getClass() != o2.getClass()) {
            Number n1 = (Number) o1;
            Number n2 = (Number) o2;
            if (o1 instanceof Float || o1 instanceof Double || o2 instanceof Float || o2 instanceof Double) {
                return n1.doubleValue() == n2.doubleValue();
            } else {
                return n1.longValue() == n2.longValue();
            }
        }
        return o1.equals(o2);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    private class Iter implements Iterator<Map.Entry<String, Object>> {

        final Iterator<Map.Entry<String, Object>> mapIter;

        public Iter(Iterator<Map.Entry<String, Object>> mapIter) {
            this.mapIter = mapIter;
        }

        @Override
        public boolean hasNext() {
            return mapIter.hasNext();
        }

        @Override
        public Map.Entry<String, Object> next() {
            Map.Entry<String, Object> entry = mapIter.next();
            if (entry.getValue() instanceof Map) {
                return new Entry(entry.getKey(), new JsonObject((Map) entry.getValue()));
            } else if (entry.getValue() instanceof List) {
                return new Entry(entry.getKey(), new JsonArray((List) entry.getValue()));
            }
            return entry;
        }

        @Override
        public void remove() {
            mapIter.remove();
        }
    }

    private static final class Entry implements Map.Entry {
        final String key;
        final Object value;

        public Entry(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public Object getKey() {
            return key;
        }

        @Override
        public Object getValue() {
            return value;
        }

        @Override
        public Object setValue(Object value) {
            throw new UnsupportedOperationException();
        }
    }
}
