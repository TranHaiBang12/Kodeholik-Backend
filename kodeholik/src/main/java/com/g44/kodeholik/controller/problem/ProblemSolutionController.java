package com.g44.kodeholik.controller.problem;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.g44.kodeholik.model.dto.request.problem.add.ProblemEditorialDto;
import com.g44.kodeholik.model.dto.request.problem.add.ShareSolutionRequestDto;
import com.g44.kodeholik.model.dto.response.problem.solution.ProblemSolutionDto;
import com.g44.kodeholik.model.dto.response.problem.solution.SolutionListResponseDto;
import com.g44.kodeholik.service.problem.ProblemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/problem-solution")
public class ProblemSolutionController {

    private final ProblemService problemService;

    @PostMapping("/list/{link}")
    public ResponseEntity<Page<SolutionListResponseDto>> getListSolutionBasedOnProblemLink(
            @PathVariable String link,
            @RequestParam int page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String languageName,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) Boolean ascending,
            @RequestBody List<String> skillList,
            Pageable pageable) {
        Page<SolutionListResponseDto> solutionPage = problemService.getProblemListSolution(link, page, size, title,
                languageName,
                skillList,
                sortBy, ascending, pageable);

        if (solutionPage.isEmpty()) {
            return ResponseEntity.noContent().build(); // Trả về 204 nếu danh sách rỗng
        }

        return ResponseEntity.ok(solutionPage);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ProblemSolutionDto> getDetailProblemSolution(@PathVariable Long id) {
        return ResponseEntity.ok(problemService.getProblemSolutionDetail(id));
    }

    @PutMapping("/upvote/{id}")
    public ResponseEntity<Void> upvoteSolution(@PathVariable Long id) {
        problemService.upvoteSolution(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/unupvote/{id}")
    public ResponseEntity<Void> unupvoteSolution(@PathVariable Long id) {
        problemService.unupvoteSolution(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/post-solution")
    public ResponseEntity<ProblemSolutionDto> postSolution(
            @RequestBody @Valid ShareSolutionRequestDto shareSolutionRequestDto) {
        return new ResponseEntity<>(problemService.postSolution(shareSolutionRequestDto), HttpStatus.CREATED);
    }

    @PutMapping("/edit-solution/{id}")
    public ResponseEntity<ProblemSolutionDto> editSolution(@PathVariable Long id,
            @RequestBody @Valid ShareSolutionRequestDto shareSolutionRequestDto) {
        ;
        return new ResponseEntity<>(problemService.editSolution(id, shareSolutionRequestDto), HttpStatus.OK);
    }
}
