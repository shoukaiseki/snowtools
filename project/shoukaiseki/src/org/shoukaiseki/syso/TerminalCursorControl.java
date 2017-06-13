package org.shoukaiseki.syso;

/** linux 終端光標控制
 *  Fedora 18 測試通過
 *.TerminalCursorControl
 * @author 蒋カイセキ    Japan-Tokyo 2013-7-2
 * <P>ブログ http://shoukaiseki.blog.163.com/
 * <P>E-メール jiang28555@Gmail.com
 */
public class TerminalCursorControl {
	/**
	 * 隱藏光標
	 */
	public static final String HIDETHECURSOR="\033[?25l\033[0m";
	/**
	 * 顯示光標
	 */
	public static final String SHOWTHECURSOR="\033[?25h\033[0m";
	/**
	 * 清除从光标到行尾的内容 
	 */
	public static final String CLEARTOENDOFLINE="\033[K\033[0m";
	
	/**
	 * 清屏
	 */
	public static final String CLEARALL="\033[2J\033[0m";
	/**
	 * 移動到行首
	 */
	public static final String MOVETOFIRSTLINE="\033[888D\033[0m";
	
	private String osName=System.getProperty("os.name");
	public TerminalCursorControl(){
	}
	
	/**
	 *  @see #CLEARTOENDOFLINE
	 */
	public void clearToEndOfLine(){
		if(osName.equalsIgnoreCase("linux")){
			print(CLEARTOENDOFLINE);	
		}else{
			print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
//			for (int i = 0; i < 200; i++) {
//				print("\b");
//			}
		}
	}
	/**
	 *  @see #CLEARALL
	 */
	public void clear(){
		if(osName.equalsIgnoreCase("linux")){
			print(CLEARALL);
		}
	}
	
	/**
	 *  @see #MOVETOFIRSTLINE
	 */
	public void moveToFirstLine(){
		if(osName.equalsIgnoreCase("linux")){
			print(MOVETOFIRSTLINE);
		}
		
	}
	
	/**
	 *  @see #HIDETHECURSOR
	 */
	public void hideTheCursor(){
		if(osName.equalsIgnoreCase("linux")){
			print(HIDETHECURSOR);
		}
	}
	
	/**
	 *  @see #SHOWTHECURSOR
	 */
	public void showTheCursor(){
		if(osName.equalsIgnoreCase("linux")){
			print(SHOWTHECURSOR);
		}
	}
	


	/** 終端輸出,如果不為 linux 系統則不輸出
	 * @param o
	 */
	public void print(Object o){
		if(osName.equalsIgnoreCase("linux")){
			System.out.print(o);
		}else{
			System.out.print(o);
		}
	}
	
	
}
