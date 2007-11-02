package org.appfuse.tool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.mapping.Column;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * This class is used to generate default data for tests, as well as manipulate
 * Strings for Freemarker templates.
 * 
 * @author mraible
 */
public class DataHelper {
    private static final Log log = LogFactory.getLog(DataHelper.class);
    private static String datePattern = "yyyy-MM-dd";
    private static String uiDatePattern = getDatePattern();
    
    /**
     * Generate a random value in a format that makes DbUnit happy.
     * @param column the column (i.e. "java.lang.String")
     * @return a generated string for the particular type
     */
    public String getTestValueForDbUnit(Column column) {
        StringBuffer result = new StringBuffer();
        String type = column.getValue().getType().getReturnedClass().getName();

        if ("java.lang.Integer".equals(type) || "int".equals(type)) {
            result.append((int) ((Math.random() * Integer.MAX_VALUE)) );
        } else if ("java.lang.Float".equals(type) || "float".equals(type)) {
            result.append((float) ((Math.random() * Float.MAX_VALUE)));
        } else if ("java.lang.Long".equals(type) || "long".equals(type)) {
            result.append((long) ((Math.random() * Long.MAX_VALUE)));
        } else if ("java.lang.Double".equals(type) || "double".equals(type)) {
            result.append((double) ((Math.random() * Double.MAX_VALUE)));
        } else if ("java.lang.Short".equals(type) || "short".equals(type)) {
            result.append((short) ((Math.random() * Short.MAX_VALUE)));
        } else if ("java.lang.Byte".equals(type) || "byte".equals(type)) {
            result.append((byte) ((Math.random() * Byte.MAX_VALUE)));
        } else if ("java.lang.Boolean".equals(type) || "boolean".equals(type)) {
            result.append("0");
        } else if ("java.util.Date".equals(type) || "java.sql.Date".equals(type)) {
            result.append(getDate(new Date()));
        } else if ("java.sql.Timestamp".equals(type)) {
            result.append(new Timestamp(new Date().getTime()).toString());
        } else { // default to String for everything else
            String stringWithQuotes = generateStringValue(column);
            result.append(stringWithQuotes.substring(1, stringWithQuotes.length()-1));
        }

        return result.toString();
    }

    /**
     * Method to generate a random value for use in setting values in a Java test
     * @param column the type of object (i.e. "java.util.Date")
     * @return The string-ified version of the type
     */
    public String getValueForJavaTest(Column column) {
        StringBuffer result = new StringBuffer();
        String type = column.getValue().getType().getReturnedClass().getName();
        
        if ("java.lang.Integer".equals(type)) {
            result.append((int) ((Math.random() * Integer.MAX_VALUE)));
        } else if ("int".equals(type)) {
            result.append("(int) ").append((int) ((Math.random() * Integer.MAX_VALUE)));
        } else if ("java.lang.Float".equals(type) ) {
            result.append("new Float(").append((float) ((Math.random() * Float.MAX_VALUE))).append(")");
        } else if ("float".equals(type)) {
            result.append("(float) ").append((float) ((Math.random() * Float.MAX_VALUE)));
        } else if ("java.lang.Long".equals(type)) {
            // not sure why, but Long.MAX_VALUE results in too large a number
            result.append(Math.random() * Integer.MAX_VALUE).append("L");
        } else if ("long".equals(type)) {
            // not sure why, but Long.MAX_VALUE results in too large a number
            result.append((long) ((Math.random() * Integer.MAX_VALUE)));
        } else if ("java.lang.Double".equals(type)) {
            result.append("new Double(").append((Math.random() * Double.MAX_VALUE)).append(")");
        } else if ("double".equals(type)) {
            result.append((Math.random() * Double.MAX_VALUE));
        } else if ("java.lang.Short".equals(type)) {
            result.append("new Short(\"").append((short) ((Math.random() * Short.MAX_VALUE))).append("\")");
        } else if ("short".equals(type)) {
            result.append("(short)").append((short) ((Math.random() * Short.MAX_VALUE)));
        } else if ("java.lang.Byte".equals(type)) {
            result.append("new Byte(\"").append((byte) ((Math.random() * Byte.MAX_VALUE))).append("\")");
        } else if ("byte".equals(type)) {
            result.append("(byte) ").append((byte) ((Math.random() * Byte.MAX_VALUE)));
        } else if ("java.lang.Boolean".equals(type)) {
            result.append("Boolean.FALSE");
        } else if ("boolean".equals(type)) {
            result.append("false");
        } else if ("java.util.Date".equals(type)) {
            result.append("new java.util.Date()");
        } else if ("java.sql.Date".equals(type)) {
            result.append("new java.sql.Date()");
        } else if ("java.sql.Timestamp".equals(type)) {
            result.append("java.sql.Timestamp.valueOf(\"")
                    .append(new Timestamp(new Date().getTime()).toString()).append("\")");
        } else { // default to String for everything else
            result.append(generateStringValue(column));
        }
        
        return result.toString();
    }

    /**
     * Method to generate a random value for use in setting WebTest parameters
     * @param column the type of object (i.e. "java.util.Date")
     * @return The string-ified version of the date
     */
    public String getValueForWebTest(Column column) {
        String type = column.getValue().getType().getReturnedClass().getName();
        String value = getTestValueForDbUnit(column);
        if (type.equalsIgnoreCase(Date.class.getName())) {
            value = getDate(new Date(), uiDatePattern);
        } else if ("boolean".equals(type) || "java.lang.Boolean".equals(type)) {
            value = "true";
        }

        return value;
    }

    private String generateStringValue(Column column) {
        int maxLen = column.getLength();
        if (maxLen > 5000) {
            log.warn("Column length greater than 5000 characters for '" + column.getName() +
                    "', setting maxlength to 5000.");
            maxLen = 5000;
        }
        
        StringBuffer result = new StringBuffer("\"");

        for (int i = 0; (i < maxLen); i++) {
            int j = 0;
            if (i % 2 == 0) {
                j = (int) ((Math.random() * 26) + 65);
            } else {
                j = (int) ((Math.random() * 26) + 97);
            }
            result.append(Character.toString((char) j));
        }


        result.append("\"");

        return result.toString();
    }

    public String generateRandomStringValue(Column column) {
        return "\"\" + Math.random()";
    }

    private static String getDate(Date aDate) {
        return getDate(aDate, datePattern);
    }

    private static String getDate(Date aDate, String pattern) {
        SimpleDateFormat df;
        String returnValue = "";

        if (aDate != null) {
            df = new SimpleDateFormat(pattern);
            returnValue = df.format(aDate);
        }

        return returnValue;
    }

    /**
     * Return default datePattern (MM/dd/yyyy)
     * @return a string representing the date pattern on the UI
     */
    public static synchronized String getDatePattern() {
        String result;
        try {
            result = ResourceBundle.getBundle("ApplicationResources", Locale.getDefault())
                .getString("date.format");
        } catch (MissingResourceException mse) {
            result = "MM/dd/yyyy";
        }
        return result;
    }

    /**
     * Parse a field name and convert it to a titled set of words.
     * @param fieldName the pojo.property
     * @return A string suitable for i18n
     */
    public String getFieldDescription(String fieldName) {
        StringBuffer buffer = new StringBuffer();
        boolean nextUpper = false;
        for (int i = 0; i < fieldName.length(); i++) {
            char c = fieldName.charAt(i);

            if (i == 0) {
                buffer.append(Character.toUpperCase(c));
                continue;
            }

            if (Character.isUpperCase(c)) {
                buffer.append(' ');
                buffer.append(c);
                continue;
            }

            if (c == '.') {
                buffer.delete(0, buffer.length());
                nextUpper = true;
                continue;
            }

            char x = nextUpper ? Character.toUpperCase(c) : c;
            buffer.append(x);
            nextUpper = false;
        }

        return buffer.toString();
    }

    /**
     * Get JDBC Type - used by iBATIS in sql-map.ftl
     * 
     * @param javaType - the Java Class
     * @return the type to use in a SQL statement
     */
    public String getJdbcType(String javaType) {
        String jdbcType = "VARCHAR";

        javaType = javaType.toLowerCase();

        if (javaType.indexOf("date") > 0) {
            jdbcType = "TIMESTAMP";
        } else if (javaType.indexOf("timestamp") > 0) {
            jdbcType = "TIMESTAMP";
        } else if ((javaType.indexOf("int") > 0) || (javaType.indexOf("long") > 0) || (javaType.indexOf("short") > 0)) {
            jdbcType = "INTEGER";
        } else if (javaType.indexOf("double") > 0) {
            jdbcType = "DOUBLE";
        } else if (javaType.indexOf("float") > 0) {
            jdbcType = "FLOAT";
        }

        return jdbcType;
    }
}
