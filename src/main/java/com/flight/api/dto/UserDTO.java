package com.flight.api.dto;

import java.util.Set;

import lombok.Data;

@Data
public class UserDTO {
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private Set<String> roles;
} 