package com.g44.kodeholik.service.course.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import com.g44.kodeholik.model.dto.request.course.search.CourseSortField;
import com.g44.kodeholik.model.dto.request.course.search.SearchCourseRequestDto;
import com.g44.kodeholik.model.dto.response.course.CourseDetailResponseDto;
import com.g44.kodeholik.model.entity.course.CourseUser;
import com.g44.kodeholik.model.entity.setting.Topic;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.course.CourseStatus;
import com.g44.kodeholik.model.enums.s3.FileNameType;
import com.g44.kodeholik.repository.course.CourseUserRepository;
import com.g44.kodeholik.repository.setting.TopicRepository;
import com.g44.kodeholik.repository.user.UserRepository;
import com.g44.kodeholik.service.aws.s3.S3Service;
import com.g44.kodeholik.service.setting.TopicService;
import com.g44.kodeholik.util.mapper.response.course.CourseDetailResponseMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.course.CourseRequestDto;
import com.g44.kodeholik.model.dto.response.course.CourseResponseDto;
import com.g44.kodeholik.model.entity.course.Course;
import com.g44.kodeholik.repository.course.CourseRepository;
import com.g44.kodeholik.service.course.CourseService;
import com.g44.kodeholik.service.user.UserService;
import com.g44.kodeholik.util.mapper.request.course.CourseRequestMapper;
import com.g44.kodeholik.util.mapper.response.course.CourseResponseMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    private final CourseRepository courseRepository;

    private final CourseRequestMapper courseRequestMapper;

    private final CourseResponseMapper courseResponseMapper;

    private final CourseDetailResponseMapper courseDetailResponseMapper;

    private final UserService userService;

    private final UserRepository userRepository;

    private final CourseUserRepository courseUserRepository;

    private final TopicRepository topicRepository;

    private final TopicService topicService;

    private final S3Service s3Service;

    @Override
    public Page<CourseResponseDto> getAllCourse(Pageable pageable) {
        Page<Course> coursePage = courseRepository
                .findByStatus(CourseStatus.ACTIVATED, pageable);
        return coursePage.map(courseResponseMapper::mapFrom);
    }

    @Override
    public CourseDetailResponseDto getCourseById(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found", "Course not found"));
        return courseDetailResponseMapper.mapFrom(course);
    }

    @Override
    public void addCourse(CourseRequestDto requestDto, MultipartFile imageFile) {
        Course course = new Course();
        course.setTitle(requestDto.getTitle());
        course.setDescription(requestDto.getDescription());
        course.setStatus(requestDto.getStatus());
        course.setNumberOfParticipant(0);
        course.setRate(0.0);
        course.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        course.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        // Lấy danh sách topics
        Set<Topic> topics = topicService.getTopicsByIds(requestDto.getTopicIds());
        course.setTopics(topics);

        // Upload ảnh lên AWS S3 nếu có
        if (imageFile != null && !imageFile.isEmpty()) {
            course.setImage(s3Service.uploadFileNameTypeFile(List.of(imageFile), FileNameType.COURSE).getFirst());
        }

        // Cập nhật người tạo course
        Users currentUser = userService.getCurrentUser();
        course.setCreatedBy(currentUser);
        course.setUpdatedBy(currentUser);

        courseRepository.save(course);
    }



    @Override
    public void editCourse(Long courseId, CourseRequestDto requestDto, MultipartFile imageFile) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        // Cập nhật các thông tin course
        course.setTitle(requestDto.getTitle());
        course.setDescription(requestDto.getDescription());
        course.setStatus(requestDto.getStatus());
        course.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        // Cập nhật danh sách topics
        Set<Topic> topics = topicService.getTopicsByIds(requestDto.getTopicIds());
        course.setTopics(topics);

        // Cập nhật ảnh nếu có ảnh mới
        if (imageFile != null && !imageFile.isEmpty()) {
            course.setImage(s3Service.uploadFileNameTypeFile(List.of(imageFile), FileNameType.COURSE).getFirst());
        }

        // Cập nhật người chỉnh sửa course
        Users currentUser = userService.getCurrentUser();
        course.setUpdatedBy(currentUser);

        courseRepository.save(course);
    }



    @Override
    public void deleteCourse(Long courseId) {
        courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found", "Course not found"));
        courseRepository.deleteById(courseId);
    }

    @Override
    public Page<CourseResponseDto> searchCourses(
            SearchCourseRequestDto request, Integer page, Integer size,
            CourseSortField sortBy, Boolean ascending) {

        String title = request.getTitle() != null ? request.getTitle().trim() : "";
        List<String> topicNames = request.getTopics();

        List<Topic> topics = new ArrayList<>();
        if (topicNames != null && !topicNames.isEmpty()) {
            Set<Topic> topicSet = topicRepository.findByNameIn(topicNames);
            topics = new ArrayList<>(topicSet);
        }

        Sort.Direction direction = (ascending != null && ascending) ? Sort.Direction.ASC : Sort.Direction.DESC;

        // Xác định field để sort
        String sortField;
        switch (sortBy) {
            case createdAt:
                sortField = "createdAt";
                break;
            case numberOfParticipant:
                sortField = "numberOfParticipant";
                break;
            default:
                sortField = "title";
        }

        Sort sort = Sort.by(direction, sortField);
        Pageable pageable = PageRequest.of(page, size, sort);

        CourseStatus activeStatus = CourseStatus.ACTIVATED;

        Page<Course> courses;
        if (title.isEmpty() && topics.isEmpty()) {
            courses = courseRepository.findByStatus(activeStatus, pageable);
        } else if (!title.isEmpty() && topics.isEmpty()) {
            courses = courseRepository.findByTitleContainingIgnoreCaseAndStatus(title, activeStatus, pageable);
        } else if (title.isEmpty()) {
            courses = courseRepository.findByTopicsInAndStatus(topics, activeStatus, pageable);
        } else {
            courses = courseRepository.findByTitleContainingIgnoreCaseAndTopicsInAndStatus(title, topics, activeStatus, pageable);
        }

        return courses.map(courseResponseMapper::mapFrom);
    }

    @Transactional
    @Override
    public void enrollUserInCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        Users user = userService.getCurrentUser();

        boolean alreadyEnrolled = courseUserRepository.existsByCourseAndUser(course, user);
        if (alreadyEnrolled) {
            throw new IllegalStateException("User is already enrolled in this course.");
        }
        log.info(course);
        log.info(user);
        CourseUser courseUser = new CourseUser(course, user);
        courseUserRepository.save(courseUser);

        course.setNumberOfParticipant(course.getNumberOfParticipant() + 1);
        courseRepository.save(course);
    }

    @Transactional
    @Override
    public void unenrollUserFromCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        Users user = userService.getCurrentUser();

        CourseUser courseUser = courseUserRepository.findByCourseAndUser(course, user)
                .orElseThrow(() -> new IllegalStateException("User is not enrolled in this course."));

        courseUserRepository.delete(courseUser);
        course.setNumberOfParticipant(Math.max(course.getNumberOfParticipant() - 1, 0));
        courseRepository.save(course);
    }
}
