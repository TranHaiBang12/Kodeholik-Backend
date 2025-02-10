package com.g44.kodeholik.service.user.impl;

import java.sql.Date;
import java.time.Instant;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.user.AddUserRequestDto;
import com.g44.kodeholik.model.dto.request.user.LoginRequestDto;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.token.TokenType;
import com.g44.kodeholik.model.enums.user.UserStatus;
import com.g44.kodeholik.repository.user.UserRepository;
import com.g44.kodeholik.service.email.EmailService;
import com.g44.kodeholik.service.token.TokenService;
import com.g44.kodeholik.service.user.UserService;
import com.g44.kodeholik.util.mapper.request.user.AddUserRequestMapper;
import com.g44.kodeholik.util.password.PasswordUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final AddUserRequestMapper addUserRequestMapper;

    private final EmailService emailService;

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

    @Override
    public Users addUser(AddUserRequestDto addUserRequestDto) {
        String password = PasswordUtils.generatePassword();
        log.info(password);
        Users user = addUserRequestMapper.mapTo(addUserRequestDto);
        user.setCreatedDate(new java.sql.Date(Date.from(Instant.now()).getTime()));
        user.setPassword(PasswordUtils.encodePassword(password));
        user.setStatus(UserStatus.ACTIVATED);
        emailService.sendEmailAddUser(user.getEmail(), "[KODEHOLIK] First Time Login", user.getUsername(),
                password);
        return userRepository.save(user);
    }

    @Override
    public Users getUserByUsernameOrEmail(String username) {
        return userRepository.existsByUsernameOrEmail(username)
                .orElseThrow(() -> new NotFoundException("User not found", "User not found"));
    }
}
