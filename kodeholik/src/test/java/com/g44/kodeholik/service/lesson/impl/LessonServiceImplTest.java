package com.g44.kodeholik.service.lesson.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.course.LessonRequestDto;
import com.g44.kodeholik.model.dto.response.course.LessonResponseDto;
import com.g44.kodeholik.model.entity.course.Chapter;
import com.g44.kodeholik.model.entity.course.Lesson;
import com.g44.kodeholik.model.entity.course.LessonProblem;
import com.g44.kodeholik.model.entity.course.UserLessonProgress;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.enums.course.LessonStatus;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.course.LessonVideoType;
import com.g44.kodeholik.repository.course.ChapterRepository;
import com.g44.kodeholik.repository.course.LessonProblemRepository;
import com.g44.kodeholik.repository.course.LessonRepository;
import com.g44.kodeholik.repository.course.UserLessonProgressRepository;
import com.g44.kodeholik.repository.problem.ProblemRepository;
import com.g44.kodeholik.service.aws.s3.S3Service;
import com.g44.kodeholik.service.course.impl.LessonServiceImpl;
import com.g44.kodeholik.service.gcs.GoogleCloudStorageService;
import com.g44.kodeholik.service.user.NotificationService;
import com.g44.kodeholik.service.user.UserService;
import com.g44.kodeholik.util.mapper.request.course.LessonRequestMapper;
import com.g44.kodeholik.util.mapper.response.course.LessonResponseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@ExtendWith(MockitoExtension.class)
class LessonServiceImplTest {

    @InjectMocks
    private LessonServiceImpl lessonService;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private ChapterRepository chapterRepository;

    @Mock
    private ProblemRepository problemRepository;

    @Mock
    private LessonProblemRepository lessonProblemRepository;

    @Mock
    private UserLessonProgressRepository userLessonProgressRepository;

    @Mock
    private UserService userService;

    @Mock
    private S3Service s3Service;

    @Mock
    private GoogleCloudStorageService gcsService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private LessonRequestMapper lessonRequestMapper;

    @Mock
    private LessonResponseMapper lessonResponseMapper;

    @Mock
    private S3Client s3Client;

    private Lesson lesson;
    private Chapter chapter;
    private Users user;
    private LessonRequestDto lessonRequestDto;
    private LessonResponseDto lessonResponseDto;
    private Problem problem;
    private LessonProblem lessonProblem;

    @BeforeEach
    void setUp() {
        user = new Users();
        user.setId(1L);
        user.setUsername("testuser");

        chapter = new Chapter();
        chapter.setId(1L);

        lesson = new Lesson();
        lesson.setId(1L);
        lesson.setTitle("Valid Lesson Title");
        lesson.setDescription("Valid Lesson Description");
        lesson.setChapter(chapter);
        lesson.setStatus(LessonStatus.ACTIVATED);
        lesson.setCreatedAt(Timestamp.from(Instant.now()));
        lesson.setCreatedBy(user);

        lessonRequestDto = new LessonRequestDto();
        lessonRequestDto.setTitle("Valid Lesson Title");
        lessonRequestDto.setDescription("Valid Lesson Description");
        lessonRequestDto.setChapterId(1L);
        lessonRequestDto.setStatus(LessonStatus.ACTIVATED);
        lessonRequestDto.setVideoType(LessonVideoType.YOUTUBE);
        lessonRequestDto.setYoutubeUrl("https://www.youtube.com/watch?v=abc123");

        lessonResponseDto = new LessonResponseDto();
        lessonResponseDto.setTitle("Valid Lesson Title");
        lessonResponseDto.setDescription("Valid Lesson Description");

        problem = new Problem();
        problem.setId(1L);
        problem.setTitle("Problem Title");
//        problem.setDifficulty("EASY");
        problem.setLink("problem-link");

        lessonProblem = new LessonProblem();
        lessonProblem.setLesson(lesson);
        lessonProblem.setProblem(problem);
    }

    @Test
    void addLessonWithYouTubeVideoShouldSucceed() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(lessonRepository.findByTitleIgnoreCaseAndChapterId(anyString(), anyLong())).thenReturn(Optional.empty());
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));
        when(lessonRequestMapper.mapTo(any(LessonRequestDto.class))).thenReturn(lesson);

        String result = lessonService.addLesson(lessonRequestDto);

        assertEquals("Add lesson successfully!", result);
        verify(lessonRepository).save(any(Lesson.class));
        verify(s3Service, never()).uploadFileToS3(any(), anyString());
        verify(gcsService, never()).uploadVideo(any());
    }

    @Test
    void addLessonWithVideoFileShouldSucceedAndUploadAsync() {
        lessonRequestDto.setVideoType(LessonVideoType.VIDEO_FILE);
        lessonRequestDto.setVideoFile(new MockMultipartFile("video", "video.mp4", "video/mp4", "video content".getBytes()));
        when(userService.getCurrentUser()).thenReturn(user);
        when(lessonRepository.findByTitleIgnoreCaseAndChapterId(anyString(), anyLong())).thenReturn(Optional.empty());
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));
        when(lessonRequestMapper.mapTo(any(LessonRequestDto.class))).thenReturn(lesson);
        when(gcsService.uploadVideo(any())).thenReturn(CompletableFuture.completedFuture("videos/lesson-video.mp4"));

        String result = lessonService.addLesson(lessonRequestDto);

        assertEquals("Add lesson successfully! We will notify you when the video has been successfully uploaded", result);
        verify(lessonRepository, times(2)).save(any(Lesson.class)); // Initial save
        verify(gcsService).uploadVideo(any());
    }

    @Test
    void addLessonWithShortTitleShouldThrowException() {
        lessonRequestDto.setTitle("Short");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> lessonService.addLesson(lessonRequestDto));
        assertEquals("Lesson title must be at least 10 characters long (excluding extra spaces): Short", exception.getMessage());
    }

    @Test
    void addLessonWithDuplicateTitleShouldThrowException() {
        when(lessonRepository.findByTitleIgnoreCaseAndChapterId(anyString(), anyLong())).thenReturn(Optional.of(lesson));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> lessonService.addLesson(lessonRequestDto));
        assertEquals("Lesson title already exists: Valid Lesson Title", exception.getMessage());
    }

    @Test
    void addLessonWithEmptyDescriptionShouldThrowException() {
        lessonRequestDto.setDescription("   ");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> lessonService.addLesson(lessonRequestDto));
        assertEquals("Lesson description cannot be empty or contain only whitespace", exception.getMessage());
    }

    @Test
    void editLessonShouldSucceed() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(lessonRepository.findByTitleIgnoreCaseAndIdNotAndChapterId(anyString(), anyLong(), anyLong())).thenReturn(Optional.empty());
//        when(lessonRequestMapper.updateFromDto(any(LessonRequestDto.class), any(Lesson.class))).thenReturn(lesson);

        String result = lessonService.editLesson(1L, lessonRequestDto);

        assertEquals("Edit lesson successfully!", result);
        verify(lessonRepository).save(lesson);
    }

    @Test
    void editLessonWithVideoFileShouldSucceedAndUploadAsync() {
        lessonRequestDto.setVideoType(LessonVideoType.VIDEO_FILE);
        lessonRequestDto.setVideoFile(new MockMultipartFile("video", "video.mp4", "video/mp4", "video content".getBytes()));
        when(userService.getCurrentUser()).thenReturn(user);
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(lessonRepository.findByTitleIgnoreCaseAndIdNotAndChapterId(anyString(), anyLong(), anyLong())).thenReturn(Optional.empty());
//        when(lessonRequestMapper.updateFromDto(any(LessonRequestDto.class), any(Lesson.class))).thenReturn(lesson);
        when(gcsService.uploadVideo(any())).thenReturn(CompletableFuture.completedFuture("videos/lesson-video.mp4"));

        String result = lessonService.editLesson(1L, lessonRequestDto);

        assertEquals("Edit lesson successfully! We will notify you when the video has been successfully uploaded", result);
        verify(lessonRepository, times(3)).save(lesson); // Initial save and async save
    }

    @Test
    void editLessonWithInvalidIdShouldThrowNotFoundException() {
        when(lessonRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> lessonService.editLesson(1L, lessonRequestDto));
        assertEquals("Lesson not found", exception.getMessage());
    }

    @Test
    void editLessonWithShortDescriptionShouldThrowException() {
        lessonRequestDto.setDescription("Short");
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> lessonService.editLesson(1L, lessonRequestDto));
        assertEquals("Lesson description must be at least 10 characters long (excluding extra spaces): Short", exception.getMessage());
    }

    @Test
    void markLessonAsCompletedShouldSucceed() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));

        lessonService.markLessonAsCompleted(1L);

        verify(userLessonProgressRepository).save(any(UserLessonProgress.class));
    }

    @Test
    void markLessonAsCompletedWithInvalidIdShouldThrowNotFoundException() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(lessonRepository.findById(eq(1L))).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> lessonService.markLessonAsCompleted(1L));
        assertEquals("Lesson not found", exception.getMessage());
    }

    @Test
    void downloadFileShouldReturnFile() {
        String key = "lessons/file.txt";
        byte[] fileContent = "file content".getBytes();
        ResponseBytes<GetObjectResponse> responseBytes = ResponseBytes.fromByteArray(
                GetObjectResponse.builder().contentType("text/plain").build(), fileContent);
        when(s3Client.getObjectAsBytes(any(GetObjectRequest.class))).thenReturn(responseBytes);

        ResponseEntity<byte[]> result = lessonService.downloadFile(key);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertArrayEquals(fileContent, result.getBody());
        assertEquals(MediaType.parseMediaType("text/plain"), result.getHeaders().getContentType());
        assertEquals("attachment; filename=\"lessons/file.txt\"", result.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
    }

    @Test
    void downloadFileWithInvalidKeyShouldReturnNotFound() {
        when(s3Client.getObjectAsBytes(any(GetObjectRequest.class))).thenThrow(new RuntimeException("Not found"));

        ResponseEntity<byte[]> result = lessonService.downloadFile("invalid-key");

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
}
