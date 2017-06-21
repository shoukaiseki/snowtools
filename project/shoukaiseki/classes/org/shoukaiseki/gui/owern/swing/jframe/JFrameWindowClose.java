package org.shoukaiseki.gui.owern.swing.jframe;

import java.awt.event.WindowEvent;


public interface JFrameWindowClose {

	/**
	 * 主框架提供的监听:关闭监听
	 * 
	 * @return true允许关闭
	 */
	public boolean windowClosing(WindowEvent e);
}


