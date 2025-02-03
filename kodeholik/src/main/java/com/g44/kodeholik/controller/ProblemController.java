package com.g44.kodeholik.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.g44.kodeholik.model.dto.request.problem.ProblemRequestDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemCompileResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemDescriptionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemResponseDto;
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

    @GetMapping("/compile-information/{id}")
    public ResponseEntity<ProblemCompileResponseDto> getProblemCompileInformationById(
            @PathVariable Long id,
            @RequestParam String languageName) {
        return ResponseEntity.ok(problemTestCaseService.getProblemCompileInformationById(id, languageName));
    }

    @GetMapping("/description/{id}")
    public ResponseEntity<ProblemDescriptionResponseDto> getProblemDescriptionById(@PathVariable Long id) {
        return ResponseEntity.ok(problemService.getProblemDescriptionById(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProblemResponseDto> getProblemResponseDtoById(@PathVariable Long id) {
        return ResponseEntity.ok(problemService.getProblemResponseDtoById(id));
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
