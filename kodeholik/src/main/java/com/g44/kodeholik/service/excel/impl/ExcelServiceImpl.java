package com.g44.kodeholik.service.excel.impl;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.model.dto.request.problem.add.ProblemTestCaseDto;
import com.g44.kodeholik.model.dto.request.problem.add.TestCaseDto;
import com.g44.kodeholik.service.excel.ExcelService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ExcelServiceImpl implements ExcelService {

    @Override
    public Sheet readExcelSheet(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream();
                Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheet("TestCase");
            return sheet;
        } catch (Exception ex) {
            log.info(ex.getMessage());
            throw new BadRequestException("Excel file in wrong format", "Excel file in wrong format");
        }
    }

    @Override
    public ProblemTestCaseDto readTestCaseExcel(MultipartFile file, List<String> inputNames) {
        Sheet sheet = readExcelSheet(file);
        ProblemTestCaseDto problemTestCaseDto = new ProblemTestCaseDto();
        List<TestCaseDto> testCases = new ArrayList();
        Row headerRow = sheet.getRow(0);
        Map<String, Integer> columnIndexMap = new HashMap<>();

        for (Cell cell : headerRow) {
            columnIndexMap.put(cell.getStringCellValue(), cell.getColumnIndex());
        }

        // Kiểm tra nếu có các cột cần thiết
        if (!columnIndexMap.containsKey("Expected Output") ||
                !columnIndexMap.containsKey("Is Sample")) {
            throw new BadRequestException("Excel file in wrong format", "Excel file in wrong format");
        }
        for (int i = 0; i < inputNames.size(); i++) {
            if (!columnIndexMap.containsKey(inputNames.get(i))) {
                throw new BadRequestException("Excel file in wrong format", "Excel file in wrong format");
            }
        }
        List<Integer> inputIndexList = new ArrayList();
        log.info(inputNames.size());
        for (int i = 0; i < inputNames.size(); i++) {
            log.info(inputNames.get(i));
            int inputIndex = columnIndexMap.get(inputNames.get(i));
            inputIndexList.add(inputIndex);
        }
        int expectedOutputIndex = columnIndexMap.get("Expected Output");
        int isSampleIndex = columnIndexMap.get("Is Sample");

        // Duyệt các dòng dữ liệu (bỏ qua header - row 0)
        Iterator<Row> rowIterator = sheet.iterator();
        rowIterator.next(); // Bỏ qua header

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Map<String, String> inputMap = new HashMap();
            for (int i = 0; i < inputIndexList.size(); i++) {
                inputMap.put(inputNames.get(i), row.getCell(inputIndexList.get(i)).toString());
            }
            Cell cell = row.getCell(expectedOutputIndex);
            String expectedOutput;
            if (cell.getCellType() == CellType.NUMERIC) {
                DecimalFormat df = new DecimalFormat("#");
                expectedOutput = df.format(cell.getNumericCellValue());
            } else {
                expectedOutput = cell.toString();
            }
            boolean isSample = row.getCell(isSampleIndex).getBooleanCellValue();

            TestCaseDto testCaseDto = new TestCaseDto(inputMap, expectedOutput, isSample);
            testCases.add(testCaseDto);
        }

        problemTestCaseDto.setTestCases(testCases);
        return problemTestCaseDto;
    }

}
