package com.expandapis.user.model.mapper;

import com.expandapis.user.model.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import static org.springframework.security.core.userdetails.User.withUsername;

@Component
public class UserMapper{
    public UserDetails toUserDetails(User userEntity) {
        return withUsername(userEntity.getUsername())
                .password(userEntity.getPassword())
                .authorities("ROLE_USER")
                .build();
    }
}
