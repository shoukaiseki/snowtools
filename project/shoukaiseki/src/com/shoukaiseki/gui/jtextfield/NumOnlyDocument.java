package com.shoukaiseki.gui.jtextfield;

/**
 * 只能输入正整数的Document模板
 */
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class NumOnlyDocument extends PlainDocument{
	  public void insertString(int offset, String s, AttributeSet attrSet)throws BadLocationException {
		   try {Integer.parseInt(s);
		   }catch(NumberFormatException ex){
		    return;
		   }
		   super.insertString(offset,s,attrSet);
		  }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame f = new JFrame("只能输入正整数");
		JPanel panel = new JPanel();
		f.getContentPane().add(panel);
		JTextField txt = new JTextField();
		txt.setColumns(20);

		NumOnlyDocument doc = new NumOnlyDocument();
		txt.setDocument(doc);

		panel.add(txt);
		f.pack();
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		txt.setText("1234567890");

	}

}


