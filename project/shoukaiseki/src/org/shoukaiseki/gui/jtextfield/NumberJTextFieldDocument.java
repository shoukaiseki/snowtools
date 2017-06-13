package org.shoukaiseki.gui.jtextfield;
/**
 * 只能输入数字的Document模板
 */
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class NumberJTextFieldDocument extends PlainDocument implements
		KeyListener, CaretListener {

	int integerLength = -1;
	int fractionLength = -1;
	double MaxValue, MinValue;
	boolean useBound = false;
	String correctValue = "";
	private int caretposold;
	private int caretpos;

	/** 
    * 
    *
     */
	public NumberJTextFieldDocument() {
		super();
	}

	/**
	 * 
	 * @param maxValue
	 * @param minValue
	 */
	public NumberJTextFieldDocument(double maxValue, double minValue) {
		super();
		setBound(maxValue, minValue);
	}

	/**
	 * 
	 * @param integerLength
	 * @param fractionLength
	 */
	public NumberJTextFieldDocument(int integerLength, int fractionLength) {
		super();
		setLength(integerLength, fractionLength);
	}

	/**
	 * 
	 * @param integerLength
	 * @param fractionLength
	 */
	public void setLength(int integerLength, int fractionLength) {
		if (integerLength < 1 || fractionLength < 0) {
			// Error
		} else {
			this.integerLength = integerLength;
			this.fractionLength = fractionLength;
		}
	}

	/**
	 * 
	 * @param maxValue
	 * @param minValue
	 */
	public void setBound(double maxValue, double minValue) {
		if (maxValue < minValue)
			return;
		MaxValue = maxValue;
		MinValue = minValue;
		useBound = true;
	}

	public void insertString(int offs, String str, AttributeSet a)
			throws BadLocationException {

		String oldtxt = getText(0, getLength());
		String newtxt = oldtxt.substring(0, offs) + str
				+ oldtxt.substring(offs);
		boolean strRight = testString(oldtxt);
		boolean newtxtRight = testString(newtxt);
		if (strRight && !newtxtRight) {
			return;
		} else if (!strRight && !newtxtRight) {
			int rlen = correctValue.length();
			int flen = oldtxt.length();
			int sublen = rlen - flen;
			for (int i = 0; i < flen; i++) {
				String substr = correctValue.substring(0, i)
						+ correctValue.substring(i + sublen);
				if (substr.equals(oldtxt)) {
					super.insertString(i,
							correctValue.substring(i, i + sublen), a);
					correctValue = String.copyValueOf(getText(0, getLength())
							.toCharArray());
					return;
				}
			}
			return;
		}

		super.insertString(offs, str, a);
		correctValue = String
				.copyValueOf(getText(0, getLength()).toCharArray());

	}

	public void caretUpdate(CaretEvent e) {
		caretposold = caretpos;
		caretpos = e.getDot();
		// System.out.print(" " + caretpos);
	}

	public void keyTyped(KeyEvent e) {

	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE
				|| e.getKeyCode() == KeyEvent.VK_DELETE) {
			JTextField field = (JTextField) e.getSource();
			try {
				String nowtxt = getText(0, getLength());
				String selecttxt = field.getSelectedText();
				if (correctValue == null
						|| (selecttxt != null && selecttxt.equals(correctValue))) {
					correctValue = null;
					return;
				}
				if (!testString(nowtxt)) {
					int nowpos = caretposold - caretpos == correctValue
							.length()
							- nowtxt.length() ? caretposold : caretpos;
					remove(0, getLength());
					insertString(0, correctValue, null);
					field.setCaretPosition(nowpos);
				} else {
					correctValue = nowtxt;
				}
			} catch (BadLocationException e1) {
				System.out.println(e1);
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		if (getLength() == 0) {
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE
				|| e.getKeyCode() == KeyEvent.VK_DELETE) {
			JTextField field = (JTextField) e.getSource();
			try {
				String nowtxt = getText(0, getLength());
				String selecttxt = field.getSelectedText();
				if (correctValue == null
						|| (selecttxt != null && selecttxt.equals(correctValue))) {
					correctValue = null;
					return;
				}
				if (!testString(nowtxt)) {
					int nowpos = caretposold - caretpos == correctValue
							.length()
							- nowtxt.length() ? caretposold : caretpos;
					remove(0, getLength());
					insertString(0, correctValue, null);
					field.setCaretPosition(nowpos);
				} else {
					correctValue = nowtxt;
				}
			} catch (BadLocationException e1) {
				System.out.println(e1);
			}
		}
	}

	private boolean testString(String waitfortest) {
		int dotindex = waitfortest.indexOf(" . ");
		int strLength = waitfortest.length();
		if (integerLength != -1 && fractionLength != -1) {
			if (fractionLength == 0 && dotindex >= 0) {
				return false;
			}
			if (dotindex > 0 && dotindex != strLength - 1) {
				String[] splitstrs = waitfortest.split(" \\. ");
				if (splitstrs.length != 2) { // impossible
					return false;
				} else if (splitstrs[0].length() > integerLength
						|| splitstrs[1].length() > fractionLength) {
					return false;
				}
			} else if ((dotindex == 0 && strLength > fractionLength + 1)
					|| (dotindex == strLength - 1 && strLength > integerLength + 1)
					|| (dotindex < 0 && strLength > integerLength)) {
				return false;
			}
		}

		waitfortest = waitfortest.toLowerCase();
		/**
		 * waitfortest.indexOf("   ") >= 0 || waitfortest.indexOf(" d ") >= 0 ||
		 * waitfortest.indexOf(" f ") >= 0
		 * 
		 */
		if (waitfortest.indexOf("   ") >= 0 || waitfortest.indexOf(" d ") >= 0
				|| waitfortest.indexOf(" f ") >= 0) {
			return false;
		}
		try {
			if (dotindex == 0) {
				waitfortest = " 0 " + waitfortest;
			}
			double num = Double.parseDouble(waitfortest);
			if (useBound && (num > MaxValue || num < MinValue)) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		JFrame f = new JFrame("TextDocumentTest");
		JPanel panel = new JPanel();
		f.getContentPane().add(panel);
		JTextField txt = new JTextField();
		txt.setColumns(20);

		NumberJTextFieldDocument doc = new NumberJTextFieldDocument();
		doc.setBound(99999, 1);
		// doc.setLength(5, 5);
		txt.setDocument(doc);
		txt.addKeyListener(doc);
		txt.addCaretListener(doc);

		panel.add(txt);
		f.pack();
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		txt.setText("123.123");

	}

}


