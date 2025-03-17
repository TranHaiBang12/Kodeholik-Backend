package com.g44.kodeholik.util.mapper.response.problem;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.request.problem.add.SolutionCodeDto;
import com.g44.kodeholik.model.dto.response.problem.solution.ProblemSolutionDto;
import com.g44.kodeholik.model.entity.problem.ProblemSolution;
import com.g44.kodeholik.model.entity.problem.SolutionCode;
import com.g44.kodeholik.util.mapper.Mapper;
import com.g44.kodeholik.util.mapper.request.exam.AddExamRequestMapper;
import com.g44.kodeholik.util.mapper.request.user.AddUserAvatarFileMapper;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProblemSolutionMapper implements Mapper<ProblemSolution, ProblemSolutionDto> {

    private final AddUserAvatarFileMapper addUserAvatarFileMapper;

    private final AddExamRequestMapper addExamRequestMapper;

    private final ModelMapper mapper;

    @Override
    public ProblemSolution mapTo(ProblemSolutionDto b) {
        return mapper.map(b, ProblemSolution.class);
    }

    @Override
    public ProblemSolutionDto mapFrom(ProblemSolution a) {
        ProblemSolutionDto problemSolutionDto = mapper.map(a, ProblemSolutionDto.class);
        for (SolutionCode solutionCode : a.getSolutionCodes()) {
            for (SolutionCodeDto solutionCodeDto : problemSolutionDto.getSolutionCodes()) {
                if (solutionCode.getId().equals(solutionCodeDto.getId())) {
                    solutionCodeDto.setSubmissionId(solutionCode.getProblemSubmission().getId().longValue());
                }
            }
        }
        return problemSolutionDto;
    }

}
