package com.g44.kodeholik.service.course.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.course.CourseRequestDto;
import com.g44.kodeholik.model.dto.response.course.CourseDetailResponseDto;
import com.g44.kodeholik.model.entity.course.Course;
import com.g44.kodeholik.model.entity.course.CourseUser;
import com.g44.kodeholik.model.entity.course.CourseUserId;
import com.g44.kodeholik.model.entity.setting.Topic;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.course.CourseStatus;
import com.g44.kodeholik.model.enums.user.UserRole;
import com.g44.kodeholik.repository.course.CourseRepository;
import com.g44.kodeholik.repository.course.CourseUserRepository;
import com.g44.kodeholik.repository.setting.TopicRepository;
import com.g44.kodeholik.service.aws.s3.S3Service;
import com.g44.kodeholik.service.setting.TopicService;
import com.g44.kodeholik.service.user.UserService;
import com.g44.kodeholik.util.mapper.request.course.CourseRequestMapper;
import com.g44.kodeholik.util.mapper.response.course.CourseDetailResponseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @InjectMocks
    private CourseServiceImpl courseService;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseUserRepository courseUserRepository;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private TopicService topicService;

    @Mock
    private S3Service s3Service;

    @Mock
    private UserService userService;

    @Mock
    private CourseRequestMapper courseRequestMapper;

    @Mock
    private CourseDetailResponseMapper courseDetailResponseMapper;

    private Course course;
    private Users user;
    private CourseRequestDto requestDto;
    private CourseDetailResponseDto responseDto;

    @BeforeEach
    void setUp() {
        user = new Users();
        user.setId(1L);
        user.setRole(UserRole.TEACHER);

        course = new Course();
        course.setId(1L);
        course.setTitle("Test Course");
        course.setCreatedBy(user);
        course.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        course.setNumberOfParticipant(0);

        requestDto = new CourseRequestDto();
        requestDto.setTitle("Updated Course");
        requestDto.setDescription("Updated Description");
        requestDto.setStatus(CourseStatus.ACTIVATED);
        requestDto.setTopicIds(Collections.singleton(1L));

        responseDto = new CourseDetailResponseDto();
    }

    @Test
    void testGetCourseByIdSuccess() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseDetailResponseMapper.mapFrom(course)).thenReturn(responseDto);

        CourseDetailResponseDto result = courseService.getCourseById(1L);
        assertNotNull(result);
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCourseByIdNotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> courseService.getCourseById(1L));
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void testAddCourseSuccess() {
        MultipartFile imageFile = mock(MultipartFile.class);
        when(imageFile.isEmpty()).thenReturn(false);
        when(s3Service.uploadFileNameTypeFile(anyList(), any())).thenReturn(List.of("image-url"));
        when(userService.getCurrentUser()).thenReturn(user);
        when(topicService.getTopicsByIds(requestDto.getTopicIds())).thenReturn(new HashSet<>());
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        courseService.addCourse(requestDto);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void testDeleteCourseSuccess() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        courseService.deleteCourse(1L);
        verify(courseRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCourseNotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> courseService.deleteCourse(1L));
    }

    @Test
    void testEnrollUserInCourseSuccess() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userService.getCurrentUser()).thenReturn(user);
        when(courseUserRepository.existsByCourseAndUser(course, user)).thenReturn(false);

        courseService.enrollUserInCourse(1L);

        verify(courseUserRepository, times(1)).save(any(CourseUser.class));
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testEnrollUserInCourseAlreadyEnrolled() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userService.getCurrentUser()).thenReturn(user);
        when(courseUserRepository.existsByCourseAndUser(course, user)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> courseService.enrollUserInCourse(1L));
    }
}