package com.g44.kodeholik.util.mapper.response.problem;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.problem.ListProblemAdminDto;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ListProblemAdminResponseMapper implements Mapper<Problem, ListProblemAdminDto> {

    private final ModelMapper mapper;

    @Override
    public Problem mapTo(ListProblemAdminDto b) {
        return mapper.map(b, Problem.class);
    }

    @Override
    public ListProblemAdminDto mapFrom(Problem a) {
        return mapper.map(a, ListProblemAdminDto.class);
    }

}
