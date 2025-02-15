package com.g44.kodeholik.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.g44.kodeholik.model.entity.user.Users;

public interface UserRepository extends JpaRepository<Users, Long> {
    public Optional<Users> findByUsername(String username);

    public Optional<Users> findByEmail(String email);

    @Query("SELECT u FROM Users u WHERE u.username = :text OR u.email = :text")
    public Optional<Users> existsByUsernameOrEmail(@Param("text") String text);

    @Query("SELECT COUNT(u) > 0 FROM Users u WHERE (u.username = :username OR u.email = :username) AND (u.status = 'BANNED' OR u.status = 'NOT_ACTIVATED')")
    public boolean isUserNotAllowed(@Param("username") String username);
}
