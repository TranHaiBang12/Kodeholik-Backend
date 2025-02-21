package com.g44.kodeholik.util.mapper.response.user;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.user.EmployeeResponseDto;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmployeeResponseMapper implements Mapper<Users, EmployeeResponseDto> {

    private final ModelMapper mapper;

    @Override
    public Users mapTo(EmployeeResponseDto b) {
        return mapper.map(b, Users.class);
    }

    @Override
    public EmployeeResponseDto mapFrom(Users a) {
        return mapper.map(a, EmployeeResponseDto.class);

    }

}
