package com.g44.kodeholik.service.course.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.model.entity.course.*;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.course.CourseStatus;
import com.g44.kodeholik.model.enums.course.LessonStatus;
import com.g44.kodeholik.model.enums.course.LessonVideoType;
import com.g44.kodeholik.model.enums.user.UserRole;
import com.g44.kodeholik.repository.course.LessonProblemRepository;
import com.g44.kodeholik.repository.course.UserLessonProgressRepository;
import com.g44.kodeholik.repository.problem.ProblemRepository;
import com.g44.kodeholik.service.aws.s3.S3Service;
import com.g44.kodeholik.service.gcs.GoogleCloudStorageService;
import com.g44.kodeholik.util.string.YoutubeUrlParser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.course.LessonRequestDto;
import com.g44.kodeholik.model.dto.response.course.LessonResponseDto;
import com.g44.kodeholik.repository.course.ChapterRepository;
import com.g44.kodeholik.repository.course.LessonRepository;
import com.g44.kodeholik.service.course.LessonService;
import com.g44.kodeholik.service.user.UserService;
import com.g44.kodeholik.util.mapper.request.course.LessonRequestMapper;
import com.g44.kodeholik.util.mapper.response.course.LessonResponseMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@Log4j2
@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    @Value("${aws.s3.bucket-name}")
    private String bucketName;
    private final S3Client s3Client;
    private final LessonRepository lessonRepository;
    private final ChapterRepository chapterRepository;
    private final LessonRequestMapper lessonRequestMapper;
    private final LessonResponseMapper lessonResponseMapper;
    private final UserService userService;
    private final S3Service s3Service;
    private final GoogleCloudStorageService gcsService;
    private final UserLessonProgressRepository userLessonProgressRepository;
    private final ProblemRepository problemRepository;
    private final LessonProblemRepository lessonProblemRepository;

    public List<LessonStatus> getAllowedStatus(){
        Users currentUser = userService.getCurrentUser();
        UserRole userRole = currentUser.getRole();
        List<LessonStatus> allowedStatuses;
        if (userRole == UserRole.STUDENT) {
            allowedStatuses = Collections.singletonList(LessonStatus.ACTIVATED);
        } else {
            allowedStatuses = Arrays.asList(LessonStatus.values());
        }
        return allowedStatuses;
    }

    @Override
    public Page<LessonResponseDto> getAllLesson(Pageable pageable) {

        Page<Lesson> lessonPage = lessonRepository.findByStatusIn(getAllowedStatus(), pageable);
        return lessonPage.map(lessonResponseMapper::mapFrom);
    }

    @Override
    public LessonResponseDto getLessonById(Long id) {
        log.info("Fetching lesson with ID: {}", id);
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Lesson not found", "Lesson not found"));
        String videoUrl = gcsService.generateSignedUrl(lesson.getVideoUrl());
        lesson.setVideoUrl(videoUrl);
        return lessonResponseMapper.mapFrom(lesson);
    }

    @Override
    public void addLesson(LessonRequestDto lessonRequestDto) {
        Lesson lesson = lessonRequestMapper.mapTo(lessonRequestDto);

        Chapter chapter = chapterRepository.findById(lessonRequestDto.getChapterId())
                .orElseThrow(() -> new NotFoundException("Chapter not found", "Chapter not found"));
        lesson.setChapter(chapter);
        lesson.setCreatedAt(Timestamp.from(Instant.now()));
        lesson.setCreatedBy(userService.getCurrentUser());

        try {
            // Upload file đính kèm lên S3 (nếu có)
            if (lessonRequestDto.getAttachedFile() != null && !lessonRequestDto.getAttachedFile().isEmpty()) {
                String s3Key = "lessons/" + UUID.randomUUID() + "-" + lessonRequestDto.getAttachedFile().getOriginalFilename();
                s3Service.uploadFileToS3(lessonRequestDto.getAttachedFile(), s3Key);
                lesson.setAttachedFile(s3Key);
                lesson.setStatus(LessonStatus.ACTIVATED);
            }

            if (lessonRequestDto.getVideoType() == LessonVideoType.YOUTUBE) {
                // Nếu là YouTube, lấy videoId từ URL và kích hoạt luôn
                lesson.setVideoUrl(YoutubeUrlParser.extractVideoId(lessonRequestDto.getYoutubeUrl()));
                lesson.setStatus(LessonStatus.ACTIVATED);
            } else if (lessonRequestDto.getVideoType() == LessonVideoType.VIDEO_FILE
                    && lessonRequestDto.getVideoFile() != null
                    && !lessonRequestDto.getVideoFile().isEmpty()) {
                // Đọc file thành byte[]
                byte[] fileBytes = lessonRequestDto.getVideoFile().getBytes();
                String originalFileName = lessonRequestDto.getVideoFile().getOriginalFilename();
                String contentType = lessonRequestDto.getVideoFile().getContentType();

                // Đặt trạng thái là IN_PROGRESS vì đang upload video
                lesson.setVideoUrl("uploading...");
                lesson.setStatus(LessonStatus.IN_PROGRESS);
                lessonRepository.save(lesson);

                // Upload video lên GCS **ASYNC**
                gcsService.uploadVideo(fileBytes, originalFileName, contentType)
                        .thenAccept(gcsPath -> {
                            lesson.setVideoUrl(gcsPath);
                            lesson.setStatus(LessonStatus.ACTIVATED); // Chuyển sang ACTIVATED khi upload xong
                            lessonRepository.save(lesson);
                        })
                        .exceptionally(ex -> {
                            log.error("Failed to upload video to GCS", ex);
                            return null;
                        });
            }

            lessonRepository.save(lesson);

            if (lessonRequestDto.getProblemIds() != null && !lessonRequestDto.getProblemIds().isEmpty()) {
                List<Problem> problems = problemRepository.findAllById(lessonRequestDto.getProblemIds());

                if (!problems.isEmpty()) {
                    List<LessonProblem> lessonProblems = problems.stream()
                            .map(problem -> new LessonProblem(lesson, problem))
                            .collect(Collectors.toList());

                    lessonProblemRepository.saveAll(lessonProblems);
                }
            }
        } catch (Exception e) {
            log.error("Error occurred while adding lesson", e);
            throw new RuntimeException("Failed to add lesson: " + e.getMessage());
        }
    }


    @Override
    public void editLesson(Long lessonId, LessonRequestDto lessonRequestDto) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NotFoundException("Lesson not found", "Lesson not found"));

        Chapter chapter = chapterRepository.findById(lessonRequestDto.getChapterId())
                .orElseThrow(() -> new NotFoundException("Chapter not found", "Chapter not found"));
        lesson.setChapter(chapter);
        lesson.setTitle(lessonRequestDto.getTitle());
        lesson.setDescription(lessonRequestDto.getDescription());
        lesson.setUpdatedAt(Timestamp.from(Instant.now()));
        lesson.setUpdatedBy(userService.getCurrentUser());

        try {
            // Xử lý file đính kèm (S3)
            if (lessonRequestDto.getAttachedFile() != null && !lessonRequestDto.getAttachedFile().isEmpty()) {
                String s3Key = "lessons/" + UUID.randomUUID() + "-" + lessonRequestDto.getAttachedFile().getOriginalFilename();
                s3Service.uploadFileToS3(lessonRequestDto.getAttachedFile(), s3Key);
                lesson.setAttachedFile(s3Key);
            }

            if (lessonRequestDto.getVideoType() == LessonVideoType.YOUTUBE) {
                // Nếu là YouTube, cập nhật videoId từ URL
                lesson.setVideoUrl(YoutubeUrlParser.extractVideoId(lessonRequestDto.getYoutubeUrl()));
                lesson.setStatus(LessonStatus.ACTIVATED);
            } else if (lessonRequestDto.getVideoType() == LessonVideoType.VIDEO_FILE
                    && lessonRequestDto.getVideoFile() != null
                    && !lessonRequestDto.getVideoFile().isEmpty()) {
                // Đọc file thành byte[]
                byte[] fileBytes = lessonRequestDto.getVideoFile().getBytes();
                String originalFileName = lessonRequestDto.getVideoFile().getOriginalFilename();
                String contentType = lessonRequestDto.getVideoFile().getContentType();

                // Đặt trạng thái IN_PROGRESS trước khi upload video mới
                lesson.setVideoUrl("uploading...");
                lesson.setStatus(LessonStatus.IN_PROGRESS);
                lessonRepository.save(lesson);

                // Upload video lên GCS **ASYNC**
                gcsService.uploadVideo(fileBytes, originalFileName, contentType)
                        .thenAccept(gcsPath -> {
                            lesson.setVideoUrl(gcsPath);
                            lesson.setStatus(LessonStatus.ACTIVATED); // Chuyển sang ACTIVATED khi upload xong
                            lessonRepository.save(lesson);
                        })
                        .exceptionally(ex -> {
                            log.error("Failed to upload video to GCS", ex);
                            return null;
                        });
            }

            lessonRepository.save(lesson);
        } catch (Exception e) {
            log.error("Error occurred while editing lesson", e);
            throw new RuntimeException("Failed to edit lesson: " + e.getMessage());
        }
    }


    @Override
    public void deleteLessonById(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Lesson not found", "Lesson not found"));

        try {
            // Xóa file trên S3 nếu có
            if (lesson.getAttachedFile() != null) {
                s3Service.deleteFileFromS3(lesson.getAttachedFile());
            }

            // Xóa video trên GCS nếu có
            if (lesson.getVideoUrl() != null) {
                gcsService.deleteFile(lesson.getVideoUrl());
            }

            lessonRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Error occurred while deleting lesson", e);
            throw new RuntimeException("Failed to delete lesson: " + e.getMessage());
        }
    }

    @Override
    public void markLessonAsCompleted(Long lessonId) {
        Users currentUser = userService.getCurrentUser();

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NotFoundException("Lesson not found",
                        "Lesson with ID " + lessonId + " does not exist"));

        UserLessonProgress progress = new UserLessonProgress();
        progress.setId(new UserLessonProgressId(currentUser.getId(), lessonId));
        progress.setUser(currentUser);
        progress.setLesson(lesson);

        userLessonProgressRepository.save(progress);
    }

    @Override
    public List<Long> getCompletedLessons() {
        Users currentUser = userService.getCurrentUser();
        return userLessonProgressRepository.findByUserId(currentUser.getId())
                .stream()
                .map(progress -> progress.getLesson().getId())
                .collect(Collectors.toList());
    }

    @Override
    public List<LessonResponseDto> getLessonByChapterId(Long id) {
        List<Lesson> lessons = lessonRepository.findByChapterIdAndStatusIn(id,getAllowedStatus());
        return lessons.stream()
                .map(lessonResponseMapper::mapFrom)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<byte[]> downloadFile(String key) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(getObjectRequest);
            if (objectBytes == null) {
                return ResponseEntity.notFound().build();
            }

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
