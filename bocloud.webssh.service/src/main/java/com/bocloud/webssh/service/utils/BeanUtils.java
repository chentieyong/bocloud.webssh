package com.bocloud.webssh.service.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author tangcq
 *
 */
@SuppressWarnings("rawtypes")
public class BeanUtils {
	/**
	 * 获取某个对象的属性 必须有get方法
	 * 
	 * @param provide
	 * @param property
	 * @return
	 */
	public static Object getProperties(Object provide, String property) {
		Object tempValue = null;
		Method[] mm = provide.getClass().getMethods();
		for (int i = 0; i < mm.length; i++) {
			if (mm[i].getName().toUpperCase().equals("GET" + property.toUpperCase())) {
				Method cm = mm[i];
				try {
					Object[] args = null;
					tempValue = cm.invoke(provide, args);
				} catch (IllegalAccessException ex) {
				} catch (IllegalArgumentException ex) {
				} catch (InvocationTargetException ex) {
				}
				break;
			}
		}
		return tempValue;
	}

	public static void setProperties(Object accept, Object data, String property) {
		Method[] mm = accept.getClass().getMethods();
		for (int i = 0; i < mm.length; i++) {
			if (mm[i].getName().toUpperCase().equals("SET" + property.toUpperCase())) {
				Method cm = mm[i];
				Class[] acls = cm.getParameterTypes();
				if (acls.length != 1) {
					continue;
				}
				Class cls = acls[0];
				Object val = infoEncode(data, cls);
				Object[] args = { val };
				try {
					cm.invoke(accept, args);
				} catch (IllegalAccessException ex) {
				} catch (IllegalArgumentException ex) {
				} catch (InvocationTargetException ex) {
				}
				break;
			}
		}
	}

	public static void setPropertiesAlias(Object accept, Object data, String property) {
		Method[] mm = accept.getClass().getMethods();
		for (int i = 0; i < mm.length; i++) {
			if (mm[i].getName().toUpperCase().equals("SET" + property.toUpperCase())) {
				Method cm = mm[i];
				Class[] acls = cm.getParameterTypes();
				if (acls.length != 1) {
					continue;
				}
				Class cls = acls[0];
				Object val = infoEncodeAlias(data, cls);
				Object[] args = { val };
				try {
					cm.invoke(accept, args);
				} catch (IllegalAccessException ex) {
				} catch (IllegalArgumentException ex) {
				} catch (InvocationTargetException ex) {
				}
				break;
			}
		}
	}

	/**
	 * 将Object obj转换成需要的Class类型
	 */
	public static Object infoEncode(Object obj, Class cls) {
		String pVal = StringUtils.toString(obj);
		return infoEncode(pVal, cls);
	}

	/**
	 * 将Object obj转换成需要的Class类型
	 */
	public static Object infoEncodeAlias(Object obj, Class cls) {
		String pVal = StringUtils.toStringAlias(obj);
		return infoEncode(pVal, cls);
	}

	/**
	 * 将Object obj转换成需要的Class类型
	 */
	public static Object infoEncode(Object obj, String className) {
		String pVal = StringUtils.toString(obj);
		return infoEncode(pVal, className);
	}

	private static Object infoEncode(String mProviderValue, Class cls) {
		String className = cls.getName();
		if ("java.lang.String".equals(className)) {
			return ObjectUtils.getString(mProviderValue);
		}
		if ("java.lang.Integer".equals(className)) {
			return ObjectUtils.getInteger(mProviderValue);
		}
		if ("java.lang.Float".equals(className)) {
			return ObjectUtils.getFloat(mProviderValue);
		}
		if ("java.lang.Long".equals(className)) {
			return ObjectUtils.getLong(mProviderValue);
		}
		if ("java.lang.Double".equals(className)) {
			return ObjectUtils.getDouble(mProviderValue);
		}
		if ("java.lang.Boolean".equals(className)) {
			return ObjectUtils.getBoolean(mProviderValue);
		}
		if ("java.sql.Date".equals(className)) {
			return ObjectUtils.getSqlDate(mProviderValue);
		}
		if ("java.utils.Date".equals(className)) {
			return ObjectUtils.getUtilDate(mProviderValue);
		}
		if ("java.sql.Timestamp".equals(className)) {
			return ObjectUtils.getTimestamp(mProviderValue);
		}

		if ("double".equals(className)) {
			return ObjectUtils.getDouble(mProviderValue);
		}
		if ("int".equals(className)) {

			return ObjectUtils.getInteger(mProviderValue);
		}
		if ("float".equals(className)) {
			return ObjectUtils.getFloat(mProviderValue);
		}
		if ("long".equals(className)) {
			return ObjectUtils.getLong(mProviderValue);
		}
		if ("boolean".equals(className)) {
			return ObjectUtils.getBoolean(mProviderValue);
		}

		return null;
	}

	private static Object infoEncode(String mProviderValue, String className) {
		if ("java.lang.String".equals(className)) {
			return ObjectUtils.getString(mProviderValue);
		}
		if ("java.lang.Integer".equals(className)) {
			return ObjectUtils.getInteger(mProviderValue);
		}
		if ("java.lang.Float".equals(className)) {
			return ObjectUtils.getFloat(mProviderValue);
		}
		if ("java.lang.Long".equals(className)) {
			return ObjectUtils.getLong(mProviderValue);
		}
		if ("java.lang.Double".equals(className)) {
			return ObjectUtils.getDouble(mProviderValue);
		}
		if ("java.lang.Boolean".equals(className)) {
			return ObjectUtils.getBoolean(mProviderValue);
		}
		if ("java.sql.Date".equals(className)) {
			return ObjectUtils.getSqlDate(mProviderValue);
		}
		if ("java.utils.Date".equals(className)) {
			return ObjectUtils.getUtilDateHMS(mProviderValue);
		}
		if ("java.sql.Timestamp".equals(className)) {
			return ObjectUtils.getTimestamp(mProviderValue);
		}

		if ("double".equals(className)) {
			return ObjectUtils.getDouble(mProviderValue);
		}
		if ("int".equals(className)) {

			return ObjectUtils.getInteger(mProviderValue);
		}
		if ("float".equals(className)) {
			return ObjectUtils.getFloat(mProviderValue);
		}
		if ("long".equals(className)) {
			return ObjectUtils.getLong(mProviderValue);
		}
		if ("boolean".equals(className)) {
			return ObjectUtils.getBoolean(mProviderValue);
		}
		return null;
	}
}
