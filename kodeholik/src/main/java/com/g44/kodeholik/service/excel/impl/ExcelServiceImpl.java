package com.g44.kodeholik.service.excel.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemTestCase;
import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.service.excel.ExcelService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
            log.info(languageName + "TestCase");
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
        log.info(expectedOutputIndex + " " + isSampleIndex);

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
                    DecimalFormat df = new DecimalFormat("#");
                    input = df.format(inputCell.getNumericCellValue());
                } else {
                    input = inputCell.toString();
                }
                inputMap.put(inputNames.get(i), input);
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
        problemTestCaseDtoList.add(problemTestCaseDto);
        return problemTestCaseDtoList;
    }

    @Override
    public byte[] generateExcelFile(List<ProblemTestCase> problemTestCases, Problem problem) {
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
                            log.info(value);
                            row.createCell(k).setCellValue(value.toString());
                            k++;
                        }
                        m++;
                        row.createCell(k++).setCellValue(data.getExpectedOutput().toString().replaceAll("\"", ""));
                        row.createCell(k++).setCellValue(data.isSample());
                    }
                }
            }
            workbook.write(outputStream);

            workbook.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }

}
