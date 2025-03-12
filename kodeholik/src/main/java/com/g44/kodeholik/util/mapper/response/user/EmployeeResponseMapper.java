package com.g44.kodeholik.util.mapper.response.user;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.user.EmployeeResponseDto;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.service.aws.s3.S3Service;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmployeeResponseMapper implements Mapper<Users, EmployeeResponseDto> {

    private final ModelMapper mapper;

    private final S3Service s3Service;

    @Override
    public Users mapTo(EmployeeResponseDto b) {
        return mapper.map(b, Users.class);
    }

    @Override
    public EmployeeResponseDto mapFrom(Users a) {
        EmployeeResponseDto employeeResponseDto = mapper.map(a, EmployeeResponseDto.class);
        if (a.getAvatar() != null && a.getAvatar().startsWith("kodeholik")) {
            employeeResponseDto.setAvatar(s3Service.getPresignedUrl(a.getAvatar()));
        }
        return employeeResponseDto;
    }

}
