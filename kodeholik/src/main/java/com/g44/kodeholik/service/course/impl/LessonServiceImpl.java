package com.g44.kodeholik.service.course.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.model.entity.course.Chapter;
import com.g44.kodeholik.model.entity.course.UserLessonProgress;
import com.g44.kodeholik.model.entity.course.UserLessonProgressId;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.course.LessonStatus;
import com.g44.kodeholik.model.enums.course.LessonVideoType;
import com.g44.kodeholik.repository.course.UserLessonProgressRepository;
import com.g44.kodeholik.service.aws.s3.S3Service;
import com.g44.kodeholik.service.gcs.GoogleCloudStorageService;
import com.g44.kodeholik.util.string.YoutubeUrlParser;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.course.LessonRequestDto;
import com.g44.kodeholik.model.dto.response.course.LessonResponseDto;
import com.g44.kodeholik.model.entity.course.Lesson;
import com.g44.kodeholik.repository.course.ChapterRepository;
import com.g44.kodeholik.repository.course.LessonRepository;
import com.g44.kodeholik.service.course.LessonService;
import com.g44.kodeholik.service.user.UserService;
import com.g44.kodeholik.util.mapper.request.course.LessonRequestMapper;
import com.g44.kodeholik.util.mapper.response.course.LessonResponseMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final ChapterRepository chapterRepository;
    private final LessonRequestMapper lessonRequestMapper;
    private final LessonResponseMapper lessonResponseMapper;
    private final UserService userService;
    private final S3Service s3Service;
    private final GoogleCloudStorageService gcsService;
    private final UserLessonProgressRepository userLessonProgressRepository;

    @Override
    public Page<LessonResponseDto> getAllLesson(Pageable pageable) {
        Page<Lesson> lessonPage = lessonRepository.findByStatus(LessonStatus.ACTIVATED, pageable);
        return lessonPage.map(lessonResponseMapper::mapFrom);
    }

    @Override
    public LessonResponseDto getLessonById(Long id) {
        log.info("Fetching lesson with ID: {}", id);
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Lesson not found", "Lesson not found"));
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
            // Upload file đính kèm lên S3 nếu có
            if (lessonRequestDto.getAttachedFile() != null && !lessonRequestDto.getAttachedFile().isEmpty()) {
                String s3Key = "lessons/" + UUID.randomUUID() + "-"
                        + lessonRequestDto.getAttachedFile().getOriginalFilename();
                s3Service.uploadFileToS3(lessonRequestDto.getAttachedFile(), s3Key);
                lesson.setAttachedFile(s3Key);
            }

            // Xử lý video theo videoType
            if (lessonRequestDto.getVideoType() == LessonVideoType.YOUTUBE) {
                lesson.setVideoUrl(YoutubeUrlParser.extractVideoId(lessonRequestDto.getYoutubeUrl()));
            } else if (lessonRequestDto.getVideoType() == LessonVideoType.VIDEO_FILE
                    && lessonRequestDto.getVideoFile() != null
                    && !lessonRequestDto.getVideoFile().isEmpty()) {
                // Đặt giá trị tạm thời cho video URL
                lesson.setVideoUrl("uploading...");
                lesson = lessonRepository.save(lesson);

                MultipartFile videoFile = lessonRequestDto.getVideoFile();
                Lesson finalLesson = lesson;
                gcsService.uploadVideo(videoFile).thenAccept(gcsPath -> updateLessonVideoUrl(finalLesson, gcsPath))
                        .exceptionally(ex -> {
                            log.error("Failed to upload video to GCS", ex);
                            return null;
                        });
            }

            // Lưu lesson vào DB ngay lập tức (video sẽ cập nhật sau)
            lessonRepository.save(lesson);
        } catch (Exception e) {
            log.error("Error occurred while adding lesson", e);
            throw new RuntimeException("Failed to add lesson: " + e.getMessage());
        }
    }

    private void updateLessonVideoUrl(Lesson lesson, String videoUrl) {
        lesson.setVideoUrl(videoUrl);
        lessonRepository.save(lesson);
    }


    @Override
    public void editLesson(Long lessonId, LessonRequestDto lessonRequestDto) {
        Lesson savedLesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NotFoundException("Lesson not found", "Lesson not found"));

        try {
            savedLesson.setTitle(lessonRequestDto.getTitle());
            savedLesson.setDescription(lessonRequestDto.getDescription());
            savedLesson.setUpdatedAt(Timestamp.from(Instant.now()));
            savedLesson.setUpdatedBy(userService.getCurrentUser());
            savedLesson.setChapter(chapterRepository.findById(lessonRequestDto.getChapterId())
                    .orElseThrow(() -> new NotFoundException("Chapter not found", "Chapter not found")));

            // Kiểm tra nếu có file mới thì xóa file cũ và upload file mới lên S3
            if (lessonRequestDto.getAttachedFile() != null && !lessonRequestDto.getAttachedFile().isEmpty()) {
                if (savedLesson.getAttachedFile() != null) {
                    s3Service.deleteFileFromS3(savedLesson.getAttachedFile());
                }
                String s3Key = "lessons/" + UUID.randomUUID() + "-"
                        + lessonRequestDto.getAttachedFile().getOriginalFilename();
                s3Service.uploadFileToS3(lessonRequestDto.getAttachedFile(), s3Key);
                savedLesson.setAttachedFile(s3Key);
            }

            // Kiểm tra nếu có video mới thì xóa video cũ và upload video mới lên GCS
            if (lessonRequestDto.getVideoFile() != null && !lessonRequestDto.getVideoFile().isEmpty()) {
                if (savedLesson.getVideoUrl() != null) {
                    gcsService.deleteFile(savedLesson.getVideoUrl());
                }
//                String gcsPath = gcsService.uploadVideo(lessonRequestDto.getVideoFile());
//                savedLesson.setVideoUrl(gcsPath);
            }

            lessonRepository.save(savedLesson);
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
        List<Lesson> lessons = lessonRepository.findByChapterId(id);
        return lessons.stream()
                .map(lessonResponseMapper::mapFrom)
                .collect(Collectors.toList());
    }
}
