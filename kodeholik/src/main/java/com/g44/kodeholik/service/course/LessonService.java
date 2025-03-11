package com.g44.kodeholik.service.course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.g44.kodeholik.model.dto.request.course.LessonRequestDto;
import com.g44.kodeholik.model.dto.response.course.LessonResponseDto;

import java.util.List;

public interface LessonService {
    public Page<LessonResponseDto> getAllLesson(Pageable pageable);

    public LessonResponseDto getLessonById(Long id);

    public void addLesson(LessonRequestDto lessonRequestDto);

    public void editLesson(Long lessonId, LessonRequestDto lessonRequestDto);

    public void deleteLessonById(Long id);

    public void markLessonAsCompleted(Long lessonId);

    public List<Long> getCompletedLessons();

    public List<LessonResponseDto> getLessonByChapterId(Long id);
}
