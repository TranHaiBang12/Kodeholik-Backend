package com.g44.kodeholik.model.dto.response.course;

import com.g44.kodeholik.model.enums.course.LessonStatus;
import com.g44.kodeholik.model.enums.course.LessonType;

import com.g44.kodeholik.model.enums.course.LessonVideoType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LessonResponseDto {
    private Long id;
    private Long chapterId;
    private String title;
    private String description;
    private int displayOrder;
    private LessonType type;
    private LessonStatus status;
    private String attachedFile;
    private String videoUrl;
    private LessonVideoType videoType;
    private boolean isCompleted;
}
