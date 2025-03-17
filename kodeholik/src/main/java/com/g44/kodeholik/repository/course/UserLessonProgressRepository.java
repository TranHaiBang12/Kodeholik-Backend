package com.g44.kodeholik.repository.course;

import com.g44.kodeholik.model.entity.course.UserLessonProgress;
import com.g44.kodeholik.model.entity.course.UserLessonProgressId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLessonProgressRepository extends JpaRepository<UserLessonProgress, UserLessonProgressId> {
    List<UserLessonProgress> findByUserId(Long userId);
    boolean existsByUserIdAndLessonId(Long userId, Long lessonId);

    @Query("SELECT ulp.lesson.id FROM UserLessonProgress ulp " +
            "WHERE ulp.user.id = :userId AND ulp.lesson.chapter.course.id = :courseId")
    List<Long> findCompletedLessonIdsByUserAndCourse(@Param("userId") Long userId,
                                                     @Param("courseId") Long courseId);
}
