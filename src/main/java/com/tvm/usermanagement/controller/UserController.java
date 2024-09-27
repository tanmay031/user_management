package com.tvm.usermanagement.controller;

import com.tvm.usermanagement.common.ApiResponse;
import com.tvm.usermanagement.exception.UserNotFoundException;
import com.tvm.usermanagement.model.UserModel;
import com.tvm.usermanagement.service.UserService;
import com.tvm.usermanagement.util.ResponseUtil;
import com.tvm.usermanagement.validation.UserModelValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserModelValidator userModelValidator;

    @GetMapping("/hello")
    public String helloWorld() {
        return "sdsddddd!";
    }

    @PostMapping("/user")
    public ResponseEntity<ApiResponse<UserModel>> createUser(@Valid @RequestBody UserModel userModel, BindingResult bindingResult) {
        userModelValidator.validate(userModel, bindingResult);
        if (bindingResult.hasErrors()) {
            String errorMessage = ResponseUtil.getErrorMessage(bindingResult);
            return ResponseUtil.createResponse(HttpStatus.BAD_REQUEST, errorMessage, 0,null);
        }

        String errorMessage = userService.checkUserExists(userModel);
        if (errorMessage != null) {
            return ResponseUtil.createResponse(HttpStatus.CONFLICT, errorMessage, 0,null);
        }

        UserModel createdUser = userService.createUser(userModel);
        return ResponseUtil.createResponse(HttpStatus.CREATED, "User created successfully",0, createdUser);
    }

    @GetMapping("/users")
    ResponseEntity<ApiResponse<List<UserModel>>> getAllUsers(@RequestParam(name = "offset", defaultValue = "0") int offset,
                                                             @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                                             @RequestParam(name = "sortBy", defaultValue = "id") String sortBy) {
        List<UserModel> users = userService.getAllUsers();
        Page<UserModel> usersWithPagination  = userService.getAllUsersByPagination(offset-1, pageSize,sortBy);

        return ResponseUtil.createResponse(HttpStatus.OK, "All users retrieved successfully.",users.size(), usersWithPagination.getContent());
    }

    // Endpoint to get all users
    @GetMapping("/all")
    public ResponseEntity<List<UserModel>> getAllUsers() {
        // Fetch all users
        List<UserModel> users = userService.getAllUsers();

        // Check if the list is empty and return appropriate status
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } else {
            return new ResponseEntity<>(users, HttpStatus.OK); // 200 OK
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<ApiResponse<UserModel>> getUserById(@PathVariable Long id) {
        UserModel user = userService.getUserById(id);
        if (user != null) {
            return ResponseUtil.createResponse(HttpStatus.OK, "User retrieved successfully.",0, user);
        } else {
            throw new UserNotFoundException("User not found with id " + id, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<ApiResponse<UserModel>>  updateUser(@PathVariable Long id,@Valid  @RequestBody UserModel userModel,BindingResult bindingResult) {
        userModelValidator.validate(userModel, bindingResult);
        if (bindingResult.hasErrors()) {
            String errorMessage = ResponseUtil.getErrorMessage(bindingResult);
            return ResponseUtil.createResponse(HttpStatus.BAD_REQUEST, errorMessage, 0,null);
        }

        UserModel updatedUser = userService.updateUser(id, userModel);
        if (updatedUser == null) {
            throw new UserNotFoundException("User not found with id " + id, HttpStatus.NOT_FOUND);
        }

        return ResponseUtil.createResponse(HttpStatus.CREATED, "User updated successfully",0, updatedUser);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<ApiResponse<UserModel>>  deleteUser(@PathVariable Long id) {
        UserModel userModel = userService.getUserById(id);
        if (userModel == null ) {
            throw new UserNotFoundException("User not found with id " + id, HttpStatus.NOT_FOUND);
        }

        userService.deleteUser(id);
        return ResponseUtil.createResponse(HttpStatus.OK, "User with id " + id + " has been deleted successfully.",0, userModel);
    }


}
