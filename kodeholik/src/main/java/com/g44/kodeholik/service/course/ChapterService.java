package com.g44.kodeholik.service.course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.g44.kodeholik.model.dto.request.course.ChapterRequestDto;
import com.g44.kodeholik.model.dto.response.course.ChapterResponseDto;

public interface ChapterService {
    public Page<ChapterResponseDto> getAllChapter(Pageable pageable);

    public ChapterResponseDto getChapterById(Long id);

    public void addChapter(ChapterRequestDto chapterRequestDto);

    public void editChapter(Long id, ChapterRequestDto chapterRequestDto);

    public void deleteChapter(Long id);
}
