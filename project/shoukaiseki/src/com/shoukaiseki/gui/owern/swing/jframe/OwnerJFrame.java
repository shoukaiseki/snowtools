package com.shoukaiseki.gui.owern.swing.jframe;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator; 
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.shoukaiseki.classes.KannriClasses;
import com.shoukaiseki.gui.jtextpane.JTextPaneDoc;
import com.shoukaiseki.gui.owern.swing.ijframe.Jnf;
import com.shoukaiseki.gui.owern.swing.risuna.ApurikeshonnShare;



/**
 * 提供底层各种监听器
 * @author shoukaiseki 
 *
 */
public class OwnerJFrame extends JFrame {

	static final long COMPONENT_HIDDEN = 1L;
	static final long COMPONENT_MOVED = 2L;
	static final long COMPONENT_RESIZED = 4L;
	static final long COMPONENT_SHOWN = 8L;
	static final long WINDOW_CLOSING = 16L;
	static final int JFRAME_WINDOWS_NOT_CLOSE=1;
	static final int JFRAME_WINDOWS_CLOSE=0;
	private boolean konnsoru=false;//是否允许输出至控制台
	private JTextPaneDoc textPane;//输出至信息框的
	private OwnerJFrame frame = this;
	Container c;

	private Map<String,JFrameWindowRisuna> windowRisunaMap = new HashMap();
	private Map<String,JFrameWindowClose> windowCloseMap = new HashMap();
	protected  Map<String, ApurikeshonnShare> asMap = new HashMap();
	private int wrKey =0;
	private int wcKey =0;
	private int jnfKey = 0;
	 
	/**
	 * class管理器
	 */
	private KannriClasses kc = new KannriClasses();

	/**
	 * 监听
	 */
	// private WindowAdapter wa;

	public OwnerJFrame() {
		super();
		textPane=new JTextPaneDoc();
		c=this.getContentPane();
		kannsiHirakui();
		this.setDefaultCloseOperation(this.DO_NOTHING_ON_CLOSE);
		this.setSize(400, 400);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public static void main(String args[]) {
		OwnerJFrame oframe = new OwnerJFrame();
		oframe.setDefaultCloseOperation(oframe.EXIT_ON_CLOSE);
		oframe.loadClass("com.shoukaiseki.gui.owern.swing.ijframe.Jnf");
//		oframe.loadClass("bennkyou.classes.AddNoClass");
	}
	/**
	 * 增加window关闭事件监视器
	 * @param wc
	 */
	public void addwindwoCloseMap(JFrameWindowClose wc){
		if (wc instanceof JFrameWindowClose) {
			windowCloseMap.put(++wcKey+"",wc);
		} 
	} 
	/**
	 * 增加window各种监视器
	 * @param wc
	 */
	public void addWindowRisuna(JFrameWindowRisuna wr){
		if (wr instanceof JFrameWindowRisuna) {
			windowRisunaMap.put(++wrKey+"",wr);
		} 
	}
	
	public void loadClass(Class className) {
		loadClass(className.getName());
		printlnConsole(className.getName());
	}
	public void loadClass(String className) {

		try {
//			System.out.println(className);
			Class clss = kc.loadClass(className);

			// 创建Object类型的类实例。
			Object object = clss.newInstance();
			/**
			 * 该对象是ApurikeshonnShare类实例或拥有该接口
			 */
			if (object instanceof ApurikeshonnShare) {
				ApurikeshonnShare jnf = (ApurikeshonnShare) object;
				asMap.put(++jnfKey+"", jnf);
				jnf.setOwnerJFrame(this);
				jnf.init();
				printlnConsole("成功加载ApurikeshonnShare类");
			}else{
				printlnConsole("加载失败,该类没有 ApurikeshonnShare接口");
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//	public void loadClass(ApurikeshonnShare jnf) {
//			asMap.put(++jnfKey+"", jnf);
//			jnf.setOwnerJFrame(this);
//			jnf.init();
//	}	

	public void init() {

	}

	public void kannsiHirakui() {
		this.addComponentListener(new ComponentListener() {

			/**
			 * 主框架各种监听
			 */
			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
//				System.out.println("componentHidden");
				windowRisuna(e, COMPONENT_HIDDEN);
				jframeComponentHidden(e);
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
//				System.out.println("componentMoved");
				windowRisuna(e, COMPONENT_MOVED);
				jframeComponentMoved(e);
			}

			@Override
			public void componentResized(ComponentEvent e) {
				// TODO Auto-generated method stub
//				System.out.println("componentResized");
				windowRisuna(e, COMPONENT_RESIZED);
				jframeComponentResized(e);

			}

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
//				System.out.println("componentShown");
				windowRisuna(e, COMPONENT_SHOWN);
				jframeComponentShown(e);

			}
		});

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
//				System.out.println("windowClosing");
				if(windowColose(e))
					frameClosing();
			}
		});
	}

	public void frameClosing() {
		
	}

	/**
	 * 遍历类,发出窗口监听的
	 * 
	 * @param e
	 * @param type
	 */
	public int windowRisuna(ComponentEvent e, long type) {
		Set<String> key = windowRisunaMap.keySet();
        Iterator it = key.iterator();
		int i = 0;
		for (String string : key) {
			JFrameWindowRisuna as = (JFrameWindowRisuna)asMap.get(string);
			if ((type & this.COMPONENT_HIDDEN) > 0) {
				as.componentHidden(e);
			} else if ((type & this.COMPONENT_MOVED) > 0) {
				as.componentMoved(e);
			} else if ((type & this.COMPONENT_RESIZED) > 0) {
				as.componentResized(e);
			} else if ((type & this.COMPONENT_SHOWN) > 0) {
				as.componentShown(e);
			}
		}
		return 0;
	}
	/**
	 * 主框架提供的监听:组件变得不可见时调用
	 * 
	 * @param e
	 */
	public void jframeComponentHidden(ComponentEvent e){
		
	}

	/**
	 * 主框架提供的监听:组件位置更改时调用。
	 * 
	 * @param e
	 */
	public void jframeComponentMoved(ComponentEvent e){
		
	}
	/**
	 * 主框架提供的监听:组件大小更改时调用。
	 * 
	 * @param e
	 */
	public void jframeComponentResized(ComponentEvent e){
		
	}
	/**
	 * 主框架提供的监听:组件变得可见时调用。
	 * 
	 * @param e
	 */
	public void jframeComponentShown(ComponentEvent e){
		
	}
	/**
	 * 发送关闭事件给拥监听器的组件
	 * @return 返回true为允许关闭
	 */
	public boolean windowColose(WindowEvent e){
//		System.out.println("有一个不允许关闭就返回取消命令");
		Set<String> key = windowCloseMap.keySet();
        Iterator it = key.iterator();
        for (String string : key) {
    		JFrameWindowClose as = (JFrameWindowClose)asMap.get(string);
    		if(!as.windowClosing(e)){ 
    			return false;//有一个不允许关闭就返回取消命令
    		}
		}
        return true;
	}



	/**
	 * 自定义SimpleAttributeSet输出
	 * @param args0
	 * @param color
	 */
	public void addLastLine(String args0,SimpleAttributeSet sas){
		textPane.addLastLine(args0,sas);
	}
	/**
	 * 自定义颜色输出
	 * @param args0
	 * @param color
	 */
	public void addLastLine(String args0,Color color){
		SimpleAttributeSet sas=new SimpleAttributeSet();
		StyleConstants.setForeground(sas, color);//字体颜色
		textPane.addLastLine(args0,sas);
	}

	/**
	 * 在content末尾加入一行内容age0,如果age0内容为空时则直接取消在String concat前的LineBreaks
	 */
	public void println(String konntenntu) {
		println(konntenntu, false);
	}
	/**
	 * 在content末尾加入一行内容age0,如果age0内容为空时则直接取消在String concat前的LineBreaks
	 * b true红色字体
	 */
	public void println(String konntenntu,boolean b) {
		try {
			textPane.addLastLine(konntenntu, b);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 输出至控制台的信息,可开启关闭
	 * @param i
	 */
	public void printlnConsole(int i){
		if(konnsoru){
			System.out.println(""+i);
		}
	}
	/**
	 * 输出至控制台的信息,可开启关闭
	 * @param i
	 */
	public void printlnConsole(String s){
		if(konnsoru){
			System.out.println(s);
		}
	}
	/**
	 * 是否允许输出至控制台
	 * @param konnsoru
	 */
	public void setKonnsoru(boolean konnsoru){
		this.konnsoru=konnsoru;
	}
	/**
	 * 返回允许输出控制台状态
	 * @return
	 */
	public boolean getKonnsoru(){
		return this.konnsoru;
	}
	/**
	 * 信息输出窗口
	 * @param textPane
	 */
	public void setTextPane(JTextPaneDoc textPane){
		this.textPane=textPane;
	}
	/**
	 * 信息输出窗口
	 * @return
	 */
	public JTextPaneDoc getTextPane(){
		return this.textPane;
	}
}

