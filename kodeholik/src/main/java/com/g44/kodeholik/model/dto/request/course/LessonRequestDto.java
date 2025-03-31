package com.g44.kodeholik.model.dto.request.course;

import com.g44.kodeholik.model.enums.course.LessonStatus;
import com.g44.kodeholik.model.enums.course.LessonType;
import com.g44.kodeholik.model.enums.course.LessonVideoType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LessonRequestDto {

    private Long chapterId;

    @Size(min = 10, max = 5000, message = "MSG34")
    private String title;


    @Size(min = 10, max = 5000, message = "MSG29")
    private String description;

    private int displayOrder;

    private LessonType type;

    private LessonStatus status;

    private MultipartFile attachedFile;

    private MultipartFile videoFile;

    private String youtubeUrl;

    private LessonVideoType videoType;

    private List<String> problemIds = new ArrayList<>();

    public void setProblemIds(String problemIdsStr) {
        if (problemIdsStr != null && !problemIdsStr.isEmpty()) {
            this.problemIds = Arrays.asList(problemIdsStr.split(","));
        }
    }

}
