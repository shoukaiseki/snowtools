package org.shoukaiseki.gui.tabbedpane;

import java.awt.event.MouseEvent;
/**
 * 标签事件
 * @author Administrator
 *
 */
public interface ClosableTabbedPaneRisuna {

	/**
	 * 触发关闭标签事件,并提供关闭对象的索引
	 * @author Administrator
	 *
	 */
	public boolean tabAboutToClose(int tabIndex,MouseEvent me);
	/**
	 *鼠标双击触发事件
	 * @param e
	 * @return
	 */
	public void tabAboutToDoubleClicked(int tabIndex);

	/**
	 *   鼠标按键在组件上单击（按下并释放）时调用。
	 * @param e
	 */
	public void tabAboutToMouseClicked(MouseEvent e);
		
	
}


