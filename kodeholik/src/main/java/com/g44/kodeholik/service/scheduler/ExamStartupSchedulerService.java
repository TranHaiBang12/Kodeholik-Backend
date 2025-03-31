package com.g44.kodeholik.service.scheduler;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.entity.exam.Exam;
import com.g44.kodeholik.service.exam.ExamService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExamStartupSchedulerService {
    private final ExamService examService;

    private final ExamSchedulerService examSchedulerService;

    @EventListener(ApplicationReadyEvent.class)
    public void schedulePendingExamOnStartUp() {
        List<Exam> exams = examService.getAllPendingExam();
        for (Exam exam : exams) {
            if (exam.getStartTime().after(Timestamp.from(Instant.now()))) {
                examSchedulerService.scheduleExamStart(exam.getCode(), exam.getStartTime().toInstant());
            }
        }
    }

}
