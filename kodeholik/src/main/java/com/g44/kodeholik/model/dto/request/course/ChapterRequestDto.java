package com.g44.kodeholik.model.dto.request.course;

import com.g44.kodeholik.model.enums.course.ChapterStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @Size(min = 10, max = 200, message = "MSG34")
    private String title;

    @Size(min = 10, max = 5000, message = "MSG29")
    private String description;

    private int displayOrder;

    private ChapterStatus status;
}
