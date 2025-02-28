package com.g44.kodeholik.util.mapper.response.user;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.user.NotificationResponseDto;
import com.g44.kodeholik.model.entity.user.Notification;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationResponseMapper implements Mapper<Notification, NotificationResponseDto> {

    private final ModelMapper mapper;

    @Override
    public Notification mapTo(NotificationResponseDto b) {
        return mapper.map(b, Notification.class);
    }

    @Override
    public NotificationResponseDto mapFrom(Notification a) {
        return mapper.map(a, NotificationResponseDto.class);

    }

}
