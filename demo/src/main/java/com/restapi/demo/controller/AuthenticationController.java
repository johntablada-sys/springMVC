package com.restapi.demo.controller;

import com.restapi.demo.dto.AuthRoleRegisterDto;
import com.restapi.demo.dto.AuthLoginDto;
import com.restapi.demo.dto.AuthUserRegisterDto;
import com.restapi.demo.entity.Auth_Role;
import com.restapi.demo.entity.Auth_User;
import com.restapi.demo.repository.AuthRoleRepository;
import com.restapi.demo.repository.AuthUserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final AuthUserRepository authUserRepo;
    private final AuthRoleRepository authRoleRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationController(AuthenticationManager authenticationManager,
                                   AuthUserRepository authUserRepo,
                                   AuthRoleRepository authRoleRepo,
                                   PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.authUserRepo = authUserRepo;
        this.authRoleRepo = authRoleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register_authorized_role")
    public ResponseEntity<String> registerAuthRole(@RequestBody AuthRoleRegisterDto authRoleRegisterDto) {
        if (authRoleRepo.existsByName(authRoleRegisterDto.getName())) {
            return new ResponseEntity<>("Role " + authRoleRegisterDto.getName() + " already created.", HttpStatus.BAD_REQUEST);
        }

        Auth_Role authRole = new Auth_Role();
        authRole.setName(authRoleRegisterDto.getName());
        authRoleRepo.save(authRole);

        return new ResponseEntity<>("Role " + authRole.getName() + " is successfully created.", HttpStatus.OK);
    }

    @PostMapping("/register_authorized_user")
    public ResponseEntity<String> registerAuthUser(@RequestBody AuthUserRegisterDto authUserRegisterDto) {
        if (authUserRepo.existsByUsername(authUserRegisterDto.getUsername())) {
            return new ResponseEntity<>("User " + authUserRegisterDto.getUsername() + " is already created.", HttpStatus.BAD_REQUEST);
        }

        Auth_User authUser = new Auth_User();
        authUser.setUsername(authUserRegisterDto.getUsername());
        authUser.setPassword(passwordEncoder.encode(authUserRegisterDto.getPassword()));

        Auth_Role authRole = authRoleRepo.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Roles USER not found"));
        authUser.setRoles(Collections.singletonList(authRole));

        authUserRepo.save(authUser);

        return new ResponseEntity<>("User " + authUser.getUsername() + " is successfully registered.", HttpStatus.OK);
    }

    @PostMapping("/auth_login")
    public ResponseEntity<String> loginAuthUser(@RequestBody AuthLoginDto authLoginDto) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authLoginDto.getUsername(), 
                authLoginDto.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        return new ResponseEntity<>("User " + authLoginDto.getUsername() + " is successfully logged in.", HttpStatus.OK);
    }

    @PostMapping("/auth_login_session")
public ResponseEntity<?> loginWithSession(@RequestBody AuthLoginDto authLoginDto,
                                          HttpServletRequest request) {

    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authLoginDto.getUsername(),
                    authLoginDto.getPassword())
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);
    HttpSession session = request.getSession(true);
    session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

    Map<String, String> response = new HashMap<>();
    response.put("message", "Authenticated successfully via session cookie.");
    return ResponseEntity.ok(response);
}

    @PostMapping("/logout")
public ResponseEntity<String> logout(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session != null) {
        session.invalidate();
    }

    SecurityContextHolder.clearContext();
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("Logged out from session container.");
}
}
