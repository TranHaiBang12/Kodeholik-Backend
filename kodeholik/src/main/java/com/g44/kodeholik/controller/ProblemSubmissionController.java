package com.g44.kodeholik.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.g44.kodeholik.model.dto.request.problem.ProblemCompileRequestDto;
import com.g44.kodeholik.model.dto.response.problem.submission.SubmissionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.run.RunProblemResponseDto;
import com.g44.kodeholik.service.problem.ProblemService;
import com.g44.kodeholik.service.problem.ProblemSubmissionService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/problem-submission")
public class ProblemSubmissionController {

    private final ProblemService problemService;

    @PostMapping("/submit/{id}")
    public SubmissionResponseDto submit(@PathVariable Long id,
            @RequestBody ProblemCompileRequestDto problemCompileRequestDto) {
        return problemService.submitProblem(id, problemCompileRequestDto);
    }

    @PostMapping("/run/{id}")
    public RunProblemResponseDto run(@PathVariable Long id,
            @RequestBody ProblemCompileRequestDto problemCompileRequestDto) {
        return problemService.run(id, problemCompileRequestDto);
    }

}
