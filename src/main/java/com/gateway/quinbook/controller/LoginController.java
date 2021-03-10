package com.gateway.quinbook.controller;

import com.gateway.quinbook.dto.LoginRequestDTO;
import com.gateway.quinbook.dto.LoginResponseDTO;
import com.gateway.quinbook.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/insert")
    public void insertIntoLogin(@RequestParam String userName,@RequestParam String password){
        loginService.insertNewLogin(userName,password);
    }

    @PostMapping("")
    public LoginResponseDTO doLogin(@RequestBody LoginRequestDTO req){
        //Cookie cookies[] = request.getCookies();
        //System.out.println(cookies[0].getName());
        return loginService.doLogin(req);
    }
}
