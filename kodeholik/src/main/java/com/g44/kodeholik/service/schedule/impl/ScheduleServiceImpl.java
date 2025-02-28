package com.g44.kodeholik.service.schedule.impl;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.service.problem.ProblemService;
import com.g44.kodeholik.service.schedule.ScheduleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ProblemService problemService;

}
