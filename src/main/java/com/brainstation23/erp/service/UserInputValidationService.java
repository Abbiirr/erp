package com.brainstation23.erp.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.brainstation23.erp.util.JwtTokenUtil;

import java.awt.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserInputValidationService {
    public void validatePageable(Pageable pageable) {
        if(pageable.getPageSize() > 100) {
            throw new IllegalArgumentException("Page size cannot exceed 100");
        }
    }

    public void validateAuthorizationHeader(String authHeader) throws Exception {
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new HeadlessException("Authorization header must be provided");
        }
        System.out.println("Authorization header is valid");

        // Extract token from header
        String token = authHeader.substring(7);

        JwtTokenUtil.validateToken(token);
    }
}

