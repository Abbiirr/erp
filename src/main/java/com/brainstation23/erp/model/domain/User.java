package com.brainstation23.erp.model.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
	private UUID id;
	private String name;
	private String email;
    private String password;
	private String role;

    private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
