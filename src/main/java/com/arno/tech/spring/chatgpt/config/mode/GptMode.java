package com.arno.tech.spring.chatgpt.config.mode;

public enum GptMode {
    DAVINCI_3("text-davinci-003"),
    CHAT_TURBO("gpt-3.5-turbo");

    private final String value;

    GptMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
