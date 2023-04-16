package com.brainstation23.erp.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.brainstation23.erp.constant.Role;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@Setter
public class LoginRequest {
    @NotNull
    @Schema(description = "User Email", example = "example@email.com")
    private String email;

    @NotNull
    @Schema(description = "User Password", example = "strongpassword")
    private String password;

}
