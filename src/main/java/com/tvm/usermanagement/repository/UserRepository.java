
package com.tvm.usermanagement.repository;

import com.tvm.usermanagement.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByEmailOrUsername(String email, String username);
}
