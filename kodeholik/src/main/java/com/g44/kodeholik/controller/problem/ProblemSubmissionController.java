package com.g44.kodeholik.controller.problem;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.g44.kodeholik.model.dto.request.problem.ProblemCompileRequestDto;
import com.g44.kodeholik.model.dto.response.problem.submission.SubmissionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.run.RunProblemResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.submit.SubmissionListResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.submit.SuccessSubmissionListResponseDto;
import com.g44.kodeholik.service.problem.ProblemService;
import com.g44.kodeholik.service.problem.ProblemSubmissionService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/problem-submission")
public class ProblemSubmissionController {

    private final ProblemService problemService;

    @PostMapping("/submit/{link}")
    public SubmissionResponseDto submit(@PathVariable String link,
            @RequestBody ProblemCompileRequestDto problemCompileRequestDto) {
        return problemService.submitProblem(link, problemCompileRequestDto);
    }

    @PostMapping("/run/{link}")
    public RunProblemResponseDto run(@PathVariable String link,
            @RequestBody ProblemCompileRequestDto problemCompileRequestDto) {
        return problemService.run(link, problemCompileRequestDto);
    }

    @GetMapping("/list/{link}")
    public ResponseEntity<List<SubmissionListResponseDto>> getSubmissionListByProblemAndUser(
            @PathVariable String link) {
        List<SubmissionListResponseDto> submissionList = problemService.getSubmissionListByUserAndProblem(link);
        if (submissionList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(submissionList);
    }

    @PostMapping("/success-list/{link}")
    public ResponseEntity<List<SuccessSubmissionListResponseDto>> getSuccessSubmissionList(
            @RequestBody List<Long> excludes,
            @PathVariable String link) {
        List<SuccessSubmissionListResponseDto> successSubmissionList = problemService.getSuccessSubmissionList(link,
                excludes);
        return ResponseEntity.ok(successSubmissionList);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<SubmissionResponseDto> getSubmissionDetail(@PathVariable Long id) {
        SubmissionResponseDto submissionDetail = problemService.getSubmissionDetail(id);
        return ResponseEntity.ok(submissionDetail);
    }

}
