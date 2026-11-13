package com.restapi.demo.repository;

import com.restapi.demo.entity.Auth_User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<Auth_User, Long> {
    Optional<Auth_User> findByUsername(String username);
    boolean existsByUsername(String username);
}
