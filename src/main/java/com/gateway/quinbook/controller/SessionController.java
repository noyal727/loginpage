package com.gateway.quinbook.controller;

import com.gateway.quinbook.service.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class SessionController {

    @Autowired
    private UserSessionService userSessionService;

    @PostMapping("/fetchUserName")
    public String fetchUserName(@RequestParam String sessionId) {
        return userSessionService.fetchUserName(sessionId);
    }
}
