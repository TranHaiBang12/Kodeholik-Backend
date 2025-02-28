package com.g44.kodeholik.service.course.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import com.g44.kodeholik.model.entity.course.Chapter;
import com.g44.kodeholik.model.enums.course.LessonStatus;
import com.g44.kodeholik.service.aws.s3.S3Service;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.course.LessonRequestDto;
import com.g44.kodeholik.model.dto.response.course.LessonResponseDto;
import com.g44.kodeholik.model.entity.course.Lesson;
import com.g44.kodeholik.repository.course.ChapterRepository;
import com.g44.kodeholik.repository.course.LessonRepository;
import com.g44.kodeholik.service.course.LessonService;
import com.g44.kodeholik.service.user.UserService;
import com.g44.kodeholik.util.mapper.request.course.LessonRequestMapper;
import com.g44.kodeholik.util.mapper.response.course.LessonResponseMapper;

import lombok.RequiredArgsConstructor;
@Log4j2
@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;

    private final ChapterRepository chapterRepository;

    private final LessonRequestMapper lessonRequestMapper;

    private final LessonResponseMapper lessonResponseMapper;

    private final UserService userService;

    private final S3Service s3Service;

    @Override
    public Page<LessonResponseDto> getAllLesson(Pageable pageable) {
        Page<Lesson> lessonPage = lessonRepository.findByStatus(LessonStatus.ACTIVATED,pageable);
        return lessonPage.map(lessonResponseMapper::mapFrom);
    }

    @Override
    public LessonResponseDto getLessonById(Long id) {
        log.info(id);
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Lesson not found", "Lesson not found"));
        return lessonResponseMapper.mapFrom(lesson);
    }

    @Override
    public void addLesson(LessonRequestDto lessonRequestDto) {
        Lesson lesson = lessonRequestMapper.mapTo(lessonRequestDto);

        Chapter chapter = chapterRepository.findById(lessonRequestDto.getChapterId())
                .orElseThrow(() -> new NotFoundException("Chapter not found", "Chapter not found"));
        lesson.setChapter(chapter);

        lesson.setCreatedAt(Timestamp.from(Instant.now()));
        lesson.setCreatedBy(userService.getCurrentUser());

        if (lessonRequestDto.getAttachedFile() != null && !lessonRequestDto.getAttachedFile().isEmpty()) {
            String key = "lessons/" + UUID.randomUUID() + "-" + lessonRequestDto.getAttachedFile().getOriginalFilename();
            s3Service.uploadFileToS3(lessonRequestDto.getAttachedFile(), key);
            lesson.setAttachedFile(key);
        }

        lessonRepository.save(lesson);
    }


    @Override
    public void editLesson(Long lessonId, LessonRequestDto lessonRequestDto) {
        Lesson savedLesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NotFoundException("Lesson not found", "Lesson not found"));
        Lesson lesson = lessonRequestMapper.mapTo(lessonRequestDto);
        lesson.setId(lessonId);
        lesson.setChapter(chapterRepository
                .findById(lessonRequestDto.getChapterId())
                .orElseThrow(() -> new NotFoundException("Chapter not found", "Chapter not found")));
        lesson.setUpdatedAt(Timestamp.from(Instant.now()));
        lesson.setUpdatedBy(userService.getCurrentUser());
        lesson.setCreatedAt(savedLesson.getCreatedAt());
        lesson.setCreatedBy(savedLesson.getCreatedBy());
        lessonRepository.save(lesson);
    }

    @Override
    public void deleteLessonById(Long id) {
        lessonRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Lesson not found", "Lesson not found"));
        lessonRepository.deleteById(id);
    }

}
