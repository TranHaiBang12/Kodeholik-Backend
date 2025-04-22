package com.g44.kodeholik.model.dto.response.exam.student;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ExamDetailResponseDto {
    private long duration;

    private Timestamp endTime;

    private List<ExamProblemDetailResponseDto> problems;
}
