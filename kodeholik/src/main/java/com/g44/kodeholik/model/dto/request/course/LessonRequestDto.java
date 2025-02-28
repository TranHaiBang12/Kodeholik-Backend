package com.g44.kodeholik.model.dto.request.course;

import com.g44.kodeholik.model.enums.course.LessonStatus;
import com.g44.kodeholik.model.enums.course.LessonType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LessonRequestDto {

    private Long chapterId;

    private String title;

    private String description;

    private int displayOrder;

    private LessonType type;

    private LessonStatus status;

    private MultipartFile attachedFile;
}
