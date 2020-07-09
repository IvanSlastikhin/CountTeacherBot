package ru.jhonsy.telegram.bots.countteacherbot.bot;

import ru.jhonsy.telegram.bots.countteacherbot.model.User;

/**
 * @Author: Ivan Slastikhin
 */
public class BotContext {

    private final ChatBot bot;
    private final User user;
    private final String input;
    private final ExampleGenerator exampleGenerator;

    public static BotContext of(ChatBot chatBot, User user, String input){
        return new BotContext(chatBot, user, input);
    }

    private BotContext(ChatBot chatBot, User user, String input){
        this.bot = chatBot;
        this.user = user;
        this.input = input;
        this.exampleGenerator = new ExampleGenerator();
    }

    public User getUser() {
        return user;
    }

    public ChatBot getBot() {
        return bot;
    }

    public String getInput() {
        return input;
    }

    public ExampleGenerator getExampleGenerator() {
        return exampleGenerator;
    }
}
