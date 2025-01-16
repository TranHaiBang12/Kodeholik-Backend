package com.g44.kodeholik.util.mapper.request.problem;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.request.problem.ProblemRequestDto;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProblemRequestMapper implements Mapper<Problem, ProblemRequestDto> {

    private final ModelMapper modelMapper;

    @Override
    public Problem mapTo(ProblemRequestDto b) {
        return modelMapper.map(b, Problem.class);
    }

    @Override
    public ProblemRequestDto mapFrom(Problem a) {
        return modelMapper.map(a, ProblemRequestDto.class);
    }

}
