package com.g44.kodeholik.model.dto.request.course;

import com.g44.kodeholik.model.enums.course.ChapterStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChapterRequestDto {
    private Long courseId;

    private String title;

    private String description;

    private int displayOrder;

    private ChapterStatus status;
}
