package org.shoukaiseki.gui.jtextpane;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.shoukaiseki.string.ConCatLineBreaks;


public class JTextPaneDoc extends JTextPane{ 

	/**
	 * 显示到jtextpane的内容
	 */
	private StyledDocument doc = null; // 非常重要插入文字样式就靠它了
	private SimpleAttributeSet attrSet=new SimpleAttributeSet();//
	private SimpleAttributeSet defuorutoAttrSet=new SimpleAttributeSet();;//
	
	public JTextPaneDoc(){
		doc=this.getStyledDocument();
//		StyleConstants.setFontFamily(defuorutoAttrSet, "微软雅黑");
		StyleConstants.setBold(defuorutoAttrSet, true);//粗体
		StyleConstants.setItalic(defuorutoAttrSet, false);//斜体
		StyleConstants.setUnderline(defuorutoAttrSet,false);//下划线
//		 StyleConstants.setAlignment(defuorutoAttrSet, 1);//左右对齐
		StyleConstants.setFontSize(defuorutoAttrSet, 16);//字体大小
		StyleConstants.setForeground(defuorutoAttrSet, new Color(Color.red.getRGB()));//字体颜色
//		StyleConstants.setBackground(defuorutoAttrSet, new Color(0, 0, 0));//字体底色
	}

	/**
	 * 自定义SimpleAttributeSet输出
	 * @param args0
	 * @param color
	 */
	public void addLastLine(String args0,SimpleAttributeSet sas){
		try {
			doc.insertString(doc.getLength(),args0+"\n",sas);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 自定义颜色输出
	 * @param args0
	 * @param color
	 */
	public void addLastLine(String args0,Color color){
		try {
			SimpleAttributeSet sas=new SimpleAttributeSet();
			StyleConstants.setForeground(sas, color);//字体颜色
			doc.insertString(doc.getLength(),args0+"\n",sas);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 在content末尾加入一行内容age0,如果age0内容为空时则直接取消在String concat前的LineBreaks
	 * @param age0
	 * 			加入的行内容
	 * @return content
	 * @throws BadLocationException 
	 */
	public void addLastLine(String age0) {
		try{
			doc.insertString(doc.getLength(),age0+"\n",attrSet);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setCaretPosition(doc.getLength());
	}
	/**
	 * 在content末尾加入一行内容age0,如果age0内容为空时则直接取消在String concat前的LineBreaks
	 * @param age0
	 * 			加入的行内容
	 * @return content
	 * @throws BadLocationException 
	 */
	public void addLastLine(String age0,boolean b) throws BadLocationException{
		if(b){
			doc.insertString(doc.getLength(),age0+"\n",defuorutoAttrSet);
		}else{
			doc.insertString(doc.getLength(),age0+"\n",attrSet);
		}
		setCaretPosition(doc.getLength());
	}
	
	public void setContent(String age0){
		setText(age0);
	}
	
	public void addLastText(String age0) throws BadLocationException{
		doc.insertString(doc.getLength(),age0,attrSet);
		setCaretPosition(doc.getLength());
	}
	public void cleanText(){
		setText("");
	}
}


