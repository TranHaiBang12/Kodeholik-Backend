package com.g44.kodeholik.service.course;

import com.g44.kodeholik.model.dto.request.course.search.CourseSortField;
import com.g44.kodeholik.model.dto.request.course.search.SearchCourseRequestDto;
import com.g44.kodeholik.model.dto.response.course.CourseDetailResponseDto;

import java.sql.Timestamp;
import java.util.List;

import com.g44.kodeholik.model.dto.response.course.EnrolledUserResponseDto;
import com.g44.kodeholik.model.dto.response.course.ListResponseDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.g44.kodeholik.model.dto.request.course.CourseRequestDto;
import com.g44.kodeholik.model.dto.response.course.CourseResponseDto;
import com.g44.kodeholik.model.dto.response.course.overview.CourseOverviewReportDto;

import org.springframework.web.multipart.MultipartFile;

public interface CourseService {
        public CourseDetailResponseDto getCourseById(Long courseId);

        public void addCourse(CourseRequestDto courseRequestDto);

        public void editCourse(Long courseId, CourseRequestDto courseRequestDto);

        public void deleteCourse(Long courseId);

        Page<CourseResponseDto> searchCourses(SearchCourseRequestDto request, Integer page, Integer size,
                        CourseSortField sortBy, Boolean ascending);

        Page<CourseResponseDto> getEnrolledCourseByUserId(int page, int size, String sortBy, String sortDir);

        public void enrollUserInCourse(Long courseId);

        public void unenrollUserFromCourse(Long courseId);

        public boolean isUserEnrolled(Long courseId);

        public void addTop5PopularCourse();

        public List<CourseResponseDto> getTop5PopularCourse();

        public void registerStartTime(Long courseId);

        public void registerEndTime(Long courseId);

        public void sendEmailBasedOnStudyStreak();

        public CourseOverviewReportDto getCourseOverviewReport(Timestamp start, Timestamp end);

        public Page<EnrolledUserResponseDto> getEnrolledUsersWithProgress(Long courseId, int page, int size,
                        String sortBy,
                        String sortDirection, String usernameSearch);

        public void sendEmailBasedOnCourseProgress(Long courseId);

        public List<ListResponseDto> getListCourseResponseDto();

}
