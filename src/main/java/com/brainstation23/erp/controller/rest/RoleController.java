package com.brainstation23.erp.controller.rest;

import com.brainstation23.erp.exception.custom.custom.UnauthorizedAccessException;
import com.brainstation23.erp.service.UserAuthorizationService;
import com.brainstation23.erp.service.UserInputValidationService;
import com.brainstation23.erp.util.JwtTokenUtil;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brainstation23.erp.service.RoleService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value = "/api/roles")
public class RoleController {


    @Autowired
    private RoleService roleService;

    @Autowired
    private UserInputValidationService userInputValidationService;

    @Autowired
    private UserAuthorizationService userAuthorizationService;

    @GetMapping
    public ResponseEntity<List<String>> getAllRoles(@ParameterObject Pageable pageable, @RequestHeader(value = "optional-value", required = true) String authHeader) throws Exception {

        userInputValidationService.validatePageable(pageable);

        userInputValidationService.validateAuthorizationHeader(authHeader);

        userAuthorizationService.authorizeRequest(authHeader);

        List<String> roles = Collections.singletonList(roleService.getAllRoles());
        return ResponseEntity.ok(roles);
    }
}
