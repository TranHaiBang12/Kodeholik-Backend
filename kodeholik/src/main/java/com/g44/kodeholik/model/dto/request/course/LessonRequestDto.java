package com.g44.kodeholik.model.dto.request.course;

import com.g44.kodeholik.model.enums.course.LessonStatus;
import com.g44.kodeholik.model.enums.course.LessonType;
import com.g44.kodeholik.model.dto.response.course.LessonVideoType;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "MSG34")
    private String title;

    @NotBlank(message = "MSG35")
    private String description;

    private int displayOrder;

    private LessonType type;

    private LessonStatus status;

    private MultipartFile attachedFile;

    private MultipartFile videoFile;

    private String youtubeUrl;

    private LessonVideoType videoType;
}
