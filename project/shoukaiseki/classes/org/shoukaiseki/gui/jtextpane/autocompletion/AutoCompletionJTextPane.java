package org.shoukaiseki.gui.jtextpane.autocompletion;

/**
 * Java实现自动补全提示的文本框
 * http://pufan53.iteye.com/blog/755079
 * 自动补全的JTextField
 */
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.Keymap;
import javax.swing.text.PlainDocument;
import javax.swing.text.Position;
import javax.swing.text.StyledDocument;

import org.shoukaiseki.gui.jtextpane.JTextPaneOperating;

public class AutoCompletionJTextPane extends JTextPane implements
		DocumentListener, MouseListener, ListSelectionListener, ActionListener,
		KeyListener, ChangeListener {
	
	TextDocument td=new TextDocument();
	private static int DEFAULT_PREFERRED_HEIGHT = 100;
	private ListPopupJTextPane popup;
	private Dimension  popupSize=new Dimension(300,200);
	private int preferredHeight = DEFAULT_PREFERRED_HEIGHT;
	private boolean zoro = true;// 为空时是否也开启补全
	Rectangle rec;
	private int position=0;//键盘上下移动前的位置
	/**
	 * 比较在DefaultCompletionFilter类
	 */
	private CompletionJTextPaneFilter filter;

	public void setFilter(CompletionJTextPaneFilter f) {
		filter = f;
	}


	public AutoCompletionJTextPane() {
		super();
		init();
	}

	public void init() {
		popup = new ListPopupJTextPane();
		addMouseListener(this);
		popup.addListSelectionListener(this);
		addKeyListener(this);
		setDocument(td);
		td.addDocumentListener(this);
		getCaret().addChangeListener(this);
		popup.setSize(popupSize);
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
	protected  void	fireCaretUpdate(CaretEvent e) {
		System.out.println("CaretEvent");
		
//		super.fireCaretUpdate(e);
		textChanged();
	}


	
	
	public Caret getCaret() {
		if(position>0){
			if(super.getCaret()!=null){
				System.out.println("this.getCaret()="+super.getCaret().getDot());
				System.out.println("position="+position);
				super.getCaret().setDot(position);
				
			}
			
		}
		return super.getCaret();
		 
	 }
	private void textChanged() {
		int i=getCaretPosition();
		if(getCaret()!=null){
			i=getCaret().getDot();
		}
		System.out.println("i = "+i);
		String str=getText();
		System.out.println("str = "+str);
		if(str.length()>=i+1){
			str=str.substring(0,i);
		}
		if(i==0){
			str="";
		}
		System.out.println("str2 = "+str);
		if (!zoro) {
			if ("".equals(str)) {
				popup.setVisible(false);
				return;
			}
		}

		if (!popup.isVisible()) {
			showPopup();
			requestFocus();
		}
		if (filter != null) {
			ArrayList array = filter.filter(str);
			changeList(array);
		}

	}
	@Override
	public void insertUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		textChanged();
	}
	private void textChanged(int offset) {
		String text = (String) popup.getSelectedValue();
		String indexString=filter.getIndexString();
		int i=getCaretPosition();
		System.out.println("text = "+text);
		if(indexString!=null){
			if(indexString.length()>0){
				text=text.substring(indexString.length(),text.length());
				System.out.println("text2 = "+text);
			}
			
		}
		try {
			td.insertString(i, text, null);
			
			
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		popup.setVisible(false);

	}

	/**
	 * 获取JTextArea,JTextPane当前光标所在坐标
	 */
	private void showPopup() {
//		popup.setPopupSize(getWidth(), preferredHeight);
		try {
//			System.out.println(getText().length()+"="+getCaretPosition());
//			if(getCaretPosition()<getText().length())
//				rec = modelToView(getCaretPosition());
			if(getCaret()!=null){
				System.out.println(getText().length()+"="+getCaret().getDot());
				if(getCaret().getDot()<getText().length())
					rec = modelToView(getCaret().getDot());
			}
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		popup.show(this, rec.x, rec.y+rec.height);
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


	
	class TextDocument  extends DefaultStyledDocument{
		  public void insertString(int offset, String s, AttributeSet attrSet)throws BadLocationException {

				 System.out.println("insertString");
			  if(s.equals("\n" )){
				 if(popup.isSelected()){
					 textChanged(offset);
					 return ;
				 }
			  }
			   super.insertString(offset,s,attrSet);
		  }
		  public Position createPosition(int offs)
          throws BadLocationException{
			  System.out.println("createPosition");
			return super.createPosition(offs);
		  }
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


	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		System.out.println("stateChanged");
		textChanged();
	}

	public void valueChanged(ListSelectionEvent e) {
		JList list = (JList) e.getSource();
		String text = list.getSelectedValue().toString();
		String indexString=filter.getIndexString();
		int i=getCaretPosition();
		System.out.println("text = "+text);
		if(indexString!=null){
			if(indexString.length()>0){
				text=text.substring(indexString.length(),text.length());
				System.out.println("text2 = "+text);
			}
			
		}
		try {
			td.insertString(i, text, null);
			
			
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//		setText(text);
		popup.setVisible(false);
		
	}

	public void actionPerformed(ActionEvent e) {
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
				if(super.getCaret()!=null){
//					System.out.println("this.getCaret()="+super.getCaret().getDot());
					position=super.getCaret().getDot();
				}
			}
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			System.out.println("VK_UP");
			if (popup.isVisible()) {
				if (!popup.isSelected())
					popup.setLastOneSelected();
				else
					popup.setSelectedIndex(popup.getSelectedIndex() - 1);
				if(super.getCaret()!=null){
//					System.out.println("this.getCaret()="+super.getCaret().getDot());
					position=super.getCaret().getDot();
				}
			}
		} else if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
			System.out.println("VK_PAGE_DOWN");
			if (popup.isVisible()) {
				if (!popup.isSelected())
					popup.setSelectedIndex(0);
				else
					popup.setSelectedIndex(popup.getSelectedIndex() + 5);
				if(super.getCaret()!=null){
//					System.out.println("this.getCaret()="+super.getCaret().getDot());
					position=super.getCaret().getDot();
				}
			}
		} else if (e.getKeyCode() == KeyEvent.VK_PAGE_UP) {
			System.out.println("VK_PAGE_UP");
			if (popup.isVisible()) {
				if (!popup.isSelected())
					popup.setLastOneSelected();
				else
					popup.setSelectedIndex(popup.getSelectedIndex() - 5);
				if(super.getCaret()!=null){
//					System.out.println("this.getCaret()="+super.getCaret().getDot());
					position=super.getCaret().getDot();
				}
			}
		}else{
			position=0;
		}
	}

	public void keyReleased(KeyEvent e) {
		
	}

	public static void main(String ages[]) throws BadLocationException {
		Vector tempvector = new Vector();
		tempvector.add("asus");
		tempvector.add("fedora");
		tempvector.add("linux"); 
		tempvector.add("linuxa");
		tempvector.add("linuxb"); 
		tempvector.add("linuxc");
		tempvector.add("linuxd");
		tempvector.add("linuxe");
		AutoCompletionJTextPane txt = new AutoCompletionJTextPane();
		txt.setFilter(new DefaultCompletionJTextPaneFileter(tempvector,","));
		// TODO Auto-generated method stub
		JFrame frame = new JFrame("补全");
		JPanel panel = new JPanel(); 
		frame.getContentPane().add(setJTextArea(txt,"我"));


//		panel.add();
		frame.pack();
		frame.setSize(700,400);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		txt.setText("li");
		 Action[] act=	txt.getActions(); 
		 for (Action action : act) {
			System.out.println("====="+action );
		}
		StyledDocument sd=txt.getStyledDocument();
		try {
			txt.setCaretPosition(sd.getLength());
			System.out.println(txt.getCaretPosition());
			sd.remove(txt.getCaretPosition()-1, txt.getCaretPosition()-1);
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
	public static JPanel setJTextArea(AutoCompletionJTextPane jta,String label){
		jta.setLayout(null);
		JScrollPane jsp = new JScrollPane(jta);// 新建一个滚动条界面，将文本框传入
    	jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JPanel jp=new JPanel();
		BorderLayout bl=new BorderLayout();
		jp.setLayout(bl); 
		JLabel jl=new JLabel(label+":",JLabel.LEADING);
		jl.setOpaque(true);
		JPanel jp2=new JPanel();
		jp2.add(jl);
		jp.add(jp2,BorderLayout.WEST);
		jp.add(jsp,BorderLayout.CENTER);
		return jp;
	}






}


