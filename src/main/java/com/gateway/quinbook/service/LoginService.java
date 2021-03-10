package com.gateway.quinbook.service;

import com.gateway.quinbook.dto.LoginRequestDTO;
import com.gateway.quinbook.dto.LoginResponseDTO;

public interface LoginService {
    LoginResponseDTO doLogin(LoginRequestDTO requestDTO);
     void insertNewLogin(String userName,String password);
}
