package com.kamran.project.uber.uberApp.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String password;
}