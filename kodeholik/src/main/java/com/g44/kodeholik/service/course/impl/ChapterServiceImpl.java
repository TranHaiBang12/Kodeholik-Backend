package com.g44.kodeholik.service.course.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.course.ChapterStatus;
import com.g44.kodeholik.model.enums.course.LessonStatus;
import com.g44.kodeholik.model.enums.user.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.course.ChapterRequestDto;
import com.g44.kodeholik.model.dto.response.course.ChapterResponseDto;
import com.g44.kodeholik.model.dto.response.course.ListResponseDto;
import com.g44.kodeholik.model.entity.course.Chapter;
import com.g44.kodeholik.model.entity.course.Course;
import com.g44.kodeholik.repository.course.ChapterRepository;
import com.g44.kodeholik.repository.course.CourseRepository;
import com.g44.kodeholik.service.course.ChapterService;
import com.g44.kodeholik.service.user.UserService;
import com.g44.kodeholik.util.mapper.request.course.ChapterRequestMapper;
import com.g44.kodeholik.util.mapper.response.course.ChapterResponseMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {

    private final CourseRepository courseRepository;

    private final ChapterRepository chapterRepository;

    private final ChapterRequestMapper chapterRequestMapper;

    private final ChapterResponseMapper chapterResponseMapper;

    private final UserService userService;

    public List<ChapterStatus> getAllowedStatus() {
        Users currentUser = userService.getCurrentUser();
        UserRole userRole = currentUser.getRole();
        List<ChapterStatus> allowedStatuses;
        if (userRole == UserRole.STUDENT) {
            allowedStatuses = Collections.singletonList(ChapterStatus.ACTIVATED);
        } else {
            allowedStatuses = Arrays.asList(ChapterStatus.values());
        }
        return allowedStatuses;
    }

    @Override
    public Page<ChapterResponseDto> getAllChapter(Pageable pageable) {
        Sort sort = Sort.by(Sort.Direction.ASC, "displayOrder");
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<Chapter> chapterPage = chapterRepository.findByStatusIn(getAllowedStatus(), sortedPageable);
        return chapterPage.map(chapterResponseMapper::mapFrom);
    }

    @Override
    public ChapterResponseDto getChapterById(Long id) {
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Chapter not found", "Chapter not found"));
        return chapterResponseMapper.mapFrom(chapter);
    }

    @Override
    public void addChapter(ChapterRequestDto chapterRequestDto) {
        String normalizedTitle = chapterRequestDto.getTitle().trim().replaceAll("[ ]+", " ");

        if (normalizedTitle.length() < 10) {
            throw new BadRequestException(
                    "Chapter title must be at least 10 characters long (excluding extra spaces): " + normalizedTitle,
                    "Chapter title must be at least 10 characters long (excluding extra spaces): " + normalizedTitle);
        }
        if (chapterRepository.findByTitleIgnoreCaseAndCourseId(normalizedTitle, chapterRequestDto.getCourseId()).isPresent()) {
            throw new BadRequestException("Chapter title already exists: " + normalizedTitle,
                    "Chapter title already exists: " + normalizedTitle);
        }
        String normalizedDescription = chapterRequestDto.getDescription().trim().replaceAll("[ ]+", " ");
        if (normalizedDescription.isEmpty()) {
            throw new BadRequestException("Chapter description cannot be empty or contain only whitespace",
                    "Chapter description cannot be empty or contain only whitespace");
        }
        if (normalizedDescription.length() < 10) {
            throw new BadRequestException(
                    "Chapter description must be at least 10 characters long (excluding extra spaces): "
                            + normalizedDescription,
                    "Chapter description must be at least 10 characters long (excluding extra spaces): "
                            + normalizedDescription);
        }

        Chapter chapter = chapterRequestMapper.mapTo(chapterRequestDto);
        chapter.setTitle(normalizedTitle);
        chapter.setDescription(normalizedDescription);
        chapter.setCourse(courseRepository
                .findById(chapterRequestDto.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found", "Course not found")));
        chapter.setCreatedAt(Timestamp.from(Instant.now()));
        chapter.setCreatedBy(userService.getCurrentUser());
        chapterRepository.save(chapter);
    }

    @Override
    public void editChapter(Long id, ChapterRequestDto chapterRequestDto) {
        Chapter savedChapter = chapterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Chapter not found", "Chapter not found"));

        String normalizedTitle = chapterRequestDto.getTitle().trim().replaceAll("[ ]+", " ");
        if (normalizedTitle.length() < 10) {
            throw new BadRequestException(
                    "Chapter title must be at least 10 characters long (excluding extra spaces): " + normalizedTitle,
                    "Chapter title must be at least 10 characters long (excluding extra spaces): " + normalizedTitle);
        }
        if (chapterRepository.findByTitleIgnoreCaseAndIdNotAndCourseId(normalizedTitle, id, chapterRequestDto.getCourseId()).isPresent()) {
            throw new BadRequestException("Chapter title already exists: " + normalizedTitle,
                    "Chapter title already exists: " + normalizedTitle);
        }
        String normalizedDescription = chapterRequestDto.getDescription().trim().replaceAll("[ ]+", " ");
        if (normalizedDescription.isEmpty()) {
            throw new BadRequestException("Chapter description cannot be empty or contain only whitespace",
                    "Chapter description cannot be empty or contain only whitespace");
        }
        if (normalizedDescription.length() < 10) {
            throw new BadRequestException(
                    "Chapter description must be at least 10 characters long (excluding extra spaces): "
                            + normalizedDescription,
                    "Chapter description must be at least 10 characters long (excluding extra spaces): "
                            + normalizedDescription);
        }

        Chapter chapter = chapterRequestMapper.mapTo(chapterRequestDto);
        chapter.setId(id);
        chapter.setTitle(normalizedTitle);
        chapter.setDescription(normalizedDescription);
        chapter.setCourse(courseRepository
                .findById(chapterRequestDto.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found", "Course not found")));
        chapter.setUpdatedAt(Timestamp.from(Instant.now()));
        chapter.setUpdatedBy(userService.getCurrentUser());
        chapter.setCreatedAt(savedChapter.getCreatedAt());
        chapter.setCreatedBy(savedChapter.getCreatedBy());
        chapterRepository.save(chapter);
    }

    @Override
    public void deleteChapter(Long id) {
        chapterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Chapter not found", "Chapter not found"));
        chapterRepository.deleteById(id);
    }

    @Override
    public List<ChapterResponseDto> getChapterByCourseId(Long id) {
        Sort sort = Sort.by(Sort.Direction.ASC, "displayOrder");
        List<Chapter> chapters = chapterRepository.findByCourseIdAndStatusIn(id, getAllowedStatus(), sort);
        return chapters.stream()
                .map(chapterResponseMapper::mapDetailFrom)
                .collect(Collectors.toList());
    }

    @Override
    public List<ListResponseDto> getListChapterResponseDtoByCourseId(Long courseId) {
        List<ListResponseDto> result = new ArrayList();
        List<Chapter> chapters = chapterRepository.findByCourseIdOrderByDisplayOrderAsc(courseId);
        for (int i = 0; i < chapters.size(); i++) {
            ListResponseDto listResponseDto = new ListResponseDto();
            listResponseDto.setId(chapters.get(i).getId());
            listResponseDto.setTitle(chapters.get(i).getTitle());
            result.add(listResponseDto);
        }
        return result;
    }

}
