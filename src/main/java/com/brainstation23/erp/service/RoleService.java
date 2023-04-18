package com.brainstation23.erp.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoleService {

    private String[] allRoles = {"ADMIN", "USER"};


    public String getAllRoles() {
        return Arrays.toString(this.allRoles);
    }
}
