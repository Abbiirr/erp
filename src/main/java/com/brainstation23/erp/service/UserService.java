package com.brainstation23.erp.service;

import java.util.Optional;

import com.brainstation23.erp.exception.custom.custom.AlreadyExistsException;
import com.brainstation23.erp.exception.custom.custom.NotFoundException;
	import com.brainstation23.erp.mapper.OrganizationMapper;
import com.brainstation23.erp.mapper.UserMapper;
import com.brainstation23.erp.model.domain.Organization;
import com.brainstation23.erp.model.domain.User;
import com.brainstation23.erp.model.dto.CreateOrganizationRequest;
import com.brainstation23.erp.model.dto.CreateUserRequest;
import com.brainstation23.erp.model.dto.UpdateOrganizationRequest;
	import com.brainstation23.erp.persistence.entity.OrganizationEntity;
import com.brainstation23.erp.persistence.entity.UserEntity;
import com.brainstation23.erp.persistence.repository.OrganizationRepository;
import com.brainstation23.erp.persistence.repository.UserRepository;
import com.brainstation23.erp.util.RandomUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
	import lombok.extern.slf4j.Slf4j;
	import org.springframework.data.domain.Page;
	import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    public static final String USER_NOT_FOUND = "User Not Found";
    private static final long JWT_EXPIRATION_TIME = 864_000_000; // 10 days in milliseconds
    private static final String JWT_SECRET_KEY = "my_secret_key";


    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Page<User> getAll(Pageable pageable) {
        var entities = userRepository.findAll(pageable);
        return entities.map(userMapper::entityToDomain);
    }

    public User getOne(UUID id) {
        var entity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        return userMapper.entityToDomain(entity);
    }

    public UUID createOne(CreateUserRequest createRequest) throws AlreadyExistsException {
    var userOptional = userRepository.findByEmail(createRequest.getEmail());
    if(userOptional.isPresent()) {
        System.out.println("User already exists with email: " + createRequest.getEmail());
        throw new AlreadyExistsException("User already exists with email: " + createRequest.getEmail());
    }
    var entity = new UserEntity();
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    String hashedPassword = passwordEncoder.encode(createRequest.getPassword());
    entity.setId(UUID.randomUUID())
          .setName(createRequest.getName())
          .setEmail(createRequest.getEmail())
          .setPassword(hashedPassword)
          .setRole(createRequest.getRole());
    var createdEntity = userRepository.save(entity);

    return createdEntity.getId();
}


//    public void updateOne(UUID id, UpdateUserRequest updateRequest) {
//        var entity = userRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
//        entity.setName(updateRequest.getName());
//        userRepository.save(entity);
//    }

    public void deleteOne(UUID id) {
        userRepository.deleteById(id);
    }

   public String authenticate(String email, String password) throws Exception {
        // Find user by email
        UserEntity userEntity =  userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        System.out.println("Password: " + userEntity.getPassword());


       BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
       boolean matches = passwordEncoder.matches(password, userEntity.getPassword());

       if (matches) {
           System.out.println("Password matches!");
       } else {
           System.out.println("Password does not match.");
           throw new Exception("Invalid credentials");
       }

//        // Check if password matches
//        if (!userEntity.getPassword().equals(password)) {
//            throw new Exception("Invalid credentials");
//        }

        // Create JWT token
        String token = Jwts.builder()
                .claim("role", userEntity.getRole())
                .setSubject(userEntity.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET_KEY.getBytes())
                .compact();
        return token;
    }


}
