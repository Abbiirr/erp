package com.brainstation23.erp.controller.rest;


import com.brainstation23.erp.mapper.UserMapper;
import com.brainstation23.erp.model.domain.User;
import com.brainstation23.erp.model.dto.CreateUserRequest;
import com.brainstation23.erp.model.dto.LoginRequest;
import com.brainstation23.erp.model.dto.UserResponse;
//import com.brainstation23.erp.model.dto.UpdateUserRequest;
import com.brainstation23.erp.service.UserService;
import com.brainstation23.erp.util.JwtTokenUtil;
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


import javax.naming.AuthenticationException;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Tag(name = "User")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/auth")
public class AuthRestController {
	 private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private static final String JWT_SECRET_KEY = "my_secret_key";

    @Operation(summary = "Create Single User")
	@PostMapping("/signup")
	public ResponseEntity<Void> createOne(@RequestBody @Valid CreateUserRequest createRequest) {
		log.info("Creating an User: {} ", createRequest);
		var id = userService.createOne(createRequest);
		var location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
		return ResponseEntity.created(location).build();
	}
    @Operation(summary = "Authenticate User")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        log.info("Authenticating User: {} ", loginRequest.getEmail());
        try {
            System.out.println("loginRequest.getEmail() = " + loginRequest.getEmail());
            System.out.println("loginRequest.getPassword() = " + loginRequest.getPassword());
//            User user = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
//            String token = jwtTokenUtil.generateToken(user);
            String token = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

            Claims claims = Jwts.parser().setSigningKey(JWT_SECRET_KEY.getBytes()).parseClaimsJws(token).getBody();
            String roles = (String) claims.get("role");
            System.out.println("User has roles: " + roles);
            return ResponseEntity.ok(token);

        } catch (Exception ex) {
            System.out.println("ex = " + ex);
            return ResponseEntity.badRequest().body("Invalid username or password");
        }
    }
}
