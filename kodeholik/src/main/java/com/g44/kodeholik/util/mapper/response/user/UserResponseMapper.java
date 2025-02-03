package com.g44.kodeholik.util.mapper.response.user;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.user.UserResponseDto;
import com.g44.kodeholik.model.entity.user.User;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserResponseMapper implements Mapper<User, UserResponseDto> {

    private final ModelMapper modelMapper;

    @Override
    public User mapTo(UserResponseDto b) {
        return modelMapper.map(b, User.class);
    }

    @Override
    public UserResponseDto mapFrom(User a) {
        return modelMapper.map(a, UserResponseDto.class);
    }

}
