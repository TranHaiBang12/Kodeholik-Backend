package com.g44.kodeholik.repository.user;

import java.sql.Date;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.user.UserRole;
import com.g44.kodeholik.model.enums.user.UserStatus;

public interface UserRepository extends JpaRepository<Users, Long> {
        public Optional<Users> findByUsername(String username);

        public Optional<Users> findByEmail(String email);

        @Query("SELECT u FROM Users u WHERE u.username = :text OR u.email = :text")
        public Optional<Users> existsByUsernameOrEmail(@Param("text") String text);

        @Query("SELECT COUNT(u) > 0 FROM Users u WHERE (u.username = :username OR u.email = :username) AND (u.status = 'BANNED' OR u.status = 'NOT_ACTIVATED')")
        public boolean isUserNotAllowed(@Param("username") String username);

        @Query("SELECT u FROM Users u WHERE (cast(:search as text) IS NULL OR " +
                "((LOWER(u.username) LIKE '%' || LOWER(cast(:search as text)) || '%') OR " +
                "(LOWER(u.fullname) LIKE '%' || LOWER(cast(:search as text)) || '%'))) " +
                "AND (COALESCE(:role, u.role) = u.role) " +
                "AND (COALESCE(:status, u.status) = u.status) " +
                "AND (u.createdDate >= :start AND u.createdDate <= :end)")
        Page<Users> getListUserByAdmin(
                @Param("search") String search,
                @Param("role") UserRole role,
                @Param("status") UserStatus status,
                @Param("start") Date start,
                @Param("end") Date end,
                Pageable pageable);

        /*
         * private String text;
         * 
         * private UserRole role;
         * 
         * private UserStatus status;
         * 
         * private Date start;
         * 
         * private Date end;
         */
}
