package com.g44.kodeholik.service.scheduler.impl;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.model.dto.response.exam.student.ExamDetailResponseDto;
import com.g44.kodeholik.model.dto.response.exam.student.ExamProblemDetailResponseDto;
import com.g44.kodeholik.service.exam.ExamService;
import com.g44.kodeholik.service.publisher.Publisher;
import com.g44.kodeholik.service.scheduler.ExamSchedulerService;
import com.g44.kodeholik.service.scheduler.ExamStartEvent;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ExamSchedulerServiceImpl implements ExamSchedulerService {

    private final TaskScheduler taskScheduler;
    private final SimpMessagingTemplate messagingTemplate;
    private final ExamService examService;
    private final Publisher publisher;
    private ScheduledFuture<?> scheduledTask;

    public ExamSchedulerServiceImpl(SimpMessagingTemplate messagingTemplate, ExamService examService,
            Publisher publisher) {
        this.messagingTemplate = messagingTemplate;
        this.examService = examService;
        this.taskScheduler = new ThreadPoolTaskScheduler(); // Sử dụng ThreadPoolTaskScheduler
        this.publisher = publisher;
        ((ThreadPoolTaskScheduler) this.taskScheduler).initialize();
    }

    @EventListener
    public void handleExamStartEvent(ExamStartEvent event) {
        scheduleExamStart(event.getExamCode(), event.getStartTime());
    }

    @Override
    public void scheduleExamStart(String examCode, Instant startTime) {
        log.info(examCode + " " + startTime);
        long delay = startTime.toEpochMilli() - System.currentTimeMillis();
        if (delay > 0) {
            scheduledTask = taskScheduler.schedule(() -> sendExamToUsers(examCode), startTime);
        }
    }

    @Transactional
    public void sendExamToUsers(String code) {
        log.info("Start: " + code);
        ExamDetailResponseDto examProblemDetailResponseDtos = examService.startExam(code);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", code);
        map.put("details", examProblemDetailResponseDtos);
        publisher.startExam(map);
    }
}
