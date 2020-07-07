package ru.jhonsy.telegram.bots.countteacherbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class CountteacherbotApplication {

    public static void main(String[] args)
    {
        ApiContextInitializer.init();
        SpringApplication.run(CountteacherbotApplication.class, args);
    }

}
