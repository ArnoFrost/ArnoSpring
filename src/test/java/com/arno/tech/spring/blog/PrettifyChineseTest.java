package com.arno.tech.spring.blog;

import org.junit.Test;
import ws.vinta.pangu.Pangu;

/**
 * 美化中文
 *
 * @author ArnoFrost
 * @since 2023/02/28
 */

public class PrettifyChineseTest {

    @Test
    public void prettify() {
        Pangu pangu = new Pangu();
        String text = "你好我叫Arno!";
        String result = pangu.spacingText(text);

        System.out.println(result);
    }
}
