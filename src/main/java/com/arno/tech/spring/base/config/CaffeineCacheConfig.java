package com.arno.tech.spring.base.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Caffeine 配置
 *
 * @author xuxin14
 * @since 2023/03/04
 */
@Component
@ConfigurationProperties(
        prefix = "localcache.caffeine"
)
public class CaffeineCacheConfig {
    int initialCapacity = 0;
    long maximumSize = 0L;
    int expireAfterAccessSec = 0;
    int expireAfterWriteSec = 0;
    int refreshAfterWriteSec = 0;
    boolean isWeakKeys = false;
    boolean isWeakValues = false;
    boolean isSoftValues = false;
    boolean isOpenRecordStats = false;

    public CaffeineCacheConfig() {
    }

    public int getInitialCapacity() {
        return this.initialCapacity;
    }

    public long getMaximumSize() {
        return this.maximumSize;
    }

    public int getExpireAfterAccessSec() {
        return this.expireAfterAccessSec;
    }

    public int getExpireAfterWriteSec() {
        return this.expireAfterWriteSec;
    }

    public int getRefreshAfterWriteSec() {
        return this.refreshAfterWriteSec;
    }

    public boolean isWeakKeys() {
        return this.isWeakKeys;
    }

    public boolean isWeakValues() {
        return this.isWeakValues;
    }

    public boolean isSoftValues() {
        return this.isSoftValues;
    }

    public boolean isOpenRecordStats() {
        return this.isOpenRecordStats;
    }

    public void setInitialCapacity(int initialCapacity) {
        this.initialCapacity = initialCapacity;
    }

    public void setMaximumSize(long maximumSize) {
        this.maximumSize = maximumSize;
    }

    public void setExpireAfterAccessSec(int expireAfterAccessSec) {
        this.expireAfterAccessSec = expireAfterAccessSec;
    }

    public void setExpireAfterWriteSec(int expireAfterWriteSec) {
        this.expireAfterWriteSec = expireAfterWriteSec;
    }

    public void setRefreshAfterWriteSec(int refreshAfterWriteSec) {
        this.refreshAfterWriteSec = refreshAfterWriteSec;
    }

    public void setWeakKeys(boolean isWeakKeys) {
        this.isWeakKeys = isWeakKeys;
    }

    public void setWeakValues(boolean isWeakValues) {
        this.isWeakValues = isWeakValues;
    }

    public void setSoftValues(boolean isSoftValues) {
        this.isSoftValues = isSoftValues;
    }

    public void setOpenRecordStats(boolean isOpenRecordStats) {
        this.isOpenRecordStats = isOpenRecordStats;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof CaffeineCacheConfig)) {
            return false;
        } else {
            CaffeineCacheConfig other = (CaffeineCacheConfig) o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getInitialCapacity() != other.getInitialCapacity()) {
                return false;
            } else if (this.getMaximumSize() != other.getMaximumSize()) {
                return false;
            } else if (this.getExpireAfterAccessSec() != other.getExpireAfterAccessSec()) {
                return false;
            } else if (this.getExpireAfterWriteSec() != other.getExpireAfterWriteSec()) {
                return false;
            } else if (this.getRefreshAfterWriteSec() != other.getRefreshAfterWriteSec()) {
                return false;
            } else if (this.isWeakKeys() != other.isWeakKeys()) {
                return false;
            } else if (this.isWeakValues() != other.isWeakValues()) {
                return false;
            } else if (this.isSoftValues() != other.isSoftValues()) {
                return false;
            } else {
                return this.isOpenRecordStats() == other.isOpenRecordStats();
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof CaffeineCacheConfig;
    }

    @Override
    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        result = result * 59 + this.getInitialCapacity();
        long $maximumSize = this.getMaximumSize();
        result = result * 59 + (int) ($maximumSize >>> 32 ^ $maximumSize);
        result = result * 59 + this.getExpireAfterAccessSec();
        result = result * 59 + this.getExpireAfterWriteSec();
        result = result * 59 + this.getRefreshAfterWriteSec();
        result = result * 59 + (this.isWeakKeys() ? 79 : 97);
        result = result * 59 + (this.isWeakValues() ? 79 : 97);
        result = result * 59 + (this.isSoftValues() ? 79 : 97);
        result = result * 59 + (this.isOpenRecordStats() ? 79 : 97);
        return result;
    }

    @Override
    public String toString() {
        return "CaffeineCacheConfig(initialCapacity=" + this.getInitialCapacity() + ", maximumSize=" + this.getMaximumSize() + ", expireAfterAccessSec=" + this.getExpireAfterAccessSec() + ", expireAfterWriteSec=" + this.getExpireAfterWriteSec() + ", refreshAfterWriteSec=" + this.getRefreshAfterWriteSec() + ", isWeakKeys=" + this.isWeakKeys() + ", isWeakValues=" + this.isWeakValues() + ", isSoftValues=" + this.isSoftValues() + ", isOpenRecordStats=" + this.isOpenRecordStats() + ")";
    }
}