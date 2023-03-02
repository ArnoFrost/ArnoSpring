package com.arno.tech.spring.chatgpt.ai.service;

import com.arno.tech.spring.chatgpt.ai.IOpenAI;
import com.arno.tech.spring.chatgpt.ai.model.AIModel;
import com.arno.tech.spring.chatgpt.ai.model.IAIModel;
import com.arno.tech.spring.chatgpt.ai.model.aggregates.AIAnswer;
import com.arno.tech.spring.chatgpt.ai.model.chat.ChatModelResponse;
import com.arno.tech.spring.chatgpt.config.mode.GptMode;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


/**
 * OpenAI 请求管理
 *
 * @author xuxin14
 * @since 2023/02/07
 */
@Service
@Slf4j
public class OpenAIServiceImpl implements IOpenAI {
    private final IAIModel model;


    @Autowired
    public OpenAIServiceImpl(AIModel model) {
        this.model = model;
    }

    @Override
    public void doChatGPT(String openAiKey, String question, @NotNull Consumer<String> consumer) {
        model.doText3(openAiKey, question, new Consumer<AIAnswer>() {
            @Override
            public void accept(AIAnswer aiAnswer) {
                StringBuilder answers = new StringBuilder();
                if (aiAnswer == null) {
                    consumer.accept("请求失败");
                    return;
                }
                List<AIAnswer.Choices> choices = aiAnswer.getChoices();
                if (choices == null || choices.size() == 0) {
                    consumer.accept("请求失败");
                    return;
                }
                for (AIAnswer.Choices choice : choices) {
                    answers.append(choice.getText());
                }
                log.info("doChatGpt:aiAnswer{}", aiAnswer);
                consumer.accept(answers.toString());
            }
        });
    }

    @Override
    public void doChatByTurbo(String openAiKey, GptMode mode, String question, Consumer<ChatModelResponse> consumer) {

        model.doChatByTurbo(openAiKey, question, new Consumer<ChatModelResponse>() {
            @Override
            public void accept(ChatModelResponse chatModelResponse) {
                //TODO:xuxin14 2023/3/2 调整逻辑拦截
//                log.info("doChatByTurbo:aiAnswer{}", aiAnswer);
                StringBuilder answers = new StringBuilder();
                if (chatModelResponse == null) {
                    consumer.accept(null);
                    return;
                }
                List<ChatModelResponse.Choice> choices = chatModelResponse.getChoices();
                if (choices == null || choices.size() == 0) {
                    consumer.accept(null);
                    return;
                }
                consumer.accept(chatModelResponse);

            }
        });
    }

}
