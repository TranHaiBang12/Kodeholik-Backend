package com.g44.kodeholik.util.mapper.response.problem;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.problem.submission.ProblemSubmissionDto;
import com.g44.kodeholik.model.entity.problem.ProblemSubmission;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProblemSubmissionMapper implements Mapper<ProblemSubmission, ProblemSubmissionDto> {

    private final ModelMapper modelMapper;

    @Override
    public ProblemSubmission mapTo(ProblemSubmissionDto b) {
        return modelMapper.map(b, ProblemSubmission.class);
    }

    @Override
    public ProblemSubmissionDto mapFrom(ProblemSubmission a) {
        return modelMapper.map(a, ProblemSubmissionDto.class);

    }

}
