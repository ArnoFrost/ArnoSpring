package com.arno.tech.spring.base.utils;

import com.arno.tech.spring.telegram.config.TgConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * 日志工具
 *
 * @author ArnoFrost
 * @since 2023/02/07
 */
@Slf4j
public class LogUtils {
    private TgConfig config;

    private LogUtils() {

    }

    public static LogUtils getInstance() {
        return LogUtilsHolder.INSTANCE;
    }

    private static class LogUtilsHolder {
        private static final LogUtils INSTANCE = new LogUtils();
    }

    public LogUtils(TgConfig config) {
        this.config = config;
    }

    public void setConfig(TgConfig config) {
        this.config = config;
    }

    public void log(int logLevel, String tag, Long chatId, String str) {
        log(logLevel, tag, chatId, str, null);
    }

    public void log(int logLevel, String tag, Long chatId, String str, Exception e) {
        if (config != null && config.isWhiteEnable()) {
            if (logLevel == LogLevel.DEBUG) {
                log.debug("{}: chatId = {}, user = {}, {}", tag, chatId, getUserNameByChatId(chatId), str, e);
            } else if (logLevel == LogLevel.INFO) {
                log.info("{}: chatId = {}, user = {}, {}", tag, chatId, getUserNameByChatId(chatId), str, e);
            } else if (logLevel == LogLevel.WARN) {
                log.warn("{}: chatId = {}, user = {}, {}", tag, chatId, getUserNameByChatId(chatId), str, e);
            } else if (logLevel == LogLevel.ERROR) {
                log.error("{}: chatId = {}, user = {}, {}", tag, chatId, getUserNameByChatId(chatId), str, e);
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

    private String getUserNameByChatId(Long chatId) {
        //TODO:ArnoFrost 2023/3/4
        return "";
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
