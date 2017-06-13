package org.shoukaiseki.gui.jcombobox;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.shoukaiseki.gui.tuuyou.IconListCellRenderer;
import org.shoukaiseki.gui.tuuyou.StringIcon;




public class JComboBoxIcon extends JComboBox
{
	
	DefaultComboBoxModel dcbDataModel= new DefaultComboBoxModel();
	JLabel label=new JLabel();
	public JComboBoxIcon(){
		super();
		setModel(dcbDataModel);
		setRenderer(new IconListCellRenderer());
	}

	public JComboBoxIcon(Object[] items) {
		// TODO Auto-generated constructor stub
		super(items);
		setModel(dcbDataModel);
		setRenderer(new IconListCellRenderer());
	}

	public void setSelectedItem(String name,ImageIcon icon){
		StringIcon si=new StringIcon(name,icon);
		dcbDataModel.setSelectedItem(si);
	}
	
	/**
	 * 
	 * DefaultComboBoxModel=在模型的末尾添加项
	 * @param name
	 * @param icon
	 * @return 返回StringIcon,调用getID()获得ID,比较此值,判断是否为此监听对象
	 */
	public StringIcon addElement(String name,ImageIcon icon){
		StringIcon si=new StringIcon(name,icon);
		dcbDataModel.addElement(si);
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
		dcbDataModel.insertElementAt(si,index);
		return si;
	}
	
	/**
	 *   返回 JComboBox 当前使用的数据模型。
	 * @return
	 */
	public DefaultComboBoxModel getDefaultComboBoxModel(){
		return dcbDataModel;
	}
	
}



