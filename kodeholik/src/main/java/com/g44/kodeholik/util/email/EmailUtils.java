package com.g44.kodeholik.util.email;

public class EmailUtils {
    public static boolean isFptEduEmail(String email) {
        if (email == null || !email.contains("@")) {
            return false;
        }

        // Tách domain từ email
        String domain = email.substring(email.lastIndexOf("@") + 1);

        // Kiểm tra domain có phải "fpt.edu.vn" không
        return "fpt.edu.vn".equalsIgnoreCase(domain);
    }
}
