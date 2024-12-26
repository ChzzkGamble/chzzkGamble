package com.chzzkGamble.utils;

public class WordDivider {

    public static String[] divideSpace(String s, int startIndex) {
        int index = findNearestSpaceIndex(s, startIndex);

        return new String[]{s.substring(0, index), s.substring(index)};
    }

    private static int findNearestSpaceIndex(String s, int startIndex) {
        if (s.length() <= startIndex || !s.contains(" ")) {
            return s.length();
        }

        int left = startIndex;
        int right = startIndex;
        while (left >= 0 || right < s.length()) {
            if (left >= 0) {
                if (s.charAt(left) == ' ') {
                    return left;
                }
                left--;
            }

            if (right < s.length()) {
                if (s.charAt(right) == ' ') {
                    return right;
                }
                right++;
            }
        }
        return s.length();
    }
}
