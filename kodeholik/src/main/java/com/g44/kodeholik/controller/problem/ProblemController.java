package com.g44.kodeholik.controller.problem;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.g44.kodeholik.model.dto.request.problem.ProblemRequestDto;
import com.g44.kodeholik.model.dto.request.problem.add.ProblemAddRequestDto;
import com.g44.kodeholik.model.dto.request.problem.add.ProblemBasicAddDto;
import com.g44.kodeholik.model.dto.request.problem.add.ProblemEditorialDto;
import com.g44.kodeholik.model.dto.request.problem.add.ProblemInputParameterDto;
import com.g44.kodeholik.model.dto.request.problem.search.ProblemSortField;
import com.g44.kodeholik.model.dto.request.problem.search.SearchProblemRequestDto;
import com.g44.kodeholik.model.dto.response.problem.NoAchivedInformationResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemBasicResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemCompileResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemDescriptionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemEditorialResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemInputParameterResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemResponseDto;
import com.g44.kodeholik.model.elasticsearch.ProblemElasticsearch;
import com.g44.kodeholik.service.problem.ProblemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
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
            @RequestPart(name = "problemBasicAddDto") @Valid ProblemBasicAddDto problemBasicAddDto,
            @RequestPart(name = "problemEditorialDto") @Valid ProblemEditorialDto problemEditorialDto,
            @RequestPart(name = "problemInputParameterDto") @Valid List<ProblemInputParameterDto> problemInputParameterDto,
            @RequestPart(name = "javaTestCaseFile") MultipartFile javaTestCaseFile,
            @RequestPart(name = "cTestCaseFile") MultipartFile cTestCaseFile) {
        problemService.addProblem(problemBasicAddDto,
                problemEditorialDto,
                problemInputParameterDto,
                javaTestCaseFile);

        // problemService.addProblem(problemRequestDto.getProblemBasicAddDto(),
        // problemRequestDto.getProblemEditorialDto(),
        // problemRequestDto.getProblemInputParameterDto(),
        // problemRequestDto.getProblemTestCaseDto());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/edit-problem/{link}")
    public ResponseEntity<?> editProblem(
            @PathVariable String link,
            @RequestPart("problemBasicAddDto") @Valid ProblemBasicAddDto problemBasicAddDto,
            @RequestPart("problemEditorialDto") @Valid ProblemEditorialDto problemEditorialDto,
            @RequestPart("problemInputParameterDto") @Valid List<ProblemInputParameterDto> problemInputParameterDto,
            @RequestPart("testcaseFile") MultipartFile testcaseFile) {
        // TODO: process POST request
        problemService.editProblem(link, problemBasicAddDto,
                problemEditorialDto,
                problemInputParameterDto,
                testcaseFile);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/basic-for-emp/{link}")
    public ResponseEntity<ProblemBasicResponseDto> getProblemBasicForEmployee(@PathVariable String link) {
        return ResponseEntity.ok(problemService.getProblemBasicResponseDto(link));
    }

    @GetMapping("/editorial-for-emp/{link}")
    public ResponseEntity<ProblemEditorialResponseDto> getProblemEditorialForEmployee(@PathVariable String link) {
        return ResponseEntity.ok(problemService.getProblemEditorialDtoList(link));
    }

    @GetMapping("/editorial/{link}")
    public ResponseEntity<ProblemEditorialResponseDto> getProblemEditorial(@PathVariable String link) {
        return ResponseEntity.ok(problemService.getProblemEditorialDtoList(link));
    }

    @GetMapping("/template-for-emp/{link}")
    public ResponseEntity<ProblemInputParameterResponseDto> getProblemTemplateForEmployee(@PathVariable String link) {
        return ResponseEntity.ok(problemService.getProblemInputParameterDtoList(link));
    }

    @GetMapping("/download-testcase/{link}")
    public ResponseEntity<byte[]> downloadProblemTestcase(@PathVariable String link) {
        byte[] excelFile = problemService.getExcelFile(link);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=data.xlsx");
        headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        return new ResponseEntity<>(excelFile, headers, HttpStatus.OK);
    }

    @PatchMapping("/activate-problem/{link}")
    public ResponseEntity<?> activateProblem(@PathVariable String link) {
        problemService.activateProblem(link);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/deactivate-problem/{link}")
    public ResponseEntity<?> deactivateProblem(@PathVariable String link) {
        problemService.deactivateProblem(link);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/search")
    public ResponseEntity<Page<ProblemElasticsearch>> searchProblem(
            @RequestParam Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) ProblemSortField sortBy,
            @RequestParam(required = false) Boolean ascending,
            @RequestBody SearchProblemRequestDto searchProblemRequestDto) {
        Page<ProblemElasticsearch> problemElasticsearch = problemService.searchProblems(searchProblemRequestDto, page,
                size, sortBy, ascending);
        if (problemElasticsearch.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(problemService.searchProblems(searchProblemRequestDto, page, size, sortBy, ascending));
    }

    @GetMapping("/suggest")
    public ResponseEntity<List<String>> getProblemSuggestions(@RequestParam String searchText) {
        return ResponseEntity.ok(problemService.getAutocompleteSuggestionsForProblemTitle(searchText));
    }

    @GetMapping("/compile-information/{link}")
    public ResponseEntity<ProblemCompileResponseDto> getProblemCompileInformationById(
            @PathVariable String link,
            @RequestParam String languageName) {
        return ResponseEntity.ok(problemService.getProblemCompileInformationById(link, languageName));
    }

    @GetMapping("/description/{link}")
    public ResponseEntity<ProblemDescriptionResponseDto> getProblemDescriptionById(@PathVariable String link) {
        return ResponseEntity.ok(problemService.getProblemDescriptionById(link));
    }

    @GetMapping("/{link}")
    public ResponseEntity<ProblemResponseDto> getProblemResponseDtoById(@PathVariable String link) {
        return ResponseEntity.ok(problemService.getProblemResponseDtoById(link));
    }

    @PostMapping("/create")
    public ResponseEntity<ProblemResponseDto> createProblem(@RequestBody ProblemRequestDto problemRequest) {
        return new ResponseEntity<>(problemService.createProblem(problemRequest), HttpStatus.CREATED);
    }

    @PutMapping("/update/{link}")
    public ResponseEntity<ProblemResponseDto> updateProblem(@RequestBody ProblemRequestDto problemRequest,
            @PathVariable String link) {
        return new ResponseEntity<>(problemService.updateProblem(link, problemRequest), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{link}")
    public ResponseEntity<Void> deleteProblem(@PathVariable String link) {
        problemService.deleteProblem(link);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/tag-favourite/{link}")

    public ResponseEntity<Void> tagFavouriteProblem(@PathVariable String link) {
        problemService.tagFavouriteProblem(link);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/untag-favourite/{link}")
    public ResponseEntity<Void> untagFavouriteProblem(@PathVariable String link) {
        problemService.untagFavouriteProblem(link);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list-favourite")
    public ResponseEntity<Page<ProblemResponseDto>> listFavouriteProblems(
            @RequestParam Integer page,
            @RequestParam(required = false) Integer size) {
        Page<ProblemResponseDto> problemResponsePage = problemService.findAllProblemUserFavourite(page, size);
        if (problemResponsePage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(problemResponsePage);
    }
}
