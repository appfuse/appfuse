package org.appfuse.webapp.util;

import static java.lang.String.format;

/**
 * An util class with assertion if long cannot be safely cast to int.
 *
 * Required to merge Wicket API with operations on a List class.
 *
 * @author Marcin Zajączkowski, 2012-11-11
 */
public class NumberRangeUtil {

    private NumberRangeUtil() {
        //utility class
    }

    public static void checkIfLongWithinIntegerRange(long... numberToCheck) {
        for (long l : numberToCheck) {
            checkIfLongWithinIntegerRange(l);
        }
    }

    public static void checkIfLongWithinIntegerRange(long numberToCheck) {
        if (numberToCheck > Integer.MAX_VALUE || numberToCheck < Integer.MIN_VALUE) {
            throw new IllegalArgumentException(
                    format("A long value (%d) is not within an integer range", numberToCheck));
        }
    }
}
