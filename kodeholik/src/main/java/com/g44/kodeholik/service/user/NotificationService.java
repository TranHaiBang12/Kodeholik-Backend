package com.g44.kodeholik.service.user;

import org.springframework.data.domain.Page;

import com.g44.kodeholik.model.dto.response.user.NotificationResponseDto;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.user.NotificationType;

public interface NotificationService {
    public Page<NotificationResponseDto> getNotifications(Users user, int page, Integer size);

    public void saveNotification(Users user, String content, String link, NotificationType type);
}
