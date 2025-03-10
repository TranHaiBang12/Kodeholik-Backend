package com.g44.kodeholik.repository.course;

import com.g44.kodeholik.model.entity.course.UserLessonProgress;
import com.g44.kodeholik.model.entity.course.UserLessonProgressId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLessonProgressRepository extends JpaRepository<UserLessonProgress, UserLessonProgressId> {
    List<UserLessonProgress> findByUserId(Long userId);
    boolean existsByUserIdAndLessonId(Long userId, Long lessonId);
}
