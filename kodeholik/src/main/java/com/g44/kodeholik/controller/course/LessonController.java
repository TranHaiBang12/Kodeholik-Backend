package com.g44.kodeholik.controller.course;

import org.apache.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.g44.kodeholik.model.dto.request.course.ChapterRequestDto;
import com.g44.kodeholik.model.dto.request.course.LessonRequestDto;
import com.g44.kodeholik.model.dto.response.course.ChapterResponseDto;
import com.g44.kodeholik.model.dto.response.course.LessonResponseDto;
import com.g44.kodeholik.service.course.ChapterService;
import com.g44.kodeholik.service.course.LessonService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lesson")
public class LessonController {
    private final LessonService lessonService;

    @GetMapping("/list")
    public ResponseEntity<Page<LessonResponseDto>> getListLesson(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.SC_OK).body(lessonService.getAllLesson(pageable));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<LessonResponseDto> getLessonrDetail(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.SC_OK).body(lessonService.getLessonById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addLesson(@RequestBody LessonRequestDto lessonRequestDto) {
        lessonService.addLesson(lessonRequestDto);
        return ResponseEntity.status(HttpStatus.SC_CREATED).build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateLesson(@PathVariable Long id, @RequestBody LessonRequestDto lessonRequestDto) {
        lessonService.editLesson(id, lessonRequestDto);
        return ResponseEntity.status(HttpStatus.SC_CREATED).build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLessonById(id);
        return ResponseEntity.noContent().build();
    }
}
