package com.g44.kodeholik.service.schedule.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.model.dto.response.exam.student.ExamProblemDetailResponseDto;
import com.g44.kodeholik.service.course.CourseService;
import com.g44.kodeholik.service.exam.ExamService;
import com.g44.kodeholik.service.publisher.Publisher;
import com.g44.kodeholik.service.problem.ProblemService;
import com.g44.kodeholik.service.publisher.Publisher;
import com.g44.kodeholik.service.schedule.ScheduleService;

import jakarta.annotation.PostConstruct;
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

    private final CourseService courseService;

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
    @Scheduled(fixedRate = 1000 * 5 * 60)
    @Override
    public void syncProblemToElasticsearch() {
        problemService.syncProblemsToElasticsearch();
    }

    @PostConstruct
    @Override
    public void runOnStart() {
        syncProblemToElasticsearch();
        addTop5PopularCourse();
        // sendEmailRemindUserStudy();
    }

    @Transactional
    // chay vao giay 0 phut 0 gio` 0 moi. ngay` moi. thang' moi. ngay` trg tuan`
    @Scheduled(cron = "0 0 0 * * *")
    @Override
    public void addTop5PopularCourse() {
        courseService.addTop5PopularCourse();
    }

    @Transactional
    // chay vao giay 0 phut 0 gio` 0 moi. ngay` moi. thang' moi. ngay` trg tuan`
    @Scheduled(cron = "0 0 0 * * *")
    @Override
    public void sendEmailRemindUserStudy() {
        courseService.sendEmailBasedOnStudyStreak();
    }

}
