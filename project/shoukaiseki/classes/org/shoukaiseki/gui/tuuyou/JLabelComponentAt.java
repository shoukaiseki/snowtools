package org.shoukaiseki.gui.tuuyou;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.shoukaiseki.gui.jbutton.TurubaGroup;
/**
 * 为ClosableTabbedPane提供唯一索引
 * @author Administrator
 *
 */
public class JLabelComponentAt extends JLabel{
	/**
	 * 唯一ID,比较此值,判断是否为此监听对象
	 */
	private static int idEQU=0;
	private int ID=0;
	TurubaGroup tg;
	String name;
	ImageIcon icon;
	
	public JLabelComponentAt(String name) {
		super(name);
		ID=++idEQU;
		this.name = name;
	} 
	public JLabelComponentAt(String name, ImageIcon icon) {
		super(name,icon,RIGHT);
		ID=++idEQU;
		this.name = name;
		this.icon = icon;
	}
	public int getID(){
		return ID;
	}
	public void setTurubaGroup(TurubaGroup tg){
		this.tg=tg;
	}	
	public TurubaGroup getTurubaGroup(){
		return tg;
	}
}


