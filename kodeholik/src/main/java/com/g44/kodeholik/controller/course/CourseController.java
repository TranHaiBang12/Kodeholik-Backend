package com.g44.kodeholik.controller.course;

import com.g44.kodeholik.model.dto.request.course.CourseRatingRequestDto;
import com.g44.kodeholik.model.dto.request.course.search.CourseSortField;
import com.g44.kodeholik.model.dto.request.course.search.SearchCourseRequestDto;
import com.g44.kodeholik.model.dto.request.discussion.AddCommentRequestDto;
import com.g44.kodeholik.model.dto.response.course.CourseDetailResponseDto;
import com.g44.kodeholik.model.dto.response.course.CourseRatingResponseDto;
import com.g44.kodeholik.model.dto.response.discussion.CommentResponseDto;
import com.g44.kodeholik.model.entity.discussion.Comment;
import com.g44.kodeholik.service.course.CourseCommentService;
import com.g44.kodeholik.service.course.CourseRatingService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.g44.kodeholik.model.dto.request.course.CourseRequestDto;
import com.g44.kodeholik.model.dto.response.course.CourseResponseDto;
import com.g44.kodeholik.service.course.CourseService;

import lombok.RequiredArgsConstructor;

import org.apache.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/course")
public class CourseController {
    private final CourseService courseService;
    private final CourseCommentService courseCommentService;
    private final CourseRatingService courseRatingService;


    @GetMapping("/list")
    public ResponseEntity<Page<CourseResponseDto>> getListCourse(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.SC_OK).body(courseService.getAllCourse(pageable));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<CourseDetailResponseDto> getCourseDetail(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.SC_OK).body(courseService.getCourseById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCourse(@RequestBody CourseRequestDto courseRequestDto) {
        courseService.addCourse(courseRequestDto);
        return ResponseEntity.status(HttpStatus.SC_CREATED).build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @RequestBody CourseRequestDto courseRequestDto) {
        courseService.editCourse(id, courseRequestDto);
        return ResponseEntity.status(HttpStatus.SC_CREATED).build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CourseResponseDto>> searchCourses(
            @RequestBody SearchCourseRequestDto request,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "title") CourseSortField sortBy,
            @RequestParam(defaultValue = "true") Boolean ascending
    ) {
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
        courseService.unenrollUserFromCourse( courseId);
        return ResponseEntity.ok("User unenrolled successfully!");
    }

    @GetMapping("/discussion/{courseId}")
    public ResponseEntity<List<Comment>> getCourseComments(@PathVariable Long courseId) {
        List<Comment> comments = courseCommentService.getAllCommentsByCourse(courseId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/rate")
    public ResponseEntity<CourseRatingResponseDto> rateCourse(@RequestBody @Valid CourseRatingRequestDto requestDto) {
        CourseRatingResponseDto responseDto = courseRatingService.rateCourse(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/comment/{courseId}")
    public ResponseEntity<CommentResponseDto> addComment(@PathVariable Long courseId,
                                                         @RequestBody @Valid AddCommentRequestDto request) {
        Comment comment = courseCommentService.addCommentToCourse(courseId, request);
        return ResponseEntity.ok(new CommentResponseDto(comment));
    }

}
