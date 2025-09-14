package com.flight.api.service;

import com.flight.api.dto.UserCreationRequest;
import com.flight.api.entity.User;

public interface UserService {
    String registerUser(UserCreationRequest request);
    User createUser(UserCreationRequest request);
    String login(String email, String password);
    User getUserById(Long userId);
    User getUserByEmail(String email);
    void updateUser(Long userId, User user);
    void deleteUser(Long userId);
} 