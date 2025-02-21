package com.g44.kodeholik.util.mapper.request.user;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.request.user.AddUserAvatarFileDto;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AddUserAvatarFileMapper implements Mapper<Users, AddUserAvatarFileDto> {

    private final ModelMapper mapper;

    @Override
    public Users mapTo(AddUserAvatarFileDto b) {
        return mapper.map(b, Users.class);
    }

    @Override
    public AddUserAvatarFileDto mapFrom(Users a) {
        return mapper.map(a, AddUserAvatarFileDto.class);

    }

}
