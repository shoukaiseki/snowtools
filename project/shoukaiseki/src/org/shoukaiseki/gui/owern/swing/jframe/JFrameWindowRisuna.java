package org.shoukaiseki.gui.owern.swing.jframe;

import java.awt.event.ComponentEvent;

public interface JFrameWindowRisuna {

	/**
	 * 主框架提供的监听:组件变得不可见时调用
	 * 
	 * @param e
	 */
	public void componentHidden(ComponentEvent e);

	/**
	 * 主框架提供的监听:组件位置更改时调用。
	 * 
	 * @param e
	 */
	public void componentMoved(ComponentEvent e);
	/**
	 * 主框架提供的监听:组件大小更改时调用。
	 * 
	 * @param e
	 */
	public void componentResized(ComponentEvent e);
	/**
	 * 主框架提供的监听:组件变得可见时调用。
	 * 
	 * @param e
	 */
	public void componentShown(ComponentEvent e);

	/**
	 * 主框架提供的监听:关闭监听
	 * 
	 * @return true允许关闭
	 */
	public boolean windowClosing();
}


