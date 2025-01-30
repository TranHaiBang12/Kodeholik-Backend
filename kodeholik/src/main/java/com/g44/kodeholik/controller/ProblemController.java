package com.g44.kodeholik.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.g44.kodeholik.model.dto.request.problem.ProblemRequestDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemTestCaseResponseDto;
import com.g44.kodeholik.service.problem.ProblemService;
import com.g44.kodeholik.service.problem.ProblemTestCaseService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/problem")
public class ProblemController {
    private final ProblemService problemService;
    private final ProblemTestCaseService problemTestCaseService;

    @GetMapping("/list")
    public ResponseEntity<List<ProblemResponseDto>> getProblems() {
        return ResponseEntity.ok(problemService.getAllProblems());
    }

    @GetMapping("/sample-test-case/{id}")
    public ResponseEntity<List<ProblemTestCaseResponseDto>> getProblemSampleTestCaseById(@PathVariable Long id) {
        return ResponseEntity.ok(problemTestCaseService.getProblemSampleTestCaseById(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProblemResponseDto> getProblemById(@PathVariable Long id) {
        return ResponseEntity.ok(problemService.getProblemById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<ProblemResponseDto> createProblem(@RequestBody ProblemRequestDto problemRequest) {
        return new ResponseEntity<>(problemService.createProblem(problemRequest), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProblemResponseDto> updateProblem(@RequestBody ProblemRequestDto problemRequest,
            @PathVariable Long id) {
        return new ResponseEntity<>(problemService.updateProblem(id, problemRequest), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProblem(@PathVariable Long id) {
        problemService.deleteProblem(id);
        return ResponseEntity.noContent().build();
    }
}
