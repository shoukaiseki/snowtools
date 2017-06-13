package org.shoukaiseki.math;

import java.text.DecimalFormat;

public class PrintPercent {
	/**
	 * 内写格式的模式 如保留2位就用"0.00"即可
	 */
	public  DecimalFormat df = new DecimalFormat("00.0000");
	/**
	 * 总条数
	 */
	public int total = 0;
	/**
	 * 已经完成的
	 */
	public int completed = 0;
	/**
	 * 每百分一的条数
	 */
	public int percent = 0;
	/**
	 * 前回输出的字符数
	 */
	public int zenkai = 0;
	/**
	 * 输出模式,true为控制台模式,false为Eclipse模式
	 */
	public Boolean mode=true;
	
	public PrintPercent() {
	}
	/**
	 * @param mode  输出模式,true为控制台模式,false为Eclipse模式
	 */
	public PrintPercent(Boolean mode) {
		this.mode=mode;
	}
	/**
	 * @param total	需要更新的总条数
	 * @param mode  输出模式,true为控制台模式,false为Eclipse模式
	 */
	public PrintPercent(int total,Boolean mode) {
		this.mode=mode;
		this.total = total;
		this.percent=total/100;
	}
	/**
	 * 
	 * @param total  需要更新的总条数
	 */
	public PrintPercent(int total) {
		this.total = total;
		this.percent=total/100;
	}
	public void setTotal(int total){
		this.total=total;
		this.percent=total/100;
	}
	/**
	 * 
	 * @return  percent  每百分一的条数
	 */
	public int getPercent(){
		return percent;
	}
	
	public void outPrint(int completed) {
		this.completed = completed;
		if (mode) {
			comPrint();
		}else{
			eclipsePrint();
		}
	}
	/**
	 *  适合在控制台运行,显示的百分比
	 */
	public void comPrint() {
		// 
		float a = (float) completed / total * 100;
		//
		String clean="";
		for (int k = 0; k < zenkai; k++) {
			clean+="\b";
		}
		System.out.print(clean);
		String string = "当前进度为:       [" + String.valueOf(df.format(a)) + "%]  "
				+ String.valueOf(completed);
		/*直接使用string.length()取得的长度用\b进行退格不准确
		 * 因为默认汉字日文都为一字符,但实际需要两个\b
		 * length方法为取得包含中日文字符串的准确字符数
		 */
		zenkai = length(string);
//		zenkai = string.length();//无汉字时使用
		System.out.print(string);
	}

	public void eclipsePrint() {
		if (completed % percent == 0 && completed / percent < 101) {
			System.out.println("当前进度为: ["
					+ String.valueOf(df.format(completed / percent))
					+ "%]   已经完成" + completed);// 每一个百分点显示一次进度
		}
	}
	
	public static int length(String s) {
		char[] c = s.toCharArray();
		int len = 0;
		for (int i = 0; i < c.length; i++) {
			len++;
			if (!isLetter(c[i])) {
				len++;
			}
		}
		return len;
	} 

	public static boolean isLetter(char c) {
		/**
		 * 判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符）
		 * 
		 * @param char c, 需要判断的字符
		 * @return boolean, 返回true,Ascill字符
		 */
		int k = 0x80;
		return c / k == 0 ? true : false;
	}
}


