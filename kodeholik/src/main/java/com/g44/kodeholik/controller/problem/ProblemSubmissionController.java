package com.g44.kodeholik.controller.problem;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.g44.kodeholik.model.dto.request.problem.ProblemCompileRequestDto;
import com.g44.kodeholik.model.dto.request.problem.search.FilterProgress;
import com.g44.kodeholik.model.dto.request.problem.search.FilterSubmission;
import com.g44.kodeholik.model.dto.response.problem.submission.SubmissionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.run.RunProblemResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.submit.SubmissionListResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.submit.SuccessSubmissionListResponseDto;
import com.g44.kodeholik.model.dto.response.user.ProblemProgressResponseDto;
import com.g44.kodeholik.model.enums.setting.Level;
import com.g44.kodeholik.service.problem.ProblemService;
import com.g44.kodeholik.service.problem.ProblemSubmissionService;
import com.g44.kodeholik.service.problem.impl.ProblemServiceImpl;
import com.g44.kodeholik.util.mapper.request.exam.AddExamRequestMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @PostMapping("/my-submission")
    public ResponseEntity<Page<SubmissionListResponseDto>> getMySubmission(
            @RequestBody FilterSubmission filterSubmission) {
        Page<SubmissionListResponseDto> mySubmission = problemService.getListSubmission(
                filterSubmission.getLink(),
                filterSubmission.getStatus(),
                filterSubmission.getStart(),
                filterSubmission.getEnd(),
                filterSubmission.getPage(),
                filterSubmission.getSize(),
                filterSubmission.getSortBy(),
                filterSubmission.getAscending());
        if (mySubmission.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(mySubmission);
    }

    @GetMapping("/problem-submitted")
    public ResponseEntity<Map<String, String>> getProblemSubmitted() {
        Map<String, String> map = problemService.getAllProblemHasSubmitted();
        if (map.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(map);
    }

    @PostMapping("/my-progress")
    public ResponseEntity<Page<ProblemProgressResponseDto>> getMyProgress(
            @RequestBody FilterProgress filterProgress) {
        Page<ProblemProgressResponseDto> myProgress = problemService.findLastSubmittedByUser(filterProgress.getPage(),
                filterProgress.getStatus(), filterProgress.getSize(), filterProgress.getSortBy(),
                filterProgress.getAscending());
        if (myProgress.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(myProgress);
    }

    @GetMapping("/number-skill")
    public ResponseEntity<List<Map<String, String>>> getNumberSkillUserSolved(
            @RequestParam Level level) {
        List<Map<String, String>> results = problemService.getNumberSkillUserSolved(level);
        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(results);
    }

    @GetMapping("/number-topic")
    public ResponseEntity<List<Map<String, String>>> getNumberTopicUserSolved() {
        List<Map<String, String>> results = problemService.getNumberTopicUserSolved();
        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(results);
    }

    @GetMapping("/number-language")
    public ResponseEntity<List<Map<String, String>>> getNumberLanguageUserSolved() {
        List<Map<String, String>> results = problemService.getNumberLanguageUserSolved();
        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(results);
    }

    @GetMapping("/acceptance-rate")
    public ResponseEntity<Map<String, String>> getAcceptanceRate() {
        return ResponseEntity.ok(problemService.getAcceptanceRateAndNoSubmissionByUser());
    }

}
