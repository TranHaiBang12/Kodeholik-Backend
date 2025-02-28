package com.g44.kodeholik.controller.course;

import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.entity.course.Lesson;
import com.g44.kodeholik.repository.course.LessonRepository;
import com.g44.kodeholik.service.aws.s3.S3Service;
import jakarta.validation.Valid;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.g44.kodeholik.model.dto.request.course.ChapterRequestDto;
import com.g44.kodeholik.model.dto.request.course.LessonRequestDto;
import com.g44.kodeholik.model.dto.response.course.ChapterResponseDto;
import com.g44.kodeholik.model.dto.response.course.LessonResponseDto;
import com.g44.kodeholik.service.course.ChapterService;
import com.g44.kodeholik.service.course.LessonService;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lesson")
public class LessonController {
    private final LessonService lessonService;

    private final LessonRepository lessonRepository;

    private final S3Service s3Service;

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @GetMapping("/list")
    public ResponseEntity<Page<LessonResponseDto>> getListLesson(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.SC_OK).body(lessonService.getAllLesson(pageable));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<LessonResponseDto> getLessonrDetail(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.SC_OK).body(lessonService.getLessonById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addLesson(@ModelAttribute @Valid LessonRequestDto lessonRequestDto) {
        lessonService.addLesson(lessonRequestDto);
        return ResponseEntity.status(HttpStatus.SC_CREATED).build();
    }



    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateLesson(@PathVariable Long id, @RequestBody @Valid LessonRequestDto lessonRequestDto) {
        lessonService.editLesson(id, lessonRequestDto);
        return ResponseEntity.status(HttpStatus.SC_CREATED).build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLessonById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/download-file")
    public ResponseEntity<byte[]> downloadFile(@RequestParam String key) {
        try {
            // Gửi yêu cầu tải file từ S3
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(getObjectRequest);

            byte[] fileBytes = objectBytes.asByteArray();
            String contentType = objectBytes.response().contentType();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + key + "\"")
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(fileBytes);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
