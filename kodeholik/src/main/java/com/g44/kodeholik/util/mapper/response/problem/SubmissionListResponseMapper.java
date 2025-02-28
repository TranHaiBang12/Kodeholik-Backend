package com.g44.kodeholik.util.mapper.response.problem;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.problem.submission.submit.SubmissionListResponseDto;
import com.g44.kodeholik.model.entity.problem.ProblemSubmission;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SubmissionListResponseMapper implements Mapper<ProblemSubmission, SubmissionListResponseDto> {

    private final ModelMapper mapper;

    @Override
    public ProblemSubmission mapTo(SubmissionListResponseDto b) {
        return mapper.map(b, ProblemSubmission.class);
    }

    @Override
    public SubmissionListResponseDto mapFrom(ProblemSubmission a) {
        SubmissionListResponseDto submissionListResponseDto = mapper.map(a, SubmissionListResponseDto.class);
        submissionListResponseDto.setLanguageName(a.getLanguage().getName());
        submissionListResponseDto.setProblemTitle(a.getProblem().getTitle());
        submissionListResponseDto.setProblemLink(a.getProblem().getLink());
        return submissionListResponseDto;
    }

}
