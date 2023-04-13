package com.brainstation23.erp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.brainstation23.erp.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.brainstation23.erp.model.dto.CreateUserRequest;
import com.brainstation23.erp.persistence.entity.UserEntity;
import com.brainstation23.erp.persistence.repository.UserRepository;
import com.brainstation23.erp.service.UserService;
import com.brainstation23.erp.util.RandomUtils;

import java.util.UUID;

@DataJpaTest
public class UserServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private UserService userService;

    private UserMapper userMapper;

    @BeforeEach
    void init() {
        userMapper = mock(UserMapper.class);
        userService = new UserService(userRepository, userMapper);
    }

    @Test
    @DisplayName("Test create user")
    void testCreateUser() {
        // Given
        CreateUserRequest request = new CreateUserRequest();
        request.setName("Test User");
        request.setEmail("test.user@example.com");
        request.setPassword("password");
        request.setRole("USER");

        UserEntity entity = new UserEntity();
        UUID id = UUID.randomUUID();
        entity.setId(id);
        entity.setName(request.getName());
        entity.setEmail(request.getEmail());
        entity.setPassword(request.getPassword());
        entity.setRole(request.getRole());

        // When
        UUID createdUserId = userService.createOne(request);

        // Then
        assertNotNull(createdUserId);
//        assertEquals(id, createdUserId);

        // Verify that the user was saved to the database
        UserEntity savedEntity = entityManager.find(UserEntity.class, id);
        assertNotNull(savedEntity);
        assertEquals(entity.getName(), savedEntity.getName());
        assertEquals(entity.getEmail(), savedEntity.getEmail());
        assertEquals(entity.getPassword(), savedEntity.getPassword());
        assertEquals(entity.getRole(), savedEntity.getRole());
    }

}
