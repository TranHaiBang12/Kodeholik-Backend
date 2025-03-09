package com.g44.kodeholik.service.schedule;

public interface ScheduleService {
    public void endExam();

    public void remindExam();

    public void syncProblemToElasticsearch();
}
