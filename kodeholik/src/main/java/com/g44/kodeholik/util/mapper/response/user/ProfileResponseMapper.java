package com.g44.kodeholik.util.mapper.response.user;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.user.ProfileResponseDto;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProfileResponseMapper implements Mapper<Users, ProfileResponseDto> {

    private final ModelMapper mapper;

    @Override
    public Users mapTo(ProfileResponseDto b) {
        return mapper.map(b, Users.class);
    }

    @Override
    public ProfileResponseDto mapFrom(Users a) {
        return mapper.map(a, ProfileResponseDto.class);

    }

}
