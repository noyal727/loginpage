package com.gateway.quinbook.service;

import com.gateway.quinbook.dto.LogoutRequestDTO;

public interface LogoutService {
    void logoutUser(LogoutRequestDTO request);
}
