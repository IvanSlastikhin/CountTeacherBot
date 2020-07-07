package ru.jhonsy.telegram.bots.countteacherbot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @Author: Ivan Slastikhin
 */

@Entity
public class User {
    @Id
    @GeneratedValue
    @Getter
    private Long id;

    @Setter
    @Getter
    private Long chatId;
    @Setter
    @Getter
    private Integer stateId;
    @Setter
    @Getter
    private Boolean admin;

    public User() {
    }

    public User(Long chatId, Integer stateId) {
        this.chatId = chatId;
        this.stateId = stateId;
    }

    public User(Long chatId, Integer stateId, Boolean admin){
        this.chatId = chatId;
        this.stateId = stateId;
        this.admin = admin;
    }

}
