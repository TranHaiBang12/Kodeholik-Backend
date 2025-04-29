package com.g44.kodeholik.service.course.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.exception.ForbiddenException;
import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.course.CourseRequestDto;
import com.g44.kodeholik.model.dto.request.course.search.CourseSortField;
import com.g44.kodeholik.model.dto.request.course.search.SearchCourseRequestDto;
import com.g44.kodeholik.model.dto.response.course.CourseDetailResponseDto;
import com.g44.kodeholik.model.dto.response.course.CourseResponseDto;
import com.g44.kodeholik.model.dto.response.course.EnrolledUserResponseDto;
import com.g44.kodeholik.model.dto.response.course.overview.CourseOverviewReportDto;
import com.g44.kodeholik.model.entity.course.*;
import com.g44.kodeholik.model.entity.setting.Topic;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.course.ChapterStatus;
import com.g44.kodeholik.model.enums.course.CourseStatus;
import com.g44.kodeholik.model.enums.course.LessonStatus;
import com.g44.kodeholik.model.enums.user.UserRole;
import com.g44.kodeholik.repository.course.*;
import com.g44.kodeholik.repository.setting.TopicRepository;
import com.g44.kodeholik.service.aws.s3.S3Service;
import com.g44.kodeholik.service.email.EmailService;
import com.g44.kodeholik.service.setting.TopicService;
import com.g44.kodeholik.service.user.UserService;
import com.g44.kodeholik.util.mapper.request.course.CourseRequestMapper;
import com.g44.kodeholik.util.mapper.response.course.CourseDetailResponseMapper;
import com.g44.kodeholik.util.mapper.response.course.CourseResponseMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @InjectMocks
    private CourseServiceImpl courseService;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseRequestMapper courseRequestMapper;

    @Mock
    private CourseResponseMapper courseResponseMapper;

    @Mock
    private CourseDetailResponseMapper courseDetailResponseMapper;

    @Mock
    private UserService userService;

    @Mock
    private UserLessonProgressRepository userLessonProgressRepository;

    @Mock
    private CourseUserRepository courseUserRepository;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private TopicService topicService;

    @Mock
    private S3Service s3Service;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private TopCourseRepository topCourseRepository;

    @Mock
    private EmailService emailService;

    private Course course;
    private CourseRequestDto courseRequestDto;
    private Users user;
    private Chapter chapter;
    private Lesson lesson;
    private Topic topic;

    @BeforeEach
    void setUp() {
        user = new Users();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setFullname("Test User");
        user.setRole(UserRole.STUDENT);

        course = new Course();
        course.setId(1L);
        course.setTitle("Valid Course Title");
        course.setDescription("Valid Course Description");
        course.setStatus(CourseStatus.ACTIVATED);
        course.setNumberOfParticipant(0);
        course.setRate(0.0);
        course.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        course.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        course.setCreatedBy(user);
        course.setUpdatedBy(user);

        chapter = new Chapter();
        chapter.setId(1L);
        chapter.setStatus(ChapterStatus.ACTIVATED);
        chapter.setDisplayOrder(1);

        lesson = new Lesson();
        lesson.setId(1L);
        lesson.setStatus(LessonStatus.ACTIVATED);
        lesson.setDisplayOrder(1);

        topic = new Topic();
        topic.setId(1L);
        topic.setName("Programming");

        courseRequestDto = new CourseRequestDto();
        courseRequestDto.setTitle("Valid Course Title");
        courseRequestDto.setDescription("Valid Course Description");
        courseRequestDto.setStatus(CourseStatus.ACTIVATED);
        courseRequestDto.setTopicIds(new HashSet<>(Arrays.asList(1L)));
        courseRequestDto.setImageFile(null);
    }

    @Test
    void getCourseByIdShouldReturnCourseDetail() {
        chapter.setLessons(Collections.singletonList(lesson));
        course.setChapters(Collections.singletonList(chapter));
        List<Long> completedLessons = Collections.singletonList(1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userService.getCurrentUser()).thenReturn(user);
        when(courseDetailResponseMapper.mapFromCourseAndLesson(any(Course.class), anyList(), anyList()))
                .thenReturn(new CourseDetailResponseDto());

        CourseDetailResponseDto result = courseService.getCourseById(1L);

        assertNotNull(result);
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void getCourseByIdWithNoChaptersShouldReturnCourseDetail() {
        course.setChapters(Collections.emptyList());

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseDetailResponseMapper.mapFromCourseAndLesson(any(Course.class), anyList(), anyList()))
                .thenReturn(new CourseDetailResponseDto());

        CourseDetailResponseDto result = courseService.getCourseById(1L);

        assertNotNull(result);
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void getCourseByIdNotFoundShouldThrowException() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> courseService.getCourseById(1L));
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void addCourseShouldSaveCourse() {
        when(courseRepository.findByTitleIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(topicService.getTopicsByIds(anySet())).thenReturn(Collections.singleton(topic));
        when(userService.getCurrentUser()).thenReturn(user);

        courseService.addCourse(courseRequestDto);

        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void addCourseWithMinTitleLengthShouldSave() {
        courseRequestDto.setTitle("Ten chars!"); // Exactly 10 chars
        when(courseRepository.findByTitleIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(topicService.getTopicsByIds(anySet())).thenReturn(Collections.singleton(topic));
        when(userService.getCurrentUser()).thenReturn(user);

        courseService.addCourse(courseRequestDto);

        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void addCourseWithMaxTitleLengthShouldSave() {
        String maxTitle = "A".repeat(200); // Exactly 200 chars
        courseRequestDto.setTitle(maxTitle);
        when(courseRepository.findByTitleIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(topicService.getTopicsByIds(anySet())).thenReturn(Collections.singleton(topic));
        when(userService.getCurrentUser()).thenReturn(user);

        courseService.addCourse(courseRequestDto);

        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void addCourseWithMinDescriptionLengthShouldSave() {
        courseRequestDto.setDescription("Ten chars!"); // Exactly 10 chars
        when(courseRepository.findByTitleIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(topicService.getTopicsByIds(anySet())).thenReturn(Collections.singleton(topic));
        when(userService.getCurrentUser()).thenReturn(user);

        courseService.addCourse(courseRequestDto);

        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void addCourseWithMaxDescriptionLengthShouldSave() {
        String maxDescription = "A".repeat(5000); // Exactly 5000 chars
        courseRequestDto.setDescription(maxDescription);
        when(courseRepository.findByTitleIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(topicService.getTopicsByIds(anySet())).thenReturn(Collections.singleton(topic));
        when(userService.getCurrentUser()).thenReturn(user);

        courseService.addCourse(courseRequestDto);

        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void addCourseWithBlankTitleShouldThrowException() {
        courseRequestDto.setTitle("   "); // Blank title
        assertThrows(BadRequestException.class, () -> courseService.addCourse(courseRequestDto),
                "MSG02");
    }

    @Test
    void addCourseWithShortTitleShouldThrowException() {
        courseRequestDto.setTitle("Short"); // 5 chars
        assertThrows(BadRequestException.class, () -> courseService.addCourse(courseRequestDto),
                "MSG34");
    }

    @Test
    void addCourseWithLongTitleShouldThrowException() {
        courseRequestDto.setTitle("A".repeat(201)); // 201 chars
        assertThrows(BadRequestException.class, () -> courseService.addCourse(courseRequestDto),
                "MSG34");
    }

    @Test
    void addCourseWithBlankDescriptionShouldThrowException() {
        courseRequestDto.setDescription("   "); // Blank description
        assertThrows(BadRequestException.class, () -> courseService.addCourse(courseRequestDto),
                "MSG02");
    }

    @Test
    void addCourseWithShortDescriptionShouldThrowException() {
        courseRequestDto.setDescription("Short"); // 5 chars
        assertThrows(BadRequestException.class, () -> courseService.addCourse(courseRequestDto),
                "MSG29");
    }

    @Test
    void addCourseWithLongDescriptionShouldThrowException() {
        courseRequestDto.setDescription("A".repeat(5001)); // 5001 chars
        assertThrows(BadRequestException.class, () -> courseService.addCourse(courseRequestDto),
                "MSG29");
    }

    @Test
    void addCourseWithDuplicateTitleShouldThrowException() {
        when(courseRepository.findByTitleIgnoreCase(anyString())).thenReturn(Optional.of(course));

        assertThrows(BadRequestException.class, () -> courseService.addCourse(courseRequestDto));
    }

    @Test
    void addCourseWithMissingTopicIdsShouldThrowException() {
        when(courseRepository.findByTitleIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(topicService.getTopicsByIds(anySet())).thenReturn(Collections.emptySet());

        assertThrows(BadRequestException.class, () -> courseService.addCourse(courseRequestDto));
    }

    @Test
    void addCourseWithEmptyTopicIdsShouldThrowException() {
        courseRequestDto.setTopicIds(new HashSet<>());
        assertThrows(BadRequestException.class, () -> courseService.addCourse(courseRequestDto));
    }

    @Test
    void editCourseShouldUpdateCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.findByTitleIgnoreCaseAndIdNot(anyString(), anyLong())).thenReturn(Optional.empty());
        when(topicService.getTopicsByIds(anySet())).thenReturn(Collections.singleton(topic));
        when(userService.getCurrentUser()).thenReturn(user);

        courseService.editCourse(1L, courseRequestDto);

        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void editCourseWithMinTitleLengthShouldSave() {
        courseRequestDto.setTitle("Ten chars!");
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.findByTitleIgnoreCaseAndIdNot(anyString(), anyLong())).thenReturn(Optional.empty());
        when(topicService.getTopicsByIds(anySet())).thenReturn(Collections.singleton(topic));
        when(userService.getCurrentUser()).thenReturn(user);

        courseService.editCourse(1L, courseRequestDto);

        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void editCourseWithMaxDescriptionLengthShouldSave() {
        courseRequestDto.setDescription("A".repeat(5000));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.findByTitleIgnoreCaseAndIdNot(anyString(), anyLong())).thenReturn(Optional.empty());
        when(topicService.getTopicsByIds(anySet())).thenReturn(Collections.singleton(topic));
        when(userService.getCurrentUser()).thenReturn(user);

        courseService.editCourse(1L, courseRequestDto);

        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void editCourseNotFoundShouldThrowException() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> courseService.editCourse(1L, courseRequestDto));
    }

    @Test
    void editCourseWithBlankTitleShouldThrowException() {
        courseRequestDto.setTitle("   ");

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> courseService.editCourse(1L, courseRequestDto));
        assertEquals("Course title must be at least 10 characters long (excluding extra spaces): ",
                exception.getMessage());
    }

    @Test
    void editCourseWithShortTitleShouldThrowException() {
        courseRequestDto.setTitle("Short"); // 5 chars

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> courseService.editCourse(1L, courseRequestDto));
        assertEquals("Course title must be at least 10 characters long (excluding extra spaces): Short",
                exception.getMessage());
    }

    @Test
    void editCourseWithBlankDescriptionShouldThrowException() {
        courseRequestDto.setDescription("   ");

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> courseService.editCourse(1L, courseRequestDto));
        assertEquals("Course description must be at least 10 characters long (excluding extra spaces): " +
                        courseRequestDto.getTitle().trim().replaceAll("[ ]+", " "),
                exception.getMessage());
    }

    @Test
    void editCourseWithShortDescriptionShouldThrowException() {
        courseRequestDto.setDescription("Short"); // 5 chars

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> courseService.editCourse(1L, courseRequestDto));
        assertEquals("Course description must be at least 10 characters long (excluding extra spaces): " +
                        courseRequestDto.getTitle().trim().replaceAll("[ ]+", " "),
                exception.getMessage());
    }

    @Test
    void editCourseWithDuplicateTitleShouldThrowException() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.findByTitleIgnoreCaseAndIdNot(anyString(), anyLong())).thenReturn(Optional.of(course));

        assertThrows(BadRequestException.class, () -> courseService.editCourse(1L, courseRequestDto));
    }
    @Test
    void searchCoursesWithTitleAndTopicsShouldReturnPage() {
        SearchCourseRequestDto request = new SearchCourseRequestDto();
        request.setTitle("test");
        request.setTopics(Arrays.asList("Programming"));
        Page<Course> coursePage = new PageImpl<>(Collections.singletonList(course));

        when(topicRepository.findByNameIn(anyList())).thenReturn(Collections.singleton(topic));
        when(userService.getCurrentUser()).thenReturn(user);
        when(userLessonProgressRepository.findByUserId(1L)).thenReturn(Collections.emptyList());
        when(courseRepository.findByTitleContainingIgnoreCaseAndTopicsInAndStatusIn(anyString(), anyList(), anyList(), any(Pageable.class)))
                .thenReturn(coursePage);
        when(courseResponseMapper.mapFromCourseAndLesson(any(Course.class), anyList())).thenReturn(new CourseResponseDto());

        Page<CourseResponseDto> result = courseService.searchCourses(request, 0, 10, CourseSortField.title, true);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void searchCoursesWithEmptyTitleAndTopicsShouldReturnPage() {
        SearchCourseRequestDto request = new SearchCourseRequestDto();
        Page<Course> coursePage = new PageImpl<>(Collections.emptyList());

        when(userService.getCurrentUser()).thenReturn(user);
        when(userLessonProgressRepository.findByUserId(1L)).thenReturn(Collections.emptyList());
        when(courseRepository.findByStatusIn(anyList(), any(Pageable.class))).thenReturn(coursePage);

        Page<CourseResponseDto> result = courseService.searchCourses(request, 0, 10, CourseSortField.title, true);

        assertNotNull(result);
        assertEquals(0, result.getContent().size());
    }

    @Test
    void searchCoursesAsAdminShouldReturnAllStatuses() {
        SearchCourseRequestDto request = new SearchCourseRequestDto();
        Page<Course> coursePage = new PageImpl<>(Collections.singletonList(course));
        user.setRole(UserRole.ADMIN);

        when(userService.getCurrentUser()).thenReturn(user);
        when(userLessonProgressRepository.findByUserId(1L)).thenReturn(Collections.emptyList());
        when(courseRepository.findByStatusIn(anyList(), any(Pageable.class))).thenReturn(coursePage);
        when(courseResponseMapper.mapFromCourseAndLesson(any(Course.class), anyList())).thenReturn(new CourseResponseDto());

        Page<CourseResponseDto> result = courseService.searchCourses(request, 0, 10, CourseSortField.title, true);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void getEnrolledCourseByUserIdShouldReturnPage() {
        CourseUser courseUser = new CourseUser(course, user);
        Page<CourseUser> courseUserPage = new PageImpl<>(Collections.singletonList(courseUser));

        when(userService.getCurrentUser()).thenReturn(user);
        when(courseUserRepository.findByUserId(eq(1L), any(Pageable.class))).thenReturn(courseUserPage);
        when(courseResponseMapper.mapToCourseResponseDto(any(CourseUser.class), anyLong())).thenReturn(new CourseResponseDto());

        Page<CourseResponseDto> result = courseService.getEnrolledCourseByUserId(0, 10, "progress", "asc");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void getEnrolledCourseByUserIdWithNoCoursesShouldReturnEmptyPage() {
        Page<CourseUser> courseUserPage = new PageImpl<>(Collections.emptyList());

        when(userService.getCurrentUser()).thenReturn(user);
        when(courseUserRepository.findByUserId(eq(1L), any(Pageable.class))).thenReturn(courseUserPage);

        Page<CourseResponseDto> result = courseService.getEnrolledCourseByUserId(0, 10, "title", "asc");

        assertNotNull(result);
        assertEquals(0, result.getContent().size());
    }

    @Test
    void enrollUserInCourseShouldSaveCourseUser() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userService.getCurrentUser()).thenReturn(user);
        when(courseUserRepository.existsByCourseAndUser(any(Course.class), any(Users.class))).thenReturn(false);

        courseService.enrollUserInCourse(1L);

        verify(courseUserRepository, times(1)).save(any(CourseUser.class));
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void enrollUserInCourseNotFoundShouldThrowException() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> courseService.enrollUserInCourse(1L));
    }

    @Test
    void enrollUserInCourseAlreadyEnrolledShouldThrowException() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userService.getCurrentUser()).thenReturn(user);
        when(courseUserRepository.existsByCourseAndUser(any(Course.class), any(Users.class))).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> courseService.enrollUserInCourse(1L));
    }

    @Test
    void unenrollUserFromCourseShouldDeleteCourseUser() {
        CourseUser courseUser = new CourseUser(course, user);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userService.getCurrentUser()).thenReturn(user);
        when(courseUserRepository.findByCourseAndUser(any(Course.class), any(Users.class))).thenReturn(Optional.of(courseUser));

        courseService.unenrollUserFromCourse(1L);

        verify(courseUserRepository, times(1)).delete(any(CourseUser.class));
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void unenrollUserFromCourseNotFoundShouldThrowException() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> courseService.unenrollUserFromCourse(1L));
    }

    @Test
    void unenrollUserFromCourseNotEnrolledShouldThrowException() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userService.getCurrentUser()).thenReturn(user);
        when(courseUserRepository.findByCourseAndUser(any(Course.class), any(Users.class))).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> courseService.unenrollUserFromCourse(1L));
    }

    @Test
    void isUserEnrolledShouldReturnTrue() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userService.getCurrentUser()).thenReturn(user);
        when(courseUserRepository.existsByCourseAndUser(any(Course.class), any(Users.class))).thenReturn(true);

        boolean result = courseService.isUserEnrolled(1L);

        assertTrue(result);
    }

    @Test
    void isUserEnrolledShouldReturnFalse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userService.getCurrentUser()).thenReturn(user);
        when(courseUserRepository.existsByCourseAndUser(any(Course.class), any(Users.class))).thenReturn(false);

        boolean result = courseService.isUserEnrolled(1L);

        assertFalse(result);
    }

    @Test
    void isUserEnrolledCourseNotFoundShouldThrowException() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> courseService.isUserEnrolled(1L));
    }

//    @Test
//    void addTop5PopularCourseShouldSaveCourses() {
//        List<Course> courses = Arrays.asList(course, course, course, course, course, course);
//        when(courseRepository.findTop6ByStatusOrderByNumberOfParticipantDescRateDesc(CourseStatus.ACTIVATED)).thenReturn(courses);
//
//        courseService.addTop5PopularCourse();
//
//        verify(topCourseRepository, times(1)).deleteAll();
//        verify(topCourseRepository, times(6)).save(any(TopCourse.class));
//    }
//
//    @Test
//    void addTop5PopularCourseWithFewCoursesShouldSaveCourses() {
//        List<Course> courses = Arrays.asList(course, course);
//        when(courseRepository.findTop6ByStatusOrderByNumberOfParticipantDescRateDesc(CourseStatus.ACTIVATED)).thenReturn(courses);
//
//        courseService.addTop5PopularCourse();
//
//        verify(topCourseRepository, times(1)).deleteAll();
//        verify(topCourseRepository, times(2)).save(any(TopCourse.class));
//    }
//
//    @Test
//    void getTop5PopularCourseShouldReturnList() {
//        TopCourse topCourse = new TopCourse();
//        topCourse.setCourse(course);
//        List<TopCourse> topCourses = Collections.singletonList(topCourse);
//
//        when(topCourseRepository.findByCourseStatusOrderByDisplayOrderDesc(CourseStatus.ACTIVATED)).thenReturn(topCourses);
//        when(courseResponseMapper.mapFrom(any(Course.class))).thenReturn(new CourseResponseDto());
//
//        List<CourseResponseDto> result = courseService.getTop5PopularCourse();
//
//        assertNotNull(result);
//        assertEquals(1, result.size());
//    }
//
//    @Test
//    void getTop5PopularCourseWithNoCoursesShouldReturnEmptyList() {
//        when(topCourseRepository.findByCourseStatusOrderByDisplayOrderDesc(CourseStatus.ACTIVATED)).thenReturn(Collections.emptyList());
//
//        List<CourseResponseDto> result = courseService.getTop5PopularCourse();
//
//        assertNotNull(result);
//        assertEquals(0, result.size());
//    }
//
//    @Test
//    void registerStartTimeShouldUpdateCourseUser() {
//        CourseUser courseUser = new CourseUser(course, user);
//        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
//        when(userService.getCurrentUser()).thenReturn(user);
//        when(courseUserRepository.findByCourseAndUser(any(Course.class), any(Users.class))).thenReturn(Optional.of(courseUser));
//
//        courseService.registerStartTime(1L);
//
//        verify(courseUserRepository, times(1)).save(any(CourseUser.class));
//    }
//
//    @Test
//    void registerStartTimeCourseNotFoundShouldThrowException() {
//        when(courseRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class, () -> courseService.registerStartTime(1L));
//    }
//
//    @Test
//    void registerStartTimeUserNotEnrolledShouldThrowException() {
//        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
//        when(userService.getCurrentUser()).thenReturn(user);
//        when(courseUserRepository.findByCourseAndUser(any(Course.class), any(Users.class))).thenReturn(Optional.empty());
//
//        assertThrows(ForbiddenException.class, () -> courseService.registerStartTime(1L));
//    }
//
//    @Test
//    void registerEndTimeSameDayShouldUpdateStudyTime() {
//        CourseUser courseUser = new CourseUser(course, user);
//        courseUser.setLastStudiedStartAt(Timestamp.from(Instant.now().minusSeconds(600))); // 10 minutes ago
//        courseUser.setStudyTime(0L); // Initialize studyTime to 0
//        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
//        when(userService.getCurrentUser()).thenReturn(user);
//        when(courseUserRepository.findByCourseAndUser(any(Course.class), any(Users.class))).thenReturn(Optional.of(courseUser));
//
//        courseService.registerEndTime(1L);
//
//        verify(courseUserRepository, times(1)).save(any(CourseUser.class));
//    }
//
//    @Test
//    void registerEndTimeDifferentDayShouldUpdateStudyTime() {
//        CourseUser courseUser = new CourseUser(course, user);
//        courseUser.setLastStudiedStartAt(Timestamp.from(Instant.now().minus(Duration.ofDays(1))));
//        courseUser.setStudyTime(0L); // Initialize studyTime to 0
//        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
//        when(userService.getCurrentUser()).thenReturn(user);
//        when(courseUserRepository.findByCourseAndUser(any(Course.class), any(Users.class))).thenReturn(Optional.of(courseUser));
//
//        courseService.registerEndTime(1L);
//
//        verify(courseUserRepository, times(1)).save(any(CourseUser.class));
//    }
//
//    @Test
//    void registerEndTimeCourseNotFoundShouldThrowException() {
//        when(courseRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class, () -> courseService.registerEndTime(1L));
//    }
//
//    @Test
//    void sendEmailBasedOnStudyStreakAchieveStreakShouldSendEmail() {
//        CourseUser courseUser = new CourseUser(course, user);
//        courseUser.setLastStudiedStartAt(Timestamp.from(Instant.now().minus(Duration.ofDays(1))));
//        courseUser.setLastStudiedEndAt(Timestamp.from(Instant.now().minus(Duration.ofDays(1)).plusSeconds(600))); // 10 minutes
//        courseUser.setStudyTime(10L);
//
//        when(courseUserRepository.findAll()).thenReturn(Collections.singletonList(courseUser));
//
//        courseService.sendEmailBasedOnStudyStreak();
//
//        verify(emailService, times(1)).sendEmailRemindLearning(anyString(), anyString(), anyString(), anyString());
//    }
//
//    @Test
//    void sendEmailBasedOnStudyStreakLoseStreakShouldSendEmail() {
//        CourseUser courseUser = new CourseUser(course, user);
//        courseUser.setLastStudiedStartAt(Timestamp.from(Instant.now().minus(Duration.ofDays(2))));
//
//        when(courseUserRepository.findAll()).thenReturn(Collections.singletonList(courseUser));
//
//        courseService.sendEmailBasedOnStudyStreak();
//
//        verify(emailService, times(1)).sendEmailRemindLearning(anyString(), anyString(), anyString(), anyString());
//    }

//    @Test
//    void getEnrolledUsersWithProgressShouldReturnPage() {
//        CourseUser courseUser = new CourseUser(course, user);
//        Page<CourseUser> courseUserPage = new PageImpl<>(Collections.singletonList(courseUser));
//
//        when(courseUserRepository.findByCourseId(eq(1L), any(Pageable.class))).thenReturn(courseUserPage);
//        when(lessonRepository.findByChapter_Course_Id(1L)).thenReturn(Collections.singletonList(lesson));
//        when(userLessonProgressRepository.findByLessonChapterCourseId(1L)).thenReturn(Collections.singletonList(new UserLessonProgress(user, lesson)));
//
//        Page<EnrolledUserResponseDto> result = courseService.getEnrolledUsersWithProgress(1L, 0, 10, "progress", "asc", null);
//
//        assertNotNull(result);
//        assertEquals(1, result.getContent().size());
//    }
//
//    @Test
//    void getEnrolledUsersWithProgressWithUsernameSearchShouldReturnPage() {
//        CourseUser courseUser = new CourseUser(course, user);
//        Page<CourseUser> courseUserPage = new PageImpl<>(Collections.singletonList(courseUser));
//
//        when(courseUserRepository.findByCourseIdAndUserUsernameContaining(eq(1L), eq("test"), any(Pageable.class))).thenReturn(courseUserPage);
//        when(lessonRepository.findByChapter_Course_Id(1L)).thenReturn(Collections.emptyList());
//
//        Page<EnrolledUserResponseDto> result = courseService.getEnrolledUsersWithProgress(1L, 0, 10, "username", "asc", "test");
//
//        assertNotNull(result);
//        assertEquals(1, result.getContent().size());
//    }

    @Test
    void sendEmailBasedOnCourseProgressShouldSendEmail() {
        CourseUser courseUser = new CourseUser(course, user);
        courseUser.setFinished(false);
        courseUser.setEnrolledAt(Timestamp.from(Instant.now()));

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userService.getCurrentUser()).thenReturn(user);
        when(courseUserRepository.findByCourseAndUser(any(Course.class), any(Users.class))).thenReturn(Optional.of(courseUser));
        doNothing().when(emailService).sendEmailCompleteCourse(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyInt());

        courseService.sendEmailBasedOnCourseProgress(1L);

        verify(emailService, times(1)).sendEmailCompleteCourse(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyInt());
    }

    @Test
    void sendEmailBasedOnCourseProgressCourseNotFoundShouldThrowException() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> courseService.sendEmailBasedOnCourseProgress(1L));
    }

    @Test
    void sendEmailBasedOnCourseProgressUserNotEnrolledShouldThrowException() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userService.getCurrentUser()).thenReturn(user);
        when(courseUserRepository.findByCourseAndUser(any(Course.class), any(Users.class))).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> courseService.sendEmailBasedOnCourseProgress(1L));
    }

    @Test
    void sendEmailBasedOnCourseProgressAlreadySentShouldNotSendEmail() {
        CourseUser courseUser = new CourseUser(course, user);
        courseUser.setFinished(true);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userService.getCurrentUser()).thenReturn(user);
        when(courseUserRepository.findByCourseAndUser(any(Course.class), any(Users.class))).thenReturn(Optional.of(courseUser));

        courseService.sendEmailBasedOnCourseProgress(1L);

        verify(emailService, never()).sendEmailCompleteCourse(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyInt());
    }

//    @Test
//    void getCourseOverviewReportWithStartAndEndShouldReturnReport() {
//        Timestamp start = Timestamp.from(Instant.now().minus(Duration.ofDays(10)));
//        Timestamp end = Timestamp.from(Instant.now());
//        List<Course> courses = Collections.singletonList(course);
//
//        when(courseRepository.findByCreatedAtBetweenOrderByNumberOfParticipantDescRateDesc(start, end)).thenReturn(courses);
//
//        CourseOverviewReportDto result = courseService.getCourseOverviewReport(start, end);
//
//        assertNotNull(result);
//        assertEquals(1, result.getTotalCourseCount());
//    }
//
//    @Test
//    void getCourseOverviewReportWithoutStartAndEndShouldReturnReport() {
//        List<Course> courses = Collections.singletonList(course);
//
//        when(courseRepository.findAllByOrderByNumberOfParticipantDescRateDesc()).thenReturn(courses);
//
//        CourseOverviewReportDto result = courseService.getCourseOverviewReport(null, null);
//
//        assertNotNull(result);
//        assertEquals(1, result.getTotalCourseCount());
//    }
//
//    @Test
//    void getCourseOverviewReportWithInvalidTimestampsShouldThrowException() {
//        Timestamp start = Timestamp.from(Instant.now());
//        Timestamp end = Timestamp.from(Instant.now().minus(Duration.ofDays(10)));
//
//        assertThrows(BadRequestException.class, () -> courseService.getCourseOverviewReport(start, end));
//    }
}