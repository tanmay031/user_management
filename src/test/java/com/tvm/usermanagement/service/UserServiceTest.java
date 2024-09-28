package com.tvm.usermanagement.service;

import com.tvm.usermanagement.model.UserModel;
import com.tvm.usermanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateUser_success() {
        // Create a test user
        UserModel user = new UserModel();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("johndoe@example.com");
        user.setUsername("johndoe");

        // Mock the behavior of the userRepository.save() method
        when(userRepository.save(user)).thenReturn(user);

        // Call the userService.createUser() method
        UserModel savedUser = userService.createUser(user);

        // Verify that the userRepository.save() method was called exactly once
        verify(userRepository, times(1)).save(user);

        // Verify that the saved user object is not null
        assertNotNull(savedUser);

        // Verify that the saved user object is equal to the original user object
        assertEquals(user, savedUser);
    }

    @Test
    public void testCreateUser_nullInput() {
        // Call the userService.createUser() method with null input
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(null));

        // Verify that the userRepository.save() method was not called
        verify(userRepository, never()).save(null);

        // Verify the exception message
        assertEquals("User object or its fields are null.", exception.getMessage());
    }

    @Test
    public void testCreateUser_missingUsername() {
        // Create a test user with a missing username
        UserModel user = new UserModel();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("johndoe@example.com");
        user.setUsername("");

        // Call the userService.createUser() method with the user with missing username
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));

        // Verify that the userRepository.save() method was not called
        verify(userRepository, never()).save(user);

        // Verify the exception message
        assertEquals("Username is required.", exception.getMessage());
    }

    @Test
    public void testCreateUser_missingEmail() {
        // Create a test user with a missing email
        UserModel user = new UserModel();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("");
        user.setUsername("johndoe");

        // Call the userService.createUser() method with the user with missing email
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));

        // Verify that the userRepository.save() method was not called
        verify(userRepository, never()).save(user);

        // Verify the exception message
        assertEquals("Email is required.", exception.getMessage());
    }

//    @Test
//    public void testCreateUser_duplicateEmail() {
//        // Create a test user with an email that already exists in the repository
//        UserModel user = new UserModel();
//        user.setId(1L);
//        user.setName("John Doe");
//        user.setEmail("johndoe@example.com");
//        user.setUsername("johndoe");
//
//        // Mock the behavior of the userRepository.findByEmail() method
//        when(userRepository.findByEmail("johndoe@example.com")).thenReturn(Optional.of(user));
//
//        // Call the userService.createUser() method with the duplicate user
//        Throwable exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
//
//        // Verify that the userRepository.save() method was not called
//        verify(userRepository, never()).save(user);
//
//        // Verify that the correct error message is returned when a user with a duplicate email address is created
//        assertEquals("Email already exists in the database.", exception.getMessage());
//    }

//    @Test
//    public void testCreateUser_duplicateUsername() {
//        // Create a test user with a username that already exists in the repository
//        UserModel user = new UserModel();
//        user.setId(1L);
//        user.setName("John Doe");
//        user.setEmail("johndoe@example.com");
//        user.setUsername("johndoe");
//
//        // Mock the behavior of the userRepository.findByUsername() method
//        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));
//
//        // Call the userService.createUser() method with the duplicate user
//        Throwable exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
//
//        // Verify that the userRepository.save() method was not called
//        verify(userRepository, never()).save(user);
//
//        // Verify that the correct error message is returned when a user with a duplicate username is created
//        assertEquals("Username already exists in the database.", exception.getMessage());
//    }

//    @Test
//    public void testCreateUser_invalidEmailFormat() {
//        // Create a test user with an invalid email format
//        UserModel user = new UserModel();
//        user.setId(1L);
//        user.setName("John Doe");
//        user.setEmail("johndoexample.com"); // invalid email format
//        user.setUsername("johndoe");
//
//        // Call the userService.createUser() method with the invalid email
//        Throwable exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
//
//        // Verify that the userRepository.save() method was not called
//        verify(userRepository, never()).save(user);
//
//        // Verify that the correct error message is returned when a user with an invalid email format is created
//        assertEquals("Email is not in a valid format.", exception.getMessage());
//    }

    @Test
    public void testGetUserById() {
        // Arrange
        UserModel user = new UserModel();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setUsername("johndoe");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        UserModel result = userService.getUserById(1L);

        // Assert
        assertEquals(user, result);
    }



    @Test
    public void testUpdateUser() {
        // Arrange
        UserModel user = new UserModel();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setUsername("johndoe");
        UserModel updatedUser = new UserModel();
        updatedUser.setId(1L);
        updatedUser.setName("Jane Doe");
        updatedUser.setEmail("jane.doe@example.com");
        updatedUser.setUsername("janedoe");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserModel.class))).thenReturn(updatedUser);

        // Act
        UserModel result = userService.updateUser(1L, updatedUser);

        // Assert
        assertEquals(updatedUser, result);
    }

    @Test
    public void testDeleteUser() {
        // Arrange
        UserModel user = new UserModel();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setUsername("johndoe");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository, times(1)).deleteById(1L);
    }
}
