package com.g44.kodeholik.model.dto.response.problem.submission;

import java.sql.Timestamp;

import com.g44.kodeholik.model.dto.response.problem.ProblemResponseDto;
import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.model.entity.user.Users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProblemSubmissionDto {
    private Long id;

    private ProblemResponseDto problem;

    private Users user;

    private String code;

    private Language language;

    private boolean isAccepted;

    private String notes;

    private double executionTime;

    private double memoryUsage;

    private Timestamp createdAt;

    private String status;

    private String inputWrong;

    private int noTestCasePassed;
}
