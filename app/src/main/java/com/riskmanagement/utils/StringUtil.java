package com.riskmanagement.utils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by codingWw on 2016/7/17.
 */
public class StringUtil {

    private static final String EMPTY = "";

    /**
     * 讲NUll转换为EMPTY
     *
     * @param string
     * @return
     */
    public static String nullToEmpty(String string) {
        return (string == null) ? "" : string;
    }

    /**
     * 将empty转换为NUll
     *
     * @param string
     * @return
     */
    public static String emptyToNull(String string) {
        return isNullOrEmpty(string) ? null : string;
    }

    /**
     * 判断是否为null或empty
     *
     * @param string
     * @return
     */
    public static boolean isNullOrEmpty(String string) {
        return string == null || string.length() == 0;
    }

    /**
     * 字符窜填充，用padChar填充头部
     *
     * @param string
     * @param minLength
     * @param padChar
     * @return
     */
    public static String padStart(String string, int minLength, char padChar) {
        if (string.length() >= minLength) {
            return string;
        }
        StringBuilder sb = new StringBuilder(minLength);
        for (int i = string.length(); i < minLength; i++) {
            sb.append(padChar);
        }
        sb.append(string);
        return sb.toString();
    }

    /**
     * 字符窜填充，用padChar填充尾部
     *
     * @param string
     * @param minLength
     * @param padChar
     * @return
     */
    public static String padEnd(String string, int minLength, char padChar) {
        if (string.length() >= minLength) {
            return string;
        }
        StringBuilder sb = new StringBuilder(minLength);
        sb.append(string);
        for (int i = string.length(); i < minLength; i++) {
            sb.append(padChar);
        }
        return sb.toString();
    }

    /**
     * 首字母大写
     */
    public static String firstUpperCase(String str) {
        Character ch = str.charAt(0);
        char[] array = str.toCharArray();
        array[0] = Character.toUpperCase(ch);
        return String.valueOf(array);
    }

    /**
     * 首字母小写
     *
     * @param str
     * @return
     */
    public static String firstLowerCase(String str) {
        Character ch = str.charAt(0);
        char[] array = str.toCharArray();
        array[0] = Character.toLowerCase(ch);
        return String.valueOf(array);
    }

    /**
     * null 安全的空字符串测试
     *
     * @return 字符串是否为null或者空白字符
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !(str == null || str.isEmpty());
    }

    /**
     * 连接字符串
     *
     * @param elements  要连接的数组
     * @param separator 分隔符
     * @return 连接结果
     */
    public static String join(Object[] elements, String separator) {
        if (elements == null) {
            return EMPTY;
        }
        if (separator == null) {
            separator = EMPTY;
        }

        StringBuilder sb = new StringBuilder(elements.length * 16);

        for (int i = 0; i < elements.length; i++) {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(elements[i]);
        }
        return sb.toString();
    }

    /**
     * 将一个字符串重复 N 次
     *
     * @param str   要重复的字符串
     * @param times 重复次数
     * @return
     */
    public static String repeat(String str, int times) {
        StringBuilder sb = new StringBuilder(str.length() * times);
        for (int i = 0; i < times; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    public static String repeat(Object str, String separator, int times) {
        Object[] array = new Object[times];
        Arrays.fill(array, str);
        return join(array, separator);
    }

    public static Object[] concatStr(Object[] obj, String str) {
        Object[] param = new Object[obj.length];
        for (int i = 0; i < obj.length; i++) {
            param[i] = obj[i].toString().concat(str);
        }
        return param;
    }

    /**
     * 利用反射将对象转换为格式化的字符串 (是null安全的)
     *
     * @param object 要格式化的对象
     * @return
     */
    public static String toString(Object object) {

        if (object == null)
            return "null";

        if (object instanceof String || isPrimaryType(object)) {
            return object.toString();
        }

        if (object.getClass().isArray()) {
            return arrayToString(object);
        }
        if (object instanceof Iterable<?>) {
            return iterableToString((Iterable<?>) object);
        }

        StringBuilder sb = new StringBuilder();

        Class<?> clazz = object.getClass();
        while (clazz != null) {
            addField(object, clazz, sb);
            clazz = clazz.getSuperclass();
            if (clazz != null && !Object.class.equals(clazz)) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    private static final String arrayToString(Object object) {
        StringBuilder sb = new StringBuilder();
        if (!object.getClass().isArray())
            return sb.toString();
        int len = Array.getLength(object);
        sb.append("[");
        for (int i = 0; i < len; i++) {
            Object obj = Array.get(object, i);
            String str = obj == null ? "null" : toString(obj);
            if (isPrimaryType(obj)) {
                sb.append(str);
            } else {
                sb.append("(").append(str).append(")");
            }
            if (i < len - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static final String iterableToString(Iterable<?> iterable) {
        StringBuilder sb = new StringBuilder();

        Iterator<?> iter = iterable.iterator();
        sb.append("[\n");
        while (true) {
            sb.append("(");
            Object obj = iter.next();
            sb.append(obj == null ? "null" : toString(obj));
            sb.append(")");
            if (iter.hasNext()) {
                sb.append(",\n");
            } else {
                break;
            }
        }
        sb.append("\n]");
        return sb.toString();
    }

    private static final String addField(Object object, Class<?> clazz,
                                         StringBuilder sb) {
        Field[] fields = clazz.getDeclaredFields();
        AccessibleObject.setAccessible(fields, true);

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            try {
                String name = field.getName();
                if (i > 0)
                    sb.append(", ");
                sb.append(name).append("=").append(toString(field.get(object)));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private static final boolean isPrimaryType(Object object) {
        return object instanceof Byte || object instanceof Short
                || object instanceof Integer || object instanceof Long
                || object instanceof Character || object instanceof Float
                || object instanceof Double || object instanceof Boolean;
    }
}
