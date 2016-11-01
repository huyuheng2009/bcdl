package com.yogapay.bcdl.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class StringUtil {

	/**
	 * 字符串左补0
	 * */
	public static String stringFillLeftZero(String str, int len) {
		if (str.length() < len) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < len - str.length(); i++)
				sb.append('0');
			sb.append(str);
			return new String(sb);
		} else
			return str;
	}
	
	/**
	 * 字符串左补0
	 * */
	public static String stringFillLeftZero(int a, int len) {
		String str = ""+a;
		if (str.length() < len) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < len - str.length(); i++)
				sb.append('0');
			sb.append(str);
			return new String(sb);
		} else
			return str;
	}
	
	/**
	 * 字符串左补0
	 * */
	public static String stringFillLeftZero(long a, int len) {
		String str = ""+a;
		if (str.length() < len) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < len - str.length(); i++)
				sb.append('0');
			sb.append(str);
			return new String(sb);
		} else
			return str;
	}
	
	/**
	 * 字符串右补空格
	 */
	public static String stringFillRightBlank(String str, int len){
		if(str.length() < len){
			StringBuffer sb = new StringBuffer(str);
			for(int i = 0;i < len - str.length();i++){
				sb.append(" ");
			}
			return sb.toString();
		}else
			return str;
	}

	public static String cardNoHidden(String cardNo) {
		String start = cardNo.substring(0, 4);
		String mid = cardNo.substring(cardNo.length() - 6, cardNo.length() - 4);
		String end = cardNo.substring(cardNo.length() - 4, cardNo.length());
		return start + " .... " + mid + " " + end;

	}

	/**
	 * 
	 * @param obj
	 * @return String
	 * @obj==null,或obj是空字符串，就返回参数ifEmptyThen，否则返回obj.toString。
	 */

	public static String ifEmptyThen(Object obj, String ifEmptyThen) {
		String ret = "";
		if (obj == null || String.valueOf(obj) == "") {
			ret = ifEmptyThen;
		} else {
			ret = obj.toString();
		}
		return ret;
	}



	// 过滤非法字符
	public static String StringFilter(String str) throws PatternSyntaxException {
		Pattern p = null;
		Matcher m = null;
		String value = null;
		// 去掉<>标签及其之间的内容
		p = Pattern.compile("(<[^>]*>)");
		m = p.matcher(str);
		String temp = str;
		// 下面的while循环式进行循环匹配替换，把找到的所有
		// 符合匹配规则的字串都替换为你想替换的内容
		while (m.find()) {
			value = m.group(0);
			temp = temp.replace(value, "");
		}
		return temp;
	}

	public static boolean isEmpty(String str){
		if(str==null || str=="") return true;
		return false;
	}
	
	public static boolean isEmptyWithTrim(String str){
		if(str==null || str.trim()=="") return true;
		return false;
	}
}
