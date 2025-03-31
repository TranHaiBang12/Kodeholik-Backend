package com.g44.kodeholik.service.chapter.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.course.ChapterRequestDto;
import com.g44.kodeholik.model.dto.request.course.ChapterRequestDto;
import com.g44.kodeholik.model.dto.response.course.ChapterResponseDto;
import com.g44.kodeholik.model.entity.course.Chapter;
import com.g44.kodeholik.model.entity.course.Course;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.repository.course.ChapterRepository;
import com.g44.kodeholik.repository.course.CourseRepository;
import com.g44.kodeholik.service.course.impl.ChapterServiceImpl;
import com.g44.kodeholik.service.user.UserService;
import com.g44.kodeholik.util.mapper.request.course.ChapterRequestMapper;
import com.g44.kodeholik.util.mapper.response.course.ChapterResponseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ChapterServiceImplTest {

    @InjectMocks
    private ChapterServiceImpl chapterService;

    @Mock
    private ChapterRepository chapterRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ChapterRequestMapper chapterRequestMapper;

    @Mock
    private ChapterResponseMapper chapterResponseMapper;

    @Mock
    private UserService userService;

    private Chapter chapter;
    private Course course;
    private Users user;
    private ChapterRequestDto chapterRequestDto;
    private ChapterResponseDto chapterResponseDto;

    @BeforeEach
    void setUp() {
        user = new Users();
        user.setId(1L);

        course = new Course();
        course.setId(1L);

        chapter = new Chapter();
        chapter.setId(1L);
        chapter.setCourse(course);
        chapter.setCreatedBy(user);
        chapter.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        chapterRequestDto = new ChapterRequestDto();
        chapterRequestDto.setCourseId(1L);

        chapterResponseDto = new ChapterResponseDto();
    }

    @Test
    void getAllChapterShouldReturnPagedChapters() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Chapter> chapterPage = new PageImpl<>(Collections.singletonList(chapter));

        when(chapterRepository.findAll(pageable)).thenReturn(chapterPage);
        when(chapterResponseMapper.mapFrom(any(Chapter.class))).thenReturn(chapterResponseDto);

        Page<ChapterResponseDto> result = chapterService.getAllChapter(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(chapterRepository, times(1)).findAll(pageable);
    }

    @Test
    void getChapterByIdShouldReturnChapter() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));
        when(chapterResponseMapper.mapFrom(chapter)).thenReturn(chapterResponseDto);

        ChapterResponseDto result = chapterService.getChapterById(1L);

        assertNotNull(result);
        verify(chapterRepository, times(1)).findById(1L);
    }

    @Test
    void getChapterByIdNotFoundShouldThrowException() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> chapterService.getChapterById(1L));
    }

    @Test
    void addChapterShouldSaveChapter() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userService.getCurrentUser()).thenReturn(user);
        when(chapterRequestMapper.mapTo(chapterRequestDto)).thenReturn(chapter);

        chapterService.addChapter(chapterRequestDto);

        verify(chapterRepository, times(1)).save(any(Chapter.class));
    }

    @Test
    void addChapterCourseNotFoundShouldThrowException() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> chapterService.addChapter(chapterRequestDto));
    }

    @Test
    void editChapterShouldUpdateChapter() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userService.getCurrentUser()).thenReturn(user);
        when(chapterRequestMapper.mapTo(chapterRequestDto)).thenReturn(chapter);

        chapterService.editChapter(1L, chapterRequestDto);

        verify(chapterRepository, times(1)).save(any(Chapter.class));
    }

    @Test
    void editChapterNotFoundShouldThrowException() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> chapterService.editChapter(1L, chapterRequestDto));
    }

    @Test
    void deleteChapterShouldDeleteSuccessfully() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));

        chapterService.deleteChapter(1L);

        verify(chapterRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteChapterNotFoundShouldThrowException() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> chapterService.deleteChapter(1L));
    }
}
