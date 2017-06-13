import org.shoukaiseki.syso.TerminalCursorControl;


public class TestTerminal {
	
	public static void main(String[] args) {
		// java -classpath /media/develop/antProject/javaant/libs/mylib/shoukaiseki.jar:. TestTerminal
		TerminalCursorControl tcc=new TerminalCursorControl();
		
		System.out.println("行清空方式:先得调用光标移到行首，再清除光标到行尾的内容");
		tcc.print("asus");
		tcc.moveToFirstLine();
		tcc.clearToEndOfLine();
		tcc.print("reprint asus");
		System.out.println();
		System.out.println();
		System.out.println("颜色输出:");
		System.out.println("#字背景颜色范围: 40--49          字颜色: 30--39");
		System.out.println("#40: 黑                           30: 黑");
		System.out.println("#41: 红                           31: 红");
		System.out.println("#42: 绿                           32: 绿");
		System.out.println("#43: 黄                           33: 黄");
		System.out.println("#44: 蓝                           34: 蓝");
		System.out.println("#45: 紫                           35: 紫");
		System.out.println("#46: 深绿                         36: 深绿");
		System.out.println("#47: 白色                         37: 白色");
		tcc.print("\033[36m设置前景色36深绿色\033[0m\n");
		tcc.print("\033[46m设置背景色46深绿色\033[0m\n");
		tcc.print("\033[44;31m设置背景色44蓝色,前景色31红色\033[0m\n");
		tcc.print("使用 printFormatColor 方法进行颜色格式化:"+new TestTerminal().printFormatColor("颜色格式化方法","31m"));
		System.out.println();
		
	}
	
	
	/**
	 * @param str 颜色内容
	 * @param colorCode 颜色代码
	 * @return
	 */
	public  String printFormatColor(String str,String colorCode) {
		// TODO Auto-generated method stub

		return new StringBuffer("\033[").append(colorCode).append(str).append("\033[0m").toString();
	}

}
