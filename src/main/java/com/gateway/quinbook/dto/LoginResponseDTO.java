package com.gateway.quinbook.dto;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String message;
    private String sessionID;
    private Boolean isRegistered;
    private String userName;
}
