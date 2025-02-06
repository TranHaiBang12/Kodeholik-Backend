package com.g44.kodeholik.util.encoder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {

    public static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    public static String encodePassword(String password) {
        return encoder.encode(password);
    }
}
