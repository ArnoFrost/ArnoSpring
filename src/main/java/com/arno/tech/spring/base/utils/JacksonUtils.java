package com.arno.tech.spring.base.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;


/**
 * json工具类
 *
 * @author xuxin14
 * @since 2023/03/02
 */
@Slf4j
public class JacksonUtils {

    /**
     * json string ---> map 格式
     */
    public static Map<String, String> stringToMap(String json) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, new TypeReference<Map<String, String>>() {
            });
        } catch (Exception ex) {
            log.error(json + " jsonToMap error: " + ex.getMessage(), ex);
        }
        return null;
    }

    public static Map<String, Object> stringToObjectMap(String json) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception ex) {
            log.error(json + " jsonToMap error: " + ex.getMessage(), ex);
        }
        return null;
    }

    /**
     * json string ---> map 格式
     */
    public static Map<Integer, String> stringToMapKeyInt(String json) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, new TypeReference<Map<Integer, String>>() {
            });
        } catch (Exception ex) {
            log.error(json + " jsonToMap error: " + ex.getMessage(), ex);
        }
        return null;
    }

    /**
     * json string ---> list 格式
     */
    public static <T> List<T> stringToList(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (Exception ex) {
            log.error(json + " jsonToList error: " + ex.getMessage(), ex);
        }
        return null;
    }

    public static <T> String beanToString(T value) {
        if (value == null) {
            return null;
        }
        Class<?> clz = value.getClass();
        if (clz == int.class || clz == Integer.class) {
            // int类型
            return String.valueOf(value);
        } else if (clz == long.class || clz == Long.class) {
            // long 类型
            return String.valueOf(value);
        } else if (clz == double.class || clz == Double.class) {
            // double 类型
            return String.valueOf(clz);
        } else if (clz == String.class) {
            // String 类型
            return (String) value;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            return "";
        }
    }

    @Nullable
    public static <T> T stringToBean(String str, Class<T> clazz) {
        if (str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(str);
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(str);
        } else if (clazz == String.class) {
            return (T) str;
        } else {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.readValue(str, clazz);
            } catch (JsonProcessingException ex) {
                return null;
            }
        }
    }

    /**
     * map --->  json string 格式
     *
     * @param map
     * @return
     */
    @Nullable
    public static String mapToString(Map<String, String> map) {
        if (map == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(map);
        } catch (Exception ex) {
            log.error(map + " mapToString error: " + ex.getMessage(), ex);
        }
        return null;
    }

    @Nullable
    public static String objectMapToString(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(map);
        } catch (Exception ex) {
            log.error(map + " mapToString error: " + ex.getMessage(), ex);
        }
        return null;
    }

}
