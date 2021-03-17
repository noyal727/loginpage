package com.gateway.quinbook.dto;

import lombok.Data;

@Data
public class RegisterRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNo;
    private String gender;
    private java.sql.Date dateOfBirth;
    private String img;
    private String password;
}
