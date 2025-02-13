package com.g44.kodeholik.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.g44.kodeholik.model.dto.request.problem.ProblemRequestDto;
import com.g44.kodeholik.model.dto.request.problem.add.ProblemAddRequestDto;
import com.g44.kodeholik.model.dto.request.problem.add.ProblemBasicAddDto;
import com.g44.kodeholik.model.dto.request.problem.add.ProblemEditorialDto;
import com.g44.kodeholik.model.dto.request.problem.add.ProblemInputParameterDto;
import com.g44.kodeholik.model.dto.request.problem.add.ProblemTestCaseDto;
import com.g44.kodeholik.model.dto.request.problem.search.ProblemSortField;
import com.g44.kodeholik.model.dto.request.problem.search.SearchProblemRequestDto;
import com.g44.kodeholik.model.dto.response.problem.NoAchivedInformationResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemCompileResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemDescriptionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemResponseDto;
import com.g44.kodeholik.model.elasticsearch.ProblemElasticsearch;
import com.g44.kodeholik.service.problem.ProblemService;
import com.g44.kodeholik.service.problem.ProblemTestCaseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/problem")
public class ProblemController {
    private final ProblemService problemService;

    @GetMapping("/list")
    public ResponseEntity<List<ProblemResponseDto>> getProblems() {
        return ResponseEntity.ok(problemService.getAllProblems());
    }

    @GetMapping("/no-achieved-info")
    public ResponseEntity<List<NoAchivedInformationResponseDto>> getNoAchievedInfo() {
        return ResponseEntity.ok(problemService.getListNoAchievedInformationByCurrentUser());
    }

    @PostMapping("/add-problem")
    public ResponseEntity<?> addProblem(
            @RequestBody ProblemAddRequestDto problemRequestDto) {
        // TODO: process POST request
        problemService.addProblem(problemRequestDto.getProblemBasicAddDto(),
                problemRequestDto.getProblemEditorialDto(),
                problemRequestDto.getProblemInputParameterDto(),
                problemRequestDto.getProblemTestCaseDto());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/edit-problem/{id}")
    public ResponseEntity<?> editProblem(
            @PathVariable Long id,
            @RequestBody ProblemAddRequestDto problemRequestDto) {
        // TODO: process POST request
        problemService.editProblem(id, problemRequestDto.getProblemBasicAddDto(),
                problemRequestDto.getProblemEditorialDto(),
                problemRequestDto.getProblemInputParameterDto(),
                problemRequestDto.getProblemTestCaseDto());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/activate-problem/{id}")
    public ResponseEntity<?> activateProblem(@PathVariable Long id) {
        problemService.activateProblem(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/deactivate-problem/{id}")
    public ResponseEntity<?> deactivateProblem(@PathVariable Long id) {
        problemService.deactivateProblem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProblemElasticsearch>> searchProblem(
            @RequestParam Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) ProblemSortField sortBy,
            @RequestParam(required = false) Boolean ascending,
            @RequestBody SearchProblemRequestDto searchProblemRequestDto) {
        return ResponseEntity.ok(problemService.searchProblems(searchProblemRequestDto, page, size, sortBy, ascending));
    }

    @GetMapping("/suggest")
    public ResponseEntity<List<String>> getProblemSuggestions(@RequestParam String searchText) {
        return ResponseEntity.ok(problemService.getAutocompleteSuggestionsForProblemTitle(searchText));
    }

    @GetMapping("/compile-information/{id}")
    public ResponseEntity<ProblemCompileResponseDto> getProblemCompileInformationById(
            @PathVariable Long id,
            @RequestParam String languageName) {
        return ResponseEntity.ok(problemService.getProblemCompileInformationById(id, languageName));
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
