package com.g44.kodeholik.util.mapper.response.problem;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.problem.ProblemResponseDto;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProblemResponseMapper implements Mapper<Problem, ProblemResponseDto> {
    private final ModelMapper modelMapper;

    @Override
    public Problem mapTo(ProblemResponseDto b) {
        return modelMapper.map(b, Problem.class);
    }

    @Override
    public ProblemResponseDto mapFrom(Problem a) {
        return modelMapper.map(a, ProblemResponseDto.class);
    }

}
