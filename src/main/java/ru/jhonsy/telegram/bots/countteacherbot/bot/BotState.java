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

            if (answer.equalsIgnoreCase("да")){
                next = TrainingStart;
            } else {
                next = GoodBye;
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },

    TrainingStart {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Сейчас я буду присылать тебе арифметические примеры, а ты должен будешь ввести ответ!\r\n" +
                    "Я проверю правильно ли ты все сделал!\r\n" +
                    "Напиши мне \"Готов\" для начала тренировки!");
        }

        @Override
        public void handleInput(BotContext context) {
            if (context.getInput().equalsIgnoreCase("готов")){
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

    Training {
        private BotState next;
        private ExampleGenerator eg = new ExampleGenerator();

        @Override
        public void enter(BotContext context) {
            eg.generateExample();
            sendMessage(context, "Сколько будет " + eg.getExample());
        }

        @Override
        public void handleInput(BotContext context) {
            try {
                int inputResult = Integer.parseInt(context.getInput());
                if (inputResult == eg.getResult()){
                    next = Correct;
                } else {
                    next = Incorrect;
                }
            } catch (NumberFormatException e){
                sendMessage(context, "Введи число!");
                next = Training;
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },

    Correct {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Верно!\r\nХочешь еще пример? (да/нет)");
        }

        @Override
        public void handleInput(BotContext context) {
            if (context.getInput().equalsIgnoreCase("да")){
                next = Training;
            } else if (context.getInput().equalsIgnoreCase("нет")){
                next = GoodBye;
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },

    Incorrect {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Неверно!\r\nХочешь еще пример? (да/нет)");
        }

        @Override
        public void handleInput(BotContext context) {
            if (context.getInput().equalsIgnoreCase("да")){
                next = Training;
            } else if (context.getInput().equalsIgnoreCase("нет")){
                next = GoodBye;
            }
        }

        @Override
        public BotState nextState() {
            return next;
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

    protected boolean isInputNeeded(){
        return inputNeeded;
    }

    protected void handleInput(BotContext context){
        //do nothing by default
    }

    public abstract void enter(BotContext context);
    public abstract BotState nextState();
}
