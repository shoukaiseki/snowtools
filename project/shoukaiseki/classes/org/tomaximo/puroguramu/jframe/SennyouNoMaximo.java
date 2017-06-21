package org.tomaximo.puroguramu.jframe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.BadLocationException;


import org.shoukaiseki.classes.KannriClasses;
import org.shoukaiseki.gui.jbutton.TurubaGroup;
import org.shoukaiseki.gui.jcombobox.JComboBoxIcon;
import org.shoukaiseki.gui.jlist.JListIcon;
import org.shoukaiseki.gui.jtable.JTableOperating;
import org.shoukaiseki.gui.jtable.TableCellRendererIcon;
import org.shoukaiseki.gui.owern.swing.ijframe.Jnf;
import org.shoukaiseki.gui.owern.swing.ijframe.MusukoOwnerJnf;
import org.shoukaiseki.gui.owern.swing.jframe.MusukoOwnerJFrame;
import org.shoukaiseki.gui.owern.swing.risuna.ApurikeshonnShare;
import org.shoukaiseki.gui.tuuyou.StringIcon;
import org.tomaximo.puroguramu.ijframe.PrintJyouhou;
import org.tomaximo.puroguramu.interfaces.DetabesuKonnfiguAppInterFace;
import org.tomaximo.puroguramu.interfaces.DetabesuKonnfiguInterFace;
import org.tomaximo.puroguramu.plugins.AddKyoka;
import org.tomaximo.puroguramu.plugins.DetabesuKonnfigu;
import org.tomaximo.puroguramu.plugins.MaximoKannkeiResolve;
import org.tomaximo.puroguramu.tuuyou.IconSigenn;
import org.tomaximo.tuuyouclass.KonntenntuOutToJInternalFrame;
/**
 * 
 * @author 蒋カイセキ    Japan-Tokyo  2012-4-14
 * @ブログ http://shoukaiseki.blog.163.com/
 * @E-メール jiang28555@Gmail.com
 */
public class SennyouNoMaximo extends MusukoOwnerJFrame{
	private PrintJyouhou JF_ConsoleOut=new PrintJyouhou();
	private  JMenuBar menuBar = new JMenuBar();
	private JDialog aboutDialog,puraguinnDialog;
	private DetabesuKonnfiguInterFace detabesuKonnfiguInterFace=null;
	public SennyouNoMaximo(){
		super();
		getJGTuruba().setComboBox(new JComboBoxIcon());
		addJnf(JF_ConsoleOut,JTP_MINAMI, "情報出力");
		JF_ConsoleOut.setVisible(true);
		setTextPane(JF_ConsoleOut.getTextPane());

		initMenuBar();
		
//		test(); 
	} 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SennyouNoMaximo snm=new SennyouNoMaximo();
		snm.addDefuorutoPuraguinn();
//		snm.setExtendedState(0);
//		snm.setExtendedState(MAXIMIZED_BOTH);
		snm.setSize(700, 500);
	}
	
	public void init(){
		super.init();
//		addJnf(JF_ConsoleOut,JTP_MINAMI, "信息");
		
	}
	
	private void initMenuBar(){
		aboutDialog=new JDialog();
		puraguinnDialog=new JDialog();
		initAboutDialog();
		JMenu menu = new JMenu("助け[帮助]");
		JMenuItem menuItem = new JMenuItem("バージョン情報[关于]");
		menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	// "关于"对话框的处理
                aboutDialog.setVisible(true);
            }
        });

		JMenuItem pdJMI = new JMenuItem("プラグイン[插件]");
		pdJMI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	// "关于"对话框的处理
            	puraguinnDialog.setVisible(true);
            }
        });
		menu.add(pdJMI);
		menu.add(menuItem);
		setJMenuBar(menuBar);
		menuBar.add(menu);
	}
	 public void initPuraguinnDialog(){
		 puraguinnDialog.setTitle("已加载的插件");
	     Container con =puraguinnDialog.getContentPane();
		 String[] columnNames = { "","名称", "备注","版本","类名", "包名"}; // 列名
		 DefaultTableModel dtm=new DefaultTableModel(columnNames,0){ 
			 public   boolean   isCellEditable(int   row,   int   column){ 
//				 if(row==1){
//					 return false;	//第二行只读
//				 }
				 return   column!=0; //第一列只读,
	        } 
		 };
		 JTableOperating table = new JTableOperating(dtm);
	     Set<String> key = asMap.keySet();
	     Iterator it = key.iterator();
	     
	     TableCellRendererIcon renderer=new TableCellRendererIcon();
		 TableColumnModel tcm = table.getColumnModel();  
		 tcm.getColumn(0).setCellRenderer(renderer);
		 int columnWidth=55;//列宽
		 table.getColumnModel().getColumn(0).setPreferredWidth(columnWidth); 
	     table.setPreferredScrollableViewportSize (new Dimension(columnWidth,0));
	     table.getColumnModel().setColumnMargin(0);
	      
	     table.setShowHorizontalLines(true);
	     table.setShowGrid(true);
	     
	     for (int i = 0; it.hasNext(); i++) {
	    	 Object ob=  it.next();
	    	 ob=asMap.get(ob);
	    	 if(ob instanceof ApurikeshonnShare){
		    	 ApurikeshonnShare as= (ApurikeshonnShare) ob;
		    	 String[] className=KannriClasses.getClassName(as.getClass());
				 if(className==null){
					 continue;
				 }
				 String path=className[0];
				 String name=className[1];
				 String dbp=null;
				 if(ob.equals(detabesuKonnfiguInterFace)){
					 dbp="DBK";
				 }
				 table.addRow(new Object[] {new StringIcon(dbp,as.getPuraguinnIcon()),as.getPuraguinnName(),
						 as.getPuraguinnTyuusyaku(),as.getPuraguinnBajyonn(),name , path });
//		    	 System.out.println("插件---"+path);
//		    	 System.out.println("插件name---"+name);
	    	 }
	     }
	     JScrollPane jScrollPane = new JScrollPane(table);

	     table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);//水平滚动条
			
	     con.add(jScrollPane);
		 puraguinnDialog.setSize(550,425);
		 puraguinnDialog.setLocationRelativeTo(null);            
		 puraguinnDialog.setModal(true);   
		 puraguinnDialog.addWindowListener(new WindowAdapter()
	     {
	           public void WindowClosing(WindowEvent e)
	           {
	               dispose();
	           }                    
	      }); 
	 }
	
    /**
     * 设置"关于"对话框的外观及响应事件,操作和JFrame一样都是在内容
     * 框架上进行的
     */
    public void initAboutDialog()
    {
        aboutDialog.setTitle("关于");
        
        Container con =aboutDialog.getContentPane();
         
        // Swing 中使用html语句
		JLabel aboutLabel = new JLabel(
				"java -version "
						+ System.getProperty("java.version")
						+ "        Chiang Kai-shek(shoukaiseki) <jiang28555@gmail.com>  "
						+ "  2012-02-20 Tokyo japan ");
		
		aboutLabel.setText(""+"<HTML>"
				+"<body align=\"center\" bgcolor=\""+aboutLabel.getBackground().toString()+"\" >"
				+"java -version <font color=\"#FF00FF\">"+System.getProperty("java.version")+"</font>"
				+"<br>"
				+"Chiang Kai-shek(<font   size=\"+1\" color=\"#8A2BE2\">shoukaiseki</font>) &lt;jiang28555@gmail.com&gt;"
				+"<br>"
				+"<font size=\"+1\" color=\"#FF4500\" >2012-04-03 Tokyo japan <font>"
				+"</body>"
				+"</HTML>" );
		aboutLabel.setText(""+"<HTML>"
				+"<body align=\"center\" bgcolor=\"#1C1D1E\" >"
				+"<font  size=\"+1\" color=\"#7FFF00\">java -version </font>"
				+"<font  size=\"+1\" color=\"#FF00FF\">"+System.getProperty("java.version")+"</font>"
				+"<br>"
				+"<font  size=\"+1\" color=\"#7FFF00\">Chiang Kai-shek(</font>"
				+"<font   size=\"+1\" color=\"#8A2BE2\">shoukaiseki</font>"
				+"<font  size=\"+1\" color=\"#7FFF00\">) &lt;jiang28555@gmail.com&gt;</font>"
				+"<br>"
				+"<font size=\"+2\" color=\"#FF8C00\" >2012-04-03 Tokyo japan <font>"
				+"</body>"
				+"</HTML>");
        aboutLabel.setHorizontalAlignment(SwingConstants.CENTER);
		aboutLabel.setOpaque(true); 
		aboutLabel.setBackground(new Color(28,29,30));
		aboutLabel.setBorder(BorderFactory.createEtchedBorder());
        con.add(aboutLabel,BorderLayout.CENTER);//,BorderLayout.CENTER
        aboutDialog.setSize(550,225);
        aboutDialog.setLocationRelativeTo(null);            
		aboutDialog.setModal(true);       
        aboutDialog.addWindowListener(new WindowAdapter()
        {
            public void WindowClosing(WindowEvent e)
            {
                dispose();
            }                    
        });            
    }
    
	public void test(){
//		addJnf(new JLabel("jtpKita"),JTP_KITA,"A");
		addJnf(new JLabel("jtpMinami"),JTP_MINAMI,"B");
		addJnf(new JLabel("jtpNisi"),JTP_NISI,"C");
		addJnf(new JLabel("jtpNisiMusuko"),JTP_NISIMUSUKO,"D");
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
	 * 加载默认插件
	 */
	public void addDefuorutoPuraguinn(){
		setKonnsoru(true);//开启调试模式
		loadClass(DetabesuKonnfigu.class);
		loadClass(AddKyoka.class);
		loadClass(MaximoKannkeiResolve.class);
		//初始化插件管理
		initPuraguinnDialog();
		if(detabesuKonnfiguInterFace!=null){
			Set<String> key = asMap.keySet();
	        Iterator it = key.iterator();
	    	for (int i = 0; it.hasNext(); i++) {
	    		Object ob= it.next();
	    		ob=asMap.get(ob);
	    		if(ob instanceof DetabesuKonnfiguAppInterFace){
	    			((DetabesuKonnfiguAppInterFace)ob).DetabesuKonnfiguAppInit();
	    			printlnConsole("DetabesuKonnfiguAppInterFace"+ob.toString());
	    		}
	    	}
		}else{					   
			JOptionPane.showMessageDialog(getContentPane(),
			       "加载中的插件中无数据库配置管理类插件!", "提示", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void println() {
		println("");
	}
	
	public void println(int age) {
		println("" + age);
	}

	public void println(String age)  {
		JF_ConsoleOut.println(age, false); 
	}

	public void println(String age, boolean b) {
		JF_ConsoleOut.println(age, b);
	}
	
	public PrintJyouhou getPrintJyouhou(){
		return JF_ConsoleOut;
	}
	/**
	 * 数据库配置信息接口
	 * @param dkif
	 */
	public void setDetabesuKonnfiguInterFace(DetabesuKonnfiguInterFace dkif){
		this.detabesuKonnfiguInterFace=dkif;
	}
	public DetabesuKonnfiguInterFace getDetabesuKonnfiguInterFace(){
		return this.detabesuKonnfiguInterFace;
	}
}


