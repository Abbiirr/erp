package com.brainstation23.erp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import com.brainstation23.erp.exception.custom.custom.AlreadyExistsException;
import com.brainstation23.erp.mapper.UserMapper;
import com.brainstation23.erp.model.domain.User;
import com.brainstation23.erp.model.dto.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.brainstation23.erp.model.dto.CreateUserRequest;
import com.brainstation23.erp.persistence.entity.UserEntity;
import com.brainstation23.erp.persistence.repository.UserRepository;
import com.brainstation23.erp.service.UserService;
import com.brainstation23.erp.util.RandomUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@DataJpaTest
public class UserServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private UserService userService;

    private UserMapper userMapper;

    private String name = "Test User";
    private String email = "test.user@example.com";
    private String password = "password";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

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
    }


    @Test
@DisplayName("Test create user with existing email")
void testCreateUserWithExistingEmail() {
    // Given
    CreateUserRequest request = new CreateUserRequest();
    UUID id = UUID.randomUUID();
//    request.setId(id);
    request.setName("Test User");
    request.setEmail("example@gmail.com");
    request.setPassword("password");
    request.setRole("USER");

    UserEntity entity = new UserEntity();
    entity.setId(id);
    entity.setName(request.getName());
    entity.setEmail(request.getEmail());
    entity.setPassword(request.getPassword());
    entity.setRole(request.getRole());

    entityManager.persist(entity);
    entityManager.flush();

    // When
    AlreadyExistsException exception = assertThrows(AlreadyExistsException.class, () -> {
        userService.createOne(request);
    });

    // Then
    assertEquals("User already exists with email: example@gmail.com", exception.getMessage());
}

    @Test
    void testGetAllUsers() {
        Page<User> users = userService.getAll(PageRequest.of(0, 10));
        assertNotNull(users);
    }

 @Test
void testGetAll() {
    // create two user entities
    UserEntity user1 = new UserEntity();
    user1.setId(UUID.randomUUID());
    user1.setName("John");
    user1.setEmail("john@example.com");
    user1.setPassword("password");
    user1.setRole("ROLE_USER");

    UserEntity user2 = new UserEntity();
    user2.setId(UUID.randomUUID());
    user2.setName("Jane");
    user2.setEmail("jane@example.com");
    user2.setPassword("password");
    user2.setRole("ROLE_ADMIN");

    // persist the user entities
    entityManager.persist(user1);
    entityManager.persist(user2);
    entityManager.flush();

     // call the service method
    Page<User> page = userService.getAll(PageRequest.of(0, 10));

    List<User> userList = page.getContent();

    System.out.println("userList = " + userList);

    // assert the result
    assertEquals(2, page.getTotalElements());
}










}
