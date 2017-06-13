package com.shoukaiseki.gui.jlist;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import com.shoukaiseki.gui.owern.swing.jframe.OwnerJFrame;
import com.shoukaiseki.gui.tuuyou.IconListCellRenderer;
import com.shoukaiseki.gui.tuuyou.StringIcon;

public class JListIcon extends JList {
	DefaultListModel dlmDataModel=new DefaultListModel();
	JLabel label=new JLabel();
	public JListIcon(){
		setModel(dlmDataModel);
		setCellRenderer(new IconListCellRenderer());
		
	}

	public JListIcon(DefaultListModel dlmDataModel){
		this.dlmDataModel=dlmDataModel;
		setModel(dlmDataModel);
		setCellRenderer(new IconListCellRenderer());
		
	}

	/**
	 * DefaultComboBoxModel=在模型的末尾添加项
	 * @param name
	 * @param icon
	 * @return 返回StringIcon,调用getID()获得ID,比较此值,判断是否为此监听对象
	 */
	public StringIcon addElement(String name,ImageIcon icon){
		StringIcon si=new StringIcon(name,icon);
		dlmDataModel.addElement(si);
		return si;
	}
	/**
	 * DefaultComboBoxModel=在指定索引处添加项。
	 * @param name
	 * @param icon
	 * @param index
	 * @return 返回StringIcon,调用getID()获得ID,比较此值,判断是否为此监听对象
	 */
	public  StringIcon insertElementAt(String name,ImageIcon icon,int index){
		StringIcon si=new StringIcon(name,icon);
		dlmDataModel.insertElementAt(si,index);
		return si;
	}
	
	/**
	 *   返回 JComboBox 当前使用的数据模型。
	 * @return
	 */
	public DefaultListModel getDefaultComboBoxModel(){
		return dlmDataModel;
	}
	
	public static void main(String args[]){
		OwnerJFrame oj = new OwnerJFrame();
		JListIcon jli=new JListIcon();
		oj.setDefaultCloseOperation(oj.EXIT_ON_CLOSE);

		ImageIcon setuzokuIcon = new ImageIcon("./image/oicons/commit.png");
		jli.addElement("commit.png", setuzokuIcon);
		setuzokuIcon = new ImageIcon("./image/oicons/check.png");
		jli.addElement("check.png", setuzokuIcon);
		

		
		oj.getContentPane().add(jli,BorderLayout.CENTER);
		oj.setVisible(true);
	}
}


