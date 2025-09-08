package ru.itmo.lab1.api;

import lombok.Data;

@Data
public class AuthResponse {
    private String username;
    private String token;
}
