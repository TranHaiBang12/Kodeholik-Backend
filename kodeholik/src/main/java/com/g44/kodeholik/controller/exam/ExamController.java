package com.g44.kodeholik.controller.exam;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.g44.kodeholik.config.WebsocketSessionManager;
import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.model.dto.request.exam.SubmitExamRequestDto;
import com.g44.kodeholik.model.dto.request.problem.ProblemCompileRequestDto;
import com.g44.kodeholik.model.dto.response.exam.student.ExamCompileInformationResponseDto;
import com.g44.kodeholik.model.dto.response.exam.student.ExamListStudentResponseDto;
import com.g44.kodeholik.model.dto.response.exam.student.ExamProblemDetailResponseDto;
import com.g44.kodeholik.model.dto.response.exam.student.ExamResultOverviewResponseDto;
import com.g44.kodeholik.model.dto.response.exam.student.NotStartedExamListDto;
import com.g44.kodeholik.model.dto.response.problem.submission.run.RunProblemResponseDto;
import com.g44.kodeholik.model.enums.exam.ExamStatus;
import com.g44.kodeholik.service.exam.ExamService;
import com.g44.kodeholik.service.problem.ProblemService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/exam")
public class ExamController {

    private final ExamService examService;

    @PostMapping("/enroll/{code}")
    public ResponseEntity<Void> enrollExam(@PathVariable String code) {
        examService.enrollExam(code);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/unenroll/{code}")
    public ResponseEntity<Void> unenrollExam(@PathVariable String code) {
        examService.unenrollExam(code);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/detail/{code}")
    public ResponseEntity<List<ExamProblemDetailResponseDto>> getExamDetail(@PathVariable String code) {
        List<ExamProblemDetailResponseDto> result = examService.getProblemDetailInExam(code);
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }

    @MessageMapping("/exam/submit/{code}")
    @SendTo("/topic/exam/{code}")
    public ResponseEntity<Double> getExamResult(
            @DestinationVariable String code,
            @Payload List<SubmitExamRequestDto> examProblemDetailResponseDtos,
            StompHeaderAccessor accessor) {
        String username = (String) accessor.getSessionAttributes().get("username");

        double grade = examService.submitExam(examProblemDetailResponseDtos,
                code,
                username);
        return ResponseEntity.ok(grade);
    }

    @GetMapping("/get-token/{code}")
    public ResponseEntity<Object> getTokenForExam(@PathVariable String code) {
        return ResponseEntity.ok(examService.generateTokenForExam(code));
    }

    @MessageExceptionHandler(BadRequestException.class)
    @SendTo("/queue/errors")
    public String handleBadRequestException(BadRequestException ex, StompHeaderAccessor accessor) {
        String username = (String) accessor.getSessionAttributes().get("username");
        return "400 Bad Request: " + ex.getMessage();
    }

    @PostMapping("/run/{code}")
    public RunProblemResponseDto run(@PathVariable String code,
            @RequestParam String link,
            @RequestBody ProblemCompileRequestDto problemCompileRequestDto) {
        return examService.runExam(code, link, problemCompileRequestDto);
    }

    @GetMapping("/result/{code}")
    public ExamResultOverviewResponseDto viewResult(@PathVariable String code) {
        return examService.viewResult(code);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<ExamListStudentResponseDto>> getListExam(
            @RequestParam ExamStatus status,
            @RequestParam int page,
            @RequestParam(required = false) Integer size) {
        Page<ExamListStudentResponseDto> ePage = examService.getListExam(status, page, size);
        if (ePage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ePage);
    }

    @GetMapping("/pending-list")
    public ResponseEntity<List<NotStartedExamListDto>> getListPendingExam() {
        List<NotStartedExamListDto> list = examService.getAllPendingExamNotOverlapTime();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

}
