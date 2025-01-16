package com.g44.kodeholik.service.problem.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.problem.ProblemRequestDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemResponseDto;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.repository.problem.ProblemRepository;
import com.g44.kodeholik.repository.user.UserRepository;
import com.g44.kodeholik.service.problem.ProblemService;
import com.g44.kodeholik.util.mapper.request.problem.ProblemRequestMapper;
import com.g44.kodeholik.util.mapper.response.problem.ProblemResponseMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProblemServiceImpl implements ProblemService {

    private final ProblemRequestMapper problemRequestMapper;

    private final ModelMapper modelMapper;

    private final ProblemResponseMapper problemResponseMapper;

    private final ProblemRepository problemRepository;

    private final UserRepository userRepository;

    @Override
    public List<ProblemResponseDto> getAllProblems() {
        List<Problem> problems = problemRepository.findAll();
        for (int i = 0; i < problems.size(); i++) {
            log.info(problems.get(i).getTitle());
        }
        return problems.stream()
                .map(problemResponseMapper::mapFrom)
                .collect(Collectors.toList());
    }

    @Override
    public ProblemResponseDto getProblemById(Long id) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Problem not found", "Problem not found"));
        return problemResponseMapper.mapFrom(problem);
    }

    @Override
    public ProblemResponseDto createProblem(ProblemRequestDto problemRequest) {
        Problem problem = problemRequestMapper.mapTo(problemRequest);
        problem.setAcceptanceRate(0);
        problem.setNoSubmission(0);
        problem.setCreatedAt(Timestamp.from(Instant.now()));
        problem.setCreatedBy(userRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("User not found", "User not found")));
        problem = problemRepository.save(problem);
        return problemResponseMapper.mapFrom(problem);
    }

    @Override
    public ProblemResponseDto updateProblem(Long id, ProblemRequestDto problemRequest) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Problem not found", "Problem not found"));
        modelMapper.map(problemRequest, problem);
        problem.setUpdatedAt(Timestamp.from(Instant.now()));
        problem.setUpdatedBy(userRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("User not found", "User not found")));
        problem = problemRepository.save(problem);
        return problemResponseMapper.mapFrom(problem);
    }

    @Override
    public void deleteProblem(Long id) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Problem not found", "Problem not found"));
        problemRepository.delete(problem);
    }

}
