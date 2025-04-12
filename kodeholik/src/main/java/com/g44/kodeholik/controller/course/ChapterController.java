package com.g44.kodeholik.controller.course;

import com.g44.kodeholik.model.dto.response.course.LessonResponseDto;
import com.g44.kodeholik.model.dto.response.course.ListResponseDto;

import jakarta.validation.Valid;
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
import com.g44.kodeholik.model.dto.request.course.CourseRequestDto;
import com.g44.kodeholik.model.dto.response.course.ChapterResponseDto;
import com.g44.kodeholik.model.dto.response.course.CourseResponseDto;
import com.g44.kodeholik.service.course.ChapterService;
import com.g44.kodeholik.service.course.CourseService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chapter")
public class ChapterController {
    private final ChapterService chapterService;

    @GetMapping("/list")
    public ResponseEntity<Page<ChapterResponseDto>> getListChapter(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.SC_OK).body(chapterService.getAllChapter(pageable));
    }

    @GetMapping("/list-by-course/{courseId}")
    public ResponseEntity<List<ListResponseDto>> getAllCourseResponseDto(@PathVariable Long courseId) {
        return ResponseEntity.status(HttpStatus.SC_OK)
                .body(chapterService.getListChapterResponseDtoByCourseId(courseId));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ChapterResponseDto> getChapterDetail(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.SC_OK).body(chapterService.getChapterById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addChapter(@RequestBody @Valid ChapterRequestDto chapterRequestDto) {
        chapterService.addChapter(chapterRequestDto);
        return ResponseEntity.status(HttpStatus.SC_CREATED).build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateChapter(@PathVariable Long id,
            @RequestBody @Valid ChapterRequestDto chapterRequestDto) {
        chapterService.editChapter(id, chapterRequestDto);
        return ResponseEntity.status(HttpStatus.SC_CREATED).build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteChapter(@PathVariable Long id) {
        chapterService.deleteChapter(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-course/{courseId}")
    public ResponseEntity<List<ChapterResponseDto>> getChapterByCourse(@PathVariable Long courseId) {
        List<ChapterResponseDto> chapters = chapterService.getChapterByCourseId(courseId);
        return ResponseEntity.ok(chapters);
    }
}
