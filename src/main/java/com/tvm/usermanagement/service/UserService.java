package com.tvm.usermanagement.service;

import com.tvm.usermanagement.exception.UserAlreadyExistsException;
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
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern emailPattern = Pattern.compile(EMAIL_REGEX);

    @Autowired
    private UserRepository userRepository;

    // Helper method to validate user data


    @Transactional
    @CacheEvict(value = {"users", "totalUsersCount", "usersPagination"}, allEntries = true) // Clears all relevant caches
    public UserModel createUser(UserModel user) {
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

    @Transactional
    @CacheEvict(value = {"users", "totalUsersCount", "usersPagination"}, allEntries = true) // Clears all relevant caches
    public UserModel updateUser(Long id, UserModel user) {
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
    public void checkUserExists(UserModel userModel) {
        Optional<UserModel> existingUser = userRepository.findByEmailOrUsername(userModel.getEmail(), userModel.getUsername());

        if (existingUser.isPresent()) {
            if (existingUser.get().getEmail().equals(userModel.getEmail())) {
                throw new UserAlreadyExistsException("email", "User with email " + userModel.getEmail() + " already exists.");
            }
            if (existingUser.get().getUsername().equals(userModel.getUsername())) {
                throw new UserAlreadyExistsException("username", "User with username " + userModel.getUsername() + " already exists.");
            }
        }
    }

    @Cacheable(value = "totalUsersCount")
    public long getTotalUsersCount() {
        logger.info("Fetching total user count from the database");
        return userRepository.count();
    }
}
