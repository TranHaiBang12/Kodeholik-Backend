package com.g44.kodeholik.service.excel.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.model.dto.request.lambda.InputVariable;
import com.g44.kodeholik.model.dto.request.problem.add.ProblemTestCaseDto;
import com.g44.kodeholik.model.dto.request.problem.add.TestCaseDto;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemTestCase;
import com.g44.kodeholik.model.entity.setting.Language;

class ExcelServiceImplTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ExcelServiceImpl excelService;

    @Mock
    private MultipartFile file;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testReadExcelSheetSuccess() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("JavaTestCase");

        workbook.write(out);
        workbook.close();
        byte[] excelBytes = out.toByteArray();

        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(excelBytes));

        Sheet result = excelService.readExcelSheet(file, "Java");

        assertNotNull(result);
        assertEquals("JavaTestCase", result.getSheetName());
    }

    @Test
    void testReadExcelSheetFileFormatException() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("JavaTestCase");

        workbook.write(out);
        workbook.close();
        byte[] excelBytes = out.toByteArray();

        when(file.getInputStream()).thenThrow(
                new IOException("File format error"));

        Exception exception = assertThrows(BadRequestException.class,
                () -> {
                    excelService.readExcelSheet(file, "Java");
                });

        assertEquals("Excel file in wrong format", exception.getMessage());
    }

    @Test
    void testReadTestCaseExcelSuccess() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("JavaTestCase");
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Input1");
        headerRow.createCell(1).setCellValue("Input2");
        headerRow.createCell(2).setCellValue("Expected Output");
        headerRow.createCell(3).setCellValue("Is Sample");
        Row row = sheet.createRow(1);
        row.createCell(0).setCellValue("test_input1");
        row.createCell(1).setCellValue("test_input2");
        row.createCell(2).setCellValue("expected_output");
        row.createCell(3).setCellValue(true);
        workbook.write(out);
        workbook.close();
        byte[] excelBytes = out.toByteArray();

        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(excelBytes));

        List<String> inputNames = Arrays.asList("Input1", "Input2");
        List<ProblemTestCaseDto> result = excelService.readTestCaseExcel(
                file, inputNames, "Java");

        assertNotNull(result);
        assertEquals(1, result.size());
        ProblemTestCaseDto dto = result.get(0);
        assertEquals("Java", dto.getLanguage());
        assertEquals(1, dto.getTestCases().size());
        TestCaseDto testCase = dto.getTestCases().get(0);
        assertEquals("test_input1", testCase.getInput().get("Input1"));
        assertEquals("test_input2", testCase.getInput().get("Input2"));
        assertEquals("expected_output", testCase.getExpectedOutput());
    }

    @Test
    public void testGenerateExcelFileSuccess() throws Exception {
        Problem problem = new Problem();
        Set<Language> languages = new HashSet<>();
        Language javaLanguage = new Language();
        javaLanguage.setName("Java");
        languages.add(javaLanguage);
        problem.setLanguageSupport(languages);
        List<ProblemTestCase> testCases = new ArrayList<>();
        ProblemTestCase testCase = new ProblemTestCase();
        testCase.setLanguage(javaLanguage);
        testCase.setExpectedOutput("42");
        testCase.setSample(true);
        testCase.setProblem(problem);
        List<InputVariable> inputVariables = new ArrayList<>();
        inputVariables.add(
                new InputVariable("input1", "INT", 10, 10));
        inputVariables.add(
                new InputVariable("input2", "INT", 32, 32));
        testCase.setInput(
                "[{\"name\":\"input1\",\"type\":\"INT\",\"value\":10}," +
                        " {\"name\":\"input2\",\"type\":\"INT\",\"value\":32}]");
        testCases.add(testCase);
        when(objectMapper.readValue(anyString(), any(TypeReference.class)))
                .thenReturn(inputVariables);

        byte[] excelBytes = excelService.generateExcelFile(testCases, problem);

        assertNotNull(excelBytes);
        assertTrue(excelBytes.length > 0);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(excelBytes);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheet("JavaTestCase");

        assertNotNull(sheet);
        assertEquals("JavaTestCase", sheet.getSheetName());

        Row headerRow = sheet.getRow(0);
        assertEquals("input1", headerRow.getCell(0).getStringCellValue());
        assertEquals("input2", headerRow.getCell(1).getStringCellValue());
        assertEquals("Expected Output", headerRow.getCell(2).getStringCellValue());
        assertEquals("Is Sample", headerRow.getCell(3).getStringCellValue());

        Row dataRow = sheet.getRow(1);
        assertEquals("10", dataRow.getCell(0).getStringCellValue());
        assertEquals("32", dataRow.getCell(1).getStringCellValue());
        assertEquals("42", dataRow.getCell(2).getStringCellValue());
        assertTrue(dataRow.getCell(3).getBooleanCellValue());

        workbook.close();
    }

}