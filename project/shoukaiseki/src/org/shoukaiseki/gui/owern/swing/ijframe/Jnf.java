package org.shoukaiseki.gui.owern.swing.ijframe;

import java.awt.Container;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;

import org.shoukaiseki.gui.jinternalframe.MCOCInternalFrameK;
import org.shoukaiseki.gui.owern.swing.jframe.JFrameWindowClose;
import org.shoukaiseki.gui.owern.swing.jframe.JFrameWindowRisuna;
import org.shoukaiseki.gui.owern.swing.jframe.OwnerJFrame;
import org.shoukaiseki.gui.owern.swing.risuna.ApurikeshonnShare;


public class Jnf extends MCOCInternalFrameK
implements ApurikeshonnShare{
	private OwnerJFrame owner=null;
	private Container c;
	protected ImageIcon puraguinnIcon=null;//插件图标
	protected String puraguinnName=null;//插件名称
	protected String puraguinnTyuusyaku=null;//插件备注
	protected String puraguinnBajyonn=null;//插件版本号
	
	
	public Jnf(){
		this.setSize(300, 200);
		this.setVisible(true);
		this.hideNorthPanel();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}




	@Override
	public void setOwnerJFrame(OwnerJFrame owner) {
		// TODO Auto-generated method stub
		this.owner=owner;
		System.out.println("this.owner="+owner);
		c=this.owner.getContentPane();
	}

	/**
	 * 添加主框架关闭事件监视器
	 * @param wc
 	 */
	public void addwindwoCloseMap(JFrameWindowClose wc){
		owner.addwindwoCloseMap(wc);
	} 
	/**
	 * 添加主框架各种事件监听器
	 * @param wr
	 */
	public void addWindowRisuna(JFrameWindowRisuna wr){
		owner.addWindowRisuna(wr);
	}
	
	public OwnerJFrame getOwnerJFrame() {
		// TODO Auto-generated method stub
		return this.owner;
	}
	/**
	 *
	 */ 
	public void init(){
	}
	/**
	 * 在content末尾加入一行内容age0,如果age0内容为空时则直接取消在String concat前的LineBreaks
	 */
	public void println(String konntenntu) {
		owner.println(konntenntu);
	}
	/**
	 * 在content末尾加入一行内容age0,如果age0内容为空时则直接取消在String concat前的LineBreaks
	 * b true红色字体
	 */
	public void println(String konntenntu,boolean b) {
		owner.println(konntenntu, b);
	}

	/**
	 * 输出至控制台的信息,可开启关闭
	 * @param i
	 */
	public void printlnConsole(int i){
		owner.printlnConsole(i);
	}
	/**
	 * 输出至控制台的信息,可开启关闭
	 * @param i
	 */
	public void printlnConsole(String s){
		owner.printlnConsole(s);
	}
	
	@Override
	public String getPuraguinnName() {
		// TODO Auto-generated method stub
		return puraguinnName;
	}

	@Override
	public String getPuraguinnBajyonn() {
		// TODO Auto-generated method stub
		return puraguinnBajyonn;
	}

	@Override
	public String getPuraguinnTyuusyaku() {
		// TODO Auto-generated method stub
		return puraguinnTyuusyaku;
	}

	@Override
	public ImageIcon getPuraguinnIcon() {
		// TODO Auto-generated method stub
		return puraguinnIcon;
	}
}


