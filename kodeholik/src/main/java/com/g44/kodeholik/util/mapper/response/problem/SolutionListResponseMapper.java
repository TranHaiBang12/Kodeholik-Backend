package com.g44.kodeholik.util.mapper.response.problem;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.problem.solution.SolutionListResponseDto;
import com.g44.kodeholik.model.entity.problem.ProblemSolution;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SolutionListResponseMapper implements Mapper<ProblemSolution, SolutionListResponseDto> {

    private final ModelMapper mapper;

    @Override
    public ProblemSolution mapTo(SolutionListResponseDto b) {
        return mapper.map(b, ProblemSolution.class);
    }

    @Override
    public SolutionListResponseDto mapFrom(ProblemSolution a) {
        return mapper.map(a, SolutionListResponseDto.class);
    }

}
