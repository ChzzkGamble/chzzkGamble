package com.chzzkGamble.utils;

import java.util.regex.Pattern;

public class EmojiParser {

    // 주요 이모지 유니코드 블록
    private static final String EMOTICONS = "[\uD83D\uDE00-\uD83D\uDE4F]"; // U+1F600–U+1F64F
    private static final String MISC_SYMBOLS = "[\u2600-\u26FF]"; // U+2600–U+26FF
    private static final String MISC_PICTOGRAPHS = "[\uD83C\uDF00-\uD83C\uDFFF]"; // U+1F300–U+1F5FF
    private static final String TRANSPORT_MAP = "[\uD83D\uDE80-\uD83D\uDEFF]"; // U+1F680–U+1F6FF
    private static final String SUPPLEMENTAL_PICTOGRAPHS = "[\uD83E\uDD00-\uD83E\uDFFF]"; // U+1F900–U+1F9FF
    private static final String GEOMETRIC_SHAPES = "[\u25A0-\u25FF]"; // U+25A0–U+25FF
    private static final String FLAGS = "[\uD83C\uDDE6-\uD83C\uDDFF]"; // U+1F3C1–U+1F3FF

    // 이모지 패턴
    private static final Pattern EMOJI_REGEX = Pattern.compile(EMOTICONS + "|" + MISC_SYMBOLS + "|" + MISC_PICTOGRAPHS + "|" +
            TRANSPORT_MAP + "|" + SUPPLEMENTAL_PICTOGRAPHS + "|" + GEOMETRIC_SHAPES + "|" + FLAGS);

    public static String removeEmojis(String input) {
        return EMOJI_REGEX.matcher(input)
                .replaceAll("");
    }
}
