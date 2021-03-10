package com.gateway.quinbook.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String isGoogle;
    private String userName;
    private String password;
    private String token;
}
