package com.g44.kodeholik.util.mapper.response.problem;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.request.lambda.InputVariable;
import com.g44.kodeholik.model.dto.response.problem.ProblemTestCaseResponseDto;
import com.g44.kodeholik.model.entity.problem.ProblemTestCase;
import com.g44.kodeholik.util.mapper.Mapper;
import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProblemTestCaseResponseMapper implements Mapper<ProblemTestCase, ProblemTestCaseResponseDto> {
    private final ModelMapper modelMapper;
    private Gson gson = new Gson();

    @Override
    public ProblemTestCase mapTo(ProblemTestCaseResponseDto b) {
        // TODO Auto-generated method stub
        return modelMapper.map(b, ProblemTestCase.class);
    }

    @Override
    public ProblemTestCaseResponseDto mapFrom(ProblemTestCase a) {
        // TODO Auto-generated method stub

        ProblemTestCaseResponseDto problemTestCaseResponseDto = modelMapper.map(a, ProblemTestCaseResponseDto.class);
        List<InputVariable> input = gson.fromJson(a.getInput(),
                new com.google.gson.reflect.TypeToken<List<InputVariable>>() {
                }.getType());
        problemTestCaseResponseDto.setInput(input);
        return problemTestCaseResponseDto;
    }

}
