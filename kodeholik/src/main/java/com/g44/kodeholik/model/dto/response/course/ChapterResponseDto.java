package com.g44.kodeholik.model.dto.response.course;

import com.g44.kodeholik.model.enums.course.ChapterStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChapterResponseDto {
    private Long id;

    private Long courseId;

    private String title;

    private String description;

    private int displayOrder;

    private ChapterStatus status;

    private List<LessonResponseDto> lessons;
}
