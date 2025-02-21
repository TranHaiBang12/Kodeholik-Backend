package com.g44.kodeholik.util.mapper.response.problem;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.problem.ProblemBasicResponseDto;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProblemBasicResponseMapper implements Mapper<Problem, ProblemBasicResponseDto> {

    private final ModelMapper modelMapper;

    @Override
    public Problem mapTo(ProblemBasicResponseDto b) {
        return modelMapper.map(b, Problem.class);
    }

    @Override
    public ProblemBasicResponseDto mapFrom(Problem a) {
        return modelMapper.map(a, ProblemBasicResponseDto.class);

    }

}
