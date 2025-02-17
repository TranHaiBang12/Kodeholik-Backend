package com.g44.kodeholik.service;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;

import com.g44.kodeholik.model.dto.request.lambda.InputVariable;
import com.g44.kodeholik.model.dto.request.lambda.ResponseResult;
import com.g44.kodeholik.model.dto.request.lambda.TestCase;
import com.g44.kodeholik.model.dto.request.lambda.TestResult;
import com.g44.kodeholik.model.enums.problem.InputType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.management.OperatingSystemMXBean;

import lombok.extern.log4j.Log4j2;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Log4j2
public class CompileService {
    private static Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    private static int TimeLimit = 5;

    private static String convertToJavaSyntax(Object input, String type) {
        if (type.equals(InputType.STRING.toString())) {
            return "\"" + input + "\""; // Chuỗi cần có dấu nháy
        } else if (type.equals(InputType.CHAR.toString())) {
            return "'" + input + "'"; // Ký tự cần có dấu nháy đơn
        } else if (type.equals(InputType.INT.toString()) || type.equals(InputType.DOUBLE.toString())
                || type.equals(InputType.LONG.toString())
                || type.equals(InputType.BOOLEAN.toString())) {
            return String.valueOf(input); // Số hoặc boolean giữ nguyên
        } else if (type.equals(InputType.LIST.toString())) {
            return "Arrays.asList(" + convertListToJavaSyntax((List<?>) input) + ")";
        } else if (type.equals(InputType.MAP.toString())) {
            // Nếu là HashMap, cần tạo new HashMap<>()
            return convertMapToJavaSyntax((Map<?, ?>) input);
        } else if (type.equals(InputType.ARR_INT.toString())) {
            List<?> list = (List<?>) input;
            int[] array = list.stream().mapToInt(o -> ((Number) o).intValue()).toArray();
            return "new int[]{" + Arrays.stream(array)
                    .mapToObj(String::valueOf)
                    .collect(Collectors.joining(", ")) + "}";
        } else if (type.equals(InputType.ARR_DOUBLE.toString())) {
            List<?> list = (List<?>) input;
            double[] array = list.stream().mapToDouble(o -> ((Number) o).doubleValue()).toArray();
            return "new double[]{" + Arrays.stream(array)
                    .mapToObj(String::valueOf)
                    .collect(Collectors.joining(", ")) + "}";
        } else if (type.equals(InputType.ARR_STRING.toString())) { // ✅ Thêm xử lý ARR_STRING
            List<?> list = (List<?>) input;
            return "new String[]{" + list.stream()
                    .map(obj -> "\"" + obj + "\"") // Bao quanh mỗi phần tử bằng dấu nháy kép
                    .collect(Collectors.joining(", ")) + "}";
        } else if (type.equals(InputType.ARR_OBJECT.toString())) {
            return "new Object[]{" + Arrays.stream((Object[]) input)
                    .map(obj -> convertToJavaSyntax(obj, detectType(obj))) // Xác định kiểu phần tử
                    .collect(Collectors.joining(", ")) + "}";
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

        if (input instanceof LinkedHashMap) {
            // Nếu input là LinkedHashMap, trích xuất tất cả các giá trị
            LinkedHashMap<?, ?> map = (LinkedHashMap<?, ?>) input;
            a = "new " + type + "(" + map.entrySet().stream()
                    .map(entry -> convertToJavaSyntax(entry.getValue(), detectType(entry.getValue()))) // Kết hợp
                                                                                                       // key-value
                    .collect(Collectors.joining(", ")) + ")";
        } else if (input instanceof Object[]) {
            // Nếu input là mảng Object
            a = "new " + type + "(" + Arrays.stream((Object[]) input)
                    .map(obj -> convertToJavaSyntax(obj, detectType(obj))) // Chuyển mỗi phần tử thành chuỗi
                    .collect(Collectors.joining(", ")) + ")";
        } else {
            // Xử lý các kiểu khác nếu cần
            a = "Unsupported type!";
        }

        return a; // Trả về chuỗi kết quả
    }

    private static String convertListToJavaSyntax(List<?> list) {
        return list.stream()
                .map(obj -> convertToJavaSyntax(obj, "LIST"))
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
        File tempDir = new File("/tmp/compileCode_" + uniqueId);
        if (!tempDir.exists())
            tempDir.mkdirs();
        String result;
        if ("java".equalsIgnoreCase(language)) {
            result = compileAndRunJava(userCode, testCases, tempDir, functionSignature);
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
            String functionSignature)
            throws IOException, InterruptedException {
        File sourceFile = new File(tempDir, "Main.java");
        copyGsonJar(tempDir);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(sourceFile))) {
            writer.write(generateJavaProgram(userCode, testCases, functionSignature));
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
            String functionSignature) {
        StringBuilder sb = new StringBuilder();
        sb.append("import java.io.*;\n");
        sb.append("import com.google.gson.Gson;\n");
        sb.append("import java.util.*;\n");
        sb.append("public class Main {\n");
        sb.append("    public static void main(String[] args) {\n");
        sb.append("        Gson gson = new Gson();\n");
        for (int i = 0; i < testCases.size(); i++) {

            List<InputVariable> inputs = testCases.get(i).getInput();
            Gson gson = new Gson();
            sb.append(" try {\n");
            sb.append("         Object result = " + functionSignature + "(");
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
        sb.append(userCode);
        sb.append("\n\nint main() {\n");
        // Xác định kiểu dữ liệu của hàm
        String functionType = detectReturnType(returnType);
        if (functionType.equals("int") || functionType.equals("long") || functionType.equals("boolean")
                || functionType.equals("char")) {
            sb.append("    int result;\n");
        } else if (functionType.equals("double")) {
            sb.append("    double result;\n");
        } else if (functionType.equals("char*")) {
            sb.append("    char* result;\n");
        } else if (functionType.equals("int_array")) {
            sb.append("    int* result;\n");
        } else if (functionType.equals("double_array")) {
            sb.append("    double* result;\n");
        } else if (functionType.equals("string_array")) {
            sb.append("    char** result;\n");
        } else if (functionType.equals("object_array")) {
            sb.append("    struct Object* result;\n");
        } else {
            sb.append("    printf(\"Error: Unsupported return type\\n\");\n");
        }

        for (int i = 0; i < testCases.size(); i++) {
            List<InputVariable> inputs = testCases.get(i).getInput();
            int size = 0;
            try {
                for (int j = 0; j < inputs.size(); j++) {
                    if (inputs.get(j).getName().equals("size")) {
                        size = Integer.parseInt(inputs.get(j).getValue().toString());
                    }
                }
            } catch (Exception e) {
                sb.append("    printf(\"Size must be an integer\n\");\n");
            }
            // Chuyển đổi danh sách tham số thành chuỗi đầu vào
            String inputString = inputs.stream()
                    .filter(input -> !"size".equals(input.getName()))
                    .map(input -> convertInputToCString(input))
                    .collect(Collectors.joining(", "));

            // Gọi hàm và xử lý kết quả dựa trên kiểu trả về
            if (functionType.equals("int") || functionType.equals("long") || functionType.equals("char")) {
                sb.append("    result = " + functionSignature + "(" + inputString + ");\n");
                sb.append("    printf(\"[Test Case " + (i + 1) + "] Output: %d\\n\", result);\n");
            } else if (functionType.equals("double")) {
                sb.append("    result = " + functionSignature + "(" + inputString + ");\n");
                sb.append("    printf(\"[Test Case " + (i + 1) + "] Output: %f\\n\", result);\n");
            } else if (functionType.equals("char*")) {
                sb.append("    result = " + functionSignature + "(" + inputString + ");\n");
                sb.append("    if (result) printf(\"[Test Case " + (i + 1) + "] Output: %s\\n\", result);\n");
            } else if (functionType.equals("boolean")) {
                sb.append("    result = " + functionSignature + "(" + inputString + ");\n");
                sb.append(
                        "    printf(\"[Test Case " + (i + 1) + "] Output: %s\\n\", result ? \"true\" : \"false\");\n");
            } else if (functionType.equals("int_array")) {
                sb.append("    result = " + functionSignature + "(" + inputString + ");\n");
                sb.append("    printf(\"[Test Case " + (i + 1) + "] Output: [\");\n");
                sb.append("    for (int i = 0; i < " + size + "; i++) {\n");
                sb.append("        printf(\"%d\", result[i]);\n");
                sb.append("        if (i < " + size + " - 1) printf(\", \");\n");
                sb.append("    }\n");
                sb.append("    printf(\"]\\n\");\n");
            } else if (functionType.equals("double_array")) {
                sb.append("    result = " + functionSignature + "(" + inputString + ");\n");
                sb.append("    printf(\"[Test Case " + (i + 1) + "] Output: [\");\n");
                sb.append("    for (int i = 0; i < " + size + "; i++) {\n");
                sb.append("        printf(\"%.1f\", result[i]);\n");
                sb.append("        if (i < " + size + " - 1) printf(\", \");\n");
                sb.append("    }\n");
                sb.append("    printf(\"]\\n\");\n");
            } else if (functionType.equals("string_array")) {
                sb.append("    result = " + functionSignature + "(" + inputString + ");\n");
                sb.append("    printf(\"[Test Case " + (i + 1) + "] Output: [\");\n");
                sb.append("    for (int i = 0; result[i] != NULL; i++) {\n");
                sb.append("        printf(\"%s\", result[i]);\n");
                sb.append("        if (result[i + 1] != NULL) printf(\", \");\n");
                sb.append("    }\n");
                sb.append("    printf(\"]\\n\");\n");
            } else if (functionType.equals("object_array")) {
                sb.append("    result = " + functionSignature + "(" + inputString + ");\n");
                sb.append("    printf(\"[Test Case " + (i + 1) + "] Output: [\");\n");
                sb.append("    for (int i = 0; i < " + size + "; i++) {\n");
                sb.append("        printf(\"{name: %s, age: %d}\", result[i].name, result[i].age);\n");
                sb.append("        if (i < " + size + " - 1) printf(\", \");\n");
                sb.append("    }\n");
                sb.append("    printf(\"]\\n\");\n");
            } else {
                sb.append("    printf(\"[Test Case " + (i + 1) + "] Error: Unsupported return type\\n\");\n");
            }
        }

        sb.append("    return 0;\n");
        sb.append("}\n");

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
            default:
                return "unknown";
        }

    }

    // Hàm xử lý đầu vào thành chuỗi C hợp lệ
    private static String convertInputToCString(InputVariable input) {
        String type = input.getType().toString();
        Object value = input.getValue();

        switch (type) {
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
                return "{ " + Arrays.stream((Object[]) value)
                        .map(item -> "\"" + item + "\"")
                        .collect(Collectors.joining(", ")) + " }";
            case "ARR_INT":
                return "{ " + Arrays.stream((Object[]) value)
                        .map(String::valueOf)
                        .collect(Collectors.joining(", ")) + " }";
            case "ARR_DOUBLE":
                return "{ " + Arrays.stream((Object[]) value)
                        .map(item -> String.format("%.6f", item))
                        .collect(Collectors.joining(", ")) + " }";
            case "POINTER":
                return "&" + value;
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
                    Object expectedOutput = testCases.get(testCaseIndex).getExpectedOutput();
                    String jsonResult = gson.toJson(testCaseResult).replaceAll("\\\\", "");
                    String realResult = jsonResult.substring(1, jsonResult.length() - 1);
                    String result = "";
                    if (language.equals("java")) {
                        log.info(realResult + " " + expectedOutput);
                        if (realResult.equals(expectedOutput)) {
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
                        if (realResult.equals(expectedOutput.toString())) {
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

        System.out.println("Gson JAR copied to: " + destPath.toString());
    }
}
