package com.gateway.quinbook.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Login {
    @Id
    private String userName;
    private String password;
}
