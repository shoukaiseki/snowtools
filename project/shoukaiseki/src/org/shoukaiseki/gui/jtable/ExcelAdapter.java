package org.shoukaiseki.gui.jtable;


import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;

import antlr.Version;
/**
 * 经过修改,已完美兼容excel,源程序常用空格,现已修改用\n(下一单元格)\t(右边单元格),增加双引号识别功能
 * 只对java Swing JTables的表有效
 * 实现Swing的JTables和Excel间的复制和粘贴功能
 * 单元格兼容,但是多个单元格不兼容
 * @author 蒋カイセキ
 *
 */
public class ExcelAdapter implements ActionListener {
	/**
	 * 包含此类字符,剪贴板单元格带双引号
	 */
	private String[] nijyuuinnyoufuValue = { "\n", "\r", "\t" };
	private String rowstring, value;
	private Clipboard system;
	private StringSelection stsel;
	private JTable jTable1;

	/**
	 * Excel 适配器由 JTable 构成， 它实现了 JTable 上的复制粘贴 功能，并充当剪贴板监听程序。
	 */
	public ExcelAdapter(JTable myJTable) {
		jTable1 = myJTable;
		KeyStroke copy = KeyStroke.getKeyStroke(KeyEvent.VK_C,
				ActionEvent.CTRL_MASK, false);
		// 确定复制按键用户可以对其进行修改
		// 以实现其它按键组合的复制功能。
		KeyStroke paste = KeyStroke.getKeyStroke(KeyEvent.VK_V,
				ActionEvent.CTRL_MASK, false);
		// 确定粘贴按键用户可以对其进行修改
		// 以实现其它按键组合的复制功能。
		jTable1.registerKeyboardAction(this, "Copy", copy,
				JComponent.WHEN_FOCUSED);
		jTable1.registerKeyboardAction(this, "Paste", paste,
				JComponent.WHEN_FOCUSED);
		system = Toolkit.getDefaultToolkit().getSystemClipboard();
	}

	/**
	 * 此适配器运行图表的公共读方法。
	 */
	public JTable getJTable() {
		return jTable1;
	}

	public void setJTable(JTable jTable1) {
		this.jTable1 = jTable1;
	}

	public void actionPerformed(ActionEvent e) {
		/**
		 * 源语句if (e.getActionCommand().compareTo("Copy") == 0) { 现取消copy
		 */
		// System.out.println(e.getActionCommand());
		if (e.getActionCommand().compareTo("Copy") == 0) {
			StringBuffer sbf = new StringBuffer();
			// 检查以确保我们仅选择了单元格的
			// 相邻块
			int numcols = jTable1.getSelectedColumnCount();
			int numrows = jTable1.getSelectedRowCount();
			int[] rowsselected = jTable1.getSelectedRows();
			int[] colsselected = jTable1.getSelectedColumns();
			if (!((numrows - 1 == rowsselected[rowsselected.length - 1]
					- rowsselected[0] && numrows == rowsselected.length) && (numcols - 1 == colsselected[colsselected.length - 1]
					- colsselected[0] && numcols == colsselected.length))) {
				JOptionPane.showMessageDialog(null, "Invalid Copy Selection",
						"Invalid Copy Selection", JOptionPane.ERROR_MESSAGE);
				return;
			}
			for (int i = 0; i < numrows; i++) {
				for (int j = 0; j < numcols; j++) {
					Object value = jTable1.getValueAt(rowsselected[i],
							colsselected[j]);
					if (value == null) {
						value = "";
					}
					if(siyouNijyuuinnyoufu((String)value)){
						value="\""+value+"\"";
//						System.out.println(value);
					}
					sbf.append(value);// 如果单元格为空则字符设置为"",不会显示null
					if (j < numcols - 1)
						sbf.append("\t");
				}
				sbf.append("\n");
			}
			stsel = new StringSelection(sbf.toString());
			system = Toolkit.getDefaultToolkit().getSystemClipboard();
			system.setContents(stsel, stsel);
		}
		if (e.getActionCommand().compareTo("Paste") == 0) {
			// System.out.println("Trying to Paste");
			int startRow = (jTable1.getSelectedRows())[0];
			int startCol = (jTable1.getSelectedColumns())[0];

			try {
				String trstring = (String) (system.getContents(this)
						.getTransferData(DataFlavor.stringFlavor));

				setTableNijyuuinnyoufu(trstring, startRow, startCol);

				// setTableNijyuuinnyoufuNai(trstring, startRow, startCol);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 兼容Excel单元格内的横向列表,换行键\t\n
	 * 
	 * @param trstring
	 * @param row
	 * @param col
	 */
	public void setTableNijyuuinnyoufu(String trstring, int row, int col) {
		int startRow = row;
		int startCol = col;
		StringBuilder sb = new StringBuilder(trstring);
		int index=sb.lastIndexOf("\n");
		if(index==sb.length()-1)sb.delete(index,sb.length());//删除最后一个回车符
		jTable1.setValueAt("",startRow,startCol);
		loop:while (sb.length() != 0) {
			int nijyuuinnyoufu = sb.indexOf("\"");// 双引号
			int ht = sb.indexOf("\t");// 横向列表
			int lf = sb.indexOf("\n");// 换行
//			 System.out.println("sb==["+sb.toString()+"]");
			// System.out.println("nijyuuinnyoufu="+nijyuuinnyoufu+",ht="+ht+",lf="+lf);
			// System.out.println("startRow="+startRow+",startCol="+startCol);
			if (nijyuuinnyoufu == 0) {
				for(nijyuuinnyoufu = sb.indexOf("\"", nijyuuinnyoufu + 1);nijyuuinnyoufu!=-1;nijyuuinnyoufu = sb.indexOf("\"", nijyuuinnyoufu + 1)){
					if("\t\n".indexOf(sb.substring(nijyuuinnyoufu + 1, nijyuuinnyoufu + 1))==-1)continue;
//					System.out.println(nijyuuinnyoufu);
					String str=sb.substring(1, nijyuuinnyoufu);
//					System.out.println(str);
					if(siyouNijyuuinnyoufu(str)){
							jTable1.setValueAt(str,
									startRow, startCol);
//							 System.out.println("sb.substring(1, nijyuuinnyoufu-1)=="+sb.substring(1, nijyuuinnyoufu));
							sb.delete(0, nijyuuinnyoufu + 1);
//							 System.out.println("+++++++str=={"+str+"}");
							
							continue loop;
					}
				}
			}
			if ((ht == lf)) {
				jTable1.setValueAt(sb.substring(0, sb.length()), startRow,
						startCol);
				sb.delete(0, sb.length());
				continue;
			}
			if ((ht < lf && ht != -1) || (ht != -1 && lf == -1)) {
				if (ht == 0) {
					++startCol;
					jTable1.setValueAt("",startRow,startCol);
					sb.delete(0, ht + 1);
					continue;
				}
				jTable1.setValueAt(sb.substring(0, ht), startRow, startCol);
				sb.delete(0, ht);
				continue;
			} else {
				if (lf == 0) {
					++startRow;
					jTable1.setValueAt("",startRow,startCol);
					startCol = col;
					sb.delete(0, lf + 1);
					continue;
				}
				jTable1.setValueAt(sb.substring(0, lf), startRow, startCol);
				sb.delete(0, lf);
				continue;
			}

		}
	}

	/**
	 * 无双引号剪贴板
	 * 
	 * @param trstring
	 * @param startRow
	 * @param startCol
	 */
	public void setTableNijyuuinnyoufuNai(String trstring, int startRow,
			int startCol) {
		// byte[] b=trstring.getBytes();
		//
		// System.out.println("将String型转换为二进制,十六进制");
		// for (int i = 0; i < b.length; i++) {
		// System.out.print(Integer.parseInt(Integer.toHexString(b[i]&0xff),
		// 16)+"\t");
		// // System.out.print(Integer.toHexString(b[i]&0xff)+"\t");
		// if((i+1)%10==0&&i!=0){
		// System.out.println();
		// }
		// }
		// System.out.println();

		// System.out.println("String is:" + trstring);
		String[] st1 = trstring.split("\n", -1);// 分割符为\n,空字符不丢弃
		/**
		 * t1.length-1为最后一个\n后面不做一个数组处理
		 */
		for (int i = 0; i < st1.length - 1; i++) {
			rowstring = st1[i];
			// rowstring=rowstring.replace("\n", "");//
			String[] st2 = rowstring.split("\t", -1);// 分割符为\t
			for (int j = 0; j < st2.length; j++) {
				value = st2[j];
				// value=value.replace("\t", "");//
				// System.out.println(value);
				if (startRow + i < jTable1.getRowCount()
						&& startCol + j < jTable1.getColumnCount())
					jTable1.setValueAt(value, startRow + i, startCol + j);
				// System.out.println("Putting " + value + " atrow="
				// + startRow + i + " column=" + startCol + j);
			}
		}
	}
	
	/**
	 * 需要使用双引号返回true
	 * @param str
	 * @return
	 */
	public boolean siyouNijyuuinnyoufu(String str){
//		toASCII(str);
		for (String s : nijyuuinnyoufuValue) {
//			toASCII(s);
			if(str.indexOf(s)!=-1){
				return true;
			}
		}
		return false;
	}
	/**
	 * String 转换为 ASCII
	 * 
	 * @param trstring
	 */
	public static void toASCII(String trstring) {
		byte[] b = trstring.getBytes();

		System.out.println("将String型转换为二进制,十六进制");
		StringBuffer sbInt = new StringBuffer();
		StringBuffer sbHex = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			sbInt.append(Integer.parseInt(Integer.toHexString(b[i] & 0xff), 16)
					+ "\t");
			sbHex.append("x" + Integer.toHexString(b[i] & 0xff) + "\t");
			if ((i + 1) % 10 == 0 && i != 0) {
				
				sbInt.append("\n");
				sbHex.append("\n");
			}
		}
		System.out.println("  十进制为:" + sbInt.toString());
		System.out.println("十六进制为:" + sbHex.toString());

		System.out.println("String is:[" + trstring + "]");
	}

}

