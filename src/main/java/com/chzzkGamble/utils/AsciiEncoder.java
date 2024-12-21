package com.chzzkGamble.utils;

import java.util.HashMap;
import java.util.Map;

public class AsciiEncoder {

    private static final Map<Character, String> ASCII_ENCODINGS = new HashMap<>();

    static {
        ASCII_ENCODINGS.put(' ', "%20");
        ASCII_ENCODINGS.put('!', "%21");
        ASCII_ENCODINGS.put('"', "%22");
        ASCII_ENCODINGS.put('#', "%23");
        ASCII_ENCODINGS.put('$', "%24");
        ASCII_ENCODINGS.put('%', "%25");
        ASCII_ENCODINGS.put('&', "%26");
        ASCII_ENCODINGS.put('\'', "%27");
        ASCII_ENCODINGS.put('(', "%28");
        ASCII_ENCODINGS.put(')', "%29");
        ASCII_ENCODINGS.put('*', "%2A");
        ASCII_ENCODINGS.put('+', "%2B");
        ASCII_ENCODINGS.put(',', "%2C");
        ASCII_ENCODINGS.put('-', "%2D");
        ASCII_ENCODINGS.put('.', "%2E");
        ASCII_ENCODINGS.put('/', "%2F");
        ASCII_ENCODINGS.put('{', "%7B");
        ASCII_ENCODINGS.put('|', "%7C");
        ASCII_ENCODINGS.put('}', "%7D");
    }

    public static String encode(String s, int limit) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(s.length(), limit); i++) {
            char c = s.charAt(i);
            if (ASCII_ENCODINGS.containsKey(c)) {
                sb.append(ASCII_ENCODINGS.get(c));
                continue;
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
