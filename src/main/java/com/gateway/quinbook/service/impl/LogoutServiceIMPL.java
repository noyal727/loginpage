package com.gateway.quinbook.service.impl;

import com.gateway.quinbook.dto.LogoutRequestDTO;
import com.gateway.quinbook.repository.SessionRepository;
import com.gateway.quinbook.service.LogoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LogoutServiceIMPL implements LogoutService {
    @Autowired
    private SessionRepository sessionRepository;


    @Override
    @Transactional
    public void logoutUser(LogoutRequestDTO requestDTO) {
        String optional = sessionRepository.findbysessionid(requestDTO.getSessionId());
        if(optional!=null){
            try{
                sessionRepository.deleteSession(requestDTO.getSessionId());

            }
            catch (Exception e){
            }

        }
    }
}
