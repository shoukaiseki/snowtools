package org.tomaximo.tuuyouclass;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.beans.PropertyVetoException;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;

import org.shoukaiseki.gui.flowlayout.ModifiedFlowLayout;
import org.shoukaiseki.gui.jtextpane.JTextPaneDoc;
import org.tomaximo.purosesu.ReadPurosesuName;


public class KonntenntuOutToJInternalFrame extends JInternalFrame implements ScrollPaneConstants {


	private JTextPaneDoc textPane;
	private JLabel jl;
	
	/**
	 * 
	 */
	public KonntenntuOutToJInternalFrame() {
		
		textPane = new JTextPaneDoc();
		textPane.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		textPane.setText("信息输出窗口!");
		textPane.setFont(new Font("宋体", Font.BOLD, 13));
		textPane.setLayout(new ModifiedFlowLayout());// 加不加都感觉有效果,如果一段英文无空格就会出现不会自动换行




		JScrollPane jsp = new JScrollPane(textPane);// 新建一个滚动条界面，将文本框传入
		// 滚动条
		jsp.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);

		jl = new JLabel("状态无异常!", SwingConstants.LEFT);
		JLabel jl1 = new JLabel(
				"java -version "
						+ System.getProperty("java.version")
						+ "        Chiang Kai-shek(shoukaiseki) <jiang28555@gmail.com>  "
						+ "  2012-02-20 Tokyo japan ",SwingConstants.CENTER);
		// jl1.setBorder(BorderFactory.createEtchedBorder());//分界线
		jl1.setFont(new Font("标楷体 ", Font.BOLD, 13));
//		Box box = Box.createVerticalBox(); // 竖结构
//		box.add(jl1);
//		box.add(Box.createVerticalStrut(8)); // 两行的间距
		Box box2 = Box.createHorizontalBox(); // 横结构
		box2.add(new JLabel("状态栏:", SwingConstants.LEFT));
		box2.add(Box.createHorizontalStrut(8));// 间距
		box2.add(jl);
		box2.add(Box.createHorizontalStrut(8));// 间距
		// box2.add(jb_insert);

		jsp.setBorder(BorderFactory.createEtchedBorder());// 分界线

		this.add(jl1, BorderLayout.NORTH);
		this.add(jsp, BorderLayout.CENTER);
		this.add(box2, BorderLayout.SOUTH);

		this.setSize(700, 500);
		// 隐藏frame的标题栏,此功暂时关闭，以方便使用window事件
		this.setLocation(200, 150);
		this.setVisible(true);
		this.setTitle("信息输出窗口!");
		textPane.cleanText();
		this.setResizable(true);
//		this.setClosable(true);
		this.setMaximizable(true);
		this.setIconifiable(true);
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws BadLocationException
	 */
	public static void main(String[] args) throws IOException,
			BadLocationException {
		KonntenntuOutToJInternalFrame kotjf = new KonntenntuOutToJInternalFrame();

		JFrame frame = new JFrame("标题");
		Container c = frame.getContentPane();
		JLabel jl1 = new JLabel(
				"java -version "
						+ System.getProperty("java.version")
						+ "        Chiang Kai-shek(shoukaiseki) <jiang28555@gmail.com>  "
						+ "  2012-02-20 Tokyo japan ",SwingConstants.CENTER);

		JList jp=new JList();
		jp.add(kotjf);
		JDesktopPane desktop =new JDesktopPane();
		desktop.add(kotjf);
		c.add(jl1,BorderLayout.NORTH);
		c.add(desktop,BorderLayout.CENTER);
		frame.setSize(700, 500);
		// frame.setLocation(200, 150);
		/**
		 * 设置显示为中心
		 */
		frame.setLocationRelativeTo(null);
		/**
		 * frame.show();失效后采用这个
		 */
		frame.setVisible(true);
		/**
		 * 关闭后终止进程=彻底退出
		 */

		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		try {
			kotjf.setMaximum(true);
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	

	/**
	 * 
	 */
	public void println(String konntenntu) {
		println(konntenntu, false);
	}
	/**
	 * 
	 */
	public void println(String konntenntu,boolean b) {
		try {
			textPane.addLastLine(konntenntu, b);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void printLabel(String s){
		jl.setText(s);
	}

	public void printLabel(int s){
		jl.setText(Integer.toString(s));
	}
	public JTextPaneDoc getTextPane()
	{
		return this.textPane;
	}
	
	public void setTextPane(JTextPaneDoc jtpd){
		 this.textPane=jtpd;
	}
}

