package com.g44.kodeholik.service.excel.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.model.dto.request.lambda.InputVariable;
import com.g44.kodeholik.model.dto.request.problem.add.ProblemTestCaseDto;
import com.g44.kodeholik.model.dto.request.problem.add.TestCaseDto;
import com.g44.kodeholik.model.dto.response.exam.examiner.ExamResultExcelDto;
import com.g44.kodeholik.model.dto.response.exam.examiner.ProblemPoint;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemTestCase;
import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.service.excel.ExcelService;
import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class ExcelServiceImpl implements ExcelService {

    private Gson gson = new Gson();

    private final ObjectMapper objectMapper;

    @Override
    public Sheet readExcelSheet(MultipartFile file, String languageName) {
        try (InputStream inputStream = file.getInputStream();
                Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheet(languageName + "TestCase");
            return sheet;
        } catch (Exception ex) {
            log.info(ex.getMessage());
            throw new BadRequestException("Excel file in wrong format", "Excel file in wrong format");
        }
    }

    @Override
    public List<ProblemTestCaseDto> readTestCaseExcel(MultipartFile file, List<String> inputNames,
            String languageName) {
        List<ProblemTestCaseDto> problemTestCaseDtoList = new ArrayList<>();
        Sheet sheet = readExcelSheet(file, languageName);
        if (sheet == null) {
            throw new BadRequestException("Please define a excel file test case for each language",
                    "Please define a excel file test case for each language");
        }
        ProblemTestCaseDto problemTestCaseDto = new ProblemTestCaseDto();
        problemTestCaseDto.setLanguage(languageName);
        List<TestCaseDto> testCases = new ArrayList();
        Row headerRow = sheet.getRow(0);
        Map<String, Integer> columnIndexMap = new HashMap<>();

        for (Cell cell : headerRow) {
            columnIndexMap.put(cell.getStringCellValue(), cell.getColumnIndex());
        }

        // Kiểm tra nếu có các cột cần thiết
        if (!columnIndexMap.containsKey("Expected Output") ||
                !columnIndexMap.containsKey("Is Sample")) {
            throw new BadRequestException("Excel file in wrong format",
                    "Please define a column name Expected Output and Is Sample");
        }
        for (int i = 0; i < inputNames.size(); i++) {
            if (!columnIndexMap.containsKey(inputNames.get(i))) {
                throw new BadRequestException("Excel file in wrong format", "Please define column for each input");
            }
        }
        List<Integer> inputIndexList = new ArrayList();
        for (int i = 0; i < inputNames.size(); i++) {
            int inputIndex = columnIndexMap.get(inputNames.get(i));
            inputIndexList.add(inputIndex);
        }
        int expectedOutputIndex = columnIndexMap.get("Expected Output");
        int isSampleIndex = columnIndexMap.get("Is Sample");

        // Duyệt các dòng dữ liệu (bỏ qua header - row 0)
        Iterator<Row> rowIterator = sheet.iterator();
        rowIterator.next(); // Bỏ qua header

        outerLoop: while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Map<String, String> inputMap = new HashMap();
            for (int i = 0; i < inputIndexList.size(); i++) {
                Cell inputCell = row.getCell(inputIndexList.get(i));
                String input;
                if (inputCell == null) {
                    break outerLoop;
                }
                if (inputCell.getCellType() == CellType.NUMERIC) {
                    BigDecimal numericValue = BigDecimal.valueOf(inputCell.getNumericCellValue());

                    // Kiểm tra nếu là số nguyên (phần thập phân == 0)
                    if (numericValue.stripTrailingZeros().scale() <= 0) {
                        input = String.valueOf(numericValue.toBigInteger()); // Chuyển thành số nguyên
                    } else {
                        input = numericValue.toPlainString(); // Chuyển thành chuỗi số thực giữ nguyên định dạng
                    }
                } else {
                    input = inputCell.toString();
                }
                log.info(input);
                inputMap.put(inputNames.get(i), input);
            }
            Cell cell = row.getCell(expectedOutputIndex);
            String expectedOutput;
            if (cell.getCellType() == CellType.NUMERIC) {
                BigDecimal numericValue = BigDecimal.valueOf(cell.getNumericCellValue());
                // log.info(numericValue + " " + Math.floor(numericValue) + " "
                // + (numericValue == Math.floor(numericValue)));
                // Kiểm tra nếu là số nguyên
                if (numericValue.stripTrailingZeros().scale() <= 0) {
                    expectedOutput = String.valueOf(numericValue.toBigInteger()); // Lấy số nguyên
                } else {
                    expectedOutput = numericValue.toPlainString(); // Lấy số thực
                }
            } else {
                expectedOutput = cell.toString();
            }
            boolean isSample = row.getCell(isSampleIndex).getBooleanCellValue();

            TestCaseDto testCaseDto = new TestCaseDto(inputMap, expectedOutput, isSample);
            testCases.add(testCaseDto);
        }

        problemTestCaseDto.setTestCases(testCases);
        problemTestCaseDtoList.add(problemTestCaseDto);
        log.info(problemTestCaseDtoList);
        return problemTestCaseDtoList;
    }

    @Override
    public byte[] generateTestCaseFile(List<ProblemTestCase> problemTestCases, Problem problem) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Set<Language> languages = problem.getLanguageSupport();
            Workbook workbook = new XSSFWorkbook();
            for (Language language : languages) {
                Sheet sheet = workbook.createSheet(language.getName() + "TestCase");
                List<String> columnHeaders = new ArrayList();
                List<List<InputVariable>> inputVariablesList = new ArrayList();
                boolean isGetInputName = false;
                for (int i = 0; i < problemTestCases.size(); i++) {
                    if (problemTestCases.get(i).getLanguage().getName().equals(language.getName())) {
                        List<InputVariable> inputVariables = objectMapper.readValue(problemTestCases.get(i).getInput(),
                                new com.fasterxml.jackson.core.type.TypeReference<List<InputVariable>>() {
                                });
                        if (!isGetInputName) {
                            for (int k = 0; k < inputVariables.size(); k++) {
                                columnHeaders.add(inputVariables.get(k).getName());
                            }
                            isGetInputName = true;
                        }
                        inputVariablesList.add(inputVariables);
                    }
                }

                columnHeaders.add("Expected Output");
                columnHeaders.add("Is Sample");

                // Tạo hàng tiêu đề
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < columnHeaders.size(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columnHeaders.get(i));
                }

                // Ghi dữ liệu từ danh sách vào file Excel
                int rowNum = 1;
                int m = 0;
                for (int i = 0; i < problemTestCases.size(); i++) {
                    if (problemTestCases.get(i).getLanguage().getName().equals(language.getName())) {
                        ProblemTestCase data = problemTestCases.get(i);
                        Row row = sheet.createRow(rowNum++);
                        int k = 0;
                        while (k < inputVariablesList.get(m).size()) {
                            Object value = inputVariablesList.get(m).get(k).getValue();
                            row.createCell(k).setCellValue(value.toString());
                            k++;
                        }
                        m++;
                        row.createCell(k++).setCellValue(data.getExpectedOutput());
                        row.createCell(k++).setCellValue(data.isSample());
                    }
                }
            }
            workbook.write(outputStream);

            workbook.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new BadRequestException("Error generating excel file", "Error generating excel file");
        }
    }

    @Override
    public byte[] generateExamResultFile(List<ExamResultExcelDto> examResultExcelDtos) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Result");
            int rowNum = 1;

            for (ExamResultExcelDto examResultExcelDto : examResultExcelDtos) {
                List<ProblemPoint> problemPoints = examResultExcelDto.getProblemPoints();
                List<String> columnHeaders = new ArrayList();

                columnHeaders.add("Username");
                columnHeaders.add("Full name");
                for (int i = 0; i < problemPoints.size(); i++) {
                    columnHeaders.add("Problem: " + problemPoints.get(i).getTitle());
                }
                columnHeaders.add("Grade");
                columnHeaders.add("Is Submitted");

                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < columnHeaders.size(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columnHeaders.get(i));
                }

                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(examResultExcelDto.getUsername());
                row.createCell(1).setCellValue(examResultExcelDto.getFullname());

                int m = 2;
                for (int i = 0; i < problemPoints.size(); i++) {
                    row.createCell(m++).setCellValue(problemPoints.get(i).getPoint());
                }
                row.createCell(m++).setCellValue(examResultExcelDto.getGrade());
                row.createCell(m++).setCellValue(examResultExcelDto.isSubmitted());
            }
            workbook.write(outputStream);

            workbook.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new BadRequestException("Error generating excel file", "Error generating excel file");
        }
    }

}
