package com.g44.kodeholik.model.dto.request.course;

import com.g44.kodeholik.model.enums.course.CourseStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseRequestDto {
    @NotBlank(message = "MSG34")
    private String title;

    @NotBlank(message = "MSG35")
    private String description;

    @NotNull
    private CourseStatus status;

    private Set<Long> topicIds;
}

