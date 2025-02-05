package com.g44.kodeholik.service.user;

import org.springframework.security.core.userdetails.UserDetails;

import com.g44.kodeholik.model.entity.user.Users;

public interface UserService {
    public Users getUserById(Long userId);

    public UserDetails getCurrentUserDetails();

    public Users getCurrentUser();
}
