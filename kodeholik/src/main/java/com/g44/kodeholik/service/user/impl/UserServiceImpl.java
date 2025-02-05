package com.g44.kodeholik.service.user.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.user.LoginRequestDto;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.token.TokenType;
import com.g44.kodeholik.repository.user.UserRepository;
import com.g44.kodeholik.service.token.TokenService;
import com.g44.kodeholik.service.user.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Users getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found", "User not found"));
    }

    @Override
    public UserDetails getCurrentUserDetails() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal != null && principal instanceof UserDetails userDetails) {
            return userDetails;
        }
        return null;
    }

    @Override
    public Users getCurrentUser() {
        UserDetails currenUserDetails = getCurrentUserDetails();
        if (currenUserDetails != null) {
            return userRepository.findByUsername(currenUserDetails.getUsername())
                    .orElseThrow(() -> new NotFoundException("User not found", "User not found"));
        }
        return null;
    }
}
