package com.g44.kodeholik.controller.examiner;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.g44.kodeholik.model.dto.request.exam.AddExamRequestDto;
import com.g44.kodeholik.model.dto.request.exam.EditExamBasicRequestDto;
import com.g44.kodeholik.model.dto.request.exam.EditExamProblemRequestDto;
import com.g44.kodeholik.model.dto.request.exam.FilterExamRequestDto;
import com.g44.kodeholik.model.dto.response.exam.examiner.ExamListResponseDto;
import com.g44.kodeholik.model.dto.response.exam.examiner.ExamResponseDto;
import com.g44.kodeholik.model.dto.response.exam.student.ExamResultOverviewResponseDto;
import com.g44.kodeholik.service.exam.ExamService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/examiner")
public class ExaminerController {

    private final ExamService examService;

    @PostMapping("/create")
    public ResponseEntity<ExamResponseDto> createExam(@RequestBody @Valid AddExamRequestDto addExamRequestDto) {
        return new ResponseEntity<>(examService.createExam(addExamRequestDto), HttpStatus.CREATED);
    }

    @PutMapping("/edit/{code}")
    public ResponseEntity<ExamResponseDto> editExam(@RequestBody @Valid AddExamRequestDto addExamRequestDto,
            @PathVariable String code) {
        return new ResponseEntity<>(examService.editExam(addExamRequestDto, code), HttpStatus.OK);
    }

    @PutMapping("/edit-basic/{code}")
    public ResponseEntity<ExamResponseDto> editExamBasic(
            @RequestBody @Valid EditExamBasicRequestDto editExamBasicRequestDto,
            @PathVariable String code) {
        return new ResponseEntity<>(examService.editExamBasic(editExamBasicRequestDto, code), HttpStatus.OK);
    }

    @PutMapping("/edit-problem/{code}")
    public ResponseEntity<ExamResponseDto> editExamProblem(
            @RequestBody @Valid EditExamProblemRequestDto editExamProblemRequestDto,
            @PathVariable String code) {
        return new ResponseEntity<>(examService.editExamProblem(editExamProblemRequestDto, code), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{code}")
    public ResponseEntity<Void> deleteExam(@PathVariable String code) {
        examService.deleteExam(code);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/list")
    public ResponseEntity<Page<ExamListResponseDto>> getListExam(
            @RequestBody @Valid FilterExamRequestDto filterExamRequestDto) {
        Page<ExamListResponseDto> ePage = examService.getListOfExam(filterExamRequestDto);
        if (ePage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ePage);
    }

    @GetMapping("/detail/{code}")
    public ResponseEntity<ExamResponseDto> getExamDetail(@PathVariable String code) {
        return ResponseEntity.ok(examService.getExamDetailByCode(code));
    }

    @GetMapping("/list-participant/{code}")
    public ResponseEntity<List<Map<String, String>>> getListParticipant(@PathVariable String code) {
        List<Map<String, String>> results = examService.getAllParticipantsInExam(code);
        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(results);
    }

    @GetMapping("/result/{code}")
    public ResponseEntity<ExamResultOverviewResponseDto> viewResultOfUser(@PathVariable String code,
            @RequestParam Long userId) {
        return ResponseEntity.ok(examService.viewResultOfASpecificParticpant(code, userId));
    }

}
