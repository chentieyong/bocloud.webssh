package com.bocloud.webssh.service.utils;

import com.alibaba.fastjson.JSONObject;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理类
 * 
 * @author tangcq
 *
 */
public class StringUtils {

	/**
	 * 判断当前字符串中是否包含汉字 或全角字符
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean hasChineseWord(Object obj) {
		String s = StringUtils.toString(obj);
		s = s.replaceAll("[^\\x00-\\xff]", "***");
		if (s.length() > StringUtils.toString(obj).length()) {
			return true;
		}
		return false;
	}

	public static long parseLong(String str) {
		try {

			return Long.parseLong(str);
		} catch (Exception ex) {
			return 0L;
		}
	}

	public static int parseInt(String str) {
		try {

			return Integer.parseInt(str);
		} catch (Exception ex) {
			return 0;
		}
	}

	public static int parseInt(String str, int i) {
		try {

			return Integer.parseInt(str);
		} catch (Exception ex) {
			return i;
		}
	}

	public static double parseDouble(String str) {
		try {
			return Double.parseDouble(str);
		} catch (Exception ex) {
			return 0;
		}
	}

	public static String toString(Object obj) {
		if (obj == null) {
			return "";
		}
		String className = obj.getClass().getName();
		if ("java.lang.String".equals(className)) {
			return toString((String) obj).trim();
		}
		if ("java.lang.Integer".equals(className)) {
			return toString((Integer) obj).trim();
		}
		if ("java.lang.Long".equals(className)) {
			return toString((Long) obj).trim();
		}
		if ("java.sql.Date".equals(className)) {
			return toString((Date) obj).trim();
		}
		if ("java.utils.Date".equals(className)) {
			return toString((java.util.Date) obj).trim();
		}
		if ("java.lang.Float".equals(className)) {
			return toString((Float) obj).trim();
		}
		if ("java.sql.Timestamp".equals(className)) {
			return toString((Timestamp) obj).trim();
		}
		if ("java.lang.Double".equals(className)) {
			return toString((Double) obj).trim();
		}
		if ("com.alibaba.fastjson.JSONObject".equals(className)) {
			return toString((JSONObject) obj).trim();
		}

		return obj.toString().trim();
	}

	/**
	 * 字符串 不启用trim
	 * 
	 * @param obj
	 * @return
	 */
	public static String toStringAlias(Object obj) {
		if (obj == null) {
			return "";
		}
		String className = obj.getClass().getName();
		if ("java.lang.String".equals(className)) {
			return toString((String) obj);
		}
		if ("java.lang.Integer".equals(className)) {
			return toString((Integer) obj).trim();
		}
		if ("java.lang.Long".equals(className)) {
			return toString((Long) obj).trim();
		}
		if ("java.sql.Date".equals(className)) {
			return toString((Date) obj).trim();
		}
		if ("java.utils.Date".equals(className)) {
			return toString((java.util.Date) obj).trim();
		}
		if ("java.lang.Float".equals(className)) {
			return toString((Float) obj).trim();
		}
		if ("java.sql.Timestamp".equals(className)) {
			return toString((Timestamp) obj).trim();
		}
		if ("java.lang.Double".equals(className)) {
			return toString((Double) obj).trim();
		}
		if ("com.alibaba.fastjson.JSONObject".equals(className)) {
			return toString((JSONObject) obj).trim();
		}

		return obj.toString();
	}

	public static String toCSV(Object obj) {
		String str = toString(obj);
		return str.replaceAll("\"", "\"\"").replaceAll("\n", "").replaceAll("\r", "");
	}

	public static String toString(int obj) {
		return String.valueOf(obj);
	}

	public static String toString(long obj) {
		return String.valueOf(obj);
	}

	public static String toString(double obj) {
		return String.valueOf(obj);
	}

	public static String toString(float obj) {
		return String.valueOf(obj);
	}

	public static String toString(boolean obj) {
		return String.valueOf(obj);
	}

	public static String toString(char obj) {
		return String.valueOf(obj);
	}

	private static String toString(String obj) {
		if (obj == null) {
			return "";
		}
		return obj;
	}

	private static String toString(Integer obj) {
		if (obj == null) {
			return "";
		}
		return obj.toString();
	}

	private static String toString(Long obj) {
		if (obj == null) {
			return "";
		}
		return obj.toString();
	}

	private static String toString(Date obj) {
		if (obj == null) {
			return "";
		}

		DateFormat dftime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = dftime.format(obj);
		if (str.indexOf("00:00:00") < 0) {
			return str;
		} else {
			DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
			return df.format(obj);
		}
	}

	private static String toString(Double obj) {
		if (obj == null) {
			return "";
		}
		return obj.doubleValue() + "";
	}

	private static String toString(Float obj) {
		if (obj == null) {
			return "";
		}
		return obj.toString();
	}

	private static String toString(Timestamp obj) {
		if (obj == null) {
			return "";
		}
		return obj.toString();
	}

	private static String toString(java.util.Date obj) {
		if (obj == null) {
			return "";
		}
		return getDateString(obj);
	}

	private static String getDateString(java.util.Date adate) {

		if (adate == null) {
			return "";
		}

		DateFormat dftime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = dftime.format(adate);
		if (str.indexOf("00:00:00") < 0) {
			return str;
		} else {
			DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
			return df.format(adate);
		}

	}

	/**
	 * 验证日期格式。
	 * 
	 */
	public static boolean isDate(String date) {
		String eL = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1][0-9])|([2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
		Pattern p = Pattern.compile(eL);
		Matcher m = p.matcher(date);
		boolean b = m.matches();
		return b;
	}

	public static boolean isEmpty(Object str) {
		return "".equals(StringUtils.toString(str));
	}

	public static boolean isNotEmpty(Object str) {
		return !"".equals(StringUtils.toString(str));
	}

	public static boolean isNumber(String str) {
		if (str == null) {
			return true;
		}
		String regExp = "^(-|\\+)?\\d+(\\.\\d+)?$";
		Pattern pattern = Pattern.compile(regExp);
		return pattern.matcher(str).matches();
	}

	/**
	 * 判断是否只是数字、字母和下划线，过滤掉特殊字符
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumberOrCharacter(String str) {
		if (str == null) {
			return false;
		}
		String regExp = "^[a-zA-Z0-9_]+$";
		Pattern pattern = Pattern.compile(regExp);
		return pattern.matcher(str).matches();
	}

	/**
	 * 判断字符串是否包含字母
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isIncludeLetter(String str) {
		if (str == null) {
			return false;
		}
		String regExp = "[a-zA-Z]+";
		Pattern pattern = Pattern.compile(regExp);
		return pattern.matcher(str).find();
	}

	/**
	 * 判断Key是否存在于数组中
	 * 
	 * @param htmlStr
	 * @return
	 */
	public static boolean isInclude(String[] keys, String key) {
		for (String name : keys) {
			if (name.equals(key)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 将list按照一个分割符拼接到字符串后面
	 * 
	 * @param data
	 * @param list
	 * @param splite
	 * @return
	 */
	public static String appendString(String data, List<String> list, String splite) {
		StringBuffer sbBuffer = new StringBuffer(2048);
		sbBuffer.append(data);
		if (list != null) {
			for (String str : list) {
				if (StringUtils.isNotEmpty(str)) {
					sbBuffer.append(str + splite);
				}
			}
		}
		return sbBuffer.toString();
	}
}
