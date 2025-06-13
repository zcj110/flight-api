package com.flight.api.mapper;

import com.flight.api.dto.UserCreationRequest;
import com.flight.api.dto.UserResponse;
import com.flight.api.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-12T12:23:34+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.6 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(UserCreationRequest request) {
        if ( request == null ) {
            return null;
        }

        User user = new User();

        user.setPassword( request.getPassword() );
        user.setEmail( request.getEmail() );
        user.setFirstName( request.getFirstName() );
        user.setLastName( request.getLastName() );
        user.setCountry( request.getCountry() );
        user.setPhone( request.getPhone() );
        user.setRole( request.getRole() );

        return user;
    }

    @Override
    public UserResponse toResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse userResponse = new UserResponse();

        userResponse.setRoles( authoritiesToRoles( user.getAuthorities() ) );
        userResponse.setUserId( user.getUserId() );
        userResponse.setEmail( user.getEmail() );
        userResponse.setFirstName( user.getFirstName() );
        userResponse.setLastName( user.getLastName() );
        userResponse.setCountry( user.getCountry() );
        userResponse.setPhone( user.getPhone() );

        return userResponse;
    }

    @Override
    public void updateEntityFromDto(UserCreationRequest request, User user) {
        if ( request == null ) {
            return;
        }

        user.setEmail( request.getEmail() );
        user.setFirstName( request.getFirstName() );
        user.setLastName( request.getLastName() );
        user.setCountry( request.getCountry() );
        user.setPhone( request.getPhone() );
        user.setRole( request.getRole() );
    }
}
