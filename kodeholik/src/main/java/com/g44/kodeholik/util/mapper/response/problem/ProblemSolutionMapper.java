package com.g44.kodeholik.util.mapper.response.problem;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.problem.solution.ProblemSolutionDto;
import com.g44.kodeholik.model.entity.problem.ProblemSolution;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProblemSolutionMapper implements Mapper<ProblemSolution, ProblemSolutionDto> {

    private final ModelMapper mapper;

    @Override
    public ProblemSolution mapTo(ProblemSolutionDto b) {
        return mapper.map(b, ProblemSolution.class);
    }

    @Override
    public ProblemSolutionDto mapFrom(ProblemSolution a) {
        return mapper.map(a, ProblemSolutionDto.class);

    }

}
