package com.g44.kodeholik.util.mapper.response.user;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.user.ProfileResponseDto;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.service.aws.s3.S3Service;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class ProfileResponseMapper implements Mapper<Users, ProfileResponseDto> {

    private final ModelMapper mapper;

    private final S3Service s3Service;

    @Override
    public Users mapTo(ProfileResponseDto b) {
        return mapper.map(b, Users.class);
    }

    @Override
    public ProfileResponseDto mapFrom(Users a) {
        ProfileResponseDto profileResponseDto = mapper.map(a, ProfileResponseDto.class);
        if (a.getAvatar() != null && a.getAvatar().startsWith("kodeholik")) {
            profileResponseDto.setAvatar(s3Service.getPresignedUrl(a.getAvatar()));
        }
        return profileResponseDto;

    }

}
