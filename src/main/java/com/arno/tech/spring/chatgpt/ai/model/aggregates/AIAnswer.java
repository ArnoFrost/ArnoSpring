package com.arno.tech.spring.chatgpt.ai.model.aggregates;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.util.List;


/**
 * 回答内容
 *
 * @author xuxin14
 * @date 2023/02/02
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class AIAnswer {

    private String id;

    private String object;

    private int created;

    private String model;

    private List<Choices> choices;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Choices {

        private String text;

        private int index;

        private String logprobs;

        private String finish_reason;
    }
}
