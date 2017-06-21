package org.shoukaiseki.gui.owern.swing.jframe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea; 
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.shoukaiseki.gui.jbutton.JButtonIcon;
import org.shoukaiseki.gui.jbutton.TurubaGroup;
import org.shoukaiseki.gui.jlist.JListIcon;
import org.shoukaiseki.gui.owern.swing.ijframe.Jnf;
import org.shoukaiseki.gui.owern.swing.risuna.ApurikeshonnShare;
import org.shoukaiseki.gui.owern.swing.risuna.MusukoApurikeshonnShare;
import org.shoukaiseki.gui.tabbedpane.ClosableTabbedPane;
import org.shoukaiseki.gui.tabbedpane.ClosableTabbedPaneRisuna;
import org.shoukaiseki.gui.tuuyou.JLabelComponentAt;
import org.shoukaiseki.gui.tuuyou.StringIcon;
import org.tomaximo.domain.AddToMaxDomain;
import org.tomaximo.purosesu.ReadPurosesuName;

public class MusukoOwnerJFrame extends OwnerJFrame
implements ClosableTabbedPaneRisuna{
	public int heightJpKita=500;//存放JSplitpane的当前位置
	public int widthJpNisi=400;//存放JSplitpane的当前位置
	public int fullHeightJpKita=500;//存放JSplitpane的全屏以前的位置
	public int fullWidthJpNisi=400;//存放JSplitpane的全屏前的位置
	/**
	 * 添加到哪个组件
	 */
	public static final long JTP_KITA = 1L;
	public static final long JTP_MINAMI = 2L;
	public static final long JTP_NISI = 4L;
	public static final long JTP_NISIMUSUKO = 8L;

	private ClosableTabbedPane jtpKita = new ClosableTabbedPane(JTabbedPane.TOP);
    private ClosableTabbedPane jtpMinami = new ClosableTabbedPane(JTabbedPane.BOTTOM);
    private ClosableTabbedPane jtpNisi = new ClosableTabbedPane(JTabbedPane.LEFT);
	private ClosableTabbedPane jtpNisiMusuko = new ClosableTabbedPane(JTabbedPane.TOP);
	
	private boolean hyoujiFull=false;//jpKita显示全屏标识
	private JSplitPane 	jpKita;
	private JSplitPane 	jpMinami;
	private JSplitPane 	jpNisi;
	private JPanel jpanelTasukuba = new JPanel(new FlowLayout(FlowLayout.LEFT)); //jpKita的任务栏
	private TurubaGroup tgTuruba=new TurubaGroup();//顶部工具栏
	private JListIcon jlistApurikeshonn;
	JScrollPane jspJList;
	
	public MusukoOwnerJFrame(){
		super();
		reiautoMana();
		init();
		setJSplitPane();
	}
	
	public void test(){
//		addJnf(new JLabel("jtpKita"),JTP_KITA,"A");
		addJnf(new JLabel("jtpMinami"),JTP_MINAMI,"B");
		addJnf(new JLabel("jtpNisi"),JTP_NISI,"C");
		addJnf(new JLabel("jtpNisiMusuko"),JTP_NISIMUSUKO,"D");
		for (int i = 0; i < 12; i++) {

			jlistApurikeshonn.addElement("香蕉",null);
			jlistApurikeshonn.addElement("水果",null);
		}
		jlistApurikeshonn.addElement("香蕉",null);
		jlistApurikeshonn.addElement("水果",null);
		JButton btn = new JButton("X");
		JLabel lab = new JLabel("选项卡I"); 
		
		FlowLayout fl=new FlowLayout(FlowLayout.RIGHT);
		Box box11 = Box.createHorizontalBox(); // 横结构 
		ImageIcon closexIcon = new ImageIcon("./image/tabClose.gif");
		JPanel tab = new JPanel();
		JLabel tablabel = new JLabel("tab" + (1)+"");
		JButton tabCloseButton = new JButton(closexIcon);
//		JTextField tablabel=new JTextField("tab");
		tabCloseButton.setPreferredSize(new Dimension(12, 12));
//		tab.setOpaque(false);
//		box11.setLayout(fl);
		tab.setLayout(fl);


		box11.add(tablabel, BorderLayout.WEST);
		box11.add(tabCloseButton, BorderLayout.EAST);
//		tab.add(tabSetuzokuButton);
//		tab.add(tablabel);
//		tab.add(tabCloseButton);
//		jtpKita.addTab("sda",lab); 
//		System.out.println(System.getProperties() );
		initTasukuba();
	}
	/**
	 * 
	 */
	public void initTasukuba(){
		ImageIcon setuzokuIcon = new ImageIcon("./image/oicons/commit.png");
//		addTasukuba(setuzokuIcon);
		
		JInternalFrame jif=new JInternalFrame();
		jif.add(new JTextArea());
		jif.setVisible(true);
//		addJnf(jif,JTP_KITA, "sou");
		TurubaGroup tg1=new TurubaGroup();

		JSplitPane jsp;
		jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tg1,
				jif);
		jsp.setDividerSize(0);//隐藏分隔条
//		jsp.setEnabled(false);//禁用分隔条
		
		JPanel jp=new JPanel();
		BorderLayout bl=new BorderLayout();
		jp.setLayout(bl);
		 jp.add(jif,BorderLayout.CENTER);
		 jp.add(tg1,BorderLayout.NORTH);
		
		jtpKita.addTab(jp,"sadsdsd",null);
		
		
//		addJnf(jsp,JTP_KITA, "sou");

//		addJnfHaveTurubaGroup(jif, "sdf");
		
		
		TurubaGroup tg=addJnfHaveTurubaGroup(new ReadPurosesuName(), "是的").getTurubaGroup();
		
		tg.addJButton(setuzokuIcon);
	}
	
	/**
	 * 添加到JTP_KITA组件,拥有工具栏
	 * @param component	
	 * @param constraints	显示的名称
	 * @return	TurubaGroup
	 */
	public JLabelComponentAt addJnfHaveTurubaGroup(Component component,String constraints){
		return addJnfHaveTurubaGroup(component,constraints,null);
	}
	/**
	 * 添加到JTP_KITA组件,拥有工具栏
	 * @param component	
	 * @param constraints	显示的名称
	 * @return	TurubaGroup
	 */
	public JLabelComponentAt addJnfHaveTurubaGroup(Component component,String constraints,ImageIcon icon){
		TurubaGroup tg=new TurubaGroup();

		JPanel jp=new JPanel();
		BorderLayout bl=new BorderLayout();
		jp.setLayout(bl);
		jp.add(component,BorderLayout.CENTER);
		jp.add(tg,BorderLayout.NORTH);
		 
		
//		JSplitPane jsp;//分隔条禁用后编辑时鼠标无法进入编辑状态
//		jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tg,
//				component);
//		jsp.setDividerSize(0);//隐藏分隔条
//		jsp.setEnabled(false);//禁用分隔条
		
		
		JLabelComponentAt jca=jtpKita.addTab(jp,constraints,icon);
		jca.setTurubaGroup(tg);
		return jca;
	}
	
	
	/**
	 * 添加到jpKita的任务栏
	 * 
	 */
	public JButton addTasukuba(ImageIcon icon){
        return tgTuruba.addJButton(icon);
	}
	
	/**
	 * 初始化方法
	 */
	public void init(){
        jlistApurikeshonn = new JListIcon(); 
        jspJList=new JScrollPane(jlistApurikeshonn);
        addJnf(jtpNisiMusuko,JTP_NISI,"常用");
        addJnf(jspJList, JTP_NISI,"应用");
        Box boxS01 = Box.createVerticalBox(); // 竖结构

        boxS01.add(jpanelTasukuba);
//        boxS01.add(new JTextArea("撒旦飞洒"));
        boxS01.add(new ReadPurosesuName());
//        addJnf(boxS01, JTP_KITA,"空间");
        jtpKita.addTabAboutToClose(this);
        
        jpNisi.addPropertyChangeListener(new java.beans.PropertyChangeListener() {  
            public void propertyChange(java.beans.PropertyChangeEvent evt) {  
                if (evt.getPropertyName().equals(JSplitPane.DIVIDER_LOCATION_PROPERTY)) {  
                    //action code  

        			widthJpNisi=jpNisi.getDividerLocation();

                }  
            }  
        });  
        jpKita.addPropertyChangeListener(new java.beans.PropertyChangeListener() {  
            public void propertyChange(java.beans.PropertyChangeEvent evt) {  
                if (evt.getPropertyName().equals(JSplitPane.DIVIDER_LOCATION_PROPERTY)) {  
                    //action code  
                		heightJpKita=jpKita.getDividerLocation();
                }  
            }  
        });  
//        jlistApurikeshonn.addMouseListener(new MouseAdapter() {
//            public void mouseClicked(MouseEvent evt) {
//                JList list = (JList)evt.getSource();
//                if (evt.getClickCount() == 2) {          // Double-click
//                    // Get item index
//                	
//                    int i=jlistApurikeshonn.getAnchorSelectionIndex() ;
//                    int index = list.locationToIndex(evt.getPoint());
//                } else if (evt.getClickCount() == 3) {   // Triple-click
//                    // Get item index
//                    int index = list.locationToIndex(evt.getPoint());
//        
//                    // Note that this list will receive a double-click event before this triple-click event
//                }
//            }
//        });
	}
	

	/**
	 * 添加到应用
	 * @param obj
	 */
	public StringIcon addApurikeshonn(String name,ImageIcon icon){
		return jlistApurikeshonn.addElement(name,icon);
	}
	/**
	 * 获取应用列表
	 * @return 应用,用于增加监听器
	 */
	public JListIcon getJListIcon(){
		return jlistApurikeshonn;
	}
	
	public void addJnf(Component component,long l,String constraints){
		addJnf(component,l,constraints,null);
	}
	/**
	 * 添加到哪个组件
	 * @param component
	 * @param l				
	 * @param constraints	显示的名称
	 */
	public void addJnf(Component component,long l,String constraints,ImageIcon icon){
		if(l==JTP_KITA){
			jtpKita.addTab(component,constraints,icon);
		}else if(l==JTP_MINAMI){
			jtpMinami.addTab(component,constraints,icon);
			jtpMinami.setBackground(Color.red);
		}else if(l==JTP_NISI){
			jtpNisi.addTab(component,constraints,icon);
		}else if(l==JTP_NISIMUSUKO){
			jtpNisiMusuko.addTab(component,constraints,icon);
		}
	}
	/**
	 * 布局方式
	 */
	public void reiautoMana(){
		jtpNisi.setHyoujiIcon(false);
		jtpMinami.setHyoujiIcon(false);
		jpKita=new JSplitPane(JSplitPane.VERTICAL_SPLIT,
        		jtpKita,jtpMinami);
		
		jpNisi=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				jtpNisi,jpKita);
		jpKita.setDividerLocation(400);
		c.add(tgTuruba,BorderLayout.NORTH);
		c.add(jpNisi,BorderLayout.CENTER);
	}
	
	public static void main(String args[]){
		MusukoOwnerJFrame moj=new MusukoOwnerJFrame();
		moj.test(); 
		moj.setJSplitPane();
		moj.setVisible(true);
	}
	/**
	 * 发送关闭事件给拥监听器的组件
	 * @return 返回true为允许关闭
	 */
	public void frameClosing(){

		// TODO Auto-generated method stub

		int userSelect = JOptionPane.showConfirmDialog(this, "确认要退出吗？", "退出",
				JOptionPane.YES_NO_OPTION);
		if (userSelect == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}
	
	/**
	 * 主框架提供的监听:组件大小更改时调用。
	 * 
	 * @param e
	 */
	public void jframeComponentResized(ComponentEvent e){
		try {
			setJSplitPane();
		} catch (Exception e2) {
			// TODO: handle exception
		}
	}
	@Override
	public boolean tabAboutToClose(int tabIndex,MouseEvent me) {
		// TODO Auto-generated method stub

//		String tab = jtpKita.getTabTitleAt(tabIndex);
//		int choice = JOptionPane.showConfirmDialog(null,
//				"You are about to close '" + tab
//						+ "'\nDo you want to proceed ?",
//				"Confirmation Dialog",
//				JOptionPane.INFORMATION_MESSAGE);
//		return choice == 0; // if returned false tab closing will be
//		// canceled
		return true; 
	}

	@Override
	public void tabAboutToDoubleClicked(int tabIndex) {
		// TODO Auto-generated method stub
		if(hyoujiFull){
			jpKita.getRightComponent().setVisible(true);
			jpNisi.getLeftComponent().setVisible(true);

			jpKita.setDividerLocation(fullHeightJpKita);
			jpNisi.setDividerLocation(fullWidthJpNisi);
		}else{
			fullHeightJpKita=jpKita.getDividerLocation();
			fullWidthJpNisi=jpNisi.getDividerLocation();
			jpKita.getRightComponent().setVisible(false);
			jpNisi.getLeftComponent().setVisible(false);
		}
		hyoujiFull=!hyoujiFull;
	}

	@Override
	public void tabAboutToMouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 返回jtpKita组件,用于增加监听器
	 * @return
	 */
	public ClosableTabbedPane getJtpKita(){
		return jtpKita;
	}
	/**
	 * 返回顶部工具栏
	 * @return
	 */
	public TurubaGroup getJGTuruba(){
		return tgTuruba;
	}
	
	public void setJSplitPane (){
		int hei=jpKita.getHeight();
		int wid=jpNisi.getWidth();
//		System.out.println(heightJpKita);
//		System.out.println(widthJpNisi);
		jpKita.setDividerLocation(heightJpKita);
		jpNisi.setDividerLocation(widthJpNisi);
//		System.out.println(jpKita.getHeight());
//		System.out.println(jpKita.getWidth());
//		System.out.println(heightJpKita);
//		System.out.println(widthJpNisi);
	}
	
	/**
	 * 刷新方法
	 */
	public void rifuressyu(){
		int status= getExtendedState();
		tgTuruba.rifuressyu();
//		setVisible(false);
		if(status!=ICONIFIED){
			if(status==MAXIMIZED_BOTH ){
				setExtendedState(0);
				for (int i = 0; i < 899999; i++) {
					for (int j = 0; j < 33; j++) {
						
					}
				}
				setExtendedState(JFrame.MAXIMIZED_BOTH);
			}else if(status==0 ){
				setExtendedState(JFrame.MAXIMIZED_BOTH);
				for (int i = 0; i < 899999; i++) {
					for (int j = 0; j < 33; j++) {
						
					}
				}
				setExtendedState(0);
			}
		}
//		setVisible(true);
	}
}



