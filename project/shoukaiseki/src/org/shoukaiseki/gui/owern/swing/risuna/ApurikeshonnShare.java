package org.shoukaiseki.gui.owern.swing.risuna;

import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.shoukaiseki.gui.owern.swing.jframe.OwnerJFrame;


public interface ApurikeshonnShare {
	/**
	 * 提供主框架,继承与JFrame
	 * @param owner
	 */
	public void setOwnerJFrame(OwnerJFrame owner);
	
	/**
	 * 初始化方法
	 */
	public void init();
	
	/**
	 * 插件图标
	 * @return
	 */
	public ImageIcon getPuraguinnIcon();
	/**
	 * 插件名称
	 * @return
	 */
	public String getPuraguinnName();
	
	/**
	 * 插件备注
	 * @return
	 */
	public String getPuraguinnTyuusyaku();
	
	/**
	 * 插件版本号
	 * @return
	 */
	public String getPuraguinnBajyonn();
}


