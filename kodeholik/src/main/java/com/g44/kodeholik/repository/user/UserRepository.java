package com.g44.kodeholik.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.user.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
