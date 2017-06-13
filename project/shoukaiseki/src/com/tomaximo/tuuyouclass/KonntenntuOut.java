package com.tomaximo.tuuyouclass;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;

import com.shoukaiseki.characterdetector.CharacterEncoding;
import com.shoukaiseki.characterdetector.utf.UnicodeReader;
import com.shoukaiseki.constantlib.CharacterEncodingName;
import com.shoukaiseki.gui.flowlayout.ModifiedFlowLayout;
import com.shoukaiseki.gui.jtextpane.JTextPaneDoc;
import com.tomaximo.kks.ImpKksGui;


public class KonntenntuOut extends JFrame implements ScrollPaneConstants {
	/**
	 * 关闭時是否退出程序,默认時退出,只作为子程序时不退出
	 */
	private boolean windowclose = true;

	private static JTextPaneDoc textPane;
	private JLabel jl;
	public KonntenntuOut() throws BadLocationException {
    	this(true);
	}
	/**
	 * 
	 */
	public KonntenntuOut(boolean windowclose) throws BadLocationException {
		this.windowclose=windowclose;
		textPane = new JTextPaneDoc();
		textPane.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		textPane.setText("信息输出窗口!");
		textPane.setFont(new Font("宋体", Font.BOLD, 13));
		textPane.setLayout(new ModifiedFlowLayout());// 加不加都感觉有效果,如果一段英文无空格就会出现不会自动换行



		// 设置主框架的布局
		Container c = getContentPane();
		// c.setLayout(new BorderLayout());

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

		c.add(jl1, BorderLayout.NORTH);
		c.add(jsp, BorderLayout.CENTER);
		c.add(box2, BorderLayout.SOUTH);

		this.setSize(700, 500);
		// 隐藏frame的标题栏,此功暂时关闭，以方便使用window事件
		this.setLocation(200, 150);
		this.setVisible(true);
		this.setTitle("信息输出窗口!");
  		if (windowclose) { 
  			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
  		}
		textPane.cleanText();
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws BadLocationException
	 */
	public static void main(String[] args) throws IOException,
			BadLocationException {
		KonntenntuOut kks = new KonntenntuOut();

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
}


