package com.restapi.demo.repository;

import com.restapi.demo.entity.Auth_Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRoleRepository extends JpaRepository<Auth_Role, Long> {
    Optional<Auth_Role> findByName(String name);
    boolean existsByName(String name);
}
