package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class User {

    private  int id ; // идентификатор
    private  String email; // почта
    private  String login; // логин
    private String name = null; // имя
    private  LocalDate birthday; // дата рождения

    public User( String email, String login, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.birthday = birthday;
    }


    public void setName( final String name) {
        this.name = name;
    }

}
