package com.arno.tech.spring.base.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.logging.Logger;

/**
 * Caffeine 配置
 *
 * @author xuxin14
 * @since 2023/03/04
 */
@Service
@Slf4j
public class CaffeinePoolFactory {
    final CaffeineCacheConfig caffeineCacheConfig;

    @Autowired
    public CaffeinePoolFactory(CaffeineCacheConfig caffeineCacheConfig) {
        this.caffeineCacheConfig = caffeineCacheConfig;
    }

    @Bean
    public Cache<String, String> cache() {
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder();
        if (this.caffeineCacheConfig.getInitialCapacity() != 0) {
            caffeine.initialCapacity(this.caffeineCacheConfig.getInitialCapacity());
        }

        if (this.caffeineCacheConfig.getMaximumSize() != 0L) {
            caffeine.maximumSize(this.caffeineCacheConfig.getMaximumSize());
        }

        if (this.caffeineCacheConfig.getExpireAfterWriteSec() != 0) {
            caffeine.expireAfterWrite(Duration.ofSeconds((long) this.caffeineCacheConfig.getExpireAfterWriteSec()));
        }

        if (this.caffeineCacheConfig.getRefreshAfterWriteSec() != 0) {
            caffeine.refreshAfterWrite(Duration.ofSeconds((long) this.caffeineCacheConfig.getRefreshAfterWriteSec()));
        }

        if (this.caffeineCacheConfig.getExpireAfterAccessSec() != 0) {
            caffeine.expireAfterAccess(Duration.ofSeconds((long) this.caffeineCacheConfig.getExpireAfterAccessSec()));
        }

        if (this.caffeineCacheConfig.isWeakKeys()) {
            caffeine.weakKeys();
        }

        if (this.caffeineCacheConfig.isWeakValues() && this.caffeineCacheConfig.isSoftValues()) {
            log.error("isWeakValues and isSoftValues cant use together");
        } else {
            if (this.caffeineCacheConfig.isWeakValues()) {
                caffeine.weakValues();
            }

            if (this.caffeineCacheConfig.isSoftValues()) {
                caffeine.softValues();
            }
        }

        if (this.caffeineCacheConfig.isOpenRecordStats()) {
            caffeine.recordStats();
        }

        return caffeine.build();
    }
}