package com.g44.kodeholik.model.entity.user;

import java.sql.Date;

import com.g44.kodeholik.model.enums.user.UserRole;
import com.g44.kodeholik.model.enums.user.UserStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Entity
@FieldNameConstants
@Table(name = "users", schema = "schema_user")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String avatar;

    private String username;

    private String fullname;

    private String password;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "created_date")
    private Date createdDate;

    public Users(String avatar, String username, String fullname, String password, String email, UserStatus status,
            UserRole role) {
        this.avatar = avatar;
        this.username = username;
        this.fullname = fullname;
        this.password = password;
        this.email = email;
        this.status = status;
        this.role = role;
        this.createdDate = new Date(System.currentTimeMillis());
    }

}
