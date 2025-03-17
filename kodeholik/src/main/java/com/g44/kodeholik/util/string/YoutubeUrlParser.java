package com.g44.kodeholik.util.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeUrlParser {
    private static final String YOUTUBE_REGEX = "(?:https?:\\/\\/)?(?:www\\.|m\\.)?(?:youtube\\.com\\/watch\\?v=|youtu\\.be\\/|youtube\\.com\\/embed\\/|youtube\\.com\\/shorts\\/|youtube\\.com\\/v\\/|youtube\\.com\\/live\\/|youtube\\.com\\/playlist\\?list=)([a-zA-Z0-9_-]{11})";

    public static String extractVideoId(String youtubeUrl) {
        Pattern pattern = Pattern.compile(YOUTUBE_REGEX);
        Matcher matcher = pattern.matcher(youtubeUrl);
        return matcher.find() ? matcher.group(1) : null;
    }

}
