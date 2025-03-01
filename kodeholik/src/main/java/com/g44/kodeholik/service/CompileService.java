package com.g44.kodeholik.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import static java.util.Map.entry;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.g44.kodeholik.model.dto.request.lambda.InputVariable;
import com.g44.kodeholik.model.dto.request.lambda.ResponseResult;
import com.g44.kodeholik.model.dto.request.lambda.TestCase;
import com.g44.kodeholik.model.dto.request.lambda.TestResult;
import com.g44.kodeholik.model.enums.problem.InputType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class CompileService {
    private static Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    private static int TimeLimit = 10;

    public static long countCharOccurrences(String str, char ch) {
        return str.chars().filter(c -> c == ch).count();
    }

    public static String detectInputType(Object obj) {
        if (obj != null) {
            return obj.getClass().getSimpleName();
        }
        return "";
    }

    public static String formatObject(Object obj) {
        if (obj instanceof List<?>) {
            return ((List<?>) obj).stream()
                    .map(CompileService::formatObject) // Gọi đệ quy
                    .collect(Collectors.joining(",", "[", "]"));
        } else if (obj instanceof String) {
            return "\"" + obj + "\""; // Thêm dấu " nếu là chuỗi
        }
        return obj.toString();
    }

    private static String convertToJavaSyntax(Object input, String type) {
        int noDimension = 0;
        String inputStr = "";
        if (input != null && input.toString().length() > 0) {
            inputStr = input.toString();
            for (int i = 0; i < inputStr.length(); i++) {
                if (inputStr.charAt(i) == '[') {
                    noDimension++;
                } else {
                    break;
                }
            }
        }
        if (type.equals(InputType.STRING.toString())) {
            return "\"" + input + "\""; // Chuỗi cần có dấu nháy
        } else if (type.equals(InputType.CHAR.toString())) {
            return "'" + input + "'"; // Ký tự cần có dấu nháy đơn
        } else if (type.equals(InputType.INT.toString()) || type.equals(InputType.DOUBLE.toString())
                || type.equals(InputType.LONG.toString())
                || type.equals(InputType.BOOLEAN.toString())) {

            return String.valueOf(input); // Số hoặc boolean giữ nguyên
        } else if (type.equals(InputType.LIST.toString())) {
            log.info("new LinkedHashSet<>(Arrays.asList(" + convertListToJavaSyntax(
                    objectMapper.convertValue(input, new TypeReference<List<?>>() {
                    })) + "))");
            try {
                return "Arrays.asList(" + convertListToJavaSyntax(
                        objectMapper.convertValue(input, new TypeReference<List<?>>() {
                        })) + ")";
            } catch (Exception e) {
                return "";
            }

        } else if (type.equals("SET")) {
            log.info("new LinkedHashSet<>(Arrays.asList(" + convertListToJavaSyntax(
                    objectMapper.convertValue(input, new TypeReference<List<?>>() {
                    })) + "))");
            try {
                return "new LinkedHashSet<>(Arrays.asList(" + convertListToJavaSyntax(
                        objectMapper.convertValue(input, new TypeReference<List<?>>() {
                        })) + "))";

            } catch (

            Exception e) {
                return "";
            }
        } else if (type.equals(InputType.MAP.toString())) {
            // Nếu là HashMap, cần tạo new HashMap<>()
            return convertMapToJavaSyntax((Map<?, ?>) input);
        } else if (type.equals(InputType.ARR_INT.toString())) {
            String dimension = "";
            for (int i = 0; i < noDimension; i++) {
                dimension = dimension += "[]";
            }
            if (inputStr.length() > 0) {
                String insideInput = inputStr.substring(1, inputStr.length() - 1);
                insideInput = insideInput.replaceAll("\\[", "{");
                insideInput = insideInput.replaceAll("\\]", "}");
                input = "{" + insideInput
                        + "}";
            }
            return "new int" + dimension + input;
        } else if (type.equals(InputType.ARR_DOUBLE.toString())) {
            String dimension = "";
            for (int i = 0; i < noDimension; i++) {
                dimension = dimension += "[]";
            }
            if (inputStr.length() > 0) {
                String insideInput = inputStr.substring(1, inputStr.length() - 1);
                insideInput = insideInput.replaceAll("\\[", "{");
                insideInput = insideInput.replaceAll("\\]", "}");
                input = "{" + insideInput
                        + "}";
            }
            return "new double" + dimension + input;
        } else if (type.equals(InputType.ARR_STRING.toString())) { // ✅ Thêm xử lý
                                                                   // ARR_STRING
            String dimension = "";

            // inputStr = "[" + ((List<?>) input).stream()
            // .map(obj -> convertToJavaSyntax(obj, detectType(obj)))
            // .collect(Collectors.joining(", ")) + "]";
            inputStr = formatObject(input);

            for (int i = 0; i < noDimension; i++) {
                dimension = dimension += "[]";
            }
            if (inputStr.length() > 0) {
                String insideInput = inputStr.substring(1, inputStr.length() - 1);
                insideInput = insideInput.replaceAll("\\[", "{");
                insideInput = insideInput.replaceAll("\\]", "}");
                input = "{" + insideInput
                        + "}";
            }
            return "new String" + dimension + input;
        } else if (type.equals(InputType.ARR_OBJECT.toString()))

        {
            String dimension = "";
            for (int i = 0; i < noDimension; i++) {
                dimension = dimension += "[]";
            }
            return "new Object" + dimension + input;
        } else if (type.equals(InputType.OBJECT.toString())) { // ✅ Thêm xử lý OBJECT
            return convertObjectToJavaSyntax(input);
        } else {
            return convertCustomObjectToJavaSyntax(input, type); // Xử lý class tùy chỉnh
        }
    }

    private static String convertObjectToJavaSyntax(Object obj) {
        if (obj instanceof String || obj instanceof Number || obj instanceof Boolean) {
            return convertToJavaSyntax(obj, detectType(obj)); // Xử lý như kiểu dữ liệu cơ bản
        }
        return String.valueOf(obj);
    }

    private static String convertCustomObjectToJavaSyntax(Object input, String type) {
        String a = "";
        int i = 0;
        log.info(input);
        if (input instanceof LinkedHashMap && type.contains("Map")) {
            // Nếu input là LinkedHashMap, trích xuất tất cả các giá trị
            LinkedHashMap<?, ?> map = (LinkedHashMap<?, ?>) input;
            StringBuilder sb = new StringBuilder("new HashMap<>(Map.of( ");
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (i > 0) {
                    sb.append(",");
                }
                sb
                        .append(convertToJavaSyntax(entry.getKey(), detectType(entry.getKey())))
                        .append(", ")
                        .append(convertToJavaSyntax(entry.getValue(), detectType(entry.getValue())));
                i++;
            }
            sb.append("))");
            a = sb.toString();
        } else if (input instanceof Object[]) {
            // Nếu input là mảng Object
            a = "new " + type + "(" + Arrays.stream((Object[]) input)
                    .map(obj -> convertToJavaSyntax(obj, detectType(obj))) // Chuyển mỗi phần tử thành chuỗi
                    .collect(Collectors.joining(", ")) + ")";
        } else if (input instanceof HashSet && type.contains("Set")) {
            // Nếu input là LinkedHashMap, trích xuất tất cả các giá trị
            HashSet<?> set = (HashSet<?>) input;
            int j = 0;
            StringBuilder sb = new StringBuilder("new HashSet<>(");
            for (Object entry : set) {
                if (j > 0) {
                    sb.append(", ");
                }
                sb.append(convertToJavaSyntax(entry, detectType(entry)));
                a = sb.toString();
                j++;
            }
            sb.append(")");
            a = sb.toString();
        } else if (input instanceof Set && type.contains("Set")) {
            log.info("SET");
            // Nếu input là LinkedHashMap, trích xuất tất cả các giá trị
            Set<?> set = (Set<?>) input;
            int j = 0;
            StringBuilder sb = new StringBuilder("new HashSet<>(");
            for (Object entry : set) {
                if (j > 0) {
                    sb.append(", ");
                }
                sb.append(convertToJavaSyntax(entry, detectType(entry)));
                a = sb.toString();
                j++;
            }
            sb.append(")");
            a = sb.toString();
        } else {
            StringBuilder sb;
            sb = new StringBuilder("new " + type + "(");
            if (input instanceof HashMap) {
                HashMap<?, ?> map = (HashMap<?, ?>) input;
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    if (i > 0) {
                        sb.append(",");
                    }
                    sb
                            .append(convertToJavaSyntax(entry.getValue(), detectType(entry.getValue())));
                    i++;
                }
            } else {
                // Xử lý các kiểu khác nếu cần
                sb = new StringBuilder("new " + type + "(" + input + ")");
            }
            sb.append(")");
            a = sb.toString();
        }

        return a; // Trả về chuỗi kết quả
    }

    private static String convertListToJavaSyntax(List<?> list) {
        return list.stream()
                .map(obj -> convertToJavaSyntax(obj, detectType(obj)))
                .collect(Collectors.joining(", "));
    }

    // Chuyển HashMap sang Java Syntax
    private static String convertMapToJavaSyntax(Map<?, ?> map) {
        StringBuilder sb = new StringBuilder("new HashMap<>() {{ ");
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            sb.append("put(")
                    .append(convertToJavaSyntax(entry.getKey(), detectType(entry.getKey())))
                    .append(", ")
                    .append(convertToJavaSyntax(entry.getValue(), detectType(entry.getValue())))
                    .append("); ");
        }
        sb.append("}}");
        return sb.toString();
    }

    private static String detectType(Object obj) {
        if (obj instanceof String)
            return "STRING";
        if (obj instanceof Character)
            return "CHAR";
        if (obj instanceof Integer)
            return "INT";
        if (obj instanceof Double)
            return "DOUBLE";
        if (obj instanceof Long)
            return "LONG";
        if (obj instanceof Boolean)
            return "BOOLEAN";
        if (obj instanceof List<?>)
            return "LIST";
        if (obj instanceof Map<?, ?>)
            return "MAP";
        if (obj instanceof int[])
            return "ARR_INT";
        if (obj instanceof double[])
            return "ARR_DOUBLE";
        if (obj instanceof Object[])
            return "ARR_OBJECT";
        return "UNKNOWN"; // Nếu không xác định được kiểu
    }

    public static String compileAndRun(String userCode, List<TestCase> testCases,
            String language,
            String functionSignature,
            String returnType)
            throws IOException, InterruptedException {
        String uniqueId = UUID.randomUUID().toString();
        // for (int i = 0; i < testCases.size(); i++) {
        // TestCase testCase = testCases.get(i);
        // List<InputVariable> inputs = testCase.getInput();
        // for (int j = 0; j < inputs.size(); j++) {
        // if (inputs.get(j).get)
        // inputs.get(j).setValue(objectMapper.writeValueAsString(inputs.get(j).getValue()));
        // }
        // testCase.setExpectedOutput(objectMapper.writeValueAsString(testCase.getExpectedOutput()));
        // }

        File tempDir = new File("/tmp/compileCode_" + uniqueId);
        if (!tempDir.exists())
            tempDir.mkdirs();
        String result;
        if ("java".equalsIgnoreCase(language)) {
            result = compileAndRunJava(userCode, testCases, tempDir, functionSignature, returnType);
        } else if ("c".equalsIgnoreCase(language)) {
            result = compileAndRunC(userCode, testCases, tempDir, functionSignature, returnType);
        } else {
            result = "Error: Unsupported language!";
        }
        deleteDirectory(tempDir);
        return result;
    }

    public static void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
            directory.delete();
        }
    }

    // --------------- JAVA ---------------
    private static String compileAndRunJava(String userCode, List<TestCase> testCases,
            File tempDir,
            String functionSignature, String returnType)
            throws IOException, InterruptedException {
        File sourceFile = new File(tempDir, "Main.java");
        copyGsonJar(tempDir);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(sourceFile))) {
            writer.write(generateJavaProgram(userCode, testCases, functionSignature, returnType));
        }
        Process compile = runCommand(new String[] {
                "javac", "-cp", tempDir.getAbsolutePath() + "/libs/gson-2.11.0.jar",
                "-d", tempDir.getAbsolutePath(), "Main.java"
        }, tempDir, TimeLimit);

        if (compile.exitValue() != 0) {
            return "Compilation Error:\n" + readOutput(compile);
        }

        // Chạy chương trình
        String classpathSeparator = System.getProperty("os.name").toLowerCase().contains("win") ? ";" : ":";

        Process run = runCommand(new String[] {
                "java", "-cp",
                tempDir.getAbsolutePath() + classpathSeparator + tempDir.getAbsolutePath() + "/libs/gson-2.11.0.jar",
                "Main"
        }, tempDir, TimeLimit);

        return generateTestCaseResults(run, testCases, "java");
    }

    private static String generateJavaProgram(String userCode, List<TestCase> testCases,
            String functionSignature, String returnType) {
        StringBuilder sb = new StringBuilder();
        sb.append("import java.io.*;\n");
        sb.append("import com.google.gson.Gson;\n");
        sb.append("import java.util.*;\n");
        sb.append("import static java.util.Map.entry;\n");
        sb.append("public class Main {\n");
        sb.append("    public static void main(String[] args) {\n");
        sb.append("        Gson gson = new Gson();\n");
        for (int i = 0; i < testCases.size(); i++) {

            List<InputVariable> inputs = testCases.get(i).getInput();
            log.info("Inputs: " + inputs);
            Gson gson = new Gson();
            sb.append(" try {\n");
            sb.append("         " + returnType + " result = " + functionSignature + "(");
            sb.append(inputs.stream()
                    .map(input -> convertToJavaSyntax(input.getValue(), input.getType())) // Xác định kiểu trước khi
                                                                                          // chuyển đổi
                    .collect(Collectors.joining(", ")));
            sb.append(");\n");
            sb.append("            System.out.println(\"[Test Case " + (i + 1)
                    + "] Output: \" + gson.toJson(result));\n");
            sb.append(" } catch (Exception e) {\n");
            sb.append("     System.out.print(\"[Test Case " + (i + 1) + "] Error: \" + e.toString());\n");
            sb.append("     StackTraceElement[] stackTrace = e.getStackTrace();\n");
            sb.append("     if (stackTrace.length > 0) {\n");
            sb.append("         int errorLine = stackTrace[0].getLineNumber();\n");
            sb.append("         System.out.print(\". Error at line: \" + errorLine);\n");

            sb.append("         try (BufferedReader br = new BufferedReader(new FileReader(\"Main.java\"))) {\n");
            sb.append("             int currentLine = 0;\n");
            sb.append("             String line;\n");
            sb.append("             while ((line = br.readLine()) != null) {\n");
            sb.append("                 currentLine++;\n");
            sb.append("                 if (currentLine == errorLine) {\n");
            sb.append("                     System.out.println(\". Faulty code: \" + line.trim());\n");
            sb.append("                     break;\n");
            sb.append("                 }\n");
            sb.append("             }\n");
            sb.append("         } catch (IOException ioException) {\n");
            sb.append("             System.out.println(\"Failed to read source file.\");\n");
            sb.append("         }\n");

            sb.append("     }\n");
            sb.append(" }\n");

        }

        sb.append("    }\n");
        sb.append(userCode);
        sb.append("\n}");
        return sb.toString();
    }

    // --------------- C LANGUAGE ---------------
    private static String compileAndRunC(String userCode, List<TestCase> testCases, File tempDir,
            String functionSignature, String returnType)
            throws IOException, InterruptedException {
        File sourceFile = new File(tempDir, "main.c");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(sourceFile))) {
            writer.write(generateCProgram(userCode, testCases, functionSignature, returnType)); // Tạo mã C và ghi vào
                                                                                                // file
        }

        // Lệnh biên dịch với cJSON
        String[] compileCommand = new String[] {
                "gcc",
                "-o", "a.out", // Đầu ra là a.out
                "main.c", // Mã nguồn C
        };

        // Biên dịch mã C
        Process compile = runCommand(compileCommand, tempDir, TimeLimit);
        if (compile.exitValue() != 0) {
            return "Compilation Error:\n" + readOutput(compile); // Xử lý lỗi biên dịch
        }

        // Chạy file thực thi
        Process run = runCommand(new String[] { tempDir.getAbsolutePath() + "/a.out" }, tempDir, 10);
        return generateTestCaseResults(run, testCases, "c"); // Lấy kết quả từ việc chạy mã
    }

    private static String generateCProgram(String userCode, List<TestCase> testCases, String functionSignature,
            String returnType) {
        StringBuilder sb = new StringBuilder();

        // Thêm thư viện cần thiết
        sb.append("#include <stdio.h>\n");
        sb.append("#include <stdlib.h>\n");
        sb.append("#include <string.h>\n");
        sb.append("#include <signal.h>\n");
        sb.append("#include <setjmp.h>\n");
        sb.append("#include <stdbool.h>\n");
        sb.append("#include <ctype.h>\n");
        sb.append(userCode);
        sb.append("\n\nint main() {\n");
        String functionType = "";
        if (returnType.equals("char")) {
            functionType = "char";
        } else if (returnType.equals("int")) {
            functionType = "int";
        } else if (returnType.equals("double")) {
            functionType = "double";
        } else if (returnType.equals("bool")) {
            functionType = "boolean";
        } else if (returnType.contains("char*")) {
            functionType = "char_array";
        } else if (returnType.equals("string")) {
            functionType = "string_array";
        } else if (returnType.contains("int*")) {
            functionType = "int_array";
        } else if (returnType.contains("double*")) {
            functionType = "double_array";
        } else if (returnType.equals("bool*")) {
            functionType = "boolean_array";
        }

        log.info(functionType + " " + returnType);
        // Xác định kiểu dữ liệu của hàm

        for (int i = 0; i < testCases.size(); i++) {
            List<InputVariable> inputs = testCases.get(i).getInput();
            int col = 0;
            int row = 0;
            for (int j = 0; j < inputs.size(); j++) {
                if (inputs.get(j).getName().equals("colOutput")) {
                    col = Integer.parseInt(inputs.get(j).getValue().toString());
                } else if (inputs.get(j).getName().equals("rowOutput")) {
                    row = Integer.parseInt(inputs.get(j).getValue().toString());
                }
            }
            sb.append("    " + (returnType.equals("string") ? "char*" : returnType) + " result;\n");

            // Chuyển đổi danh sách tham số thành chuỗi đầu vào
            String inputString = inputs.stream()
                    .filter(input -> !"rowOutput".equals(input.getName()) && !"colOutput".equals(input.getName()))
                    .map(input -> convertInputToCString(input, inputs))
                    .collect(Collectors.joining(", "));

            // Gọi hàm và xử lý kết quả dựa trên kiểu trả về
            if (functionType.equals("int") || functionType.equals("long") || functionType.equals("char")) {
                sb.append("    result = " + functionSignature + "(" + inputString + ");\n");
                sb.append("    printf(\"[Test Case " + (i + 1) + "] Output: %d\\n\", result);\n");
            } else if (functionType.equals("double")) {
                sb.append("    result = " + functionSignature + "(" + inputString + ");\n");
                sb.append("    printf(\"[Test Case " + (i + 1) + "] Output: %f\\n\", result);\n");
            } else if (functionType.equals("char")) {
                sb.append("    result = " + functionSignature + "(" + inputString + ");\n");
                sb.append("    if (result) printf(\"[Test Case " + (i + 1) + "] Output: %s\\n\", result);\n");
            } else if (functionType.equals("boolean")) {
                sb.append("    result = " + functionSignature + "(" + inputString + ");\n");
                sb.append(
                        "    printf(\"[Test Case " + (i + 1) + "] Output: %s\\n\", result ? \"true\" : \"false\");\n");
            } else if (functionType.equals("int_array")) {
                if (row == 0) {
                    sb.append("    result = " + functionSignature + "(" + inputString + ");\n");
                    sb.append("    printf(\"[Test Case " + (i + 1) + "] Output: [\");\n");
                    sb.append("    for (int i = 0; i < " + col + "; i++) {\n");
                    sb.append("        printf(\"%d\", result[i]);\n");
                    sb.append("        if (i < " + col + " - 1) printf(\", \");\n");
                    sb.append("    }\n");
                    sb.append("    printf(\"]\\n\");\n");
                } else {
                    sb.append("    result = " + functionSignature + "(" + inputString + ");\n");
                    sb.append("    printf(\"[Test Case " + (i + 1) + "] Output: [\");\n");
                    sb.append("    for (int i = 0; i < " + row + "; i++) {\n");
                    sb.append("printf(\"[\");");
                    sb.append("    for (int j = 0; j < " + col + "; j++) {\n");
                    sb.append("        printf(\"%d\", result[i][j]);\n");
                    sb.append("        if (j < " + col + " - 1) printf(\", \");\n");

                    sb.append("    }\n");
                    sb.append("printf(\"]\");");
                    sb.append("        if (i < " + row + " - 1) printf(\", \");\n");
                    sb.append("    }\n");
                    sb.append("    printf(\"]\\n\");\n");
                }
            } else if (functionType.equals("double_array")) {
                if (row == 0) {
                    sb.append("    result = " + functionSignature + "(" + inputString + ");\n");
                    sb.append("    printf(\"[Test Case " + (i + 1) + "] Output: [\");\n");
                    sb.append("    for (int i = 0; i < " + col + "; i++) {\n");
                    sb.append("        printf(\"%.2f\", result[i]);\n");
                    sb.append("        if (i < " + col + " - 1) printf(\", \");\n");
                    sb.append("    }\n");
                    sb.append("    printf(\"]\\n\");\n");
                } else {
                    sb.append("    result = " + functionSignature + "(" + inputString + ");\n");
                    sb.append("    printf(\"[Test Case " + (i + 1) + "] Output: [\");\n");
                    sb.append("    for (int i = 0; i < " + row + "; i++) {\n");
                    sb.append("printf(\"[\");");
                    sb.append("    for (int j = 0; j < " + col + "; j++) {\n");
                    sb.append("        printf(\"%.2f\", result[i][j]);\n");
                    sb.append("        if (j < " + col + " - 1) printf(\", \");\n");

                    sb.append("    }\n");
                    sb.append("printf(\"];\")");
                    sb.append("        if (i < " + row + " - 1) printf(\", \");\n");
                    sb.append("    }\n");
                    sb.append("    printf(\"]\\n\");\n");
                }
            } else if (functionType.equals("boolean_array")) {
                if (row == 0) {
                    sb.append("    result = " + functionSignature + "(" + inputString + ");\n");
                    sb.append("    printf(\"[Test Case " + (i + 1) + "] Output: [\");\n");
                    sb.append("    for (int i = 0; i < " + col + "; i++) {\n");
                    sb.append("        printf(\"%d\", result[i]);\n");
                    sb.append("        if (i < " + col + " - 1) printf(\", \");\n");
                    sb.append("    }\n");
                    sb.append("    printf(\"]\\n\");\n");
                } else {
                    sb.append("    result = " + functionSignature + "(" + inputString + ");\n");
                    sb.append("    printf(\"[Test Case " + (i + 1) + "] Output: [\");\n");
                    sb.append("    for (int i = 0; i < " + row + "; i++) {\n");
                    sb.append("printf(\"[\");");
                    sb.append("    for (int j = 0; j < " + col + "; j++) {\n");
                    sb.append("        printf(\"%d\", result[i][j]);\n");
                    sb.append("        if (j < " + col + " - 1) printf(\", \");\n");

                    sb.append("    }\n");
                    sb.append("printf(\"];\")");
                    sb.append("        if (i < " + row + " - 1) printf(\", \");\n");
                    sb.append("    }\n");
                    sb.append("    printf(\"]\\n\");\n");
                }
            } else if (functionType.equals("char_array")) {
                if (row == 0) {
                    sb.append("    result = " + functionSignature + "(" + inputString + ");\n");
                    sb.append("    printf(\"[Test Case " + (i + 1) + "] Output: [\");\n");
                    sb.append("    for (int i = 0; i < " + col + "; i++) {\n");
                    sb.append("        printf(\"%c\", result[i]);\n");
                    sb.append("        if (i < " + col + " - 1) printf(\", \");\n");
                    sb.append("    }\n");
                    sb.append("    printf(\"]\\n\");\n");
                } else {
                    sb.append("    result = " + functionSignature + "(" + inputString + ");\n");
                    sb.append("    printf(\"[Test Case " + (i + 1) + "] Output: [\");\n");
                    sb.append("    for (int i = 0; i < " + row + "; i++) {\n");
                    sb.append("printf(\"[\");");
                    sb.append("    for (int j = 0; j < " + col + "; j++) {\n");
                    sb.append("        printf(\"%c\", result[i][j]);\n");
                    sb.append("        if (j < " + col + " - 1) printf(\", \");\n");

                    sb.append("    }\n");
                    sb.append("printf(\"]\");");
                    sb.append("        if (i < " + row + " - 1) printf(\", \");\n");
                    sb.append("    }\n");
                    sb.append("    printf(\"]\\n\");\n");
                }
            } else if (functionType.equals("string_array")) {
                if (row == 0) {
                    sb.append("    result = " + functionSignature + "(" + inputString + ");\n");
                    sb.append("    printf(\"[Test Case " + (i + 1) + "] Output: \");\n");
                    sb.append("    for (int i = 0; i < " + col + "; i++) {\n");
                    sb.append("        printf(\"%c\", result[i]);\n");
                    sb.append("    }\n");
                    sb.append("    printf(\"\\n\");\n");
                } else {
                    sb.append("    result = " + functionSignature + "(" + inputString + ");\n");
                    sb.append("    printf(\"[Test Case " + (i + 1) + "] Output: \");\n");
                    sb.append("    for (int i = 0; i < " + row + "; i++) {\n");
                    sb.append("    for (int j = 0; j < " + col + "; j++) {\n");
                    sb.append("        printf(\"%c\", result[i][j]);\n");

                    sb.append("    }\n");
                    sb.append("    }\n");
                    sb.append("    printf(\"\\n\");\n");
                }
            } else {
                sb.append("    printf(\"[Test Case " + (i + 1) + "] Error: Unsupported return type\\n\");\n");
            }
        }

        sb.append("    return 0;\n");
        sb.append("}\n");
        log.info(sb.toString());
        return sb.toString();

    }

    private static String detectReturnType(String returnType) {
        switch (returnType) {
            case "STRING":
                return "char*";
            case "CHAR":
                return "int";
            case "INT":
                return "int";
            case "DOUBLE":
                return "double";
            case "LONG":
                return "int";
            case "BOOLEAN":
                return "bool";
            case "LIST":
                return "list";
            case "MAP":
                return "map";
            case "ARR_INT":
                return "int_array";
            case "ARR_DOUBLE":
                return "double_array";
            case "ARR_OBJECT":
                return "object_array";
            case "ARR_STRING":
                return "string_array";
            case "ARR_BOOLEAN":
                return "boolean_array";
            default:
                return "unknown";
        }

    }

    // Hàm xử lý đầu vào thành chuỗi C hợp lệ
    private static String convertInputToCString(InputVariable input, List<InputVariable> inputVariables) {
        String type = input.getType().toString();
        Object value = input.getValue();
        int noDimension = 0;
        String inputStr = "";
        log.info("Variable: " + inputVariables);
        if (input != null && value.toString().length() > 0) {
            inputStr = value.toString();
            for (int i = 0; i < inputStr.length(); i++) {
                if (inputStr.charAt(i) == '[') {
                    noDimension++;
                } else {
                    break;
                }
            }
        }
        log.info(input);
        String dimension = "";
        switch (type) {
            case "CHAR":
                return "\'" + value + "\'";
            case "STRING":
                return "\"" + value + "\"";
            case "INT":
                return String.valueOf(value);
            case "DOUBLE":
                return String.format("%.6f", value);
            case "BOOLEAN":
                return Boolean.parseBoolean(String.valueOf(value)) ? "1" : "0";
            case "LIST":
                List<?> list = (List<?>) value;
                return "{" + list.stream().map(String::valueOf).collect(Collectors.joining(", ")) + "}";
            case "MAP":
                Map<?, ?> map = (Map<?, ?>) value;
                return "{ " + map.entrySet().stream()
                        .map(e -> "\"" + e.getKey() + "\": \"" + e.getValue() + "\"")
                        .collect(Collectors.joining(", ")) + " }";
            case "ARR_STRING":
                int col = 0;
                int row = 0;
                for (int i = 0; i < inputVariables.size(); i++) {
                    if (inputVariables.get(i).getName().equals("row")) {
                        row = Integer.parseInt(inputVariables.get(i).getValue().toString());
                    } else if (inputVariables.get(i).getName().equals("col")) {
                        col = Integer.parseInt(inputVariables.get(i).getValue().toString());
                    }
                }
                dimension = "(char";
                if (noDimension == 1) {
                    dimension += "[]";
                } else if (noDimension >= 2) {
                    dimension += "[" + row + "][" + col + "]";
                }
                dimension += ")";
                inputStr = formatObject(input.getValue());

                if (inputStr.length() > 0) {
                    String insideInput = inputStr.substring(1, inputStr.length() - 1);
                    insideInput = insideInput.replaceAll("\\[", "{");
                    insideInput = insideInput.replaceAll("\\]", "}");
                    insideInput = insideInput.replaceAll("\"", "\'");
                    input.setValue("{" + insideInput
                            + "}");
                }
                return dimension + input.getValue();
            case "ARR_INT":
                col = 0;
                row = 0;
                for (int i = 0; i < inputVariables.size(); i++) {
                    if (inputVariables.get(i).getName().equals("row")) {
                        row = Integer.parseInt(inputVariables.get(i).getValue().toString());
                    } else if (inputVariables.get(i).getName().equals("col")) {
                        col = Integer.parseInt(inputVariables.get(i).getValue().toString());
                    }
                }
                String insideInput = inputStr.substring(1, inputStr.length() - 1)
                        .replace("[", "{")
                        .replace("]", "}");

                // // Xác định số chiều của mảng
                // dimension = "(int";
                // for (int i = 1; i < noDimension; i++) { // Bắt đầu từ 1 vì đã có "int"
                // dimension += "[]";
                // }
                // dimension += ")";

                // // Xác định số cột nếu là mảng 2D trở lên
                // long noArray = countCharOccurrences(insideInput, '{'); // Số lần xuất hiện
                // '{'
                // long noElementInArr = (countCharOccurrences(insideInput, ',') - (noArray -
                // 1)) / noArray + 1;

                // // Nếu là mảng 2D trở lên, cần thêm số cột vào khai báo
                // if (noDimension >= 2) {
                // dimension = "(int[][" + noElementInArr + "])";
                // }

                // Tạo compound literal đúng cú pháp
                dimension = "(int";
                if (noDimension == 1) {
                    dimension += "[]";
                } else if (noDimension >= 2) {
                    dimension += "[" + row + "][" + col + "]";
                }
                dimension += ")";
                String compoundLiteral = dimension + "{" + insideInput + "}";
                return compoundLiteral;
            case "ARR_DOUBLE":
                col = 0;
                row = 0;
                for (int i = 0; i < inputVariables.size(); i++) {
                    if (inputVariables.get(i).getName().equals("row")) {
                        row = Integer.parseInt(inputVariables.get(i).getValue().toString());
                    } else if (inputVariables.get(i).getName().equals("col")) {
                        col = Integer.parseInt(inputVariables.get(i).getValue().toString());
                    }
                }
                dimension = "(double";
                if (noDimension == 1) {
                    dimension += "[]";
                } else if (noDimension >= 2) {
                    dimension += "[" + row + "][" + col + "]";
                }
                dimension += ")";
                insideInput = inputStr.substring(1, inputStr.length() - 1)
                        .replace("[", "{")
                        .replace("]", "}");
                compoundLiteral = dimension + "{" + insideInput + "}";
                return compoundLiteral;
            case "ARR_BOOLEAN":
                col = 0;
                row = 0;
                for (int i = 0; i < inputVariables.size(); i++) {
                    if (inputVariables.get(i).getName().equals("row")) {
                        row = Integer.parseInt(inputVariables.get(i).getValue().toString());
                    } else if (inputVariables.get(i).getName().equals("col")) {
                        col = Integer.parseInt(inputVariables.get(i).getValue().toString());
                    }
                }
                dimension = "(bool";
                if (noDimension == 1) {
                    dimension += "[]";
                } else if (noDimension >= 2) {
                    dimension += "[" + row + "][" + col + "]";
                }
                dimension += ")";
                insideInput = inputStr.substring(1, inputStr.length() - 1)
                        .replace("[", "{")
                        .replace("]", "}");
                compoundLiteral = dimension + "{" + insideInput + "}";
                log.info("Input: " + compoundLiteral);
                return compoundLiteral;
            case "POINTER_INT":
                return "&(int){" + value + "}";
            case "POINTER_CHAR":
                return "&(char){'" + value + "'}";
            case "POINTER_DOUBLE":
                return "&(double){" + value + "}";
            case "POINTER_BOOLEAN":
                return "&(boolean){" + value + "}";
            default:
                return String.valueOf(value);
        }
    }

    // --------------- HELPER METHODS ---------------
    private static Process runCommand(String[] command, File dir, int timeout)
            throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(dir);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        boolean finished = process.waitFor(timeout, TimeUnit.SECONDS);
        if (!finished) {
            process.destroy();
            return new ProcessBuilder("echo", "Runtime Error: Timeout").start();
        }
        return process;
    }

    private static String readOutput(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        return output.toString();
    }

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static String generateTestCaseResults(Process process, List<TestCase> testCases, String language)
            throws IOException {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        int testCaseIndex = 0;
        float startTime = System.nanoTime();
        List<TestResult> testResults = new ArrayList();
        boolean isAccepted = true;
        long pid = getProcessId(process);
        long memoryBefore = getMemoryUsage(pid);
        int noSuccessTestcase = 0;
        TestResult inputWrong = new TestResult();
        boolean flagFirstWrong = true;
        while ((line = reader.readLine()) != null) {
            String[] outputParts = line.split(": ", 2);
            if (outputParts.length == 2) {
                if (testCaseIndex < testCases.size()) {
                    Object testCaseResult = outputParts[1];
                    List<InputVariable> inputList = testCases.get(testCaseIndex).getInput();
                    Object expectedOutput = objectMapper
                            .writeValueAsString(testCases.get(testCaseIndex).getExpectedOutput());
                    Object jsonResult = gson.toJson(testCaseResult).replaceAll("\\\\", "");
                    Object realResult = jsonResult.toString().substring(1, jsonResult.toString().length() - 1);
                    String result = "";
                    if (language.equals("java")) {
                        if (gson.toJson(realResult.toString().replaceAll(" ", ""))
                                .equals(gson.toJson(expectedOutput.toString().replaceAll(" ", "")))) {
                            result = "Success";
                            noSuccessTestcase++;
                        } else {
                            result = "Failed";
                            isAccepted = false;
                            if (flagFirstWrong) {
                                inputWrong = new TestResult(testCaseIndex + 1, inputList, expectedOutput, result,
                                        realResult);
                                flagFirstWrong = false;
                            }
                        }
                    } else {
                        expectedOutput = expectedOutput.toString().replaceAll("\"", "");
                        if (gson.toJson(realResult.toString().replaceAll(" ", ""))
                                .equals(gson.toJson(expectedOutput.toString().replaceAll(" ", "")))) {
                            result = "Success";
                            noSuccessTestcase++;
                        } else {
                            result = "Failed";
                            isAccepted = false;
                            if (flagFirstWrong) {
                                inputWrong = new TestResult(testCaseIndex + 1, inputList, expectedOutput, result,
                                        realResult);
                                flagFirstWrong = false;
                            }
                        }
                    }
                    testResults.add(new TestResult(testCaseIndex + 1, inputList, expectedOutput, result, realResult));
                    testCaseIndex++;
                }
            }
        }
        float endTime = System.nanoTime();
        float timeTaken = ((endTime - startTime) / 1000000);
        // Tính mức sử dụng bộ nhớ cực đại (đổi sang MB)
        long total = Runtime.getRuntime().totalMemory();
        long used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        ResponseResult result = new ResponseResult(isAccepted, testResults, String.format("%.2f", timeTaken),
                used / (1024 * 1024),
                noSuccessTestcase,
                inputWrong);
        return gson.toJson(result);

    }

    private static long getProcessId(Process process) {
        try {
            Field f = process.getClass().getDeclaredField("pid");
            f.setAccessible(true);
            return f.getLong(process);
        } catch (Exception e) {
            return -1; // Lỗi không lấy được PID
        }
    }

    private static long getMemoryUsage(long pid) {
        if (pid == -1)
            return 0;

        try {
            Process memoryProcess;
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                // Windows: Dùng tasklist
                memoryProcess = new ProcessBuilder("tasklist", "/FI", "PID eq " + pid, "/FO", "CSV", "/NH").start();
            } else {
                // Linux/macOS: Dùng ps (RSS - Resident Set Size)
                memoryProcess = new ProcessBuilder("ps", "-o", "rss=", "-p", String.valueOf(pid)).start();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(memoryProcess.getInputStream()));
            String line = reader.readLine();
            if (line != null && line.trim().length() > 0) {
                long memoryKB = Long.parseLong(line.trim().replaceAll("[^0-9]", "")); // Lấy số
                return memoryKB / 1024; // Đổi từ KiB -> MiB
            }
        } catch (Exception ignored) {
        }

        return 0;
    }

    public static void copyGsonJar(File tempDir) throws IOException {
        // Đường dẫn thư mục chứa file gson-2.11.0.jar
        Path sourcePath = Paths.get("target/dependency/gson-2.11.0.jar");

        // Tạo thư mục libs trong tempDir nếu chưa có
        File libsDir = new File(tempDir, "libs");
        if (!libsDir.exists()) {
            libsDir.mkdirs();
        }

        // Đường dẫn đích trong thư mục tempDir
        Path destPath = Paths.get(libsDir.getAbsolutePath(), "gson-2.11.0.jar");

        // Sao chép file
        Files.copy(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);
    }
}
