package com.g44.kodeholik.util.mapper.response.problem;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.problem.solution.SolutionListResponseDto;
import com.g44.kodeholik.model.dto.response.user.EmployeeResponseDto;
import com.g44.kodeholik.model.dto.response.user.UserResponseDto;
import com.g44.kodeholik.model.entity.problem.ProblemSolution;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.service.aws.s3.S3Service;
import com.g44.kodeholik.util.mapper.Mapper;
import com.g44.kodeholik.util.mapper.request.exam.AddExamRequestMapper;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SolutionListResponseMapper implements Mapper<ProblemSolution, SolutionListResponseDto> {

    private final ModelMapper mapper;

    private final S3Service s3Service;

    public Users currentUser;

    @Override
    public ProblemSolution mapTo(SolutionListResponseDto b) {
        return mapper.map(b, ProblemSolution.class);
    }

    @Override
    public SolutionListResponseDto mapFrom(ProblemSolution a) {
        SolutionListResponseDto solutionListResponseDto = mapper.map(a, SolutionListResponseDto.class);
        updateAvatar(solutionListResponseDto.getCreatedBy());
        if (a.getUserVote().contains(currentUser)) {
            solutionListResponseDto.setCurrentUserVoted(true);
        } else {
            solutionListResponseDto.setCurrentUserVoted(false);
        }
        solutionListResponseDto.setNoUpvote(a.getNoUpvote());
        return solutionListResponseDto;
    }

    private void updateAvatar(UserResponseDto user) {
        if (user != null && user.getAvatar() != null && user.getAvatar().startsWith("kodeholik")) {
            user.setAvatar(s3Service.getPresignedUrl(user.getAvatar()));
        }
    }

}
