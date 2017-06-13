package org.shoukaiseki.gui.jtextfield;

/**
 * Java实现自动补全提示的文本框
 * http://pufan53.iteye.com/blog/755079
 * 自动补全的JTextField
 */
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class AutoCompletionField extends JTextField implements
		DocumentListener, MouseListener, ListSelectionListener, ActionListener,
		KeyListener {

	private static int DEFAULT_PREFERRED_HEIGHT = 100;
	private ListPopup popup;
	private int preferredHeight = DEFAULT_PREFERRED_HEIGHT;
	private boolean zoro = true;// 为空时是否也开启补全
	/**
	 * 比较在DefaultCompletionFilter类
	 */
	private CompletionFilter filter;

	public void setFilter(CompletionFilter f) {
		filter = f;
	}

	public AutoCompletionField(String text, int columns) {
		super(text, columns);
		init();
	}

	public AutoCompletionField() {
		super();
		init();
	}

	public void init() {
		popup = new ListPopup();
		getDocument().addDocumentListener(this);
		addMouseListener(this);
		popup.addListSelectionListener(this);
		addActionListener(this);
		addKeyListener(this);
	}

	public void setPopupPreferredHeight(int h) {
		preferredHeight = h;
	}

	private boolean isListChange(ArrayList array) {
		if (array.size() != popup.getItemCount()) {
			return true;
		}
		for (int i = 0; i < array.size(); i++) {
			if (!array.get(i).equals(popup.getItem(i))) {
				return true;
			}
		}
		return false;
	}

	private void textChanged() {
		if (!zoro) {
			if ("".equals(this.getText())) {
				popup.setVisible(false);
				return;
			}

		}

		if (!popup.isVisible()) {
			showPopup();
			requestFocus();
		}
		if (filter != null) {
			ArrayList array = filter.filter(getText());
			changeList(array);
		}

	}

	private void showPopup() {
		popup.setPopupSize(getWidth(), preferredHeight);
		popup.show(this, 0, getHeight() - 1);
	}

	private void changeList(ArrayList array) {
		if (array.size() == 0) {
			if (popup.isVisible()) {
				popup.setVisible(false);
			}
		} else {
			if (!popup.isVisible()) {
				showPopup();
			}
		}
		if (isListChange(array) && array.size() != 0) {
			popup.setList(array);
		}
	}

	public void insertUpdate(DocumentEvent e) {
		textChanged();
	}

	public void removeUpdate(DocumentEvent e) {
		textChanged();
	}

	public void changedUpdate(DocumentEvent e) {
		textChanged();
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() > 1 && !popup.isVisible())
			textChanged();
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void valueChanged(ListSelectionEvent e) {
		JList list = (JList) e.getSource();
		String text = list.getSelectedValue().toString();
		setText(text);
		popup.setVisible(false);
	}

	public void actionPerformed(ActionEvent e) {
		System.out.println("actionPerformed");
		if (popup.isVisible()) {
			Object o = popup.getSelectedValue();
			if (o != null)
				setText(o.toString());
			popup.setVisible(false);
		}
		this.selectAll();
		this.requestFocus();
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			System.out.println("VK_DOWN");
			if (popup.isVisible()) {
				if (!popup.isSelected())
					popup.setSelectedIndex(0);
				else
					popup.setSelectedIndex(popup.getSelectedIndex() + 1);
			}
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			System.out.println("VK_UP");
			if (popup.isVisible()) {
				if (!popup.isSelected())
					popup.setLastOneSelected();
				else
					popup.setSelectedIndex(popup.getSelectedIndex() - 1);
			}
		} else if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
			System.out.println("VK_PAGE_DOWN");
			if (popup.isVisible()) {
				if (!popup.isSelected())
					popup.setSelectedIndex(0);
				else
					popup.setSelectedIndex(popup.getSelectedIndex() + 5);
			}
		} else if (e.getKeyCode() == KeyEvent.VK_PAGE_UP) {
			System.out.println("VK_PAGE_UP");
			if (popup.isVisible()) {
				if (!popup.isSelected())
					popup.setLastOneSelected();
				else
					popup.setSelectedIndex(popup.getSelectedIndex() - 5);
			}
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	public static void main(String ages[]) {
		Vector tempvector = new Vector();
		tempvector.add("asus");
		tempvector.add("fedora");
		tempvector.add("linux");
		tempvector.add("linuxa");
		AutoCompletionField txt = new AutoCompletionField();
		txt.setFilter(new DefaultCompletionFilter(tempvector,","));
		// TODO Auto-generated method stub
		JFrame frame = new JFrame("补全");
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel,BorderLayout.CENTER);
		frame.getContentPane().add(new JTextField("sd"),BorderLayout.NORTH);

		txt.setColumns(20);

		panel.add(txt);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		txt.setText("l");
	}
}


