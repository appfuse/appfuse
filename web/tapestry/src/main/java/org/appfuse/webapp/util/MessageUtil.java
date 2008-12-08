package org.appfuse.webapp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtil {

    private static String REGEX = "\\{\\d+\\}";
    private static String REPLACE = "%s";

    private static Pattern pattern = Pattern.compile(REGEX);

    private MessageUtil() {
    }

    /**
     * Convert from MesssageFormat to String.format
     *
     * @param message
     * @return reformatted string
     */
    public static String convert(String message) {
        StringBuffer sb = new StringBuffer();
        Matcher m = pattern.matcher(message);
        while (m.find()) {
            m.appendReplacement(sb, REPLACE);
        }
        m.appendTail(sb);
        return sb.toString();
	}
}
