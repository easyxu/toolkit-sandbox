package com.phoenix.common;
import java.text.DateFormat;
import java.util.Locale;
@SuppressWarnings("unchecked")
public class LocaleHelper {
	
	private static final ThreadLocal threadLocales = new ThreadLocal();

	  public static final int FORMAT_SHORT = DateFormat.SHORT;

	  public static final int FORMAT_MEDIUM = DateFormat.MEDIUM;

	  public static final int FORMAT_LONG = DateFormat.LONG;

	  public static final int FORMAT_FULL = DateFormat.FULL;

	  public static final int FORMAT_IGNORE = -1;

	  private static Locale defaultLocale;

	  public static final String UTF_8 = "UTF-8"; //$NON-NLS-1$

	  private static String encoding = UTF_8;

	  public static final String LEFT_TO_RIGHT = "LTR"; //$NON-NLS-1$

	  private static String textDirection = LEFT_TO_RIGHT;

	  public static void setDefaultLocale(Locale newLocale) {
	    defaultLocale = newLocale;
	  }

	  public static Locale getDefaultLocale() {
	    return defaultLocale;
	  }

	  public static void setLocale(Locale newLocale) {
	    threadLocales.set(newLocale);
	  }

	  public static Locale getLocale() {
	    Locale rtn = (Locale) threadLocales.get();
	    if (rtn != null) {
	      return rtn;
	    }
	    defaultLocale = Locale.getDefault();
	    setLocale(defaultLocale);
	    return defaultLocale;
	  }

	  public static void setSystemEncoding(String encoding) {
	    LocaleHelper.encoding = encoding;
	  }

	  public static void setTextDirection(String textDirection) {
	    // TODO make this ThreadLocal
	    LocaleHelper.textDirection = textDirection;
	  }

	  public static String getSystemEncoding() {
	    return encoding;
	  }

	  public static String getTextDirection() {
	    // TODO make this ThreadLocal
	    return textDirection;
	  }

	  public static DateFormat getDateFormat(int dateFormat, int timeFormat) {

	    if (dateFormat != FORMAT_IGNORE && timeFormat != FORMAT_IGNORE) {
	      return DateFormat.getDateTimeInstance(dateFormat, timeFormat, getLocale());
	    } else if (dateFormat != FORMAT_IGNORE) {
	      return DateFormat.getDateInstance(dateFormat, getLocale());
	    } else if (timeFormat != FORMAT_IGNORE) {
	      return DateFormat.getTimeInstance(timeFormat, getLocale());
	    } else {
	      return null;
	    }

	  }

	  public static DateFormat getShortDateFormat(boolean date, boolean time) {
	    if (date && time) {
	      return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, getLocale());
	    } else if (date) {
	      return DateFormat.getDateInstance(DateFormat.SHORT, getLocale());
	    } else if (time) {
	      return DateFormat.getTimeInstance(DateFormat.SHORT, getLocale());
	    } else {
	      return null;
	    }
	  }

	  public static DateFormat getMediumDateFormat(boolean date, boolean time) {
	    if (date && time) {
	      return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, getLocale());
	    } else if (date) {
	      return DateFormat.getDateInstance(DateFormat.MEDIUM, getLocale());
	    } else if (time) {
	      return DateFormat.getTimeInstance(DateFormat.MEDIUM, getLocale());
	    } else {
	      return null;
	    }
	  }

	  public static DateFormat getLongDateFormat(boolean date, boolean time) {
	    if (date && time) {
	      return DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, getLocale());
	    } else if (date) {
	      return DateFormat.getDateInstance(DateFormat.LONG, getLocale());
	    } else if (time) {
	      return DateFormat.getTimeInstance(DateFormat.LONG, getLocale());
	    } else {
	      return null;
	    }
	  }

	  public static DateFormat getFullDateFormat(boolean date, boolean time) {
	    if (date && time) {
	      return DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, getLocale());
	    } else if (date) {
	      return DateFormat.getDateInstance(DateFormat.FULL, getLocale());
	    } else if (time) {
	      return DateFormat.getTimeInstance(DateFormat.FULL, getLocale());
	    } else {
	      return null;
	    }
	  }
}
