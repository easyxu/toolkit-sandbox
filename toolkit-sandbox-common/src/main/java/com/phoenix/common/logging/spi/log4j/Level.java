package com.phoenix.common.logging.spi.log4j;

/**
 * 定义新的level。
 */
public class Level extends org.apache.log4j.Level {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5523860878775558772L;
	/**
     * The <code>TRACE</code> Level designates fine-grained informational events that are most
     * useful to debug an application.
     */
    public static final int TRACE_INT = DEBUG_INT - 5000;
    public static final Level TRACE = new Level(Level.TRACE_INT, "TRACE",
                                                Level.DEBUG.getSyslogEquivalent());

    /**
     * 创建一个level。
     *
     * @param level level的整数值
     * @param levelStr level的字符串表示
     * @param syslogEquivalent 在syslog中的整数值
     */
    protected Level(int level, String levelStr, int syslogEquivalent) {
        super(level, levelStr, syslogEquivalent);
    }

    /**
     * Convert the string passed as argument to a level. If the conversion fails, then this method
     * returns {@link #TRACE}.
     */
    public static org.apache.log4j.Level toLevel(String sArg) {
        return (Level) toLevel(sArg, Level.TRACE);
    }

    /**
     * Convert an integer passed as argument to a level. If the conversion fails, then this method
     * returns {@link #TRACE}.
     */
    public static org.apache.log4j.Level toLevel(int val) {
        return (Level) toLevel(val, Level.TRACE);
    }

    /**
     * Convert an integer passed as argument to a level. If the conversion fails, then this method
     * returns the specified default.
     */
    public static org.apache.log4j.Level toLevel(int val, org.apache.log4j.Level defaultLevel) {
        switch (val) {
            case ALL_INT:
                return ALL;

            case TRACE_INT:
                return Level.TRACE;

            case DEBUG_INT:
                return Level.DEBUG;

            case INFO_INT:
                return Level.INFO;

            case WARN_INT:
                return Level.WARN;

            case ERROR_INT:
                return Level.ERROR;

            case FATAL_INT:
                return Level.FATAL;

            case OFF_INT:
                return OFF;

            default:
                return defaultLevel;
        }
    }

    /**
     * Convert the string passed as argument to a level. If the conversion fails, then this method
     * returns the value of <code>defaultLevel</code>.
     */
    public static org.apache.log4j.Level toLevel(String sArg, org.apache.log4j.Level defaultLevel) {
        if (sArg == null) {
            return defaultLevel;
        }

        String s = sArg.toUpperCase();

        if (s.equals("ALL")) {
            return Level.ALL;
        }

        if (s.equals("TRACE")) {
            return Level.TRACE;
        }

        if (s.equals("DEBUG")) {
            return Level.DEBUG;
        }

        if (s.equals("INFO")) {
            return Level.INFO;
        }

        if (s.equals("WARN")) {
            return Level.WARN;
        }

        if (s.equals("ERROR")) {
            return Level.ERROR;
        }

        if (s.equals("FATAL")) {
            return Level.FATAL;
        }

        if (s.equals("OFF")) {
            return Level.OFF;
        }

        return defaultLevel;
    }
}
