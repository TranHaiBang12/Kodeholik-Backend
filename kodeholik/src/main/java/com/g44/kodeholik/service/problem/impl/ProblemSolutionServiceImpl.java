package com.g44.kodeholik.service.problem.impl;

import java.util.List;
import java.util.Set;
import java.math.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.response.problem.solution.ProblemSolutionDto;
import com.g44.kodeholik.model.dto.response.problem.solution.SolutionListResponseDto;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemSolution;
import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.repository.problem.ProblemSolutionRepository;
import com.g44.kodeholik.service.problem.ProblemSolutionService;
import com.g44.kodeholik.util.mapper.response.problem.ProblemSolutionMapper;
import com.g44.kodeholik.util.mapper.response.problem.SolutionListResponseMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProblemSolutionServiceImpl implements ProblemSolutionService {

    private final ProblemSolutionRepository problemSolutionRepository;

    private final ProblemSolutionMapper problemSolutionMapper;

    private final SolutionListResponseMapper solutionListResponseMapper;

    @Override
    public ProblemSolution save(ProblemSolution problemSolution) {
        return problemSolutionRepository.save(problemSolution);
    }

    @Override
    public List<ProblemSolution> saveAll(List<ProblemSolution> problemSolutions) {
        return problemSolutionRepository.saveAll(problemSolutions);
    }

    @Override
    public List<ProblemSolution> findEditorialByProblem(Problem problem) {
        return problemSolutionRepository.findByProblemAndIsProblemImplementation(problem, true);
    }

    @Transactional
    @Override
    public void deleteEditorialByProblem(Problem problem) {
        problemSolutionRepository.deleteAllByProblemAndIsProblemImplementation(problem, true);
    }

    @Override
    public Page<ProblemSolutionDto> findListSolutionByProblem(Problem problem, Pageable pageable) {
        Page<ProblemSolution> solution = problemSolutionRepository.findByProblem(problem, pageable);
        return solution.map(problemSolutionMapper::mapFrom);
    }

    @Override
    public ProblemSolutionDto findSolutionDtoById(Long id) {
        ProblemSolution solution = problemSolutionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Solution not found", "Solution not found"));
        return problemSolutionMapper.mapFrom(solution);
    }

    @Override
    public ProblemSolution findSolutionById(Long id) {
        ProblemSolution solution = problemSolutionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Solution not found", "Solution not found"));
        return solution;
    }

    @Override
    public Page<SolutionListResponseDto> findOtherSolutionByProblem(Problem problem, int page, Integer size,
            String title,
            Language language,
            String sortBy, Boolean ascending, Pageable pageable) {
        int sizeN = 5;
        if (size != null) {
            sizeN = size;
        }
        pageable = PageRequest.of(page, sizeN);
        if (sortBy != null) {
            Sort sort;
            switch (sortBy) {
                case "DATE":
                    sort = ascending != null && ascending.booleanValue() ? Sort.by("createdAt").ascending()
                            : Sort.by("createdAt").descending();
                    pageable = PageRequest.of(page, sizeN, sort);
                    break;
                case "VOTE":
                    sort = ascending != null && ascending.booleanValue() ? Sort.by("noUpvote").ascending()
                            : Sort.by("noUpvote").descending();
                    pageable = PageRequest.of(page, sizeN, sort);
                    break;
                case "COMMENT":
                    sort = ascending != null && ascending.booleanValue() ? Sort.by("noComment").ascending()
                            : Sort.by("noComment").descending();
                    pageable = PageRequest.of(page, sizeN, sort);
                    break;
                default:
                    throw new BadRequestException("Sort field is wrong", "Sort field is wrong");
            }
        }
        Page<ProblemSolution> solution = problemSolutionRepository
                .findByProblemAndIsProblemImplementationAndTitleContain(
                        problem,
                        title,
                        language,
                        false,
                        pageable);
        List<ProblemSolution> solutionList = solution.getContent();
        Page<SolutionListResponseDto> solutionPage = solution.map(solutionListResponseMapper::mapFrom);
        int index = 0;
        for (SolutionListResponseDto solutionListResponseDto : solutionPage) {
            solutionListResponseDto.setNoComment(solutionList.get(index).getComments().size());
            solutionListResponseDto.setNoUpvote(solutionList.get(index).getNoUpvote());
        }
        return solutionPage;
    }

    @Override
    public void upvoteSolution(Long solutionId, Users user) {
        ProblemSolution problemSolution = findSolutionById(solutionId);
        Set<Users> usersVote = problemSolution.getUserVote();
        for (Users userVote : usersVote) {
            if (userVote.getEmail().equals(user.getEmail())) {
                throw new BadRequestException("You have already voted this solution",
                        "You have already voted this solution");
            }
        }
        usersVote.add(user);
        problemSolution.setNoUpvote(problemSolution.getNoUpvote() + 1);
        problemSolutionRepository.save(problemSolution);
    }

}
