package com.g44.kodeholik.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.user.Users;
import java.util.List;

public interface UserRepository extends JpaRepository<Users, Long> {
    public Optional<Users> findByUsername(String username);
}
