package com.g44.kodeholik.util.password;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {

    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()_+-=[]{}|;:,.<>?";

    private static final String ALL_CHARS = UPPERCASE + LOWERCASE + DIGITS + SPECIAL_CHARS;
    private static final SecureRandom random = new SecureRandom();

    private static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    public static String encodePassword(String password) {
        return encoder.encode(password);
    }

    public static boolean verifyPassword(String password, String encryptedPassword) {
        return encoder.matches(password, encryptedPassword);
    }

    public static String generatePassword() {
        // Đảm bảo mật khẩu có ít nhất 1 ký tự từ mỗi loại
        List<Character> passwordChars = new ArrayList<>();
        passwordChars.add(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        passwordChars.add(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        passwordChars.add(DIGITS.charAt(random.nextInt(DIGITS.length())));
        passwordChars.add(SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length())));

        // Thêm các ký tự ngẫu nhiên còn lại
        for (int i = 4; i < 20; i++) {
            passwordChars.add(ALL_CHARS.charAt(random.nextInt(ALL_CHARS.length())));
        }

        // Xáo trộn danh sách để mật khẩu trông ngẫu nhiên hơn
        Collections.shuffle(passwordChars);

        // Chuyển danh sách thành chuỗi
        StringBuilder password = new StringBuilder();
        for (char c : passwordChars) {
            password.append(c);
        }

        return password.toString();
    }
}
