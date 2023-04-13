package com.brainstation23.erp.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@Setter
public class CreateUserRequest {
	@NotNull
	@Schema(description = "User Name", example = "Brain Station 23")
	private String name;

    @NotNull
    @Schema(description = "User Email", example = "Brain Station 23")
    private String email;

    @NotNull
    @Schema(description = "User Password", example = "Brain Station 23")
    private String password;
}
