package com.phoenix.common.convert;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phoenix.common.lang.StringUtil;

public class DateConverter {
	private static final Logger log = LoggerFactory.getLogger(DateConverter.class);

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public DateConverter(String formatPattern) {
        if (StringUtil.isNotBlank(formatPattern)) {
            format = new SimpleDateFormat(formatPattern);
        }
    }

    /**
     * 将字符串转换成java.util.Date
     * @param arg0 Class
     * @param value Object
     * @return Object
     */
    public Date convert(Object value) {
        try {
            String dateStr = (String) value;

            if (StringUtil.isNotBlank(dateStr)) {
                return format.parse(dateStr);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 将字符串转换成java.util.Date
     * @param arg0 Class
     * @param value Object
     * @return Object
     */
    public Date convertNeedException(Object value) {
        try {
            String dateStr = (String) value;

            if (StringUtil.isNotBlank(dateStr)) {
                return format.parse(dateStr);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("the date convert wrong, value = " +  value);
        }
        return null;
    }
}
