package com.g44.kodeholik.service.user.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.exception.UnauthorizedException;
import com.g44.kodeholik.model.entity.user.UserPrincipal;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(username);
        Users user = userRepository.existsByUsernameOrEmail(username)
                .orElseThrow(() -> new UnauthorizedException("User not found", "User not found"));
        UserDetails userDetails = new UserPrincipal(user);
        return userDetails;

    }

}
