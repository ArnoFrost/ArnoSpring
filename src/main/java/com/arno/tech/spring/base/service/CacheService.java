package com.arno.tech.spring.base.service;

import com.arno.tech.spring.chatgpt.ai.utils.BeanStringUtils;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 缓存服务
 *
 * @author ArnoFrost
 * @since 2023/03/02
 */
@Service
@Slf4j
public class CacheService implements ICacheService {
    private final RedissonClient redissonClient;

    private final Cache<String, String> caffeineCache;

    @Autowired
    public CacheService(RedissonClient redissonClient, Cache<String, String> caffeineCache) {
        this.redissonClient = redissonClient;
        this.caffeineCache = caffeineCache;
    }

    @Override
    public void setString(String key, String value) {
        redissonClient.getBucket(key).set(value);
    }

    @Override
    public String getString(String key) {
        Object result = redissonClient.getBucket(key).get();
        if (result == null) {
            return null;
        } else {
            return result.toString();
        }
    }

    @Override
    public boolean delete(String key) {
//        return redissonClient.getBucket(key).delete();
        setString(key, "");
        return true;
    }

    @Override
    public <T> boolean setLocal(String key, T value) {
        try {
            String valueStr = BeanStringUtils.beanToString(value);
            caffeineCache.put(key, valueStr);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public <T> T getLocal(String key, Class<T> clz) {
        try {
            String value = (String) caffeineCache.asMap().get(key);
            return BeanStringUtils.stringToBean(value, clz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean setLocalStr(String key, String value) {
        try {
            caffeineCache.put(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public String getLocalStr(String key) {
        try {
            return caffeineCache.asMap().get(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void deleteLocal(String key) {
        try {
            caffeineCache.invalidate(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }
}
