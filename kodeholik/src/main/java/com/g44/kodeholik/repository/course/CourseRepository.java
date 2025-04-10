package com.g44.kodeholik.repository.course;

import com.g44.kodeholik.model.entity.setting.Topic;
import com.g44.kodeholik.model.enums.course.CourseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.course.Course;
import com.g44.kodeholik.model.entity.problem.Problem;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

        Page<Course> findByTitle(String title, Pageable pageable);

        Page<Course> findByStatusIn(List<CourseStatus> statuses, Pageable pageable);

        Page<Course> findByTitleContainingIgnoreCaseAndStatusIn(String title, List<CourseStatus> statuses,
                        Pageable pageable);

        Page<Course> findByTopicsInAndStatusIn(List<Topic> topics, List<CourseStatus> statuses, Pageable pageable);

        Page<Course> findByTitleContainingIgnoreCaseAndTopicsInAndStatusIn(String title, List<Topic> topics,
                        List<CourseStatus> statuses, Pageable pageable);

        List<Course> findTop5ByOrderByNumberOfParticipantDescRateDesc();

        boolean existsByTitle(String title);

        boolean existsByTitleAndIdNot(String title, Long id);

        List<Course> findTop6ByOrderByNumberOfParticipantDescRateDesc();

        public List<Course> findByCreatedAtBetweenOrderByNumberOfParticipantDescRateDesc(
                        Timestamp start, Timestamp end);

        public List<Course> findAllByOrderByNumberOfParticipantDescRateDesc();

        Optional<Course> findById(Long courseId);
}
