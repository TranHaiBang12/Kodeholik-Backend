package com.g44.kodeholik.service.lesson.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.course.LessonRequestDto;
import com.g44.kodeholik.model.dto.response.course.LessonResponseDto;
import com.g44.kodeholik.model.entity.course.Chapter;
import com.g44.kodeholik.model.entity.course.Lesson;
import com.g44.kodeholik.model.enums.course.LessonStatus;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.repository.course.ChapterRepository;
import com.g44.kodeholik.repository.course.LessonRepository;
import com.g44.kodeholik.repository.course.UserLessonProgressRepository;
import com.g44.kodeholik.service.aws.s3.S3Service;
import com.g44.kodeholik.service.course.impl.LessonServiceImpl;
import com.g44.kodeholik.service.gcs.GoogleCloudStorageService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceImplTest {

    @InjectMocks
    private LessonServiceImpl lessonService;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private ChapterRepository chapterRepository;

    @Mock
    private LessonRequestMapper lessonRequestMapper;

    @Mock
    private LessonResponseMapper lessonResponseMapper;

    @Mock
    private UserService userService;

    @Mock
    private S3Service s3Service;

    @Mock
    private GoogleCloudStorageService gcsService;

    @Mock
    private UserLessonProgressRepository userLessonProgressRepository;

    private Lesson lesson;
    private Users user;
    private Chapter chapter;
    private LessonRequestDto requestDto;
    private LessonResponseDto responseDto;

    @BeforeEach
    void setUp() {
        user = new Users();
        user.setId(1L);

        chapter = new Chapter();
        chapter.setId(1L);

        lesson = new Lesson();
        lesson.setId(1L);
        lesson.setChapter(chapter);
        lesson.setCreatedBy(user);
        lesson.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        lesson.setStatus(LessonStatus.ACTIVATED);

        requestDto = new LessonRequestDto();
        requestDto.setChapterId(1L);

        responseDto = new LessonResponseDto();
    }

    @Test
    void testGetLessonByIdSuccess() {
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(lessonResponseMapper.mapFrom(lesson)).thenReturn(responseDto);

        LessonResponseDto result = lessonService.getLessonById(1L);
        assertNotNull(result);
        verify(lessonRepository, times(1)).findById(1L);
    }

    @Test
    void testGetLessonByIdNotFound() {
        when(lessonRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> lessonService.getLessonById(1L));
    }

    @Test
    void testAddLessonSuccess() throws IOException {
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));
        when(userService.getCurrentUser()).thenReturn(user);
        when(lessonRequestMapper.mapTo(requestDto)).thenReturn(lesson);
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);

        MultipartFile mockVideoFile = mock(MultipartFile.class);
        when(mockVideoFile.isEmpty()).thenReturn(false);
        when(gcsService.uploadVideo(mockVideoFile)).thenReturn("gcs-video-url");

        requestDto.setVideoFile(mockVideoFile);
        lessonService.addLesson(requestDto);

        verify(lessonRepository, times(1)).save(any(Lesson.class));
    }

    @Test
    void testDeleteLessonByIdSuccess() {
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        lessonService.deleteLessonById(1L);
        verify(lessonRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteLessonByIdNotFound() {
        when(lessonRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> lessonService.deleteLessonById(1L));
    }
}
