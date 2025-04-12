package com.g44.kodeholik.service.course.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.model.dto.response.course.LessonProblemResponseDto;
import com.g44.kodeholik.model.entity.course.*;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.course.CourseStatus;
import com.g44.kodeholik.model.enums.course.LessonStatus;
import com.g44.kodeholik.model.enums.course.LessonVideoType;
import com.g44.kodeholik.model.enums.user.NotificationType;
import com.g44.kodeholik.model.enums.user.UserRole;
import com.g44.kodeholik.repository.course.LessonProblemRepository;
import com.g44.kodeholik.repository.course.UserLessonProgressRepository;
import com.g44.kodeholik.repository.problem.ProblemRepository;
import com.g44.kodeholik.service.aws.s3.S3Service;
import com.g44.kodeholik.service.gcs.GoogleCloudStorageService;
import com.g44.kodeholik.util.string.YoutubeUrlParser;
import jakarta.transaction.Transactional;
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
import com.g44.kodeholik.service.user.NotificationService;
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
    private final NotificationService notificationService;

    public List<LessonStatus> getAllowedStatus() {
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
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Lesson not found", "Lesson not found"));

        String videoUrl = lesson.getVideoUrl();
        if (videoUrl != null && videoUrl.startsWith("videos/")) {
            videoUrl = gcsService.generateSignedUrl(videoUrl);
            lesson.setVideoUrl(videoUrl);
        }

        List<LessonProblem> lessonProblems = lessonProblemRepository.findByLesson_Id(id);

        List<LessonProblemResponseDto> lessonProblemDtos = lessonProblems.stream()
                .map(lp -> new LessonProblemResponseDto(
                        lp.getProblem().getTitle(),
                        lp.getProblem().getDifficulty(),
                        lp.getProblem().getLink()))
                .collect(Collectors.toList());

        LessonResponseDto lessonResponse = lessonResponseMapper.mapFrom(lesson);
        lessonResponse.setProblems(lessonProblemDtos);

        return lessonResponse;
    }

    @Override
    public String addLesson(LessonRequestDto lessonRequestDto) {
        Users currentUser = userService.getCurrentUser();
        String normalizedTitle = lessonRequestDto.getTitle().trim().replaceAll("[ ]+", " ");
        if (normalizedTitle.length() < 10) {
            throw new BadRequestException(
                    "Lesson title must be at least 10 characters long (excluding extra spaces): " + normalizedTitle,
                    "Lesson title must be at least 10 characters long (excluding extra spaces): " + normalizedTitle);
        }
        if (lessonRepository.existsByTitle(normalizedTitle)) {
            throw new BadRequestException("Lesson title already exists: " + normalizedTitle,
                    "Lesson title already exists: " + normalizedTitle);
        }
        String message = "Add lesson successfully!";

        String normalizedDescription = lessonRequestDto.getDescription().trim().replaceAll("[ ]+", " ");
        if (normalizedDescription.isEmpty()) {
            throw new BadRequestException("Lesson description cannot be empty or contain only whitespace",
                    "Lesson description cannot be empty or contain only whitespace");
        }
        if (normalizedDescription.length() < 10) {
            throw new BadRequestException(
                    "Lesson description must be at least 10 characters long (excluding extra spaces): "
                            + normalizedDescription,
                    "Lesson description must be at least 10 characters long (excluding extra spaces): "
                            + normalizedDescription);
        }
        Lesson lesson = lessonRequestMapper.mapTo(lessonRequestDto);
        lesson.setTitle(normalizedTitle);
        lesson.setDescription(normalizedDescription);
        Chapter chapter = chapterRepository.findById(lessonRequestDto.getChapterId())
                .orElseThrow(() -> new NotFoundException("Chapter not found", "Chapter not found"));
        lesson.setChapter(chapter);
        lesson.setCreatedAt(Timestamp.from(Instant.now()));
        lesson.setCreatedBy(currentUser);

        try {
            // Upload file đính kèm lên S3 (nếu có)
            if (lessonRequestDto.getAttachedFile() != null && !lessonRequestDto.getAttachedFile().isEmpty()) {
                String s3Key = "lessons/" + UUID.randomUUID() + "-"
                        + lessonRequestDto.getAttachedFile().getOriginalFilename();
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

                lesson.setVideoUrl("uploading...");
                lesson.setStatus(LessonStatus.IN_PROGRESS);
                lessonRepository.save(lesson);
                message = "Add lesson successfully! We will notify you when the video has been successfully uploaded";

                // Upload video lên GCS **ASYNC**
                gcsService.uploadVideo(fileBytes, originalFileName, contentType)
                        .thenAccept(gcsPath -> {
                            lesson.setVideoUrl(gcsPath);
                            lesson.setStatus(LessonStatus.ACTIVATED);
                            lessonRepository.save(lesson);
                            notificationService.saveNotification(currentUser,
                                    "Video in lesson " + lesson.getTitle() + " uploaded successfully", "",
                                    NotificationType.SYSTEM);
                        })
                        .exceptionally(ex -> {
                            log.error("Failed to upload video to GCS", ex);
                            return null;
                        });
            }

            lessonRepository.save(lesson);

            if (lessonRequestDto.getProblemIds() != null && !lessonRequestDto.getProblemIds().isEmpty()) {
                List<LessonProblem> lessonProblems = lessonRequestDto.getProblemIds().stream()
                        .map(problemLink -> problemRepository.findByLink(problemLink)
                                .map(problem -> new LessonProblem(lesson, problem))
                                .orElse(null)) // Nếu không tìm thấy thì bỏ qua
                        .filter(Objects::nonNull) // Lọc bỏ null
                        .collect(Collectors.toList());

                if (!lessonProblems.isEmpty()) {
                    lessonProblemRepository.saveAll(lessonProblems);
                }
            }

        } catch (Exception e) {
            log.error("Error occurred while adding lesson", e);
            throw new RuntimeException("Failed to add lesson: " + e.getMessage());
        }
        return message;
    }

    @Override
    @Transactional
    public String editLesson(Long lessonId, LessonRequestDto lessonRequestDto) {
        Users currentUser = userService.getCurrentUser();
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NotFoundException("Lesson not found", "Lesson not found"));
        String message = "Edit lesson successfully!";
        String normalizedTitle = lessonRequestDto.getTitle().trim().replaceAll("[ ]+", " ");
        if (normalizedTitle.length() < 10) {
            throw new BadRequestException(
                    "Lesson title must be at least 10 characters long (excluding extra spaces): " + normalizedTitle,
                    "Lesson title must be at least 10 characters long (excluding extra spaces): " + normalizedTitle);
        }
        if (lessonRepository.existsByTitleAndIdNot(normalizedTitle, lessonId)) {
            throw new BadRequestException("Lesson title already exists: " + normalizedTitle,
                    "Lesson title already exists: " + normalizedTitle);
        }
        String normalizedDescription = lessonRequestDto.getDescription().trim().replaceAll("[ ]+", " ");
        if (normalizedDescription.isEmpty()) {
            throw new BadRequestException("Lesson description cannot be empty or contain only whitespace",
                    "Lesson description cannot be empty or contain only whitespace");
        }
        if (normalizedDescription.length() < 10) {
            throw new BadRequestException(
                    "Lesson description must be at least 10 characters long (excluding extra spaces): "
                            + normalizedDescription,
                    "Lesson description must be at least 10 characters long (excluding extra spaces): "
                            + normalizedDescription);
        }
        // Update lesson properties using mapper
        lessonRequestMapper.updateFromDto(lessonRequestDto, lesson);
        lesson.setTitle(normalizedTitle);
        lesson.setDescription(normalizedDescription);
        lesson.setUpdatedAt(Timestamp.from(Instant.now()));
        lesson.setUpdatedBy(currentUser);
        // Update chapter if changed
        if (lessonRequestDto.getChapterId() != null &&
                !lessonRequestDto.getChapterId().equals(lesson.getChapter().getId())) {
            Chapter chapter = chapterRepository.findById(lessonRequestDto.getChapterId())
                    .orElseThrow(() -> new NotFoundException("Chapter not found", "Chapter not found"));
            lesson.setChapter(chapter);
        }

        try {
            // Handle attached file update
            if (lessonRequestDto.getAttachedFile() != null && !lessonRequestDto.getAttachedFile().isEmpty()) {
                if (lesson.getAttachedFile() != null && s3Service.isObjectExist(lesson.getAttachedFile())) {
                    s3Service.deleteFileFromS3(lesson.getAttachedFile());
                }
                String s3Key = "lessons/" + UUID.randomUUID() + "-"
                        + lessonRequestDto.getAttachedFile().getOriginalFilename();
                s3Service.uploadFileToS3(lessonRequestDto.getAttachedFile(), s3Key);
                lesson.setAttachedFile(s3Key);
                lesson.setStatus(LessonStatus.ACTIVATED);
            }

            // Handle video update
            if (lessonRequestDto.getVideoType() != null) {
                if (lessonRequestDto.getVideoType() == LessonVideoType.YOUTUBE) {
                    String videoId = YoutubeUrlParser.extractVideoId(lessonRequestDto.getYoutubeUrl());
                    lesson.setVideoUrl(videoId);
                    lesson.setStatus(LessonStatus.ACTIVATED);

                } else if (lessonRequestDto.getVideoType() == LessonVideoType.VIDEO_FILE &&
                        lessonRequestDto.getVideoFile() != null &&
                        !lessonRequestDto.getVideoFile().isEmpty()) {
                    byte[] fileBytes = lessonRequestDto.getVideoFile().getBytes();
                    String originalFileName = lessonRequestDto.getVideoFile().getOriginalFilename();
                    String contentType = lessonRequestDto.getVideoFile().getContentType();

                    lesson.setVideoUrl("uploading...");
                    lesson.setStatus(LessonStatus.IN_PROGRESS);
                    lessonRepository.save(lesson);
                    message = "Edit lesson successfully! We will notify you when the video has been successfully uploaded";

                    gcsService.uploadVideo(fileBytes, originalFileName, contentType)
                            .thenAccept(gcsPath -> {

                                lesson.setVideoUrl(gcsPath);
                                lesson.setStatus(LessonStatus.ACTIVATED);
                                lessonRepository.save(lesson);
                                notificationService.saveNotification(currentUser,
                                        "Video in lesson " + lesson.getTitle() + " uploaded successfully", "",
                                        NotificationType.SYSTEM);
                            })
                            .exceptionally(ex -> {
                                log.error("Failed to update video to GCS", ex);
                                return null;
                            });
                }
            }

            // Update problems if provided
            if (lessonRequestDto.getProblemIds() != null) {
                lessonProblemRepository.deleteByLesson_Id(lesson.getId());
                if (!lessonRequestDto.getProblemIds().isEmpty()) {
                    List<LessonProblem> lessonProblems = lessonRequestDto.getProblemIds().stream()
                            .map(problemLink -> problemRepository.findByLink(problemLink)
                                    .map(problem -> new LessonProblem(lesson, problem))
                                    .orElse(null))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    if (!lessonProblems.isEmpty()) {
                        lessonProblemRepository.saveAll(lessonProblems);
                    }
                }
            }

            lessonRepository.save(lesson);

        } catch (Exception e) {
            log.error("Error occurred while editing lesson", e);
            throw new RuntimeException("Failed to edit lesson: " + e.getMessage());
        }
        return message;
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
        if (currentUser == null) {
            return Collections.emptyList();
        }
        return userLessonProgressRepository.findByUserId(currentUser.getId())
                .stream()
                .map(progress -> progress.getLesson().getId())
                .collect(Collectors.toList());
    }

    @Override
    public List<LessonResponseDto> getLessonByChapterId(Long id) {
        List<Long> completedLessons = getCompletedLessons();
        List<Lesson> lessons = lessonRepository.findByChapterIdAndStatusIn(id, getAllowedStatus());

        return lessons.stream()
                .map(lesson -> {
                    LessonResponseDto dto = lessonResponseMapper.mapFrom(lesson);
                    dto.setCompleted(completedLessons.contains(lesson.getId()));
                    return dto;
                })
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
