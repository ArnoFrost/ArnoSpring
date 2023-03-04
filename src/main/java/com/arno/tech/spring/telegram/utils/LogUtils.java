package com.arno.tech.spring.telegram.utils;

import com.arno.tech.spring.telegram.config.TgConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 日志工具
 *
 * @author ArnoFrost
 * @since 2023/02/07
 */
@Slf4j
@Component
public class LogUtils {
    private final TgConfig config;

    @Autowired
    public LogUtils(TgConfig config) {
        this.config = config;
    }

    public void log(int logLevel, String tag, Long chatId, String str, Exception e) {
        if (config.isWhiteEnable()) {
            if (logLevel == LogLevel.DEBUG) {
                log.debug("{}: chatId = {}, user = {}, {}", tag, chatId, config.getUserNameByChatId(chatId), str, e);
            } else if (logLevel == LogLevel.INFO) {
                log.info("{}: chatId = {}, user = {}, {}", tag, chatId, config.getUserNameByChatId(chatId), str, e);
            } else if (logLevel == LogLevel.WARN) {
                log.warn("{}: chatId = {}, user = {}, {}", tag, chatId, config.getUserNameByChatId(chatId), str, e);
            } else if (logLevel == LogLevel.ERROR) {
                log.error("{}: chatId = {}, user = {}, {}", tag, chatId, config.getUserNameByChatId(chatId), str, e);
            }
        } else {
            if (logLevel == LogLevel.DEBUG) {
                log.debug("{}: chatId = {}, {}", tag, chatId, str, e);
            } else if (logLevel == LogLevel.INFO) {
                log.info("{}: chatId = {}, {}", tag, chatId, str, e);
            } else if (logLevel == LogLevel.WARN) {
                log.warn("{}: chatId = {}, {}", tag, chatId, str, e);
            } else if (logLevel == LogLevel.ERROR) {
                log.error("{}: chatId = {}, {}", tag, chatId, str, e);
            }
        }
    }

    /**
     * 日志级别
     *
     * @author ArnoFrost
     * @since 2023/02/07
     */
    public interface LogLevel {
        int DEBUG = 0;
        int INFO = 1;

        int WARN = 2;

        int ERROR = 3;
    }
}
