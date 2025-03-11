package com.g44.kodeholik.service.scheduler;

import java.time.Instant;

import org.springframework.stereotype.Service;

public interface ExamSchedulerService {
    public void scheduleExamStart(String examCode, Instant startTime);
}
