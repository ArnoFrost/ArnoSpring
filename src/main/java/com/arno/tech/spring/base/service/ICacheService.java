package com.arno.tech.spring.base.service;

/**
 * 缓存服务
 *
 * @author ArnoFrost
 * @since 2023/03/02
 */
public interface ICacheService {

    void setString(String key, String value);

    String getString(String key);

    boolean delete(String key);


    <T> boolean setLocal(String key, T value);

    <T> T getLocal(String key, Class<T> value);

    boolean setLocalStr(String key, String value);

    String getLocalStr(String key);

    void deleteLocal(String key);
}
