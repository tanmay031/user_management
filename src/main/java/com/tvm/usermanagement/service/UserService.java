package com.tvm.usermanagement.service;

import com.tvm.usermanagement.model.UserModel;
import com.tvm.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    public UserModel createUser(UserModel user) {
        if (user == null) {
            throw new IllegalArgumentException("User object or its fields are null.");
        }
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required.");
        }
        // Other validations...
        return userRepository.save(user);
    }

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    public Page<UserModel> getAllUsersByPagination(int offset, int pageSize, String sortParam){
        Page<UserModel> products = userRepository.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(sortParam).descending()));
        return  products;
    }

    public UserModel getUserById(Long id) {
        Optional<UserModel> user = userRepository.findById(id);
        return user.orElse(null);
    }

    public UserModel getUserByEmail(String email) {
        Optional<UserModel> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }

    public UserModel getUserByUsername(String username) {
        Optional<UserModel> user = userRepository.findByUsername(username);
        return user.orElse(null);
    }

    public UserModel updateUser(Long id, UserModel user) {
        Optional<UserModel> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            UserModel existingUser = optionalUser.get();
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setUsername(user.getUsername());
            return userRepository.save(existingUser);
        } else {
            return null;
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public String checkUserExists(UserModel userModel) {
        UserModel existingUserByEmail = this.getUserByEmail(userModel.getEmail());
        if (existingUserByEmail != null) {
            return "User with email " + userModel.getEmail() + " already exists.";
        }

        UserModel existingUserByUsername = this.getUserByUsername(userModel.getUsername());
        if (existingUserByUsername != null) {
            return "User with username " + userModel.getUsername() + " already exists.";
        }

        return null;
    }
}

