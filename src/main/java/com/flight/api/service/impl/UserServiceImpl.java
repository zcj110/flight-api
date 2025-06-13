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
            log.info("New user {} registered and logged in successfully.", request.getEmail());
            return jwtTokenProvider.generateToken(authentication);
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
        return userRepository.save(user);
    }

    @Override
    public String login(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );
            log.info("User {} logged in successfully.", email);
            return jwtTokenProvider.generateToken(authentication);
        } catch (AuthenticationException e) {
            log.error("Authentication failed for user {}: {}", email, e.getMessage());
            throw e; // Re-throw the exception after logging
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
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
        userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
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
        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
} 