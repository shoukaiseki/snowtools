package org.shoukaiseki.jeval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.shoukaiseki.jeval.function.SFunction;
import org.shoukaiseki.jeval.function.math.Owner;

public class SEvaluator {
	/**
	 * 定义运算符
	 */
	public static List<String> lc = new ArrayList<String>();
	static {
		lc.add("+");
		lc.add("`");
		lc.add("*");
		lc.add("/");
	}
	/**
	 * 定义逻辑运算符
	 */
	public static List<String> lj = new ArrayList<String>();
	static {
		lj.add(">");
		lj.add("<");
		lj.add("=");
		lj.add("!");
	}
	/**
	 * int数组注释,第一个标识:0自带函数 1自定义函数;第二个标识:参数个数
	 */
	public static Map<String,SFunction> funMap = new HashMap<String, SFunction>();
	static {
		// 自定义函数
		addSFunction(new Owner());	
	}

	public static void addSFunction(SFunction fun){
		
		funMap.put(fun.getName(), fun);
	}
	/**
	 * 公式初始化转换
	 * 
	 * @param str
	 * @return 处理过的计算客串
	 */
	public static String strCast(String str) {
		str = str.toLowerCase();// 去除空格，变小写

		if (str == null ? true : str.length() == 0)
			return "0";
		if (!checkFormula(str))
			return "0";
		str = str.replaceAll("\\*-", "**");
		str = str.replaceAll("-\\*", "**");
		str = str.replaceAll("/-", "//");
		str = str.replaceAll("-/", "//");
		str = str.replaceAll("\\+-", "-");
		str = str.replaceAll("-\\+", "-");
		str = str.replaceAll("-", "`");
		str = str.replaceAll("\\*\\*", "*-");
		str = str.replaceAll("//", "/-");
		str = str.replaceAll(" ", "");
		return str;
	}
	
	/**
	 * 检查公式中括号出现次数是否正确
	 * 
	 * @param formulaStr
	 * @return 公式中的括号是否成对
	 */
	public static boolean checkFormula(String formulaStr) {
		boolean flag = true;
		int count = 0;
		for (int i = 0; i < formulaStr.length(); i++) {
			String s = String.valueOf(formulaStr.charAt(i));
			if ("(".equals(s))
				count++;
			else if (")".equals(s))
				count--;
		}
		flag = count == 0;
		return flag;
	}
	
	/**
	 * 分割函数
	 * 
	 * @param str
	 * @param bs
	 * @return 分割后的客串
	 */
	public static String[] spliteFun(String str, String bs) {
		List<String> list = new ArrayList<String>();
		String bds = "";
		int bracket = 0;
		int len = str.length();
		for (int i = 0; i < len; i++) {
			String s = String.valueOf(str.charAt(i));
			if ("(".equals(s)) {
				bracket++;
			} else if (")".equals(s)) {
				bracket--;
			}

			if (bracket == 0 && bs.equals(s)) {
				list.add(bds);
				bds = "";
				continue;
			}

			bds += s;
		}

		list.add(bds);

		String[] ss = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			ss[i] = list.get(i);
		}

		return ss;
	}

	/**
	 * 用户自定义函数
	 * 
	 * @param str
	 * @param funStr
	 * @return 处理用户自定义函数
	 */
	public static String customFun(String str, String funStr) {
		String reval = "0";
		System.out.println("str="+str+",funStr="+funStr);
		SFunction sFunction = funMap.get(funStr);
		if(sFunction!=null){
			reval=sFunction.execute(str);
		}
		return reval;
	}

	/**
	 * 逻辑表达式判断
	 * 
	 * @param str
	 * @return true or false
	 */
	public static boolean compare(String str) {
		boolean flag = false;
		boolean bs = false;
		int len = str.length();
		int bracket = 0;
		String ljbds = "";
		double d_left = 0;
		double d_right = 0;

		for (int i = 0; i < len; i++) {
			String s = String.valueOf(str.charAt(i));
			if ("(".equals(s)) {
				bracket++;
			} else if (")".equals(s)) {
				bracket--;
			}

			if (bracket == 0 && lj.contains(s)) {
				for (int j = i; j < len; j++) {
					String ts = String.valueOf(str.charAt(j));
					if (lj.contains(ts)) {
						ljbds += ts;
					} else {
						bs = true;
						break;
					}
				}
			}
			if (bs)
				break;
		}

		String[] s = str.split(ljbds);
		d_left = new Double(calculate(s[0]));
		d_right = new Double(calculate(s[1]));

		if ("<".equals(ljbds)) {
			if (d_left < d_right)
				return true;
		} else if (">".equals(ljbds)) {
			if (d_left > d_right)
				return true;
		} else if ("=".equals(ljbds)) {
			if (d_left == d_right)
				return true;
		} else if (">=".equals(ljbds)) {
			if (d_left >= d_right)
				return true;
		} else if ("<=".equals(ljbds)) {
			if (d_left <= d_right)
				return true;
		} else if ("<>".equals(ljbds) || "!=".equals(ljbds)) {
			if (d_left != d_right)
				return true;
		}

		return flag;
	}

	/**
	 * 递归调用运算
	 * 
	 * @param str
	 * @return String
	 */
	public static String calculate(String str) {

		String reval = "";
		String bds = "";
		int bracket = 0;// 对应括号个数
		int pos = 0;
		boolean title = false;

		if (str.substring(0, 1).equals("`")) {
			str = str.substring(1);
			title = true;
		}

		int len = str.length();

		for (int i = 0; i < len; i++) {
			String s = String.valueOf(str.charAt(i));
			pos = i;
			bracket = 0;
			if (!lc.contains(s)) {// 如果没遇到运算符
				if ("(".equals(s)) {// 如果遇到左括号
					if (funMap.containsKey(bds)) {// 如果左括号前是函数
						for (int j = i + 1; j < len; j++) {// 从左括号后开始循环
							pos++;// 累计移动字符位数
							String ts = String.valueOf(str.charAt(j));// 单个字符
							// reval+=ts;
							if ("(".equals(ts))// 如果是左括号累计
								bracket++;
							else if (")".equals(ts)) {// 如果是右括号进行减少
								bracket--;
								if (bracket == -1) {// 如果是-1,标识括号结束
									reval = reval.substring(0, reval.length()
											- bds.length());// 重新获得去掉函数头的表达式
									reval += funCalculate(
											str.substring(i + 1, j), bds);// 表达式加上函数结果,形成新表达式
									i = pos;// 计数器增加
									bds = "";// 函数头清空
									break;// 退出本次循环
								}
							}
						}
					} else {// 如果是普通运算
						for (int j = i + 1; j < len; j++) {
							pos++;
							String ts = String.valueOf(str.charAt(j));
							if ("(".equals(ts))
								bracket++;
							else if (")".equals(ts)) {
								bracket--;
								if (bracket == -1) {
									reval += calculate(str
											.substring(i + 1, pos));
									i = pos;
									bds = "";
									break;
								}
							}
						}
					}
				} else {// 累加总表达式和最后一个运算数(或函数)
					bds += s;
					reval += s;
				}
			} else {// 遇到运算符最后一个运算数(或函数)清空
				bds = "";
				reval += s;
			}
		}

		if (title)
			reval = "-" + reval;
		System.out.println("reval="+reval);
		return szys(reval);
	}

	/**
	 * 函数运算
	 * 
	 * @param gs
	 * @param flag
	 * @return String
	 */
	public static String funCalculate(String gs, String funStr) {
		String rval = "0";
		System.out.println("funCalculate");
		if (funMap.containsKey(funStr)) {
			try {
				rval = customFun(gs, funStr);
			} catch (Exception e) {

			}
		}

		return rval;
	}

	/**
	 * 公式里的参数分割
	 * 
	 * @param str
	 * @return String[]
	 */
	public static String[] zlcs(String str) {
		int len = str.length();
		boolean flag = true;
		String tstr = "";

		for (int i = 0; i < len; i++) {
			String s = String.valueOf(str.charAt(i));
			if ("(".equals(s)) {
				flag = false;
			} else if (")".equals(s)) {
				flag = true;
			}
			if (flag && ",".equals(s)) {
				tstr += "@";
			} else {
				tstr += s;
			}
		}

		return tstr.split("@");

	}

	/**
	 * 四则运算表达式处理
	 * 
	 * @param str
	 * @return String
	 */
	public static String szys(String gs) {
		System.out.println("gs="+gs);
		gs = gs + "+0"; // 因为下面的计算是遇到符号才进行,所以多加入一个计算符号,不影响值.
		String c1 = "";// 第一个运算符号
		String c2 = "";// 第二个运算符号
		String s1 = "";// 第一个运算数
		String s2 = "";// 第二个运算数
		String s3 = "";// 第三个运算数

		int len = gs.length();
		for (int i = 0; i < len; i++) {
			String s = String.valueOf(gs.charAt(i));// 获得该位置字符并转换成字符串做比较

			if (lc.contains(s)) { // 如果是运算符号
				if (c1.length() == 0)// 如果第一个运算符号为空,加入
					c1 = s;
				else if (c2.length() == 0) {// 否则,如果第二个运算符号为空,加入
					c2 = s;// 第二个运算符号
					if ("+".equals(c2) || "`".equals(c2)) {// 如果第二个运算符号级别低,那么进行计算
						s1 = _4zys(s1, c1, s2);// 第一个和第二个数计算
						c1 = c2;// 保存第二个运算符,其他为空
						c2 = "";
						s2 = "";
					}
				} else {// 上述都保存过
					if ("+".equals(s) || "`".equals(s)) {// 如果第三个运算符级别低,进行运算
						s2 = _4zys(s2, c2, s3);// 先算第二三个数,保存至第二个
						s1 = _4zys(s1, c1, s2);// 再算第一二个,保存至第一个
						c1 = s;// 保存当前运算符,其他为空
						s2 = "";
						c2 = "";
						s3 = "";
					} else {// 如果第三个运算符级别高
						s2 = _4zys(s2, c2, s3);// 先算第二三个数,保存至第二个
						c2 = s;// 前面不动,保存运算符
						s3 = "";
					}
				}
			} else if (s1.length() > 0 && c1.length() > 0 && c2.length() == 0) {// 如果第一个数,第一个运算符已保存,第二个运算符未保存,保存第二哥数
				s2 += s;
			} else if (c1.length() == 0) {// 如果没有运算符,保存第一个数
				s1 += s;
			} else if (s1.length() > 0 && s2.length() > 0 && c1.length() > 0
					&& c2.length() > 0) {// 如果第一二个数和运算符都有,保存第三个数
				s3 += s;
			}
		}
		return s1;
	}

	/**
	 * 基本四则运算
	 * 
	 * @param c1
	 *            运算数1
	 * @param s1
	 *            运算符(加减乘除)
	 * @param c2
	 *            运算数2
	 * @return String
	 */
	public static String _4zys(String c1, String s1, String c2) {
		String reval = "0";

		try {
			double ln = Double.valueOf(c1).doubleValue();
			double rn = Double.valueOf(c2).doubleValue();
			if ("+".equals(s1)) {
				return (ln + rn) + "";
			} else if ("`".equals(s1)) {
				return (ln - rn) + "";
			} else if ("*".equals(s1)) {
				return (ln * rn) + "";
			} else if ("/".equals(s1)) {
				if (rn == 0)
					return reval;
				else
					return (ln / rn) + "";
			}
		} catch (Exception e) {
		} finally {
		}

		return reval;
	}
	
	public static void main(String[] args) {
		System.out.println(calculate(strCast("owner(4*-5)")));
	}
}
