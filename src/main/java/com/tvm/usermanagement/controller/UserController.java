package com.tvm.usermanagement.controller;

import com.tvm.usermanagement.common.ApiResponse;
import com.tvm.usermanagement.exception.UserNotFoundException;
import com.tvm.usermanagement.model.UserModel;
import com.tvm.usermanagement.service.UserService;
import com.tvm.usermanagement.util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * Creates a new user.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UserModel>> createUser(@Valid @RequestBody UserModel userModel, BindingResult bindingResult) {
        logger.info("Creating a new user with username: {}", userModel.getUsername());

        // Check for validation errors
        if (bindingResult.hasErrors()) {
            String errorMessage = ResponseUtil.getErrorMessage(bindingResult);
            logger.warn("User creation failed: {}", errorMessage);
            return ResponseUtil.createResponse(HttpStatus.BAD_REQUEST, errorMessage, 0, null);
        }

        userService.checkUserExists(userModel);


        UserModel createdUser = userService.createUser(userModel);
        logger.info("User created successfully with ID: {}", createdUser.getId());
        return ResponseUtil.createResponse(HttpStatus.CREATED, "User created successfully", 0, createdUser);
    }

    /**
     * Retrieves all users with pagination and sorting.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserModel>>> getAllUsers(
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy) {
        logger.info("Retrieving all users with pagination - offset: {}, pageSize: {}, sortBy: {}", offset, pageSize, sortBy);

        long totalUsers = userService.getTotalUsersCount();
        Page<UserModel> usersWithPagination = userService.getAllUsersByPagination(offset, pageSize, sortBy);
        return ResponseUtil.createResponse(HttpStatus.OK, "All users retrieved successfully.", (int) totalUsers, usersWithPagination.getContent());
    }

    /**
     * Retrieves a user by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserModel>> getUserById(@PathVariable Long id) {
        logger.info("Retrieving user with ID: {}", id);

        UserModel user = userService.getUserById(id);
        if (user != null) {
            return ResponseUtil.createResponse(HttpStatus.OK, "User retrieved successfully.", 0, user);
        } else {
            logger.warn("User not found with ID: {}", id);
            throw new UserNotFoundException("User not found with id " + id);
        }
    }

    /**
     * Updates an existing user.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserModel>> updateUser(@PathVariable Long id, @Valid @RequestBody UserModel userModel, BindingResult bindingResult) {
        logger.info("Updating user with ID: {}", id);

        // Check for validation errors
        if (bindingResult.hasErrors()) {
            String errorMessage = ResponseUtil.getErrorMessage(bindingResult);
            logger.warn("Validation failed: {}", errorMessage);
            return ResponseUtil.createResponse(HttpStatus.BAD_REQUEST, errorMessage, 0, null);
        }

        userService.checkUserExists(userModel);

        UserModel updatedUser = userService.updateUser(id, userModel);
        if (updatedUser == null) {
            logger.warn("User not found with ID: {}", id);
            throw new UserNotFoundException("User not found with id " + id);
        }

        logger.info("User updated successfully with ID: {}", id);
        return ResponseUtil.createResponse(HttpStatus.OK, "User updated successfully", 0, updatedUser);
    }

    /**
     * Deletes a user by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<UserModel>> deleteUser(@PathVariable Long id) {
        logger.info("Deleting user with ID: {}", id);

        UserModel userModel = userService.getUserById(id);
        if (userModel == null) {
            logger.warn("User not found with ID: {}", id);
            throw new UserNotFoundException("User not found with id " + id);
        }

        userService.deleteUser(id);
        logger.info("User with ID: {} has been deleted successfully.", id);
        return ResponseUtil.createResponse(HttpStatus.OK, "User with ID " + id + " has been deleted successfully.", 0, userModel);
    }
}
