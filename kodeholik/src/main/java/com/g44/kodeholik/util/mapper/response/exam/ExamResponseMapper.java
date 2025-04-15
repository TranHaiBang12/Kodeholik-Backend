package com.g44.kodeholik.util.mapper.response.exam;

import com.g44.kodeholik.util.mapper.response.user.UserResponseMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.exam.examiner.ExamResponseDto;
import com.g44.kodeholik.model.dto.response.user.UserResponseDto;
import com.g44.kodeholik.model.entity.exam.Exam;
import com.g44.kodeholik.service.aws.s3.S3Service;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExamResponseMapper implements Mapper<Exam, ExamResponseDto> {

    private final ModelMapper mapper;

    private final S3Service s3Service;

    private final UserResponseMapper userResponseMapper;

    @Override
    public Exam mapTo(ExamResponseDto b) {
        return mapper.map(b, Exam.class);
    }

    @Override
    public ExamResponseDto mapFrom(Exam a) {
        ExamResponseDto examResponseDto = mapper.map(a, ExamResponseDto.class);
        if (a.getCreatedBy() != null) {
            userResponseMapper.mapFrom(a.getCreatedBy());
        }
        updateAvatar(examResponseDto.getCreatedBy());
        updateAvatar(examResponseDto.getUpdatedBy());
        return examResponseDto;
    }

    private void updateAvatar(UserResponseDto user) {
        if (user != null && user.getAvatar() != null && user.getAvatar().startsWith("kodeholik")) {
            user.setAvatar(s3Service.getPresignedUrl(user.getAvatar()));
        }
    }
}
