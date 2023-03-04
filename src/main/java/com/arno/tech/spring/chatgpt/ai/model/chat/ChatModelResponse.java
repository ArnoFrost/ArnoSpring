package com.arno.tech.spring.chatgpt.ai.model.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;


/**
 * Chat 结构返回
 *
 * @author ArnoFrost
 * @since 2023/03/02
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class ChatModelResponse {
    private String id;
    private String object;
    private long created;
    private String model;
    private Usage usage;
    private List<Choice> choices;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Usage {
        @JsonProperty("prompt_tokens")
        private int promptTokens;
        @JsonProperty("completion_tokens")
        private int completionTokens;
        @JsonProperty("total_tokens")
        private int totalTokens;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Choice {
        private Message message;
        @JsonProperty("finish_reason")
        private String finishReason;
        private int index;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Message {
        private String role;
        private String content;

    }


}
