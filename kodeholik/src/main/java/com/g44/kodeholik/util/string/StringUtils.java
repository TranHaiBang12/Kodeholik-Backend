package com.g44.kodeholik.util.string;

public class StringUtils {
    public static String removeSpecialChars(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("[^a-zA-Z0-9 ]", ""); // Chỉ giữ lại chữ cái và số
    }
}
