package com.g44.kodeholik.util.mapper.response.user;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.user.UserResponseDto;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.service.aws.s3.S3Service;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserResponseMapper implements Mapper<Users, UserResponseDto> {

    private final ModelMapper modelMapper;

    private final S3Service s3Service;

    @Override
    public Users mapTo(UserResponseDto b) {
        return modelMapper.map(b, Users.class);
    }

    @Override
    public UserResponseDto mapFrom(Users a) {
        UserResponseDto userResponseDto = modelMapper.map(a, UserResponseDto.class);
        if (a.getAvatar() != null && a.getAvatar().startsWith("kodeholik")) {
            userResponseDto.setAvatar(s3Service.getPresignedUrl(a.getAvatar()));
        }
        return userResponseDto;
    }

}
