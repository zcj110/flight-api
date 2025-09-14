package com.flight.api.dto;

import java.util.Set;

import lombok.Data;

@Data
public class UserResponse {

    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String country;
    private String phone;
    private Set<String> roles;
} 