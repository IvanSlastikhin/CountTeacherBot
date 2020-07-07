package ru.jhonsy.telegram.bots.countteacherbot.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @Author: Ivan Slastikhin
 */
public enum BotState {

    Start {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Привет!");
        }

        @Override
        public BotState nextState() {
            return WantToTrain;
        }
    },

    WantToTrain {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Хочешь потренироваться в устном счете? (да/нет)");
        }

        @Override
        public void handleInput(BotContext context){
            String answer = context.getInput();

            if (answer.equals("да")){
                next = Training;
            } else {
                next = GoodBye;
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },

    Training(false) {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Пока не поддерживается(((");
        }

        @Override
        public BotState nextState() {
            return GoodBye;
        }
    },

    GoodBye(false) {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "До новых встреч!");
        }

        @Override
        public BotState nextState() {
            return Start;
        }
    };

    private static BotState[] states;
    private final boolean inputNeeded;

    BotState() {
        this.inputNeeded = true;
    }

    BotState(boolean inputNeeded) {
        this.inputNeeded = inputNeeded;
    }

    public static BotState getInitialState(){
        return byId(0);
    }

    public static BotState byId(int id){
        if (states == null){
            states = BotState.values();
        }

        return states[id];
    }

    protected void sendMessage(BotContext context, String text){
        SendMessage message = new SendMessage()
                .setChatId(context.getUser().getChatId())
                .setText(text);

        try{
            context.getBot().execute(message);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    public boolean isInputNeeded(){
        return inputNeeded;
    }

    public void handleInput(BotContext context){
        //do nothing by default
    }

    public abstract void enter(BotContext context);
    public abstract BotState nextState();
}
