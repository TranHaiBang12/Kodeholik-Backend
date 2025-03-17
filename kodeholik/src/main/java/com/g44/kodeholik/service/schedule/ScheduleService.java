package com.g44.kodeholik.service.schedule;

public interface ScheduleService {
    public void runOnStart();

    public void endExam();

    public void remindExam();

    public void syncProblemToElasticsearch();

    public void addTop5PopularCourse();

    public void sendEmailRemindUserStudy();
}
