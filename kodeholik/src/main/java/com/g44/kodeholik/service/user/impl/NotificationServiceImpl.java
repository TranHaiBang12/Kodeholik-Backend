package com.g44.kodeholik.service.user.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.model.dto.response.user.NotificationResponseDto;
import com.g44.kodeholik.model.entity.user.Notification;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.user.NotificationType;
import com.g44.kodeholik.repository.user.NotificationRepository;
import com.g44.kodeholik.service.publisher.Publisher;
import com.g44.kodeholik.service.user.NotificationService;
import com.g44.kodeholik.util.mapper.response.user.NotificationResponseMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    private final NotificationResponseMapper notificationResponseMapper;

    private final Publisher publisher;

    private final ObjectMapper mapper;

    @Override
    public Page<NotificationResponseDto> getNotifications(Users user, int page, Integer size) {
        if (page < 0) {
            throw new BadRequestException("Page number must be greater than 0", "Page number must be greater than 0");
        }
        Pageable pageable = PageRequest.of(page, size != null ? size.intValue() : 5);
        Page<Notification> notificationPage = notificationRepository.findByUser(user, pageable);
        return notificationPage.map(notificationResponseMapper::mapFrom);

    }

    @Async("githubTaskExecutor")
    @Transactional
    @Override
    public void saveNotification(Users user, String content, String link, NotificationType type) {
        Notification notification = new Notification();
        notification.setContent(content);
        notification.setLink(link);
        notification.setType(type);
        notification.setUser(user);
        notification.setDate(Timestamp.from(Instant.now()));
        notificationRepository.save(notification);

        Map<String, Object> notifications = new HashMap();
        try {
            String json = mapper.writeValueAsString(notificationResponseMapper.mapFrom(notification));
            notifications.put("notification", json);
            notifications.put("username", user.getUsername());
            publisher.sendNotification(notifications);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
