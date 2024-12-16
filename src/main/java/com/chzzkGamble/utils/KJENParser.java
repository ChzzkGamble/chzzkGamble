package com.chzzkGamble.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KJENParser {

    private static final String ENGLISH_UPPERCASE = "[A-Z]";
    private static final String ENGLISH_LOWERCASE = "[a-z]";
    private static final String DIGITS = "[0-9]";
    private static final String BASIC_PUNCTUATION = "[\u0020-\u002F\u003A-\u0040\u007B-\u007E]";
    private static final String HANGUL_JAMO = "[\u1100-\u11FF]";
    private static final String HANGUL_COMPATIBILITY_JAMO = "[\u3130-\u318F]";
    private static final String HANGUL_SYLLABIC_BLOCKS = "[가-힣]";
    private static final String HIRAGANA = "[\u3040-\u309F]";
    private static final String KATAKANA = "[\u30A0-\u30FF]";
    private static final String KANA_EXTENDED = "[\u31F0-\u31FF]";
    private static final String CJK_IDEOGRAPHS = "\u4E00-\u9FFF";
    private static final String CJK_IDEOGRAPHS_EXTENSION_A = "\u3400-\u4DBF";
    private static final String CJK_IDEOGRAPHS_EXTENSION_B = "[\uD842\uDC00-\uD87F\uDFFF]";
    private static final String CJK_IDEOGRAPHS_EXTENSION_C = "[\uD88F\uDC00-\uD89F\uDFFF]";
    private static final String CJK_IDEOGRAPHS_EXTENSION_D = "[\uD89F\uDC00-\uD8AF\uDFFF]";
    private static final String CJK_IDEOGRAPHS_BRACKET = "[\u3008-\u3011\u3016-\u3017]";
    private static final String FULL_WIDTH_AND_HALF_WIDTH = "[\uFF01-\uFF9F]";

    private static final Pattern KJEN_REGEX = Pattern.compile(ENGLISH_UPPERCASE +
            "|" + ENGLISH_LOWERCASE +
            "|" + DIGITS +
            "|" + BASIC_PUNCTUATION +
            "|" + HANGUL_JAMO +
            "|" + HANGUL_COMPATIBILITY_JAMO +
            "|" + HANGUL_SYLLABIC_BLOCKS +
            "|" + HIRAGANA +
            "|" + KATAKANA +
            "|" + KANA_EXTENDED +
            "|" + CJK_IDEOGRAPHS +
            "|" + CJK_IDEOGRAPHS_EXTENSION_A +
            "|" + CJK_IDEOGRAPHS_EXTENSION_B +
            "|" + CJK_IDEOGRAPHS_EXTENSION_C +
            "|" + CJK_IDEOGRAPHS_EXTENSION_D +
            "|" + CJK_IDEOGRAPHS_BRACKET +
            "|" + FULL_WIDTH_AND_HALF_WIDTH
    );

    public static String extractKJEN(String input) {
        StringBuilder sb = new StringBuilder();
        Matcher matcher = KJEN_REGEX.matcher(input);
        while (matcher.find()) {
            sb.append(matcher.group());
        }
        return sb.toString();
    }
}
