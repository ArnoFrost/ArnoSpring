package com.arno.tech.spring.chatgpt;

import com.arno.tech.spring.chatgpt.ai.IOpenAI;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootRunTest {
    private final Logger logger = LoggerFactory.getLogger(SpringBootRunTest.class);
    @Value("${chatbot-api.key}")
    private String openAiKey;

    @Autowired
    private IOpenAI openAI;

    @Test
    public void test_openAi() throws IOException {
//        String response = openAI.doChatGPT(openAiKey, "如果我有两个LinearLayout控件在上面的横滑控件里嵌套着,如何继续在上面的方案里,往最里层的LinearLayout添加元素?");
        String response = openAI.doChatGPT(openAiKey, "帮我解释一下下面java 代码的含义");
        logger.info("测试结果：{}", response);
    }

}
