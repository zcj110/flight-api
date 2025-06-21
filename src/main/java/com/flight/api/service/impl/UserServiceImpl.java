package com.flight.api.service.impl;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flight.api.dto.UserCreationRequest;
import com.flight.api.entity.User;
import com.flight.api.mapper.UserMapper;
import com.flight.api.repository.UserRepository;
import com.flight.api.security.JwtTokenProvider;
import com.flight.api.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private  UserMapper userMapper;
    @Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private  JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public String registerUser(UserCreationRequest request) {
        // Create the user first
        User newUser = createUser(request);

        // Authenticate the newly registered user to generate a token
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            String token = jwtTokenProvider.generateToken(authentication);
            log.info("New user {} registered and logged in successfully.", request.getEmail());
            log.info("Returning JWT token for registered user: {}", request.getEmail());
            return token;
        } catch (AuthenticationException e) {
            log.error("Authentication failed immediately after registration for user {}: {}", request.getEmail(), e.getMessage());
            throw new RuntimeException("Registration successful but immediate login failed.", e);
        }
    }

    @Override
    @Transactional
    public User createUser(UserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);
        log.info("Created user with ID: {}, email: {}", savedUser.getUserId(), savedUser.getEmail());
        return savedUser;
    }

    @Override
    public String login(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );
            String token = jwtTokenProvider.generateToken(authentication);
            log.info("User {} logged in successfully.", email);
            log.info("Returning JWT token for logged in user: {}", email);
            return token;
        } catch (AuthenticationException e) {
            log.error("Authentication failed for user {}: {}", email, e.getMessage());
            throw e; // Re-throw the exception after logging
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
        log.info("Retrieved user with ID: {}, email: {}", user.getUserId(), user.getEmail());
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        log.info("Retrieved user with email: {}, ID: {}", user.getEmail(), user.getUserId());
        return user;
    }

    @Override
    @Transactional
    public void updateUser(Long userId, User user) {
        User existingUser = getUserById(userId);
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        User updatedUser = userRepository.save(existingUser);
        log.info("Updated user with ID: {}, email: {}", updatedUser.getUserId(), updatedUser.getEmail());
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
        log.info("Deleted user with ID: {}", userId);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Attempting to load user by email: {}", email);
        com.flight.api.entity.User user = userRepository.findByEmail(email)
            .orElseThrow(() -> {
                log.warn("User with email {} not found.", email);
                return new UsernameNotFoundException("User not found with email: " + email);
            });
        log.info("User {} found. User ID: {}", user.getEmail(), user.getUserId());

        // Return Spring Security's User object
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
        log.info("Returning UserDetails for user: {}", email);
        return userDetails;
    }
} 