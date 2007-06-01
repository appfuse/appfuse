package org.appfuse.webapp.action;

import java.text.ParseException;
import java.util.Map;
import java.util.Date;

import org.apache.struts2.util.StrutsTypeConverter;
import org.appfuse.util.DateUtil;
import com.opensymphony.xwork2.util.TypeConversionException;

public class DateConverter extends StrutsTypeConverter {
    public Object convertFromString(Map ctx, String[] value, Class arg2) {
        if (value[0] == null || value[0].trim().equals("")) {
            return null;
        }
        try {
            DateUtil.convertStringToDate(value[0]);
        } catch (ParseException pe) {
            pe.printStackTrace();
            throw new TypeConversionException(pe.getMessage());
        }
        return null;
    }

    public String convertToString(Map ctx, Object data) {
        return DateUtil.convertDateToString((Date) data);
    }
} 