package com.flight.api.mapper;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.security.core.GrantedAuthority;

import com.flight.api.dto.UserCreationRequest;
import com.flight.api.dto.UserResponse;
import com.flight.api.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "password", source = "password")
    @Mapping(target = "authorities", ignore = true)
    User toEntity(UserCreationRequest request);

    @Mapping(target = "roles", source = "authorities", qualifiedByName = "authoritiesToRoles")
    UserResponse toResponse(User user);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "password", ignore = true) // Password should not be updated directly via DTO
    @Mapping(target = "authorities", ignore = true)
    void updateEntityFromDto(UserCreationRequest request, @MappingTarget User user);

    @Named("authoritiesToRoles")
    default Set<String> authoritiesToRoles(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());
    }
} 