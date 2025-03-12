package com.g44.kodeholik.util.mapper.request.user;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.request.user.EditUserAvatarFileDto;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EditUserAvatarFileMapper implements Mapper<Users, EditUserAvatarFileDto> {

    private final ModelMapper mapper;

    @Override
    public Users mapTo(EditUserAvatarFileDto b) {
        return mapper.map(b, Users.class);
    }

    @Override
    public EditUserAvatarFileDto mapFrom(Users a) {
        return mapper.map(a, EditUserAvatarFileDto.class);

    }

}
