package com.g44.kodeholik.service.excel;

import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.web.multipart.MultipartFile;

import com.g44.kodeholik.model.dto.request.problem.add.ProblemTestCaseDto;
import com.g44.kodeholik.model.dto.response.exam.examiner.ExamResultExcelDto;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemTestCase;

public interface ExcelService {
    public Sheet readExcelSheet(MultipartFile file, String languageName);

    public List<ProblemTestCaseDto> readTestCaseExcel(MultipartFile file, List<String> inputName,
            String languageName);

    public byte[] generateTestCaseFile(List<ProblemTestCase> problemTestCases, Problem problem);

    public byte[] generateExamResultFile(List<ExamResultExcelDto> examResultExcelDtos);
}
