package com.g44.kodeholik.controller.exam;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.g44.kodeholik.service.exam.ExamService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/exam")
public class ExamController {

    private final ExamService examService;

    @PostMapping("/enroll")
    public ResponseEntity<Void> enrollExam(@RequestParam String code) {
        examService.enrollExam(code);
        return ResponseEntity.noContent().build();
    }

}
