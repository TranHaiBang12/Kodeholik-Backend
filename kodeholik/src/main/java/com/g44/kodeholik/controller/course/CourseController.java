package com.g44.kodeholik.controller.course;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/course")
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/list")
    public ResponseEntity<Page<CourseResponseDto>> getListCourse(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.SC_OK).body(courseService.getAllCourse(pageable));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<CourseResponseDto> getCourseDetail(@PathVariable Long id) {
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

}
