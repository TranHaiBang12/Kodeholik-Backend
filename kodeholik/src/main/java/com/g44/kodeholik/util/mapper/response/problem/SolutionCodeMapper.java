package com.g44.kodeholik.util.mapper.response.problem;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.request.problem.add.SolutionCodeDto;
import com.g44.kodeholik.model.entity.problem.SolutionCode;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SolutionCodeMapper implements Mapper<SolutionCode, SolutionCodeDto> {

    private final ModelMapper mapper;

    @Override
    public SolutionCode mapTo(SolutionCodeDto b) {
        return mapper.map(b, SolutionCode.class);
    }

    @Override
    public SolutionCodeDto mapFrom(SolutionCode a) {
        SolutionCodeDto result = new SolutionCodeDto();
        result.setSolutionCode(a.getCode());
        result.setSolutionLanguage(a.getLanguage().getName());
        if (a.getProblemSubmission() != null)
            result.setSubmissionId(a.getProblemSubmission().getId());
        return result;

    }

}
