package io.recepkara.project.utils;

public class StringUtils {

    /**
     * Copy of method: indexOfIgnoreCase in
     * <a href="https://github.com/apache/commons-lang/blob/master/src/main/java/org/apache/commons/lang3/StringUtils.java">StringUtils</a>
     *
     */
    public static int indexOfIgnoreCase(String baseString, String searchString, int fromPosition) {
        if (baseString == null || searchString == null) {
            return -1;
        }
        if (fromPosition < 0) {
            fromPosition = 0;
        }
        final int endLimit = baseString.length() - searchString.length() + 1;

        if (fromPosition > endLimit) {
            return -1;
        }
        if (searchString.length() == 0) {
            return fromPosition;
        }
        for (int i = fromPosition; i < endLimit; i++) {
            if (baseString.regionMatches(true, i, searchString, 0, searchString.length())) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(StringUtils.indexOfIgnoreCase(" RECEP KARA ", "KAR", 0));
    }
}
