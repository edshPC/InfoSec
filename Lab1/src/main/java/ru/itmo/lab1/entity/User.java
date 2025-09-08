package ru.itmo.lab1.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class User {
    @Id
    private String username;
    private String password;
    private String data;
}
