package com.g44.kodeholik.util.validation;

import java.util.regex.Pattern;

public class Validation {
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&+=])[a-zA-Z0-9!@#$%^&+=]{8,20}$";

    private static final Pattern PASSWORD_REGEX = Pattern.compile(PASSWORD_PATTERN);

    // Regex kiểm tra email
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private static final Pattern EMAIL_REGEX = Pattern.compile(EMAIL_PATTERN);

    // Hàm kiểm tra mật khẩu hợp lệ
    public static boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }
        return PASSWORD_REGEX.matcher(password).matches();
    }

    // Hàm kiểm tra email hợp lệ
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        return EMAIL_REGEX.matcher(email).matches();
    }
}
