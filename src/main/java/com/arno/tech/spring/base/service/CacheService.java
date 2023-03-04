package com.arno.tech.spring.base.service;

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
public class CacheService implements ICacheService {
    private final RedissonClient redissonClient;

    @Autowired
    public CacheService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void setString(String key, String value) {
        redissonClient.getBucket(key).set(value);
    }

    @Override
    public String getString(String key) {
        return redissonClient.getBucket(key).get().toString();
    }

    @Override
    public boolean delete(String key) {
        return redissonClient.getBucket(key).delete();
    }
}
