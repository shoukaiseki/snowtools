package org.shoukaiseki.gui.jbutton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.Border;

import org.shoukaiseki.gui.jcombobox.JComboBoxIcon;

/**
 * 按钮组
 * @author Administrator
 *
 */
public class TurubaGroup extends JSplitPane{
	private  JPanel jButtonGroup=new JPanel(new FlowLayout(FlowLayout.LEFT));
	private  Dimension comboBoxSize =new Dimension(100,0);//为空时最小大小
	private  JComboBoxIcon comboBox =new JComboBoxIcon();
	public TurubaGroup(){ 
		setOrientation(HORIZONTAL_SPLIT);
		setLeftComponent(jButtonGroup);
		setRightComponent(comboBox);
		setDividerSize(0);//隐藏分隔条
		setEnabled(false);//禁用分隔条
		setResizeWeight(1);//多余的空间分配左边
		comboBox.setMinimumSize(comboBoxSize);
		jButtonGroup.setBorder(BorderFactory.createEmptyBorder());
		setBorder(BorderFactory.createEmptyBorder());
//		setBorder(BorderFactory.createEtchedBorder());// 创建蚀刻边框  
//		setBorder(BorderFactory.createLineBorder(Color.blue));//创建颜色的线状边框 
		setBackground(new Color(184,201,227));
//		jButtonGroup.setBackground(new Color(184,201,227));
//		jButtonGroup.setBackground(new Color(200,221,242));
	}
	
	/**
	 * 添加到按钮的任务栏,提供图标返回添加的JButtonIcon
	 */
	public JButtonIcon addJButton(ImageIcon icon){
		Border emptyBorder = BorderFactory.createEmptyBorder(0,0, 0, 0);  
		JButtonIcon button=new JButtonIcon(icon);
		button.setOpaque(false);
		button.setBorder(emptyBorder);
		button.setContentAreaFilled(false);//开启透明色
		button.setFocusPainted(false);
		button.setRolloverEnabled(true);//设置 rolloverEnabled 属性，若需要翻转效果，该属性必须为 true。
		button.setPreferredSize(new Dimension(20, 20));
		jButtonGroup.add(button);
        return button;
	}
	
	/**
	 * 添加到按钮的任务栏
	 */
	public void addJButton(JButton button){
		jButtonGroup.add(button);
	}
	
	/**
	 * 为comboBox项列表添加项
	 */
	public void addComboBoxItem(Object anObject){
		comboBox.addItem(anObject);
	}
	/**
	 * 返回存放按钮组的JPanel组件
	 * @return
	 */
	public JPanel getJButtonGroup(){
		return jButtonGroup;
	}
	/**
	 * 返回JComboBox组件
	 * @return
	 */
	public JComboBoxIcon getComboBox(){
		return comboBox;
	}
	
	public void setComboBox(JComboBoxIcon comboBox){
		this.comboBox=comboBox;
		setRightComponent(comboBox);
		this.comboBox.setMinimumSize(new Dimension(100,0));
	}
	/**
	 * 刷新方法
	 */
	public void rifuressyu(){
		if(comboBox.getComponentCount()!=-1){
			comboBox.setMinimumSize(null);
		}else{
			comboBox.setMinimumSize(comboBoxSize);
		}
//		repaint();
//		invalidate();
//		validate();//刷新,调用该方法两次即可,或放外部调用
	}
}


