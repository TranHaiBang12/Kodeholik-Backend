package com.g44.kodeholik.model.dto.request.problem;

import java.security.Timestamp;

import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.model.entity.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProblemSubmissionRequestDto {
    private Long id;

    private User user;

    private Problem problem;

    private String code;

    private Language language;

    private String notes;

    private double executionTime;

    private double memoryUsage;

    private Timestamp createdAt;

    private boolean isAccepted;

    private String status;

    private String inputWrong;
}
