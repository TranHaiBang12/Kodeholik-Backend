package com.g44.kodeholik.repository.course;

import com.g44.kodeholik.model.entity.course.LessonProblem;
import com.g44.kodeholik.model.entity.course.LessonProblemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonProblemRepository extends JpaRepository<LessonProblem, LessonProblemId> {
}

