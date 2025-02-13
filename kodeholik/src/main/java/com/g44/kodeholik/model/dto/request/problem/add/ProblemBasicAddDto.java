package com.g44.kodeholik.model.dto.request.problem.add;

import java.util.List;

import com.g44.kodeholik.model.enums.problem.Difficulty;
import com.g44.kodeholik.model.enums.problem.ProblemStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProblemBasicAddDto {
    private String title;

    private Difficulty difficulty;

    private String description;

    private ProblemStatus status;

    private List<String> topics;

    private List<String> skills;

    private Boolean isActive;
}
