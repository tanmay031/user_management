package com.tvm.usermanagement.service;

import com.tvm.usermanagement.model.UserModel;
import com.tvm.usermanagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern emailPattern = Pattern.compile(EMAIL_REGEX);

    @Autowired
    private UserRepository userRepository;

    // Helper method to validate user data
    private void validateUser(UserModel user) {
        if (user == null) {
            throw new IllegalArgumentException("User object is null.");
        }
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required.");
        }
        if (!emailPattern.matcher(user.getEmail()).matches()) {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }

    @Transactional
    @CacheEvict(value = {"users", "totalUsersCount", "usersPagination"}, allEntries = true) // Clears all relevant caches
    public UserModel createUser(UserModel user) {
        validateUser(user);
        String existsMessage = checkUserExists(user);
        if (existsMessage != null) {
            throw new IllegalArgumentException(existsMessage);
        }
        logger.info("Creating a new user with username: {}", user.getUsername());
        return userRepository.save(user);
    }

    @Cacheable(value = "users")
    public List<UserModel> getAllUsers() {
        logger.info("Fetching all users.");
        return userRepository.findAll();
    }

    @Cacheable(value = "usersPagination", key = "{#offset, #pageSize, #sortParam}")
    public Page<UserModel> getAllUsersByPagination(int offset, int pageSize, String sortParam) {
        logger.info("Fetching users with pagination - offset: {}, pageSize: {}, sortParam: {}", offset, pageSize, sortParam);
        return userRepository.findAll(PageRequest.of(offset, pageSize, Sort.by(sortParam).descending()));
    }

    @Cacheable(value = "users", key = "#id")
    public UserModel getUserById(Long id) {
        logger.info("Fetching user by ID: {}", id);
        return userRepository.findById(id).orElse(null);
    }

    @Cacheable(value = "usersByEmail", key = "#email")
    public UserModel getUserByEmail(String email) {
        logger.info("Fetching user by email: {}", email);
        return userRepository.findByEmail(email).orElse(null);
    }

    @Cacheable(value = "usersByUsername", key = "#username")
    public UserModel getUserByUsername(String username) {
        logger.info("Fetching user by username: {}", username);
        return userRepository.findByUsername(username).orElse(null);
    }

    @Transactional
    @CacheEvict(value = {"users", "totalUsersCount", "usersPagination"}, allEntries = true) // Clears all relevant caches
    public UserModel updateUser(Long id, UserModel user) {
        validateUser(user);
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setUsername(user.getUsername());
            logger.info("Updating user with ID: {}", id);
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
    }

    @Transactional
    @CacheEvict(value = {"users", "totalUsersCount", "usersPagination"}, allEntries = true) // Clears all relevant caches
    public void deleteUser(Long id) {
        logger.info("Deleting user with ID: {}", id);
        userRepository.deleteById(id);
    }

    // Helper method to check if a user already exists by email or username
    public String checkUserExists(UserModel userModel) {
        if (userRepository.findByEmail(userModel.getEmail()).isPresent()) {
            return "User with email " + userModel.getEmail() + " already exists.";
        }
        if (userRepository.findByUsername(userModel.getUsername()).isPresent()) {
            return "User with username " + userModel.getUsername() + " already exists.";
        }
        return null;
    }

    @Cacheable(value = "totalUsersCount")
    public long getTotalUsersCount() {
        logger.info("Fetching total user count from the database");
        return userRepository.count();
    }
}
