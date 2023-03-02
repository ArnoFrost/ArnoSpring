package com.arno.tech.spring.chatgpt.ai.vo;

import com.arno.tech.spring.chatgpt.ai.model.chat.ChatModelResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ChatVo {
    private String id;
    private String object;
    private long created;
    private String model;
    private ChatModelResponse.Usage usage;
    private List<ChatModelResponse.Choice> choices;


    @Data
    public static class Usage {
        private int promptTokens;
        private int completionTokens;
        private int totalTokens;
    }


    @Data
    public static class Choice {
        private ChatModelResponse.Message message;
        private String finishReason;
        private int index;
    }

    @Data
    public static class Message {
        private String role;
        private String content;

    }

}
