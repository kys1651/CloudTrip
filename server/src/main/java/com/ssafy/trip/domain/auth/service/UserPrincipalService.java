package com.ssafy.trip.domain.auth.service;

import com.ssafy.trip.core.exception.CustomException;
import com.ssafy.trip.core.exception.ErrorCode;
import com.ssafy.trip.domain.auth.entity.UserPrincipal;
import com.ssafy.trip.domain.user.entity.User;
import com.ssafy.trip.domain.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class UserPrincipalService implements UserDetailsService {
    private final UserMapper userMapper;

    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findByUsername(username).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        return new UserPrincipal(user);
    }

    @Bean
    public Function<UserDetails, User> fetchUser() {
        return userDetails -> this.loadUserByUsername(userDetails.getUsername()).getUser();
    }
}
