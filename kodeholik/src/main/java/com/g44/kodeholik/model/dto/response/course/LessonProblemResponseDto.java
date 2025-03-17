package com.g44.kodeholik.model.dto.response.course;

import com.g44.kodeholik.model.enums.problem.Difficulty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonProblemResponseDto {
    private String title;
    private Difficulty difficulty;
    private String problemLink;
}

