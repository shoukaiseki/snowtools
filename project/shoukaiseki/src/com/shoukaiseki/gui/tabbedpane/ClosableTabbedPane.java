package com.shoukaiseki.gui.tabbedpane;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


import com.shoukaiseki.gui.owern.swing.jframe.JFrameWindowClose;
import com.shoukaiseki.gui.owern.swing.jframe.JFrameWindowRisuna;
import com.shoukaiseki.gui.owern.swing.jframe.OwnerJFrame;
import com.shoukaiseki.gui.owern.swing.risuna.ApurikeshonnShare;
import com.shoukaiseki.gui.tuuyou.JLabelComponentAt;
import com.shoukaiseki.gui.tuuyou.StringIcon;
import com.tomaximo.puroguramu.tuuyou.IconSigenn;

public class ClosableTabbedPane extends JTabbedPane {
	private boolean hyoujiIcon=true;//是否显示关闭图标
	private Map<String, ClosableTabbedPaneRisuna> ctprMao = new HashMap();
	private int ctprKey = 0;
	private TabCloseUI closeUI = new TabCloseUI(this);

	/**
	 *  创建一个具有默认的 JTabbedPane.TOP 选项卡布局的空 TabbedPane。
	 */
	public ClosableTabbedPane(){
		super();
	}
		
	/**
	 * 创建一个空的 TabbedPane，使其具有以下指定选项卡布局中的一种：
	 * JTabbedPane.TOP、JTabbedPane.BOTTOM、JTabbedPane.LEFT 或 JTabbedPane.RIGHT。
	 * @param tabPlacement
	 */
	public ClosableTabbedPane(int tabPlacement) {
		// TODO Auto-generated constructor stub
		super(tabPlacement);
	}

	/**
	 * 创建一个空的 TabbedPane，使其具有指定的选项卡布局和选项卡布局策略
	 * @param tabPlacement
	 * @param tabLayoutPolicy
	 */
	public ClosableTabbedPane(int tabPlacement, int tabLayoutPolicy){
		super(tabPlacement,tabLayoutPolicy);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		if(hyoujiIcon){
			closeUI.paint(g);
		}
	}

	public JLabelComponentAt addTab( Component component,String title, ImageIcon icon){
		JLabelComponentAt jca=null;
		String titleName=title+(hyoujiIcon?"  ":"");
		if (icon==null) {
			jca=new JLabelComponentAt(titleName);
		}else{
			jca=new JLabelComponentAt(titleName,icon);
		}
		super.addTab(titleName,component);
//		System.out.println(getTabCount()-1);
		setTabComponentAt(getTabCount()-1 , jca);
		return jca;
	}
	
	/**
	 * 返回唯一索引标识
	 * @param component
	 * @param title
	 * @return
	 */
	public JLabelComponentAt addTab(Component component,String title) {
		return addTab(component,title,null);
	}

	public String getTabTitleAt(int index) {
		return super.getTitleAt(index).trim();
	}

	private class TabCloseUI extends MouseAdapter {
		private ClosableTabbedPane tabbedPane;
		private int closeX = 0, closeY = 0, meX = 0, meY = 0;
		private int selectedTab;
		private final int width = 8, height = 8;
		private Rectangle rectangle = new Rectangle(0, 0, width, height);

		private TabCloseUI() {
		}

		public TabCloseUI(ClosableTabbedPane pane) {

			tabbedPane = pane;
			tabbedPane.addMouseMotionListener(this);
			tabbedPane.addMouseListener(this);
		}

		public void mouseDragged(MouseEvent e){
//			System.out.println("撒反对");
		}
		
		public void mouseClicked(MouseEvent e){ 
			  if   (tabbedPane.indexAtLocation(e.getX(),   e.getY())   ==   -1){
			  }else if(e.getClickCount() == 2){
				  tabAboutToDoubleClicked(tabbedPane.getSelectedIndex());
//					System.out.println(tabbedPane.getSelectedIndex());
			  }
			  tabAboutToMouseClicked(e);
		} 
		/**
		 * 触发关闭事件
		 */
		public void mouseReleased(MouseEvent me) {

			if(hyoujiIcon){
			if (closeUnderMouse(me.getX(), me.getY())) {
				boolean isToCloseTab = tabAboutToClose(selectedTab,me);
				if (isToCloseTab && selectedTab > -1) {
					tabbedPane.removeTabAt(selectedTab);
				}
				selectedTab = tabbedPane.getSelectedIndex();
			}
			}
		}

		public void mouseMoved(MouseEvent me) {
			meX = me.getX();
			meY = me.getY();
			if(hyoujiIcon){
				if (mouseOverTab(meX, meY)) {
					controlCursor();
					tabbedPane.repaint();
				}
			}
		}

		private void controlCursor() {
			if (tabbedPane.getTabCount() > 0)
				if (closeUnderMouse(meX, meY)) {
					tabbedPane.setCursor(new Cursor(Cursor.HAND_CURSOR));
					if (selectedTab > -1)
						tabbedPane.setToolTipTextAt(selectedTab, "关闭  "
								+ tabbedPane.getTitleAt(selectedTab));
				} else {
					tabbedPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					if (selectedTab > -1)
						tabbedPane.setToolTipTextAt(selectedTab, "");
				}
		}

		private boolean closeUnderMouse(int x, int y) {
			rectangle.x = closeX;
			rectangle.y = closeY;
			return rectangle.contains(x, y);
		}

		public void paint(Graphics g) {

			int tabCount = tabbedPane.getTabCount();
			for (int j = 0; j < tabCount; j++)
				if (tabbedPane.getComponent(j).isShowing()) {
					int x = tabbedPane.getBoundsAt(j).x
							+ tabbedPane.getBoundsAt(j).width - width - 5;
					int y = tabbedPane.getBoundsAt(j).y + 5;
					drawClose(g, x, y);
					break;
				}
			if (mouseOverTab(meX, meY)) {
				drawClose(g, closeX, closeY);
			}
		}

		private void drawClose(Graphics g, int x, int y) {
			if (tabbedPane != null && tabbedPane.getTabCount() > 0) {
				Graphics2D g2 = (Graphics2D) g;
				drawColored(g2, isUnderMouse(x, y) ? Color.RED : Color.WHITE,
						x, y);
			}
		}

		private void drawColored(Graphics2D g2, Color color, int x, int y) {
			g2.setStroke(new BasicStroke(5, BasicStroke.JOIN_ROUND,
					BasicStroke.CAP_ROUND));
			g2.setColor(Color.BLACK);
			g2.drawLine(x, y, x + width, y + height);
			g2.drawLine(x + width, y, x, y + height);
			g2.setColor(color);
			g2.setStroke(new BasicStroke(3, BasicStroke.JOIN_ROUND,
					BasicStroke.CAP_ROUND));
			g2.drawLine(x, y, x + width, y + height);
			g2.drawLine(x + width, y, x, y + height);

		}

		private boolean isUnderMouse(int x, int y) {
			if (Math.abs(x - meX) < width && Math.abs(y - meY) < height)
				return true;
			return false;
		}

		private boolean mouseOverTab(int x, int y) {
			int tabCount = tabbedPane.getTabCount();
			for (int j = 0; j < tabCount; j++)
				if (tabbedPane.getBoundsAt(j).contains(meX, meY)) {
					selectedTab = j;
					closeX = tabbedPane.getBoundsAt(j).x
							+ tabbedPane.getBoundsAt(j).width - width - 5;
					closeY = tabbedPane.getBoundsAt(j).y + 5;
					return true;
				}
			return false;
		}
	}

	/**
	 * 鼠标双击触发事件
	 * @param e
	 */
	public void tabAboutToDoubleClicked(int i){
//		System.out.println("有一个不允许关闭就返回取消命令");
		Set<String> key = ctprMao.keySet();
        Iterator it = key.iterator();
        for (String string : key) {
        	ClosableTabbedPaneRisuna ctpr = (ClosableTabbedPaneRisuna)ctprMao.get(string);
    		ctpr.tabAboutToDoubleClicked(i);
		}
	}
	
	/**
	 * 鼠标按键在组件上单击（按下并释放）时调用。
	 * @param e
	 */
	public void tabAboutToMouseClicked(MouseEvent e){
		Set<String> key = ctprMao.keySet();
        Iterator it = key.iterator();
        for (String string : key) {
        	ClosableTabbedPaneRisuna ctpr = (ClosableTabbedPaneRisuna)ctprMao.get(string);
    		ctpr.tabAboutToMouseClicked(e);
		}
	}

	/**
	 * 增加ClosableTabbedPaneRisuna监听器
	 * 
	 * @param wc
	 */
	public void addTabAboutToClose(ClosableTabbedPaneRisuna ctpr) {

		if (ctpr instanceof ClosableTabbedPaneRisuna) {
			ctprMao.put(++ctprKey + "", ctpr);
		}
	}

	/**
	 * TabbedPane关闭监听器
	 */
	public boolean tabAboutToClose(int tabIndex,MouseEvent me){
//		System.out.println("有一个不允许关闭就返回取消命令");
		Set<String> key = ctprMao.keySet();
        Iterator it = key.iterator();
        for (String string : key) {
        	ClosableTabbedPaneRisuna ctpr = (ClosableTabbedPaneRisuna)ctprMao.get(string);
    		if(!ctpr.tabAboutToClose(tabIndex,me)){ 
    			return false;//有一个不允许关闭就返回取消命令
    		}
		}
        return true;
	}

	/**
	 * 设置是否显示图标
	 * @param hyoujiIcon
	 */
	public void setHyoujiIcon(boolean hyoujiIcon){
		this.hyoujiIcon=hyoujiIcon;
	}
	/**
	 * 设返回是否显示图标状态
	 */
	public boolean getJyoujiIcon(){
		return this.hyoujiIcon;
	}
	
	
	
	
	public static void main(String args[]){

		OwnerJFrame oj = new OwnerJFrame();
		final ClosableTabbedPane tp=new ClosableTabbedPane();

		ImageIcon setuzokuIcon = new ImageIcon("./image/oicons/commit.png");
		 final JTextArea jt=new JTextArea();
		 final JTextField jtf=new JTextField();
		 final JLabelComponentAt jtfid=tp.addTab(jtf, "是的",IconSigenn.DATABASE);
		 final JLabelComponentAt jtid=tp.addTab(jt,"山东省的", setuzokuIcon );
		oj.getContentPane().add(tp); 
		oj.setDefaultCloseOperation(oj.EXIT_ON_CLOSE);
		
        // Create menu for adding tabs
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem menuItem = new JMenuItem("Add Tab");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	tp.addTab("TAB " + (tp.getTabCount() + 1),
                        new JPanel());
            }
        });
        menu.add(menuItem);
        menuBar.add(menu);
        oj. setJMenuBar(menuBar);
        
        
        tp.addTabAboutToClose(new ClosableTabbedPaneRisuna(){

			@Override
			public boolean tabAboutToClose(int tabIndex, MouseEvent me) {
				// TODO Auto-generated method stub
				ClosableTabbedPane ctp=((ClosableTabbedPane)me.getSource());
				Object ob=ctp;
				 ob=ctp.getTabComponentAt(ctp.getSelectedIndex()) ;
					
				if (ob.equals(jtid)) {
					System.out.println("jtid关闭はJTextArea");
				}else if (ob.equals(jtfid)){
					System.out.println("jtfid关闭はJTextField");
				}if (ob.equals(jt)) {
					System.out.println("jt关闭はJTextArea");
				}if (ob.equals(jtf)) {
					System.out.println("jtf关闭はJTextField");
				}
//				System.out.println(((JLabelComponentAt)ob).getID());
				return true;
			}

			@Override
			public void tabAboutToDoubleClicked(int tabIndex) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void tabAboutToMouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

		        System.out.println(tp.getSelectedComponent());
		        
			}
        	
        });
        System.out.println(tp.indexOfTabComponent((Component)jtid));
        System.out.println(tp.getSelectedComponent());

		oj.setVisible(true);
		
	}
	
	
	
	
	
}


