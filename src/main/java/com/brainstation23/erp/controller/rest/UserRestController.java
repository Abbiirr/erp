package com.brainstation23.erp.controller.rest;


import com.brainstation23.erp.exception.custom.custom.UnauthorizedAccessException;
import com.brainstation23.erp.mapper.UserMapper;
import com.brainstation23.erp.model.dto.CreateUserRequest;
import com.brainstation23.erp.model.dto.UserResponse;
//import com.brainstation23.erp.model.dto.UpdateUserRequest;
import com.brainstation23.erp.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.brainstation23.erp.util.JwtTokenUtil;

import javax.validation.Valid;
import java.util.UUID;

@Tag(name = "User")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/users")
public class UserRestController {
	private final UserService userService;
	private final UserMapper userMapper;

	private static final String JWT_SECRET_KEY = "my_secret_key";

	@Operation(summary = "Get All Users")
	@GetMapping
	public ResponseEntity<Page<UserResponse>> getAll(@ParameterObject Pageable pageable, @RequestHeader(value = "optional-value", required = true) String authHeader) throws Exception {
		log.info("Getting List of Users");
		System.out.println("Trying to get all users");

		System.out.println("authHeader = " + authHeader);

		if(authHeader == null || !authHeader.startsWith("Bearer ")){
			throw new Exception("Authorization header must be provided");
		}

		// Extract token from header
		String token = authHeader.substring(7);

		System.out.println("token = " + token);

		boolean isAdmin =  JwtTokenUtil.checkIfAdmin(token);
		if(!isAdmin) {
			throw new UnauthorizedAccessException("Only admins can access this endpoint");
		}

		// Parse token and check role
//		Claims claims = null;
//		try {
//			claims = Jwts.parser().setSigningKey(JWT_SECRET_KEY.getBytes()).parseClaimsJws(token).getBody();
//		} catch (Exception e) {
//			System.out.println("Failed to parse JWT token");
//			e.printStackTrace();
//			throw new Exception("Invalid JWT token");
//		}
//
//		String roles = (String) claims.get("role");
//		System.out.println("roles = " + roles);
//
//		if (!roles.contains("ADMIN")) {
//			throw new UnauthorizedAccessException("Only admins can access this endpoint");
//		}

		var domains = userService.getAll(pageable);
		return ResponseEntity.ok(domains.map(userMapper::domainToResponse));
	}


	@Operation(summary = "Get Single User")
	@GetMapping("{id}")
	public ResponseEntity<UserResponse> getOne(@PathVariable UUID id) {
		log.info("Getting Details of User({})", id);
		var domain = userService.getOne(id);
		return ResponseEntity.ok(userMapper.domainToResponse(domain));
	}


//	@Operation(summary = "Update Single User")
//	@PutMapping("{id}")
//	public ResponseEntity<Void> updateOne(@PathVariable UUID id,
//			@RequestBody @Valid UpdateUserRequest updateRequest) {
//		log.info("Updating an User({}): {} ", id, updateRequest);
//		UserService.updateOne(id, updateRequest);
//		return ResponseEntity.noContent().build();
//	}

	@Operation(summary = "Delete Single User")
	@DeleteMapping("{id}")
	public ResponseEntity<Void> deleteOne(@PathVariable UUID id) {
		log.info("Deleting an User({}) ", id);
		userService.deleteOne(id);
		return ResponseEntity.noContent().build();
	}
}
