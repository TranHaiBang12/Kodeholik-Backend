package com.g44.kodeholik.service.scheduler;

import java.time.Instant;

import org.springframework.context.ApplicationEvent;

public class ExamStartEvent extends ApplicationEvent {

    private String examCode;

    private Instant startTime;

    public ExamStartEvent(Object source, String examCode, Instant startTime) {
        super(source);
        this.examCode = examCode;
        this.startTime = startTime;
    }

    public String getExamCode() {
        return examCode;
    }

    public Instant getStartTime() {
        return startTime;
    }

}
