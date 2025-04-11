package com.g44.kodeholik.model.dto.request.course;

import com.g44.kodeholik.model.enums.course.CourseStatus;

import com.g44.kodeholik.util.validation.image.ValidImage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
    @NotBlank(message = "MSG02")
    @Size(min = 10, max = 200, message = "MSG34")
    private String title;

    @NotBlank(message = "MSG02")
    @Size(min = 10, max = 5000, message = "MSG29")
    private String description;

    private CourseStatus status;

    @NotEmpty
    private Set<Long> topicIds;

    @ValidImage
    private MultipartFile imageFile;
}
