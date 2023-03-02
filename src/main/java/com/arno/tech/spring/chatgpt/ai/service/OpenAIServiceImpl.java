package com.arno.tech.spring.chatgpt.ai.service;

import com.arno.tech.spring.chatgpt.ai.IOpenAI;
import com.arno.tech.spring.chatgpt.ai.OpenAiResult;
import com.arno.tech.spring.chatgpt.ai.model.AIModel;
import com.arno.tech.spring.chatgpt.ai.model.IAIModel;
import com.arno.tech.spring.chatgpt.ai.model.aggregates.AIAnswer;
import com.arno.tech.spring.chatgpt.ai.model.chat.ChatModelResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;


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
    public void doByText3(String openAiKey, String question, @NotNull OpenAiResult<String> consumer) {
        model.doText3(openAiKey, question, aiAnswer -> {
            StringBuilder answers = new StringBuilder();
            if (aiAnswer == null) {
                consumer.onResult(null, "请求失败");
                return;
            }
            List<AIAnswer.Choices> choices = aiAnswer.getChoices();
            if (choices == null || choices.size() == 0) {
                consumer.onResult(null, "请求失败");
                return;
            }
            for (AIAnswer.Choices choice : choices) {
                answers.append(choice.getText());
            }
            log.info("doChatGpt:aiAnswer{}", aiAnswer);
            consumer.onResult(answers.toString(), "成功");
        });
    }

    @Override
    public void doChatByTurbo(String openAiKey, String question, @NotNull OpenAiResult<ChatModelResponse> consumer) {

        model.doChatByTurbo(openAiKey, question, chatModelResponse -> {
            //TODO:xuxin14 2023/3/2 调整逻辑拦截
//                log.info("doChatByTurbo:aiAnswer{}", aiAnswer);
            StringBuilder answers = new StringBuilder();
            if (chatModelResponse == null) {
                consumer.onResult(null, "请求失败");
                return;
            }
            List<ChatModelResponse.Choice> choices = chatModelResponse.getChoices();
            if (choices == null || choices.size() == 0) {
                consumer.onResult(null, "请求失败");
                return;
            }
            consumer.onResult(chatModelResponse, "成功");

        });
    }

}
