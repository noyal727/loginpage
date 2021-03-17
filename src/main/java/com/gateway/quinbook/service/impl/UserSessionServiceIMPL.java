package com.gateway.quinbook.service.impl;

import com.gateway.quinbook.repository.SessionRepository;
import com.gateway.quinbook.service.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSessionServiceIMPL implements UserSessionService {
    @Autowired
    private SessionRepository sessionRepository;

    @Override
    public String fetchUserName(String sessionId) {

        String response  = sessionRepository.findbysessionid(sessionId);
        if(response!=null) return response;
        else return null;
    }
}
