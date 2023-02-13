# ArnoSpring
Telegram Bot ChatGpt

配置文件: 
`application-dev.properties`

  - `chatbot-api.key` 填写openai的key, 使用okhttp方式请求
  - `telegram.bot.token` 填写telegram bot token, 使用[https://github.com/pengrad/java-telegram-bot-api](java-telegram-bot-api) 请求tg api

`TelegramManager` 中有主要逻辑, 
- `init()` 初始化相关配置,目前通过长轮询方式取tg消息,好处是无需webhook的公网ip调用
- `dispatchUpdate(Update update)` 是主要分发信息方式, 规则配置,可以定制简化如`/`就直接触发机器人,甚至去掉`/`匹配

ps:最近有些忙,可能来不及及时更新文档 Sorry~
