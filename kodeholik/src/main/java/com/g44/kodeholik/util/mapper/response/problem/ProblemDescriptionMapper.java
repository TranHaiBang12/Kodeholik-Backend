package com.g44.kodeholik.util.mapper.response.problem;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.problem.ProblemDescriptionResponseDto;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProblemDescriptionMapper implements Mapper<Problem, ProblemDescriptionResponseDto> {

    private final ModelMapper modelMapper;

    @Override
    public Problem mapTo(ProblemDescriptionResponseDto b) {
        // TODO Auto-generated method stub
        return modelMapper.map(b, Problem.class);
    }

    @Override
    public ProblemDescriptionResponseDto mapFrom(Problem a) {
        // TODO Auto-generated method stub
        return modelMapper.map(a, ProblemDescriptionResponseDto.class);
    }

}
