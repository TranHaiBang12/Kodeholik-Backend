package com.g44.kodeholik.util.mapper.request.user;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.request.user.EditProfileRequestDto;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EditProfileRequestMapper implements Mapper<Users, EditProfileRequestDto> {

    private final ModelMapper mapper;

    @Override
    public Users mapTo(EditProfileRequestDto b) {
        return mapper.map(b, Users.class);
    }

    @Override
    public EditProfileRequestDto mapFrom(Users a) {
        return mapper.map(a, EditProfileRequestDto.class);

    }

}
