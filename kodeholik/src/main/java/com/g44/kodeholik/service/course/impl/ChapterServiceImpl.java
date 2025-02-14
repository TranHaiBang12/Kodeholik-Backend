package com.g44.kodeholik.service.course.impl;

import java.sql.Timestamp;
import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.course.ChapterRequestDto;
import com.g44.kodeholik.model.dto.response.course.ChapterResponseDto;
import com.g44.kodeholik.model.entity.course.Chapter;
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

    @Override
    public Page<ChapterResponseDto> getAllChapter(Pageable pageable) {
        Page<Chapter> chapterPage = chapterRepository.findAll(pageable);
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
        Chapter chapter = chapterRequestMapper.mapTo(chapterRequestDto);
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
        Chapter chapter = chapterRequestMapper.mapTo(chapterRequestDto);
        chapter.setId(id);
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

}
