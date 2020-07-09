package ru.jhonsy.telegram.bots.countteacherbot.bot;

import java.util.Random;

/**
 * @Author: Ivan Slastikhin
 */
public class ExampleGenerator {

    private String example;
    private int result;
    private final Character[] operations = {'/', '*', '+', '-'};

    public ExampleGenerator() {
        generateExample();
    }

    protected void generateExample() {
        Random random = new Random();

        int operationIndex = random.nextInt(4);

        switch (operations[operationIndex]) {
            case '/':
                int divider = random.nextInt(9) + 1;
                int dividend = divider * random.nextInt(20);
                this.result = dividend / divider;
                this.example = dividend + " / " + divider + " = ?";
                break;
            case '*':
                int factor1 = random.nextInt(100);
                int factor2 = random.nextInt(10);
                this.result = factor1 * factor2;
                this.example = factor1 + " * " + factor2 + " = ?";
                break;
            case '+':
                int term1 = random.nextInt(100);
                int term2 = random.nextInt(100);
                this.result = term1 + term2;
                this.example = term1 + " + " + term2 + " = ?";
                break;
            case '-':
                int minuend = random.nextInt(100);
                int subtrahend = random.nextInt(minuend);
                this.result = minuend - subtrahend;
                this.example = minuend + " - " + subtrahend + " = ?";
                break;
            default:
                this.example = "";
                this.result = 0;
        }

    }

    public String getExample() {
        return example;
    }

    public int getResult() {
        return result;
    }
}
