package com.g44.kodeholik.service.course.impl;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.g44.kodeholik.model.dto.request.course.search.CourseSortField;
import com.g44.kodeholik.model.dto.request.course.search.SearchCourseRequestDto;
import com.g44.kodeholik.model.dto.response.course.CourseDetailResponseDto;
import com.g44.kodeholik.model.dto.response.course.EnrolledUserResponseDto;
import com.g44.kodeholik.model.dto.response.user.UserResponseDto;
import com.g44.kodeholik.model.entity.course.*;
import com.g44.kodeholik.model.entity.setting.Topic;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.course.CourseStatus;
import com.g44.kodeholik.model.enums.s3.FileNameType;
import com.g44.kodeholik.model.enums.user.UserRole;
import com.g44.kodeholik.repository.course.CourseUserRepository;
import com.g44.kodeholik.repository.course.LessonRepository;
import com.g44.kodeholik.repository.course.TopCourseRepository;
import com.g44.kodeholik.repository.course.UserLessonProgressRepository;
import com.g44.kodeholik.repository.setting.TopicRepository;
import com.g44.kodeholik.repository.user.UserRepository;
import com.g44.kodeholik.service.aws.s3.S3Service;
import com.g44.kodeholik.service.setting.TopicService;
import com.g44.kodeholik.util.mapper.request.exam.AddExamRequestMapper;
import com.g44.kodeholik.util.mapper.response.course.CourseDetailResponseMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.exception.ForbiddenException;
import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.course.CourseRequestDto;
import com.g44.kodeholik.model.dto.response.course.CourseResponseDto;
import com.g44.kodeholik.repository.course.CourseRepository;
import com.g44.kodeholik.service.course.CourseService;
import com.g44.kodeholik.service.email.EmailService;
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

    private final UserLessonProgressRepository userLessonProgressRepository;

    private final CourseUserRepository courseUserRepository;

    private final TopicRepository topicRepository;

    private final TopicService topicService;

    private final S3Service s3Service;

    private final LessonRepository lessonRepository;

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

    private final TopCourseRepository topCourseRepository;

    private final EmailService emailService;

    @Override
    public CourseDetailResponseDto getCourseById(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found", "Course not found"));

        List<Chapter> chapters = course.getChapters();
        if (chapters == null || chapters.isEmpty()) {
            return courseDetailResponseMapper.mapFromCourseAndLesson(course, Collections.emptyList(),
                    Collections.emptyList());
        }

        List<Long> lessonIds = chapters.stream()
                .flatMap(chapter -> {
                    List<Lesson> lessons = chapter.getLessons();
                    return lessons != null ? lessons.stream() : Stream.empty();
                })
                .map(Lesson::getId)
                .collect(Collectors.toList());

        // Sử dụng getCompletedLessons() để lấy danh sách lesson đã hoàn thành
        List<Long> completedLessons = getCompletedLessons()
                .stream()
                .filter(lessonId -> lessonIds.contains(lessonId)) // Chỉ lấy lesson thuộc course này
                .collect(Collectors.toList());

        return courseDetailResponseMapper.mapFromCourseAndLesson(course, lessonIds, completedLessons);
    }

    @Override
    public void addCourse(CourseRequestDto requestDto) {
        Course course = new Course();
        course.setTitle(requestDto.getTitle());
        course.setDescription(requestDto.getDescription());
        course.setStatus(requestDto.getStatus());
        course.setNumberOfParticipant(0);
        course.setRate(0.0);
        course.setCreatedAt(new Timestamp(System.currentTimeMillis() + 25200000));
        course.setUpdatedAt(new Timestamp(System.currentTimeMillis() + 25200000));

        Set<Topic> topics = topicService.getTopicsByIds(requestDto.getTopicIds());
        // Kiểm tra xem tất cả topicIds có tồn tại không
        if (topics.size() != requestDto.getTopicIds().size()) {
            Set<Long> foundIds = topics.stream().map(Topic::getId).collect(Collectors.toSet());
            Set<Long> missingIds = new HashSet<>(requestDto.getTopicIds());
            missingIds.removeAll(foundIds);
            throw new IllegalArgumentException("Các topic ID không tồn tại: " + missingIds);
        }

        // Upload ảnh lên AWS S3 nếu có
        if (requestDto.getImageFile() != null && !requestDto.getImageFile().isEmpty()) {
            course.setImage(s3Service.uploadFileNameTypeFile(List.of(requestDto.getImageFile()), FileNameType.COURSE)
                    .getFirst());
        }

        // Cập nhật người tạo course
        Users currentUser = userService.getCurrentUser();
        course.setCreatedBy(currentUser);
        course.setUpdatedBy(currentUser);

        courseRepository.save(course);
    }

    @Override
    public void editCourse(Long courseId, CourseRequestDto requestDto) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        // Cập nhật các thông tin course
        course.setTitle(requestDto.getTitle());
        course.setDescription(requestDto.getDescription());
        course.setStatus(requestDto.getStatus());
        course.setUpdatedAt(new Timestamp(System.currentTimeMillis() + 25200000));

        // Cập nhật danh sách topics
        Set<Topic> topics = topicService.getTopicsByIds(requestDto.getTopicIds());
        course.setTopics(topics);

        // Cập nhật ảnh nếu có ảnh mới
        if (requestDto.getImageFile() != null && !requestDto.getImageFile().isEmpty()) {
            course.setImage(s3Service.uploadFileNameTypeFile(List.of(requestDto.getImageFile()), FileNameType.COURSE)
                    .getFirst());
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

        Users currentUser = null;
        try {
            currentUser = userService.getCurrentUser();
        } catch (Exception e) {
        }

        List<CourseStatus> allowedStatuses;
        if (currentUser == null || currentUser.getRole() == UserRole.STUDENT) {
            allowedStatuses = Collections.singletonList(CourseStatus.ACTIVATED);
        } else {
            allowedStatuses = Arrays.asList(CourseStatus.values());
        }

        Page<Course> courses;
        if (title.isEmpty() && topics.isEmpty()) {
            courses = courseRepository.findByStatusIn(allowedStatuses, pageable);
        } else if (!title.isEmpty() && topics.isEmpty()) {
            courses = courseRepository.findByTitleContainingIgnoreCaseAndStatusIn(title, allowedStatuses, pageable);
        } else if (title.isEmpty()) {
            courses = courseRepository.findByTopicsInAndStatusIn(topics, allowedStatuses, pageable);
        } else {
            courses = courseRepository.findByTitleContainingIgnoreCaseAndTopicsInAndStatusIn(title, topics,
                    allowedStatuses, pageable);
        }

        List<Long> completedLessons = currentUser != null ? getCompletedLessons() : Collections.emptyList();

        // return courses.map(course ->
        // courseResponseMapper.mapFromCourseAndLesson(course, completedLessons));
        return courses.map(course -> courseResponseMapper.mapFrom(course));
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
        CourseUserId courseUserId = new CourseUserId(course.getId(), user.getId());
        courseUser.setId(courseUserId);
        courseUser.setStudyStreak(0);
        courseUser.setStudyTime(0L);
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

    @Override
    public boolean isUserEnrolled(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        Users user = userService.getCurrentUser();

        return courseUserRepository.existsByCourseAndUser(course, user);
    }

    @Override
    public void addTop5PopularCourse() {
        List<Course> courses = courseRepository.findTop5ByOrderByNumberOfParticipantDescRateDesc();
        topCourseRepository.deleteAll();
        int top = 5;
        for (int i = 0; i < courses.size(); i++) {
            TopCourse topCourse = new TopCourse();
            topCourse.setCourse(courses.get(i));
            topCourse.setDisplayOrder(top);
            if (top > 0) {
                top--;
            }
            topCourseRepository.save(topCourse);
        }
    }

    @Override
    public List<CourseResponseDto> getTop5PopularCourse() {
        List<TopCourse> topCourses = topCourseRepository.findByOrderByDisplayOrderDesc();
        List<CourseResponseDto> result = new ArrayList();
        for (int i = 0; i < topCourses.size(); i++) {
            result.add(courseResponseMapper.mapFrom(topCourses.get(i).getCourse()));
        }
        return result;
    }

    private Course getEntityCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found", "Course not found"));
    }

    @Transactional
    @Override
    public void registerStartTime(Long courseId) {
        Users currentUser = userService.getCurrentUser();
        Course course = getEntityCourseById(courseId);
        CourseUser courseUser = courseUserRepository.findByCourseAndUser(course, currentUser)
                .orElseThrow(() -> new ForbiddenException("This user is not participated in this course",
                        "This user is not participated in this course"));
        courseUser.setLastStudiedStartAt(Timestamp.from(Instant.now()));
        log.info(courseUser);
        log.info(courseUser.getLastStudiedStartAt());
        courseUserRepository.save(courseUser);

    }

    @Transactional
    @Override
    public void registerEndTime(Long courseId) {
        Users currentUser = userService.getCurrentUser();
        Course course = getEntityCourseById(courseId);
        CourseUser courseUser = courseUserRepository.findByCourseAndUser(course, currentUser)
                .orElseThrow(() -> new ForbiddenException("This user is not participated in this course",
                        "This user is not participated in this course"));

        Timestamp now = Timestamp.from(Instant.now());
        courseUser.setLastStudiedEndAt(now);
        log.info(courseUser);
        log.info(courseUser.getLastStudiedEndAt());
        courseUser.setStudyTime(isSameDay(courseUser.getLastStudiedStartAt(),
                courseUser.getLastStudiedEndAt())
                        ? courseUser.getStudyTime()
                                + getMinuteDifference(courseUser.getLastStudiedStartAt(),
                                        courseUser.getLastStudiedEndAt())
                        : courseUser.getStudyTime()
                                +
                                getMinuteDifference(getMidnightTimestamp(courseUser.getLastStudiedStartAt()),
                                        courseUser.getLastStudiedEndAt()));
        courseUserRepository.save(courseUser);
    }

    @Override
    public void sendEmailBasedOnStudyStreak() {
        List<CourseUser> courseUsers = courseUserRepository.findAll();
        for (int i = 0; i < courseUsers.size(); i++) {
            sendEmailBasedOnStudyStreakForEachCourseUser(courseUsers.get(i));
        }
    }

    @Override
    public Page<EnrolledUserResponseDto> getEnrolledUsersWithProgress(Long courseId, int page, int size, String sortBy,
            String sortDirection, String usernameSearch) {
        Sort sort = "progress".equalsIgnoreCase(sortBy)
                ? Sort.unsorted()
                : Sort.by(sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
                        validateSortField(sortBy));

        Pageable pageable = PageRequest.of(page, size, sort);

        // Lấy danh sách CourseUser với phân trang và tìm kiếm
        Page<CourseUser> courseUsersPage;
        if (usernameSearch != null && !usernameSearch.trim().isEmpty()) {
            courseUsersPage = courseUserRepository.findByCourseIdAndUserUsernameContaining(
                    courseId, usernameSearch, pageable);
        } else {
            courseUsersPage = courseUserRepository.findByCourseId(courseId, pageable);
        }

        // Lấy tổng số lesson và progress của tất cả user trong course
        List<Lesson> lessons = lessonRepository.findByChapter_Course_Id(courseId);
        int totalLessons = lessons.size();
        List<UserLessonProgress> progresses = userLessonProgressRepository.findByLessonChapterCourseId(courseId);
        Map<Long, Long> userCompletedLessons = progresses.stream()
                .collect(Collectors.groupingBy(p -> p.getUser().getId(), Collectors.counting()));

        // Map từ CourseUser sang EnrolledUserResponseDto và tính progress
        List<EnrolledUserResponseDto> dtos = courseUsersPage.getContent().stream()
                .map(courseUser -> {
                    Long userId = courseUser.getUser().getId();
                    int completedLessons = userCompletedLessons.getOrDefault(userId, 0L).intValue();
                    double progress = totalLessons > 0 ? (completedLessons * 100.0) / totalLessons : 0.0;

                    UserResponseDto userDto = new UserResponseDto(courseUser.getUser());
                    return EnrolledUserResponseDto.builder()
                            .user(userDto)
                            .enrolledAt(
                                    courseUser.getEnrolledAt() != null ? courseUser.getEnrolledAt().getTime() : null)
                            .progress(progress)
                            .build();
                })
                .collect(Collectors.toList());

        // Sắp xếp trong bộ nhớ nếu sortBy là progress
        if ("progress".equalsIgnoreCase(sortBy)) {
            dtos.sort((a, b) -> sortDirection.equalsIgnoreCase("asc")
                    ? Double.compare(a.getProgress(), b.getProgress())
                    : Double.compare(b.getProgress(), a.getProgress()));
        }

        // Tạo Page từ danh sách đã sắp xếp
        return new PageImpl<>(dtos, pageable, courseUsersPage.getTotalElements());
    }

    @Override
    public void sendEmailBasedOnCourseProgress(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));
        Users currentUser = userService.getCurrentUser();
        String subject = "[KODEHOLIK] You completed " + course.getTitle();
        String content = "Congratulations! You have successfully completed the course: " + course.getTitle();
        emailService.sendEmailCompleteCourse(currentUser.getEmail(), subject, currentUser.getUsername(), content);
    }

    private String validateSortField(String sortBy) {
        switch (sortBy.toLowerCase()) {
            case "enrolledat":
                return "enrolledAt";
            case "username":
                return "user.username";
            case "progress":
                return "enrolledAt";
            default:
                return "enrolledAt";
        }
    }

    private void sendEmailBasedOnStudyStreakForEachCourseUser(CourseUser courseUser) {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        log.info(isSameDay(courseUser.getLastStudiedStartAt(), yesterday));
        if (courseUser.getLastStudiedStartAt() != null && isSameDay(courseUser.getLastStudiedStartAt(), yesterday)) {
            // nguoi dung da hoc xong trong hom qua
            if (courseUser.getLastStudiedEndAt() != null
                    && (courseUser.getLastStudiedEndAt().getTime() >= courseUser.getLastStudiedStartAt().getTime())) {
                // hoc du 10 phut
                if (courseUser.getStudyTime() >= 10) {
                    log.info("Hoc do roi");
                    achieveNewStudyStreak(courseUser);
                }
                // hoc chua du 10 phut
                else {
                    loseStudyStreak(courseUser);
                }
            }
            // nguoi dung chua hoc xong va van dang hoc
            else {
                long duration = courseUser.getStudyTime()
                        + getMinuteDifference(courseUser.getLastStudiedStartAt(), Timestamp.from(Instant.now()));
                // hoc hon 10 phut
                if (duration >= 10) {
                    achieveNewStudyStreak(courseUser);
                }
                // hoc chua du 10 phut
                else {
                    loseStudyStreak(courseUser);
                }
            }
        }
        // chua hoc trong hom qua
        else {
            loseStudyStreak(courseUser);
        }
        courseUser.setStudyTime(0L);
        courseUserRepository.save(courseUser);
    }

    private void achieveNewStudyStreak(CourseUser courseUser) {
        long oldStudyStreak = courseUser.getStudyStreak();
        courseUser.setStudyStreak(courseUser.getStudyStreak() > 0 ? courseUser.getStudyStreak() + 1 : 1);
        sendEmailAchieveNewStudyStreak(oldStudyStreak, courseUser.getStudyStreak(),
                courseUser.getCourse().getTitle(),
                courseUser.getUser().getFullname(), courseUser.getUser().getEmail());
    }

    private void loseStudyStreak(CourseUser courseUser) {
        long oldStudyStreak = courseUser.getStudyStreak();
        courseUser.setStudyStreak(courseUser.getStudyStreak() > 0 ? -1 : courseUser.getStudyStreak() - 1);
        sendEmailLoseStudyStreak(oldStudyStreak, courseUser.getStudyStreak(),
                courseUser.getCourse().getTitle(),
                courseUser.getUser().getFullname(), courseUser.getUser().getEmail());
    }

    private void sendEmailAchieveNewStudyStreak(long oldStudyStreak, long newStudyStreak, String courseTitle,
            String fullName, String email) {
        // nguoi dung quay tro lai hoc
        String emailContent = "";
        if (oldStudyStreak < 0) {
            emailContent = fullName + " đã đăng ký học khóa học " + courseTitle
                    + " và sau 1 thời gian thì đã quay trở lại học. Bạn có thể viết một email vui nhộn, cá nhân hóa với mục đích khen cũng như động viên "
                    + fullName
                    + " để cậu ấy tiếp tục học, bạn chỉ cần viết nội dung và có thể bỏ qua các phần chào cũng như trân trọng. Hãy viết nội dung bằng tiếng Anh nhé, giới hạn trong 120 ký tự";
        } else {
            if (newStudyStreak == 1 || newStudyStreak == 2 || newStudyStreak == 3) {
                emailContent = fullName + " đã đăng ký học khóa học " + courseTitle
                        + " và đang có chuỗi học liên tục trong " + newStudyStreak
                        + " ngày. Bạn có thể viết một email vui nhộn, cá nhân hóa với mục đích khen " + fullName
                        + " vì đã đạt được thành tích này, bạn chỉ cần viết nội dung và có thể bỏ qua các phần chào cũng như trân trọng. Hãy viết nội dung bằng tiếng Anh nhé, giới hạn trong 120 ký tự";

            } else if (newStudyStreak == 5) {
                emailContent = fullName + " đã đăng ký học khóa học " + courseTitle
                        + " và đang có chuỗi học liên tục trong " + newStudyStreak
                        + " ngày. Đây là một thành tựu lớn. Bạn có thể viết một email vui nhộn, cá nhân hóa với mục đích khen "
                        + fullName
                        + " vì đã đạt được thành tích này, bạn chỉ cần viết nội dung và có thể bỏ qua các phần chào cũng như trân trọng. Hãy viết nội dung bằng tiếng Anh nhé, giới hạn trong 120 ký tự";

            } else if (newStudyStreak == 10) {
                emailContent = fullName + " đã đăng ký học khóa học " + courseTitle
                        + " và đang có chuỗi học liên tục trong " + newStudyStreak
                        + " ngày. Đây là một thành tựu lớn mà chỉ 20-30% người dùng đạt được. Bạn có thể viết một email vui nhộn, cá nhân hóa, bày tỏ một chút ngưỡng mộ với mục đích khen "
                        + fullName
                        + " vì đã đạt được thành tích này động viên họ tiếp tục cố gắng, bạn chỉ cần viết nội dung và có thể bỏ qua các phần chào cũng như trân trọng. Hãy viết nội dung bằng tiếng Anh nhé, giới hạn trong 120 ký tự";

            } else if (newStudyStreak == 20) {
                emailContent = fullName + " đã đăng ký học khóa học " + courseTitle
                        + " và đang có chuỗi học liên tục trong " + newStudyStreak
                        + " ngày. Đây là một thành tựu xuất sắc mà chỉ 10-15% người dùng đạt được. Bạn có thể viết một email vui nhộn, cá nhân hóa, bày tỏ sự tự hào, ngưỡng mộ với mục đích khen "
                        + fullName
                        + " vì đã đạt được thành tích này, động viên họ tiếp tục cố gắng, bạn chỉ cần viết nội dung và có thể bỏ qua các phần chào cũng như trân trọng. Hãy viết nội dung bằng tiếng Anh nhé, giới hạn trong 120 ký tự";

            } else if (newStudyStreak == 30) {
                emailContent = fullName + " đã đăng ký học khóa học " + courseTitle
                        + " và đang có chuỗi học liên tục trong " + newStudyStreak
                        + " ngày. Đây là một thành tựu xuất sắc, hiếm có khi chỉ người dùng đạt được. Bạn có thể viết một email vui nhộn, cá nhân hóa, bày tỏ sự tự hào, ngưỡng mộ đối với sự chăm chỉ với mục đích khen "
                        + fullName
                        + " vì đã đạt được thành tích này động viên họ tiếp tục cố gắng, bạn chỉ cần viết nội dung và có thể bỏ qua các phần chào cũng như trân trọng. Hãy viết nội dung bằng tiếng Anh nhé, giới hạn trong 120 ký tự";

            }
        }
        emailService.sendEmailRemindLearning(email, "[KODEHOLIK] New Study Streak", fullName, emailContent);

    }

    private void sendEmailLoseStudyStreak(long oldStudyStreak, long newStudyStreak, String courseTitle,
            String fullName, String email) {
        // nguoi dung nghi 1 hom sau 1 tgian hoc
        String emailContent = "";
        if (oldStudyStreak > 0) {
            emailContent = fullName + " đã đăng ký học khóa học " + courseTitle
                    + ". Nhưng hôm nay cậu ấy bỗng nhiên nghỉ không học. Bạn có thể viết một email hỏi thăm, động viên "
                    + fullName
                    + " để cậu ấy tiếp tục học, bạn chỉ cần viết nội dung và có thể bỏ qua các phần chào cũng như trân trọng. Hãy viết nội dung bằng tiếng Anh nhé, giới hạn trong 120 ký tự";
        } else {
            if (newStudyStreak == -1
                    || newStudyStreak == -2
                    || newStudyStreak == -3
                    || newStudyStreak == -4
                    || newStudyStreak == -5
                    || newStudyStreak == -6
                    || newStudyStreak == -7
                    || newStudyStreak == -8
                    || newStudyStreak == -9
                    || newStudyStreak == -10) {
                emailContent = fullName + " đã đăng ký học khóa học " + courseTitle
                        + ". Nhưng cậu ấy đã không học trong " + (0 - newStudyStreak)
                        + " ngày liên tiếp. Bạn có thể viết một email động viên với thái độ nghiêm túc để nhắc "
                        + fullName
                        + "  tiếp tục học, bạn chỉ cần viết nội dung và có thể bỏ qua các phần chào cũng như trân trọng. Hãy viết nội dung bằng tiếng Anh nhé, giới hạn trong 120 ký tự";
            } else if (newStudyStreak == -15) {
                emailContent = fullName + " đã đăng ký học khóa học " + courseTitle
                        + ". Nhưng cậu ấy đã không học trong 15 ngày liên tiếp. Bạn có thể viết một email động viên với thái độ nghiêm túc hơn để nhắc "
                        + fullName
                        + "  tiếp tục học, bạn chỉ cần viết nội dung và có thể bỏ qua các phần chào cũng như trân trọng. Hãy viết nội dung bằng tiếng Anh nhé, giới hạn trong 120 ký tự";
            } else if (newStudyStreak == -20) {
                emailContent = fullName + " đã đăng ký học khóa học " + courseTitle
                        + ". Nhưng cậu ấy đã không học trong 20 ngày liên tiếp. Bạn có thể viết một email động viên với thái độ nghiêm túc hơn để nhắc "
                        + fullName
                        + "  tiếp tục học, bạn chỉ cần viết nội dung và có thể bỏ qua các phần chào cũng như trân trọng. Hãy viết nội dung bằng tiếng Anh nhé, giới hạn trong 120 ký tự";

            } else if (newStudyStreak == -30) {
                emailContent = fullName + " đã đăng ký học khóa học " + courseTitle
                        + ". Nhưng cậu ấy đã không học trong 30 ngày liên tiếp. Bạn có thể viết một email động viên với thái độ nghiêm túc hơn để nhắc "
                        + fullName
                        + "  tiếp tục học, bạn chỉ cần viết nội dung và có thể bỏ qua các phần chào cũng như trân trọng. Hãy viết nội dung bằng tiếng Anh nhé, giới hạn trong 120 ký tự";

            }
        }
        emailService.sendEmailRemindLearning(email, "[KODEHOLIK] Remind Learning", fullName, emailContent);

    }

    private boolean isSameDay(Timestamp lastStudiedStartAt, LocalDate yesterday) {
        LocalDate date1 = lastStudiedStartAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return date1.equals(yesterday);
    }

    private boolean isSameDay(Timestamp lastStudiedStartAt, Timestamp lastStudiedEndAt) {
        LocalDate date1 = lastStudiedStartAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate date2 = lastStudiedEndAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return date1.equals(date2);
    }

    private long getMinuteDifference(Timestamp ts1, Timestamp ts2) {
        Instant instant1 = ts1.toInstant();
        Instant instant2 = ts2.toInstant();
        return Duration.between(instant1, instant2).toMinutes();
    }

    public static Timestamp getMidnightTimestamp(Timestamp timestamp) {
        // Chuyển Timestamp -> LocalDateTime
        LocalDateTime localDateTime = timestamp.toInstant()
                .atZone(ZoneId.systemDefault()) // Dùng múi giờ hệ thống
                .toLocalDate()
                .atStartOfDay(); // Đặt về 00:00:00

        // Chuyển LocalDateTime -> Timestamp
        return Timestamp.valueOf(localDateTime);
    }

}
