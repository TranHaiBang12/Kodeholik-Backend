package com.g44.kodeholik.model.dto.request.course;

import com.g44.kodeholik.model.enums.course.CourseStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Size(min = 10, max = 5000, message = "MSG29")
    @NotBlank(message = "MSG34")
    private String title;

    @Size(min = 10, max = 5000, message = "MSG29")
    @NotBlank(message = "MSG35")
    private String description;

    @NotNull
    private CourseStatus status;

    private Set<Long> topicIds;
}

