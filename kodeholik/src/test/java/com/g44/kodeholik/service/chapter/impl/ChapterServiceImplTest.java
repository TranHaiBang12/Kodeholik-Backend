package com.g44.kodeholik.service.chapter.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.course.ChapterRequestDto;
import com.g44.kodeholik.model.dto.response.course.ChapterResponseDto;
import com.g44.kodeholik.model.dto.response.course.ListResponseDto;
import com.g44.kodeholik.model.entity.course.Chapter;
import com.g44.kodeholik.model.entity.course.Course;
import com.g44.kodeholik.model.enums.user.UserRole;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.course.ChapterStatus;
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
import org.springframework.data.domain.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

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
    private ListResponseDto listResponseDto;

    @BeforeEach
    void setUp() {
        user = new Users();
        user.setId(1L);
        user.setRole(UserRole.ADMIN);

        course = new Course();
        course.setId(1L);

        chapter = new Chapter();
        chapter.setId(1L);
        chapter.setTitle("Valid Chapter Title");
        chapter.setDescription("Valid Chapter Description");
        chapter.setCourse(course);
        chapter.setCreatedBy(user);
        chapter.setCreatedAt(Timestamp.from(Instant.now()));
        chapter.setStatus(ChapterStatus.ACTIVATED);
        chapter.setDisplayOrder(1);

        chapterRequestDto = new ChapterRequestDto();
        chapterRequestDto.setCourseId(1L);
        chapterRequestDto.setTitle("Valid Chapter Title");
        chapterRequestDto.setDescription("Valid Chapter Description");
        chapterRequestDto.setStatus(ChapterStatus.ACTIVATED);
        chapterRequestDto.setDisplayOrder(1);

        chapterResponseDto = new ChapterResponseDto();
        chapterResponseDto.setId(1L);
        chapterResponseDto.setTitle("Valid Chapter Title");

        listResponseDto = new ListResponseDto();
        listResponseDto.setId(1L);
        listResponseDto.setTitle("Valid Chapter Title");
    }

    // Normal Case: Retrieve all chapters with valid pagination
    @Test
    void getAllChapterShouldReturnPagedChapters() {
        Pageable pageable = PageRequest.of(0, 10);
        Sort sort = Sort.by(Sort.Direction.ASC, "displayOrder");
        Pageable sortedPageable = PageRequest.of(0, 10, sort);
        Page<Chapter> chapterPage = new PageImpl<>(Collections.singletonList(chapter));

        when(userService.getCurrentUser()).thenReturn(user);
        when(chapterRepository.findByStatusIn(anyList(), eq(sortedPageable))).thenReturn(chapterPage);
        when(chapterResponseMapper.mapFrom(any(Chapter.class))).thenReturn(chapterResponseDto);

        Page<ChapterResponseDto> result = chapterService.getAllChapter(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(chapterRepository, times(1)).findByStatusIn(anyList(), eq(sortedPageable));
    }

    // Boundary Case: Empty chapter list
    @Test
    void getAllChapterEmptyListShouldReturnEmptyPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Sort sort = Sort.by(Sort.Direction.ASC, "displayOrder");
        Pageable sortedPageable = PageRequest.of(0, 10, sort);
        Page<Chapter> emptyPage = new PageImpl<>(Collections.emptyList());

        when(userService.getCurrentUser()).thenReturn(user);
        when(chapterRepository.findByStatusIn(anyList(), eq(sortedPageable))).thenReturn(emptyPage);

        Page<ChapterResponseDto> result = chapterService.getAllChapter(pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(chapterRepository, times(1)).findByStatusIn(anyList(), eq(sortedPageable));
    }

    // Normal Case: Retrieve chapter by ID
    @Test
    void getChapterByIdShouldReturnChapter() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));
        when(chapterResponseMapper.mapFrom(chapter)).thenReturn(chapterResponseDto);

        ChapterResponseDto result = chapterService.getChapterById(1L);

        assertNotNull(result);
        assertEquals(chapterResponseDto.getId(), result.getId());
        verify(chapterRepository, times(1)).findById(1L);
    }

    // Abnormal Case: Chapter not found by ID
    @Test
    void getChapterByIdNotFoundShouldThrowException() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> chapterService.getChapterById(1L));
        verify(chapterRepository, times(1)).findById(1L);
    }

    // Normal Case: Add chapter with valid input
    @Test
    void addChapterShouldSaveChapter() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userService.getCurrentUser()).thenReturn(user);
        when(chapterRequestMapper.mapTo(chapterRequestDto)).thenReturn(chapter);
        when(chapterRepository.findByTitleIgnoreCaseAndCourseId(anyString(), anyLong())).thenReturn(Optional.empty());

        chapterService.addChapter(chapterRequestDto);

        verify(chapterRepository, times(1)).save(any(Chapter.class));
    }

    // Boundary Case: Add chapter with title exactly 10 characters
    @Test
    void addChapterWithMinTitleLengthShouldSave() {
        chapterRequestDto.setTitle("Ten chars!!"); // Exactly 10 chars after normalization
        chapter.setTitle("Ten chars!!");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userService.getCurrentUser()).thenReturn(user);
        when(chapterRequestMapper.mapTo(chapterRequestDto)).thenReturn(chapter);
        when(chapterRepository.findByTitleIgnoreCaseAndCourseId(anyString(), anyLong())).thenReturn(Optional.empty());

        chapterService.addChapter(chapterRequestDto);

        verify(chapterRepository, times(1)).save(any(Chapter.class));
    }

    // Boundary Case: Add chapter with title exactly 200 characters
    @Test
    void addChapterWithMaxTitleLengthShouldSave() {
        String maxTitle = "A".repeat(200); // 200 chars
        chapterRequestDto.setTitle(maxTitle);
        chapter.setTitle(maxTitle);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userService.getCurrentUser()).thenReturn(user);
        when(chapterRequestMapper.mapTo(chapterRequestDto)).thenReturn(chapter);
        when(chapterRepository.findByTitleIgnoreCaseAndCourseId(anyString(), anyLong())).thenReturn(Optional.empty());

        chapterService.addChapter(chapterRequestDto);

        verify(chapterRepository, times(1)).save(any(Chapter.class));
    }

    // Abnormal Case: Add chapter with title less than 10 characters
    @Test
    void addChapterWithShortTitleShouldThrowException() {
        chapterRequestDto.setTitle("Short"); // Less than 10 chars

        assertThrows(BadRequestException.class, () -> chapterService.addChapter(chapterRequestDto));
        verify(chapterRepository, never()).save(any(Chapter.class));
    }

    // Abnormal Case: Add chapter with duplicate title
    @Test
    void addChapterWithDuplicateTitleShouldThrowException() {
        when(chapterRepository.findByTitleIgnoreCaseAndCourseId(anyString(), anyLong())).thenReturn(Optional.of(chapter));

        assertThrows(BadRequestException.class, () -> chapterService.addChapter(chapterRequestDto));
        verify(chapterRepository, never()).save(any(Chapter.class));
    }

    // Abnormal Case: Add chapter with empty description
    @Test
    void addChapterWithEmptyDescriptionShouldThrowException() {
        chapterRequestDto.setDescription("   "); // Whitespace only

        assertThrows(BadRequestException.class, () -> chapterService.addChapter(chapterRequestDto));
        verify(chapterRepository, never()).save(any(Chapter.class));
    }

    // Abnormal Case: Add chapter with course not found
    @Test
    void addChapterCourseNotFoundShouldThrowException() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());
        when(chapterRequestMapper.mapTo(chapterRequestDto)).thenReturn(chapter); // Mock mapper to return a valid Chapter

        assertThrows(NotFoundException.class, () -> chapterService.addChapter(chapterRequestDto));
        verify(chapterRepository, never()).save(any(Chapter.class));
    }

    // Normal Case: Edit chapter with valid input
    @Test
    void editChapterShouldUpdateChapter() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userService.getCurrentUser()).thenReturn(user);
        when(chapterRequestMapper.mapTo(chapterRequestDto)).thenReturn(chapter);
        when(chapterRepository.findByTitleIgnoreCaseAndIdNotAndCourseId(anyString(), anyLong(), anyLong())).thenReturn(Optional.empty());

        chapterService.editChapter(1L, chapterRequestDto);

        verify(chapterRepository, times(1)).save(any(Chapter.class));
    }

    // Boundary Case: Edit chapter with description exactly 5000 characters
    @Test
    void editChapterWithMaxDescriptionLengthShouldSave() {
        String maxDescription = "A".repeat(5000); // 5000 chars
        chapterRequestDto.setDescription(maxDescription);
        chapter.setDescription(maxDescription);

        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userService.getCurrentUser()).thenReturn(user);
        when(chapterRequestMapper.mapTo(chapterRequestDto)).thenReturn(chapter);
        when(chapterRepository.findByTitleIgnoreCaseAndIdNotAndCourseId(anyString(), anyLong(), anyLong())).thenReturn(Optional.empty());

        chapterService.editChapter(1L, chapterRequestDto);

        verify(chapterRepository, times(1)).save(any(Chapter.class));
    }

    // Abnormal Case: Edit chapter with title already exists
    @Test
    void editChapterWithDuplicateTitleShouldThrowException() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));
        when(chapterRepository.findByTitleIgnoreCaseAndIdNotAndCourseId(anyString(), anyLong(), anyLong())).thenReturn(Optional.of(chapter));

        assertThrows(BadRequestException.class, () -> chapterService.editChapter(1L, chapterRequestDto));
        verify(chapterRepository, never()).save(any(Chapter.class));
    }

    // Abnormal Case: Edit chapter not found
    @Test
    void editChapterNotFoundShouldThrowException() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> chapterService.editChapter(1L, chapterRequestDto));
        verify(chapterRepository, never()).save(any(Chapter.class));
    }

    // Normal Case: Get chapters by course ID
    @Test
    void getChapterByCourseIdShouldReturnChapters() {
        Sort sort = Sort.by(Sort.Direction.ASC, "displayOrder");
        List<Chapter> chapters = Collections.singletonList(chapter);

        when(userService.getCurrentUser()).thenReturn(user);
        when(chapterRepository.findByCourseIdAndStatusIn(eq(1L), anyList(), eq(sort)))
                .thenReturn(chapters);
        when(chapterResponseMapper.mapDetailFrom(any(Chapter.class))).thenReturn(chapterResponseDto);

        List<ChapterResponseDto> result = chapterService.getChapterByCourseId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(chapterRepository, times(1)).findByCourseIdAndStatusIn(eq(1L), anyList(), eq(sort));
    }

    // Boundary Case: Get chapters by course ID with no chapters
    @Test
    void getChapterByCourseIdNoChaptersShouldReturnEmptyList() {
        Sort sort = Sort.by(Sort.Direction.ASC, "displayOrder");

        when(userService.getCurrentUser()).thenReturn(user);
        when(chapterRepository.findByCourseIdAndStatusIn(eq(1L), anyList(), eq(sort)))
                .thenReturn(Collections.emptyList());

        List<ChapterResponseDto> result = chapterService.getChapterByCourseId(1L);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(chapterRepository, times(1)).findByCourseIdAndStatusIn(eq(1L), anyList(), eq(sort));
    }

    // Normal Case: Get list response DTO by course ID
    @Test
    void getListChapterResponseDtoByCourseIdShouldReturnList() {
        List<Chapter> chapters = Collections.singletonList(chapter);

        when(chapterRepository.findByCourseIdOrderByDisplayOrderAsc(1L)).thenReturn(chapters);

        List<ListResponseDto> result = chapterService.getListChapterResponseDtoByCourseId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(chapter.getId(), result.get(0).getId());
        assertEquals(chapter.getTitle(), result.get(0).getTitle());
        verify(chapterRepository, times(1)).findByCourseIdOrderByDisplayOrderAsc(1L);
    }

    // Boundary Case: Get list response DTO with no chapters
    @Test
    void getListChapterResponseDtoByCourseIdNoChaptersShouldReturnEmptyList() {
        when(chapterRepository.findByCourseIdOrderByDisplayOrderAsc(1L)).thenReturn(Collections.emptyList());

        List<ListResponseDto> result = chapterService.getListChapterResponseDtoByCourseId(1L);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(chapterRepository, times(1)).findByCourseIdOrderByDisplayOrderAsc(1L);
    }

    // Normal Case: Get allowed statuses for admin
    @Test
    void getAllowedStatusForAdminShouldReturnAllStatuses() {
        when(userService.getCurrentUser()).thenReturn(user);

        List<ChapterStatus> result = chapterService.getAllowedStatus();

        assertEquals(Arrays.asList(ChapterStatus.values()), result);
        verify(userService, times(1)).getCurrentUser();
    }

    // Normal Case: Get allowed statuses for student
    @Test
    void getAllowedStatusForStudentShouldReturnActivatedOnly() {
        user.setRole(UserRole.STUDENT);
        when(userService.getCurrentUser()).thenReturn(user);

        List<ChapterStatus> result = chapterService.getAllowedStatus();

        assertEquals(Collections.singletonList(ChapterStatus.ACTIVATED), result);
        verify(userService, times(1)).getCurrentUser();
    }
}