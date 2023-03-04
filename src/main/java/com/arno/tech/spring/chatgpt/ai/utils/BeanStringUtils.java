package com.arno.tech.spring.chatgpt.ai.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.lang.Nullable;

/**
 * Bean与String的转换工具类
 *
 * @author ArnoFrost
 */
public class BeanStringUtils {
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

}
