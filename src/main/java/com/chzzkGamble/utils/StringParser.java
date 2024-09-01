package com.chzzkGamble.utils;

public class StringParser {

    static String parseFromTo(String text, char from, char to) {
        if (from == to) {
            throw new IllegalArgumentException("같은 char로 파싱할 수 없습니다 : " + from);
        }

        int indexFrom = text.indexOf(from);
        int indexTo = text.indexOf(to);

        if (indexFrom < 0 || indexTo < 0 || indexFrom >= indexTo) {
            throw new IllegalArgumentException("해당 문자열을 파싱할 수 없습니다 : " + text);
        }
        if (indexFrom + 1 == indexTo) {
            throw new IllegalArgumentException("구분 기호 내 문자열이 비어있습니다.");
        }

        return text.substring(indexFrom + 1, indexTo);
    }
}
