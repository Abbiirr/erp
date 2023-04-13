package com.brainstation23.erp.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@ToString
@Getter
@Setter
public class UserResponse {
	@Schema(description = "User ID", example = "3F41A301-25ED-4F0F-876F-7657BEABB00F")
	private UUID id;

	@Schema(description = "User Name", example = "Brain Station 23")
	private String name;

	@Schema(description = "User Code", example = "ORG000001")
	private String email;

    @Schema(description = "User Password", example = "ORG000001")
	private String password;

	@Schema(description = "User Role", example = "EMPLOYEE")
	private String role;
}
