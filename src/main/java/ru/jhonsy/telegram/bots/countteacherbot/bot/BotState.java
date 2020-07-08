package ru.jhonsy.telegram.bots.countteacherbot.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButtonPollType;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Ivan Slastikhin
 */
public enum BotState {

    Start {
        private BotState next;
        private ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        @Override
        public void enter(BotContext context) {

            keyboardMarkup.setSelective(true);
            keyboardMarkup.setResizeKeyboard(true);
            keyboardMarkup.setOneTimeKeyboard(true);

            List<KeyboardRow> buttonsList = new ArrayList<>();
            KeyboardRow firstKeyboardRow = new KeyboardRow();

            firstKeyboardRow.add(new KeyboardButton("Да"));
            firstKeyboardRow.add(new KeyboardButton("Нет"));

            buttonsList.add(firstKeyboardRow);

            keyboardMarkup.setKeyboard(buttonsList);

            sendMessage(context, "Хочешь потренироваться в устном счете?", keyboardMarkup);
        }

        @Override
        public void handleInput(BotContext context) {
            String answer = context.getInput();

            if (answer.equalsIgnoreCase("да")) {
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
        private ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        @Override
        public void enter(BotContext context) {
            keyboardMarkup.setSelective(true);
            keyboardMarkup.setResizeKeyboard(true);
            keyboardMarkup.setOneTimeKeyboard(true);

            List<KeyboardRow> buttonsList = new ArrayList<>();
            KeyboardRow firstKeyboardRow = new KeyboardRow();

            firstKeyboardRow.add(new KeyboardButton("Готов"));
            firstKeyboardRow.add(new KeyboardButton("Стоп"));

            buttonsList.add(firstKeyboardRow);

            keyboardMarkup.setKeyboard(buttonsList);
            sendMessage(context, "Сейчас я буду присылать тебе арифметические примеры, а ты должен будешь ввести ответ!\r\n" +
                    "Я проверю правильно ли ты все сделал!\r\n" +
                    "Нажми \"Готов\" для начала тренировки!\r\n" +
                    "Нажми \"Стоп\" для остановки тренировки!", keyboardMarkup);
        }

        @Override
        public void handleInput(BotContext context) {

            if (context.getInput().equalsIgnoreCase("готов")) {
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
        private ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        @Override
        public void enter(BotContext context) {
            eg.generateExample();
            keyboardMarkup.setSelective(true);
            keyboardMarkup.setResizeKeyboard(true);
            keyboardMarkup.setOneTimeKeyboard(false);

            List<KeyboardRow> buttonsList = new ArrayList<>();
            KeyboardRow firstKeyboardRow = new KeyboardRow();

            firstKeyboardRow.add(new KeyboardButton("Стоп"));

            buttonsList.add(firstKeyboardRow);

            keyboardMarkup.setKeyboard(buttonsList);
            sendMessage(context, eg.getExample(), keyboardMarkup);
        }

        @Override
        public void handleInput(BotContext context) {
            try {
                int inputResult = Integer.parseInt(context.getInput());
                if (inputResult == eg.getResult()) {
                    next = Correct;
                } else {
                    next = Incorrect;
                }
            } catch (NumberFormatException e) {
                if (context.getInput().equalsIgnoreCase("стоп")) {
                    next = GoodBye;
                } else {
                    sendMessage(context, "Введи число или \"Стоп\"!");
                    next = Training;
                }
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },

    Correct(false) {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Верно!");
        }

        @Override
        public BotState nextState() {
            return Training;
        }
    },

    Incorrect(false) {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Неверно!");
        }


        @Override
        public BotState nextState() {
            return Training;
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

    public static BotState getInitialState() {
        return byId(0);
    }

    public static BotState byId(int id) {
        if (states == null) {
            states = BotState.values();
        }

        return states[id];
    }

    protected void sendMessage(BotContext context, String text) {
        SendMessage message = new SendMessage()
                .setChatId(context.getUser().getChatId())
                .setText(text);

        try {
            context.getBot().execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    protected void sendMessage(BotContext context, String text, ReplyKeyboardMarkup keyboardMarkup) {
        SendMessage message = new SendMessage()
                .setReplyMarkup(keyboardMarkup)
                .setChatId(context.getUser().getChatId())
                .setText(text);

        try {
            context.getBot().execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    protected boolean isInputNeeded() {
        return inputNeeded;
    }

    protected void handleInput(BotContext context) {
        //do nothing by default
    }

    public abstract void enter(BotContext context);

    public abstract BotState nextState();
}
