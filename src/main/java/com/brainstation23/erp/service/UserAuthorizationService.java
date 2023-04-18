package com.brainstation23.erp.service;


import com.brainstation23.erp.exception.custom.custom.UnauthorizedAccessException;
import com.brainstation23.erp.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.stereotype.Service;

import java.awt.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserAuthorizationService {
    public void authorizeRequest(String authHeader) throws Exception {
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new HeadlessException("Authorization header must be provided");
        }

        // Extract token from header
        String token = authHeader.substring(7);

        boolean isAdmin = JwtTokenUtil.checkIfAdmin(token);
        if(!isAdmin) {
            throw new UnauthorizedAccessException("Only admins can access this endpoint");
        }
    }
}

