package com.g44.kodeholik.util.mapper.response.problem;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.problem.ProblemBasicResponseDto;
import com.g44.kodeholik.model.dto.response.user.EmployeeResponseDto;
import com.g44.kodeholik.model.dto.response.user.UserResponseDto;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.service.aws.s3.S3Service;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProblemBasicResponseMapper implements Mapper<Problem, ProblemBasicResponseDto> {

    private final ModelMapper modelMapper;

    private final S3Service s3Service;

    @Override
    public Problem mapTo(ProblemBasicResponseDto b) {
        return modelMapper.map(b, Problem.class);
    }

    @Override
    public ProblemBasicResponseDto mapFrom(Problem a) {
        ProblemBasicResponseDto problemBasicResponseDto = modelMapper.map(a, ProblemBasicResponseDto.class);
        updateAvatar(problemBasicResponseDto.getCreatedBy());
        updateAvatar(problemBasicResponseDto.getUpdatedBy());
        return problemBasicResponseDto;
    }

    private void updateAvatar(EmployeeResponseDto user) {
        if (user != null && user.getAvatar() != null && user.getAvatar().startsWith("kodeholik")) {
            user.setAvatar(s3Service.getPresignedUrl(user.getAvatar()));
        }
    }

}
