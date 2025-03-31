package com.g44.kodeholik.repository.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.user.Notification;
import com.g44.kodeholik.model.entity.user.Users;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    public Page<Notification> findByUser(Users user, Pageable pageable);
}
