package com.minefrozen.pos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

public class UserDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginRes {
        private String username;
        private String token;
        private String role;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ErrorRes {
        HttpStatus httpStatus;
        String message;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserLogin{
        private Integer id;
        private String username;
        private String password;
        private String role;
        private Integer userStore;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User{
        private Integer id;
        private String username;
        private String password;
        private String role;
        private Integer userStore;
        private Integer iPgunRekam;
        private Timestamp dPgunRekam;
        private Integer iPgunUbah;
        private Timestamp dPgunUbah;
    }

}
