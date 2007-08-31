package org.appfuse.tool;

import java.util.regex.Pattern;

/**
 * This class contains methods than can be used in
 * Freemarker templates for String manipulation.
 */
public class StringUtils {
    private static final Pattern ES = Pattern.compile("^.*(sh|ss|ch|o|i)$");
    private static final Pattern ICES = Pattern.compile("^.*(ex|ix)$");
    private static final Pattern NOT_VOWEL_Y = Pattern.compile("^.*[^aeiou]y$");

    public String getPluralForWord(final String word) {
        String plural = null;

        if (StringUtils.isNotBlank(word)) {
            plural = word + "s";

            if (ES.matcher(word).matches()) {
                plural = word + "es";
            } else if (NOT_VOWEL_Y.matcher(word).matches()) {
                String stripY = word.substring(0, word.length() - 1);
                plural = stripY + "ies";
            } else if (ICES.matcher(word).matches()) {
                String strip_X = word.substring(0, word.length() - 2);
                plural = strip_X + "ices";
            }
        }

        return plural;
    }

    private static boolean isNotBlank(String word) {
        return word != null && word.trim().length() > 0;
    }
}
