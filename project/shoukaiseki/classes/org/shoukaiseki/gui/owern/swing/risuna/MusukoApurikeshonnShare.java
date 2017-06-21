package org.shoukaiseki.gui.owern.swing.risuna;

import java.awt.event.MouseEvent;

import javax.swing.event.ListSelectionEvent;

import org.shoukaiseki.gui.owern.swing.ijframe.Jnf;
import org.shoukaiseki.gui.owern.swing.jframe.MusukoOwnerJFrame;
import org.shoukaiseki.gui.owern.swing.jframe.OwnerJFrame;
import org.shoukaiseki.gui.tuuyou.JLabelComponentAt;

public interface MusukoApurikeshonnShare extends ApurikeshonnShare{
	/**
	 * JTabbedPane标签页唯一索引标识
	 * @param jca
	 */
	public void setJLabelComponentAt(JLabelComponentAt jca);
	/**
	 * 应用初始化
	 */
	public void appInit();
	
	public JLabelComponentAt getJLabelComponentAt();
	
	public boolean tabuTojiruRisuna(int tabIndex,MouseEvent me);
}


