package ru.job4j.cars.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "auto_user")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String login;

    private String password;
}