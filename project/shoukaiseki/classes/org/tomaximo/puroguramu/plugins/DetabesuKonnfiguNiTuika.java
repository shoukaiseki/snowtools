package org.tomaximo.puroguramu.plugins;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.shoukaiseki.gui.jbutton.JButtonIcon;
import org.shoukaiseki.gui.jtable.JTableOperating;
import org.shoukaiseki.gui.jtextpane.JTextPaneOperating;
import org.shoukaiseki.gui.jtree.IconJTreeObject;
import org.shoukaiseki.gui.tabbedpane.ClosableTabbedPane;
import org.shoukaiseki.gui.tuuyou.JLabelComponentAt;
import org.shoukaiseki.sql.ConnectionKonnfigu;
import org.tomaximo.puroguramu.ijframe.PrintJyouhou;
import org.tomaximo.puroguramu.ijframe.SennyouNoMaximoJnf;
import org.tomaximo.puroguramu.interfaces.DetabesuKonnfiguAppInterFace;
import org.tomaximo.puroguramu.interfaces.DetabesuKonnfiguInterFace;
import org.tomaximo.puroguramu.jframe.SennyouNoMaximo;
import org.tomaximo.puroguramu.tuuyou.IconSigenn;

public class DetabesuKonnfiguNiTuika extends SennyouNoMaximoJnf 
implements DetabesuKonnfiguAppInterFace{

	String tabRaberuName;
	/**
	 * 应用程序
	 */
	JButtonIcon runButton,commitButton,rollbackButton,bajyonn;
	//公用
	private static DetabesuKonnfiguInterFace detabesuKonnfiguInterFace;
	
	private JTableOperating table;

	private String sql = "";
	private Connection con = null;
	ConnectionKonnfigu ck=null;
	private String url ;
	private String driver ;
	private String user ;
	private String password ;
	private PrintJyouhou JF_ConsoleOut;
	private JTextPaneOperating maximoApp=new JTextPaneOperating();
	private JTextPaneOperating maximoKyoka=new JTextPaneOperating();
	private JTextPaneOperating maximoMusi=new JTextPaneOperating();
	public DetabesuKonnfiguNiTuika(){
		super();
		puraguinnIcon=null;//插件图标
		puraguinnName="AddKyoka";//插件名称
		puraguinnTyuusyaku="添加至权限组";//插件备注
		puraguinnBajyonn="V1.0.0";//插件版本号
	}
	
	public DetabesuKonnfiguNiTuika(String string,SennyouNoMaximo sennyouNoMaximo,ConnectionKonnfigu ck) {
		super();
		// TODO Auto-generated constructor stub
		this.con=con;
		this.ck=ck;
		printDatabesu(ck);
		this.tabRaberuName=string;
		this.sennyouNoMaximo=sennyouNoMaximo;
		JF_ConsoleOut=sennyouNoMaximo.getPrintJyouhou();
		setBorder(BorderFactory.createEmptyBorder());
	}
	
	public void printDatabesu(ConnectionKonnfigu ck){
		try {
			con=detabesuKonnfiguInterFace.getConnection(ck);
			Statement st = con.createStatement();
			ResultSet r = st
					.executeQuery("SELECT UTL_INADDR.get_host_name() 服务器主机名," +
							"utl_inaddr.get_host_address 服务器IP ," +
							"SYS_CONTEXT('USERENV','TERMINAL'),sysdate,user FROM dual");

			ResultSetMetaData rsmd=r.getMetaData();
			int column=rsmd.getColumnCount();
			System.out.println("列数为"+column);
			for (int i = 0; i < rsmd.getColumnCount() ; i++) {
				System.out.print(rsmd.getColumnName(i+1)+"\t");
			}
			System.out.println();
			while (r.next()) {
				for (int i = 0; i < column; i++) {
					System.out.print(r.getString(i+1)+"\t");
				}
				System.out.println();
			}

			r.close();
			st.close();
			
			//select SYS_CONTEXT('USERENV','TERMINAL') from dual
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 应用初始化
	 */
	public void appInit(){
		initAboutDialog();
		runButton=tg.addJButton(IconSigenn.RUN);
		runButton.setToolTipText("执行");
		commitButton= tg.addJButton(IconSigenn.COMMIT);
		commitButton.setToolTipText("提交更改");
		rollbackButton= tg.addJButton(IconSigenn.ROLLBACK);
		rollbackButton.setToolTipText("回退更改");
		bajyonn=tg.addJButton(IconSigenn.KOKUMINNTOU);
		bajyonn.setToolTipText("バージョン情報[关于]");
		commitButton.setEnabled(false);
		rollbackButton.setEnabled(false);
		String konnfigumeishou=detabesuKonnfiguInterFace.getKonnfiguMeishou(ck);
		setTurubaGroupJComboBox(detabesuKonnfiguInterFace, tg);
		tg.getComboBox().setMinimumSize(null);
		String[] columnNames= new String[]{ "APPLICATIONAUTHID","GROUPNAME","APP","OPTIONNAME","SQL","F" ,"G","H","I","J"};
		
		JSplitPane jsp;
		table=new JTableOperating(new DefaultTableModel(columnNames, 55));
		table.init();
		JPanel box=new JPanel() ;
		box.setLayout(new GridLayout(3, 1));
		JPanel jp=setJTextArea(maximoApp,"应用:"); 
		
		JPanel jp2=setJTextArea(maximoKyoka,"权限:");
		
		JPanel jp3=setJTextArea(maximoMusi,"忽略:");
		box.add(jp);
		box.add(jp2);
		box.add(jp3);
		jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, box ,
				table.getJScrollPane());
		c.add(jsp,BorderLayout.CENTER);
		

		
		
		tg.getComboBox().setSelectedItem(konnfigumeishou,IconSigenn.DATABASE);
		if(tg!=null)
			tg.getJButtonGroup().setBackground(new Color(200,221,242));
		
		
		runButton.addActionListener(new ActionListener() {// 添加事件
			public void actionPerformed(ActionEvent e) {
				commitButton.setEnabled(true);
				rollbackButton.setEnabled(true);
			}
		});
		
		
		 commitButton.addActionListener(new ActionListener() {// 添加事件
				public void actionPerformed(ActionEvent e) {
					conCommit();
				}
			});
		 rollbackButton.addActionListener(new ActionListener() {// 添加事件
				public void actionPerformed(ActionEvent e) {
						conRollback();
				}
			});
		 bajyonn.addActionListener(new ActionListener() {// 添加事件
				public void actionPerformed(ActionEvent e) {
					setAboutDialogDataBesuBajyonn(con);
					aboutDialog.setVisible(true);
					printlnConsole("aboutDialog.setVisible(true);");
			}
		});
	}

	public JPanel setJTextArea(JTextPaneOperating jta,String label){
		jta.setLayout(null);
		JScrollPane jspa = new JScrollPane(jta);// 新建一个滚动条界面，将文本框传入
		jspa.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JPanel jp=new JPanel();
		BorderLayout bl=new BorderLayout();
		jp.setLayout(bl); 
		JLabel jl=new JLabel(label+":",JLabel.LEADING);
		jl.setOpaque(true);
		JPanel jp2=new JPanel();
		jp2.add(jl);
		jp.add(jp2,BorderLayout.WEST);
		jp.add(jspa,BorderLayout.CENTER);
		jta.addLastLine("输过中文后才会自动换行");
		jta.cleanText();
		return jp;
	}
	
	/**
	 * 初始化,总调度专用,挂载点
	 */
	public void init(){ 
		super.init();
		addApurikeshonn("数据库配置Ghost",IconSigenn.RUN);
		addRisuna();
	}
	
	/**
	 * 各种监听器
	 */
	private void addRisuna() {
		// TODO Auto-generated method stub
		//winodw关闭监听器
		addwindwoCloseMap(this);
		
	}
	
	public JLabelComponentAt getJLabelComponentAt(){
		return jtpID;
	}

	protected void AppOpen() {
		// TODO Auto-generated method stub
		AppOpen(null);
	}
	
	public void AppOpen(ConnectionKonnfigu ck){
		//新建窗口
		AddKyoka ak=new AddKyoka("添加到权限组",sennyouNoMaximo,ck);
		JLabelComponentAt jca=sennyouNoMaximo.addJnfHaveTurubaGroup(ak,"权限");
		 tg=jca.getTurubaGroup();
//		ak.setJLabelComponentAt(jca);
//		printlnConsole("AppOpen");
		ak.setOwnerJFrame(getOwnerJFrame());
		addTabAboutToClose(ak,jca);//增加标签页关闭监听器
		
	}
	
	/**
	 * 鼠标双击事件
	 * @param evt
	 * @return
	 */
	protected void ApurikeshonnDaburuKurikku(MouseEvent e) {
		AppOpen();
	}
	
	
	/**
	 * con提交更改
	 * 要事先con.setAutoCommit(false);自动更新关闭掉
	 */
	public void conCommit(){
		try {
			int suteetasu = JOptionPane.showConfirmDialog(null,
						"确定要提交更改吗?\n取消后将不提交,但是也不会取消更改.", "提示!!",
						JOptionPane.YES_NO_OPTION);
			if (suteetasu == 0) {
				con.commit();
				println("**********提交成功!**********",true);
				commitButton.setEnabled(false);
				rollbackButton.setEnabled(false);
			}else{
				println("**********已取消提交!**********",true);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			conRollback();
			e.printStackTrace();
		}
	}
	/**
	 * @throws BadLocationException 
	 * 
	 */
	public void conRollback(){
			try {
				con.rollback();
				commitButton.setEnabled(false);
				rollbackButton.setEnabled(false);
				println("**********本地更改已清除!**********", true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	
	public boolean tabuTojiruRisuna(int tabIndex,MouseEvent me){
		ClosableTabbedPane ctp=(ClosableTabbedPane) me.getSource();
		String tab = ctp.getTabTitleAt(tabIndex)+"  ";
		int choice = //0;
			JOptionPane.showConfirmDialog(null,
				"是否关闭 '" + tab
						+ "'标签页\nラベルは閉じる,かどうか ?",
				"提示",
				JOptionPane.INFORMATION_MESSAGE);
		System.out.println("sennyouNoMaximo--="+sennyouNoMaximo);
		sennyouNoMaximo.printlnConsole("---tabuTojiruRisuna");
		return choice == 0; 
	}
	
	public String puraguinnName() {
		// TODO Auto-generated method stub
		return "AddKyoka";
	}

	@Override
	public void DetabesuKonnfiguAppInit() {
		// TODO Auto-generated method stub
		detabesuKonnfiguInterFace=sennyouNoMaximo.getDetabesuKonnfiguInterFace();
		IconJTreeObject ijto=new IconJTreeObject("添加到权限组"){
			public void mouseReleased(MouseEvent er,JTree tree,
					TreePath path,DefaultMutableTreeNode selectNode){
				/**
				 * 鼠标双击监听
				 */
				if(er.getClickCount() == 2
						&& SwingUtilities.isLeftMouseButton(er)){
					DefaultMutableTreeNode dmtn=(DefaultMutableTreeNode)selectNode.getParent();
					String konnfigumeishou=((IconJTreeObject)dmtn.getUserObject()).name;
					
					ConnectionKonnfigu ck=detabesuKonnfiguInterFace.getConnectionKonnfigu(konnfigumeishou);
					AppOpen(ck);
				}
			}

		};
		ijto.setTreeRootLeafIcon(IconSigenn.APPLICATION, IconSigenn.APPLICATION);
		
		JMenuItem jmenuItem=new JMenuItem("打开",IconSigenn.OPEN);
		jmenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent er) {
				ck=detabesuKonnfiguInterFace.getSelectDatabesu();
				AppOpen(ck);
			}
		});
			
		
		Vector<JMenuItem> jMenuItemVector=new Vector();
		jMenuItemVector.add(jmenuItem);
		ijto.setJMenuItem(jMenuItemVector);
		
		DefaultMutableTreeNode dmtn=new DefaultMutableTreeNode(ijto);
		DefaultMutableTreeNode[] dmtns=new DefaultMutableTreeNode[]{dmtn};
		detabesuKonnfiguInterFace.addAppDatabesu(dmtns);
		
	}
}


