package com.shoukaiseki.string;

/** 正则表达式,只对sun有效
 * @author 蒋カイセキ    Japan-Tokyo  2012-5-24
 * @ブログ http://shoukaiseki.blog.163.com/
 * @E-メール jiang28555@Gmail.com  
 */
public class StringHouhou {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str = "   asus    ";
		String s1 = leftTrim(str);
		System.out.println(str + "[sunJDK取左空]" + s1);
		 s1 = rightTrim(str);
		System.out.println(str + "[sunJDK取右空]" + s1);
		

		 s1 = trimLeft(str);
		System.out.println(str + "[普通JDK取左空]" + s1);
		 s1 = trimRight(str);
		System.out.println(str + "[普通JDK取右空]" + s1);
		
	}

	/**
	 *  SUN JDK左右空格都去掉 
	 **/
	public static String trim(String str) {
		if (str == null || str.equals("")) {
			return str;
		} else {
			// return leftTrim(rightTrim(str));
			return str.replaceAll("^[　 ]+|[　 ]+$", "");
		}
	}

	/**
	 * SUN JDK去左空格 
	 */
	public static String leftTrim(String str) {
		if (str == null || str.equals("")) {
			return str;
		} else {
			return str.replaceAll("^[　 ]+", "");
		}
	}

	/**
	 *  SUN JDK去右空格 
	 */
	public static String rightTrim(String str) {
		if (str == null || str.equals("")) {
			return str;
		} else {
			return str.replaceAll("[　 ]+$", "");
		}

	}

	/**
	 * 去掉String字符串左边的空格
	 * 
	 * @param src
	 *            要处理的String字符串
	 * @return 处理好的String字符串
	 */
	public static String trimLeft(String src) {
		StringBuffer sb = new StringBuffer();
		int len = src.length();
		char c;
		int i = 0;
		for (i = 0; i < len; i++) {
			c = src.charAt(i);
			if (c != ' ' && c != '\t' && c != '\n') {
				break;
			}
		}
		sb.append(src.substring(i, len));
		return sb.toString();
	}

	/**
	 * 去掉String字符串右边的空格
	 * 
	 * @param src
	 *            要处理的String字符串
	 * @return 处理好的String字符串
	 */
	public static String trimRight(String src) {
		StringBuffer sb = new StringBuffer();
		int len = src.length();
		char c;
		int i = 0;
		for (i = len; i > 0; i--) {
			c = src.charAt(i - 1);
			if (c != ' ' && c != '\t' && c != '\n') {
				break;
			}
		}
		sb.append(src.substring(0, i));
		return sb.toString();
	}
}


