package com.g44.kodeholik.service.schedule.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.model.dto.response.exam.student.ExamProblemDetailResponseDto;
import com.g44.kodeholik.service.exam.ExamService;
import com.g44.kodeholik.service.publisher.Publisher;
import com.g44.kodeholik.service.problem.ProblemService;
import com.g44.kodeholik.service.publisher.Publisher;
import com.g44.kodeholik.service.schedule.ScheduleService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ProblemService problemService;

    private final Publisher publisher;

    private final ExamService examService;

    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    // @Scheduled(fixedRate = 5000)
    @Override
    public void endExam() {
        examService.endExam();
    }

    @Transactional
    // @Scheduled(fixedRate = 5000)
    @Override
    public void remindExam() {
        examService.sendNotiToUserExamAboutToStart();
        ;
    }

    @Transactional
    // @Scheduled(fixedRate = 1000 * 5 * 60)
    @Override
    public void syncProblemToElasticsearch() {
        problemService.syncProblemsToElasticsearch();
    }

}
