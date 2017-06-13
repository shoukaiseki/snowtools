package org.tomaximo.puroguramu.ijframe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

import org.shoukaiseki.gui.flowlayout.ModifiedFlowLayout;
import org.shoukaiseki.gui.jtextpane.JTextPaneDoc;
import org.shoukaiseki.gui.owern.swing.ijframe.MusukoOwnerJnf;
import org.tomaximo.puroguramu.tuuyou.IconSigenn;
import org.tomaximo.tuuyouclass.KonntenntuOutToJInternalFrame;

public class PrintJyouhou extends MusukoOwnerJnf implements ScrollPaneConstants {
	private JTextPaneDoc textPane;
	private JLabel jl;
	public JPopupMenu jPopupMenu = new JPopupMenu();
	/**
	 * 
	 */
	public PrintJyouhou() {
		
		textPane = new JTextPaneDoc();
		textPane.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		textPane.setText("信息输出窗口!");
		textPane.setFont(new Font("宋体", Font.BOLD, 13));
		textPane.setLayout(new ModifiedFlowLayout());// 加不加都感觉有效果,如果一段英文无空格就会出现不会自动换行




		JScrollPane jsp = new JScrollPane(textPane);// 新建一个滚动条界面，将文本框传入
		// 滚动条
		jsp.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);

		jl = new JLabel("状态无异常!", SwingConstants.LEFT);
		
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
		setBorder(BorderFactory.createEmptyBorder());
		jsp.setBorder(BorderFactory.createEmptyBorder());
		box2.setBorder(BorderFactory.createEmptyBorder());
//		textPane.setBackground(Color.red);
//		jsp.setBackground(Color.red);
		box2.setBackground(Color.red);
//		this.getContentPane().setBackground(Color.red);
		popupMenuInit();
		textPane.add(jPopupMenu);
		
		
		textPane.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent er) {
				// 是否右键单击
				if (er.getClickCount() == 1
						&& SwingUtilities.isRightMouseButton(er)) {

					jPopupMenu.show(textPane, er.getX(), er.getY());
				}
			}
		});
	}
	/**
	 * 菜单初始化
	 */
	public void popupMenuInit() {
		JMenuItem clear = new JMenuItem("清空 ",IconSigenn.CLEAR);
//		JMenuItem item2 = new JMenuItem("编辑节点 ");
//		JMenuItem item3 = new JMenuItem("删除节点 ");
		jPopupMenu.add(clear);
//		jPopupMenu.add(item2);
//		jPopupMenu.add(item3);
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				textPane.cleanText();
			}
		});
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


