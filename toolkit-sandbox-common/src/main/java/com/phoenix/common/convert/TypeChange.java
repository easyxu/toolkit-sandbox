package com.phoenix.common.convert;

/**
 * <p>Title: 类型转换公共方法类</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: phoenix</p>
 * @author leau
 * @bug 2009-05-15 
 */
public class TypeChange {
	/**
	 * 业务常用的toString()方法，使一般业务处理不用处理Integer类型等等
	 * @param o
	 * @return
	 */
	public static String toString(Object o){
		return o == null ? "" : o.toString();
	}
    /**
     * 通过字符串转换成相应的DOUBLE，并返回。
     * @param strValue String 待转换的字符串
     * @return double 转换完成的DOUBLE
     * */
    public static double getStrToDouble(String strValue) {
        if (null == strValue) {
            return 0;
        }
        double dValue = 0;
        try {
            dValue = Double.parseDouble(strValue.trim());
        } catch (Exception ex) {
            dValue = 0;
        }
        return dValue;
    }

    /**
     * 通过字符串转换成相应的整型，并返回。
     * FrameWork使用
     * @param strValue String 待转换的字符串
     * @return int 转换完成的整型
     * */
    public static int getStrToInt(String strValue) {
        int iValue = 0;
        if (null == strValue) {
            return iValue;
        }
        try {
            iValue = new Integer(strValue.trim()).intValue();
        } catch (Exception ex) {
            iValue = 0;
        }
        return iValue;
    }

    /**
     * 通过字符串转换成相应的短整型，并返回。
     * FrameWork使用
     * @param strValue String 待转换的字符串
     * @return short 转换完成的短整型
     * */
    public static short getStrToShort(String strValue) {
        short iValue = 0;
        if (null == strValue) {
            return iValue;
        }
        try {
            iValue = new Short(strValue.trim()).shortValue();
        } catch (Exception ex) {
            iValue = 0;
        }
        return iValue;
    }

    /**
     * 通过字符串转换成相应的长整型，并返回。
     * @param strValue String 待转换的字符串
     * @return long 转换完成的长整型
     * */
    public static long getStrToLong(String strValue) {
        if (null == strValue) {
            return 0;
        }
        long lValue = 0;
        try {
            lValue = new Long(strValue.trim()).longValue();
        } catch (Exception ex) {
            lValue = 0;
        }
        return lValue;
    }

    /**
     * 把int转为String
     * @param i int
     * @return String
     */
    public static String getIntToStr(int i) {
        return new Integer(i).toString();
    }

    /**
     * 把double转为String
     * @param d double
     * @return String
     */
    public static String getDoubleToStr(double d) {
        return new Double(d).toString();
    }

    /**
     * 把long转为String
     * @param l long
     * @return String
     */
    public static String getLongToStr(long l) {
        return new Long(l).toString();
    }

    /**
     * 把long转为int
     * @param l long
     * @return int
     */
    public static int getLongToInt(long l) {
        return new Long(l).intValue();
    }

    /**
     * 把short转为String
     * @param n short
     * @return String
     */
    public static String getShortToStr(short n) {
        return new Short(n).toString();
    }

    /**
     * 把Object数组转成String数组
     * @param seqObj Object[]
     * @return String[]
     */
    public static String[] getObjArrayToStringArray(Object[] seqObj) {
        String[] seqStr = new String[seqObj.length];
        for (int i = 0; i < seqObj.length; i++) {
            seqStr[i] = seqObj[i].toString();
        }
        return seqStr;
    }
}
