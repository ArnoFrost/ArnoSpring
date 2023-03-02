package com.arno.tech.spring.base.service;

import com.arno.tech.spring.telegram.config.RedisKey;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CacheServiceTest {

    @Autowired
    private CacheService cacheService;

    private static final String TEST_KEY = "test";
    @Test
    void setString() {
        cacheService.setString(RedisKey.KEY_PREFIX + TEST_KEY, "test");
    }

    @Test
    void getString() {
        String string = cacheService.getString(RedisKey.KEY_PREFIX + TEST_KEY);
        System.out.println(string);
        assert string.equals("test");
    }

    @Test
    void delete() throws InterruptedException {
        Thread.sleep(1000L);
        boolean delete = cacheService.delete(RedisKey.KEY_PREFIX + TEST_KEY);
        assert delete;
    }
}