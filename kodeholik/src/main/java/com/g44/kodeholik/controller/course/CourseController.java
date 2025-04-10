package com.g44.kodeholik.controller.course;

import com.g44.kodeholik.model.dto.request.course.CourseRatingRequestDto;
import com.g44.kodeholik.model.dto.request.course.EnrolledUsersRequestDto;
import com.g44.kodeholik.model.dto.request.course.search.CourseSortField;
import com.g44.kodeholik.model.dto.request.course.search.SearchCourseRequestDto;
import com.g44.kodeholik.model.dto.request.discussion.AddCommentRequestDto;
import com.g44.kodeholik.model.dto.response.course.CourseDetailResponseDto;
import com.g44.kodeholik.model.dto.response.course.CourseRatingResponseDto;
import com.g44.kodeholik.model.dto.response.course.EnrolledUserResponseDto;
import com.g44.kodeholik.model.dto.response.discussion.CommentResponseDto;
import com.g44.kodeholik.model.entity.discussion.Comment;
import com.g44.kodeholik.service.course.CourseCommentService;
import com.g44.kodeholik.service.course.CourseRatingService;
import com.g44.kodeholik.util.validation.image.ValidImage;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import com.g44.kodeholik.model.dto.request.course.CourseRequestDto;
import com.g44.kodeholik.model.dto.response.course.CourseResponseDto;
import com.g44.kodeholik.model.dto.response.course.overview.CourseOverviewReportDto;
import com.g44.kodeholik.service.course.CourseService;

import lombok.RequiredArgsConstructor;

import org.apache.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/course")
public class CourseController {
    private final CourseService courseService;
    private final CourseCommentService courseCommentService;
    private final CourseRatingService courseRatingService;

    @GetMapping("/top-popular")
    public ResponseEntity<List<CourseResponseDto>> getTop5PopularCourse() {
        return ResponseEntity.status(HttpStatus.SC_OK).body(courseService.getTop5PopularCourse());
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<CourseDetailResponseDto> getCourseDetail(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.SC_OK).body(courseService.getCourseById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCourse(
            @ModelAttribute @Valid CourseRequestDto requestDto) {
        courseService.addCourse(requestDto);
        return ResponseEntity.status(HttpStatus.SC_CREATED).build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> editCourse(
            @PathVariable Long id,
            @ModelAttribute @Valid CourseRequestDto requestDto) {

        courseService.editCourse(id, requestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/search")
    public ResponseEntity<Page<CourseResponseDto>> searchCourses(
            @RequestBody SearchCourseRequestDto request,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "title") CourseSortField sortBy,
            @RequestParam(defaultValue = "true") Boolean ascending) {
        Page<CourseResponseDto> courses = courseService.searchCourses(request, page, size, sortBy, ascending);
        return ResponseEntity.ok(courses);
    }

    @PostMapping("/enroll/{courseId}")
    public ResponseEntity<String> enrollUserInCourse(@PathVariable Long courseId) {
        courseService.enrollUserInCourse(courseId);
        return ResponseEntity.ok("User enrolled successfully!");
    }

    @DeleteMapping("/unenroll/{courseId}")
    public ResponseEntity<String> unenrollUserFromCourse(@PathVariable Long courseId) {
        courseService.unenrollUserFromCourse(courseId);
        return ResponseEntity.ok("User unenrolled successfully!");
    }

    @PostMapping("/rate")
    public ResponseEntity<CourseRatingResponseDto> rateCourse(@RequestBody @Valid CourseRatingRequestDto requestDto) {
        CourseRatingResponseDto responseDto = courseRatingService.rateCourse(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/comment")
    public ResponseEntity<Void> createComment(@RequestBody AddCommentRequestDto requestDto) {
        courseCommentService.createComment(requestDto);
        return ResponseEntity.status(HttpStatus.SC_CREATED).build();
    }

    @GetMapping("/discussion/{courseId}")
    public ResponseEntity<Page<CommentResponseDto>> getCourseDiscussions(
            @PathVariable Long courseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "noUpvote") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Page<CommentResponseDto> discussions = courseCommentService.getDiscussionByCourseId(courseId, page, size,
                sortBy, sortDirection);
        return ResponseEntity.ok(discussions);
    }

    @GetMapping("/rating/{courseId}")
    public ResponseEntity<List<CourseRatingResponseDto>> getCommentAndRatingByCourseId(@PathVariable Long courseId) {
        List<CourseRatingResponseDto> ratings = courseRatingService.getCourseRating(courseId);
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/enroll/check/{courseId}")
    public ResponseEntity<Boolean> checkUserEnrollment(@PathVariable Long courseId) {
        boolean isEnrolled = courseService.isUserEnrolled(courseId);
        return ResponseEntity.ok(isEnrolled);
    }

    @PutMapping("/register-start/{courseId}")
    public ResponseEntity<Void> registerStart(@PathVariable Long courseId) {
        courseService.registerStartTime(courseId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/register-end/{courseId}")
    public ResponseEntity<Void> registerEnd(@PathVariable Long courseId) {
        courseService.registerEndTime(courseId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list-reply/{id}")
    public ResponseEntity<List<CommentResponseDto>> getListReplyByCommentId(@PathVariable Long id) {
        if (courseCommentService.getAllCommentReplyByComment(id).isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(courseCommentService.getAllCommentReplyByComment(id));
    }

    @GetMapping("/overview-report")
    public ResponseEntity<CourseOverviewReportDto> getCourseOverviewReport(
            @RequestParam(required = false) Timestamp start,
            @RequestParam(required = false) Timestamp end) {
        return ResponseEntity.ok(courseService.getCourseOverviewReport(start, end));
    }

    @PostMapping("/enrolled-users/{courseId}")
    public ResponseEntity<Page<EnrolledUserResponseDto>> getEnrolledUsers(
            @PathVariable Long courseId,
            @RequestBody EnrolledUsersRequestDto request) {
        Page<EnrolledUserResponseDto> enrolledUsers = courseService.getEnrolledUsersWithProgress(
                courseId,
                request.getPage(),
                request.getSize(),
                request.getSortBy(),
                request.getSortDir(),
                request.getUsername());
        return ResponseEntity.ok(enrolledUsers);
    }

    @PostMapping("/completed/{courseId}")
    public ResponseEntity<String> handleCourseCompletion(@PathVariable Long courseId) {
        courseService.sendEmailBasedOnCourseProgress(courseId);
        return ResponseEntity.ok("Course completed and email sent successfully");
    }

    @GetMapping("/my-course")
    public ResponseEntity<Page<CourseResponseDto>> getEnrolledCoursesByUserId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "enrolledAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Page<CourseResponseDto> enrolledCourses = courseService.getEnrolledCourseByUserId(page, size, sortBy, sortDir);
        return ResponseEntity.ok(enrolledCourses);
    }

}
