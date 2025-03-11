package com.g44.kodeholik.service.user.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.model.dto.response.user.NotificationResponseDto;
import com.g44.kodeholik.model.entity.user.Notification;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.user.NotificationType;
import com.g44.kodeholik.repository.user.NotificationRepository;
import com.g44.kodeholik.service.publisher.Publisher;
import com.g44.kodeholik.util.mapper.response.user.NotificationResponseMapper;

public class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationResponseMapper notificationResponseMapper;

    @Mock
    private Publisher publisher;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetNotifications() {
        Users user = new Users();
        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);
        Notification notification = new Notification();
        Page<Notification> notificationPage = new PageImpl<>(List.of(notification));
        NotificationResponseDto responseDto = new NotificationResponseDto();

        when(notificationRepository.findByUser(user, pageable)).thenReturn(notificationPage);
        when(notificationResponseMapper.mapFrom(notification)).thenReturn(responseDto);

        Page<NotificationResponseDto> result = notificationService.getNotifications(user, page, size);

        verify(notificationRepository).findByUser(user, pageable);
        verify(notificationResponseMapper).mapFrom(notification);
    }

    @Test
    public void testGetNotificationsWithInvalidPage() {
        Users user = new Users();
        int page = -1;
        int size = 5;

        assertThrows(BadRequestException.class, () -> {
            notificationService.getNotifications(user, page, size);
        });
    }

    @Test
    public void testSaveNotification() throws JsonProcessingException {
        Users user = new Users();
        user.setUsername("testUser");
        String content = "Test Content";
        String link = "http://test.com";
        NotificationType type = NotificationType.SYSTEM;
        Notification notification = new Notification();
        notification.setContent(content);
        notification.setLink(link);
        notification.setType(type);
        notification.setUser(user);
        notification.setDate(Timestamp.from(Instant.now()));
        NotificationResponseDto responseDto = new NotificationResponseDto();

        when(notificationResponseMapper.mapFrom(notification)).thenReturn(responseDto);
        when(mapper.writeValueAsString(responseDto)).thenReturn("jsonString");

        notificationService.saveNotification(user, content, link, type);

        verify(notificationRepository).save(any(Notification.class));
        verify(publisher).sendNotification(any(Map.class));
    }
}