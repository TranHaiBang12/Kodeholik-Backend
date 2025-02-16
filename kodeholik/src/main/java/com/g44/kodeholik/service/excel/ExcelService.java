package com.g44.kodeholik.service.excel;

import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.web.multipart.MultipartFile;

import com.g44.kodeholik.model.dto.request.problem.add.ProblemTestCaseDto;

public interface ExcelService {
    public Sheet readExcelSheet(MultipartFile file);

    public ProblemTestCaseDto readTestCaseExcel(MultipartFile file, List<String> inputName);

}
