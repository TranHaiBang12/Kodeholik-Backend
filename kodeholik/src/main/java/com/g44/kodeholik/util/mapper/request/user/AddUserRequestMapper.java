package com.g44.kodeholik.util.mapper.request.user;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

import com.g44.kodeholik.model.dto.request.user.AddUserRequestDto;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AddUserRequestMapper implements Mapper<Users, AddUserRequestDto> {

    private final ModelMapper modelMapper;

    @Override
    public Users mapTo(AddUserRequestDto b) {
        return modelMapper.map(b, Users.class);
    }

    @Override
    public AddUserRequestDto mapFrom(Users a) {
        return modelMapper.map(a, AddUserRequestDto.class);

    }

}
