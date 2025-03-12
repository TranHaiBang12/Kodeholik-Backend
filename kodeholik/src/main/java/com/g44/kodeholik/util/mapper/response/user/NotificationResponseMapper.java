package com.g44.kodeholik.util.mapper.response.user;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.user.NotificationResponseDto;
import com.g44.kodeholik.model.entity.user.Notification;
import com.g44.kodeholik.service.aws.s3.S3Service;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationResponseMapper implements Mapper<Notification, NotificationResponseDto> {

    private final ModelMapper mapper;

    private final S3Service s3Service;

    @Override
    public Notification mapTo(NotificationResponseDto b) {
        return mapper.map(b, Notification.class);
    }

    @Override
    public NotificationResponseDto mapFrom(Notification a) {
        NotificationResponseDto notificationResponseDto = mapper.map(a, NotificationResponseDto.class);
        if (a.getUser() != null && a.getUser().getAvatar() != null && a.getUser().getAvatar().startsWith("kodeholik")) {
            notificationResponseDto.getUser().setAvatar(s3Service.getPresignedUrl(a.getUser().getAvatar()));
        }
        return notificationResponseDto;
    }

}
