package ru.itmo.lab1.api;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}
