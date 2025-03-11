package com.g44.kodeholik.model.dto.response.exam.student;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ExamProblemDetailResponseDto {
    private String problemTitle;

    private String problemDescription;

    private List<ExamCompileInformationResponseDto> compileInformation;

}
