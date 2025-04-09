package com.g44.kodeholik.repository.course;

import com.g44.kodeholik.model.entity.course.LessonProblem;
import com.g44.kodeholik.model.entity.course.LessonProblemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonProblemRepository extends JpaRepository<LessonProblem, LessonProblemId> {
    List<LessonProblem> findByLesson_Id(Long lessonId);
    void deleteByLesson_Id(Long lessonId);
}