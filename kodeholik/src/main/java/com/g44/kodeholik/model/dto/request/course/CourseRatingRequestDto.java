package com.g44.kodeholik.model.dto.request.course;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CourseRatingRequestDto {
    @NotNull(message = "Course ID is required")
    private Long courseId;

    private Long userId;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    private Integer rating;

    @NotNull
    @NotBlank(message = "Comment not blank")
    @Size(min = 10, max = 5000, message = "MSG29")
    private String comment;
}

