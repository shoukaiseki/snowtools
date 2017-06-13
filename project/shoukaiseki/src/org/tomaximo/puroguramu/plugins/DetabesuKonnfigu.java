package org.tomaximo.puroguramu.plugins;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.MenuElement;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.shoukaiseki.characterdetector.CharacterEncoding;
import org.shoukaiseki.characterdetector.utf.UnicodeReader;
import org.shoukaiseki.constantlib.CharacterEncodingName;
import org.shoukaiseki.file.SousaFile;
import org.shoukaiseki.gui.jcombobox.JComboBoxIcon;
import org.shoukaiseki.gui.jtextpane.JTextPaneDoc;
import org.shoukaiseki.gui.jtree.IconJTreeObject;
import org.shoukaiseki.gui.jtree.IconTreeCellRenderer;
import org.shoukaiseki.gui.jtree.JMenuItemJTree;
import org.shoukaiseki.gui.owern.swing.ijframe.MusukoOwnerJnf;
import org.shoukaiseki.gui.owern.swing.risuna.ApurikeshonnShare;
import org.shoukaiseki.gui.tuuyou.StringIcon;
import org.shoukaiseki.sql.ConnectionKonnfigu;
import org.tomaximo.puroguramu.ijframe.SennyouNoMaximoJnf;
import org.tomaximo.puroguramu.interfaces.DetabesuKonnfiguInterFace;
import org.tomaximo.puroguramu.tuuyou.IconSigenn;

public class DetabesuKonnfigu  extends SennyouNoMaximoJnf
implements DetabesuKonnfiguInterFace{

	private String fileName = "./config/DetabesuKonnfigu.ini";
	public File txtfile = new File(fileName);
	
	private JPanel jp;
	private JButton jb_cleantext,jb_readsetting;// 清空记录,重读配置
	private JButton JComboBox[]=new JButton[6]; 
	private JTextPaneDoc textPane;
	private JPopupMenu jPopupMenu = new JPopupMenu();
	private DefaultMutableTreeNode root;
	private JTree tree;

	private Connection con = null;
	private String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String user = "orclzhjq";
	private String password = "orclzhjq";
	private JComboBoxIcon comboBox =new JComboBoxIcon();
	private Map<String, ConnectionKonnfigu> konnfiguMap=new HashMap();
	private Map<ConnectionKonnfigu,Connection> conMap=new HashMap();
	private ConnectionKonnfigu selectConnectionKonnfigu =null;//选中
	private ConnectionKonnfigu ck=null;
	private String defuorutomeishou="";
	
	private Vector<DefaultMutableTreeNode> appDataBesuVector=new Vector();
	
	private MouseAdapter mouseAdapter;
	
	/**
	 * 操作文件类
	 */
	SousaFile sousaFile=new SousaFile(false);
	
	public DetabesuKonnfigu(){
		super();
		puraguinnIcon=IconSigenn.DATABASE;//插件图标
		puraguinnName="DetabesuKonnfigu";//插件名称
		puraguinnTyuusyaku="数据库连接配置";//插件备注
		puraguinnBajyonn="V1.0.0";//插件版本号
	}
	/**
	 * 初始化,总调度专用,挂载点
	 */
	public void init(){ 
		super.init();
		addApurikeshonn("数据库",IconSigenn.COMMIT);
		sennyouNoMaximo.addJnf(this,sennyouNoMaximo.JTP_NISIMUSUKO,"连接",IconSigenn.DBCONN);
		sennyouNoMaximo.setDetabesuKonnfiguInterFace(this);
		IconJTreeObject ijto=new IconJTreeObject("连接");
		ijto.setTreeRootLeafIcon(IconSigenn.DATABASE, IconSigenn.DATABASE,IconSigenn.DATABASE);
		ijto.setKurikkuCollapsed(false);//关闭单击折叠
		ijto.setKurikkuExpanded(false);//关闭单击展开
		textPane=sennyouNoMaximo.getTextPane(); 
		comboBox=sennyouNoMaximo.getJGTuruba().getComboBox(); 
		IconTreeCellRenderer itcr=new IconTreeCellRenderer();
		itcr.setIconJTreeObject(ijto);
		
		ijto= ijto.clone();
		ijto.setTreeRootLeafIcon(IconSigenn.DBCONN, IconSigenn.DBCONN);
		ijto.setName("连接");
//		ijto.setExpanded(false);
		ijto.setCollapsed(false);
		root = new DefaultMutableTreeNode(ijto);
		tree = new JTree(root);
		add(tree);
		tree.setCellRenderer(itcr);
		readSetting();
//		addRisuna();
		
		tree.addMouseListener(mouseAdapter);
	}
	
	/**
	 * 各种监听器
	 */
	private void addRisuna() {
		// TODO Auto-generated method stub
		mouseAdapter=new MouseAdapter(){
			public void mouseReleased(MouseEvent er) {
				 System.out.println("mouseReleased");

				// 是否右键单击
				if (er.getClickCount() == 1
						&& SwingUtilities.isRightMouseButton(er)) {
					TreePath path = tree.getPathForLocation(er.getX(), er
							.getY());
					if (path == null)
						return;

					tree.setSelectionPath(path);
					DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode) tree
							.getLastSelectedPathComponent();

					Object ob = selectNode.getUserObject();

					if (ob instanceof IconJTreeObject) {
						IconJTreeObject si = ((IconJTreeObject) ob);
						// System.out.println(si.name + "---" + si.getObject());
						if (si.getObject() instanceof JPopupMenu) {
							// System.out.println("instanceof");
							// jPopupMenu.removeAll();
							JPopupMenu jPo = (JPopupMenu) si.getObject();
							//右键菜单添加JPopupMenu,JPopupMenu 添加另一个 JPopupMenu
							for ( MenuElement me : jPopupMenu.getSubElements()) {
								jPo.add((JMenuItem) me);
							}
							tree.add(jPo);
							// tree.setPopupSize(new Dimension(333,333)) ;
							jPo.show(tree, er.getX(), er.getY());
							return;
						}
					}
					// JPopupMenu jPo=tree.getUI();
//					System.out.println(tree.getUI());
					jPopupMenu.show(tree, er.getX(), er.getY());
				}
			}
		};
		
		
		
		tree.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent er) {
				JTree tree = (JTree) er.getSource();
				// System.out.println("mouseReleased");

				// 是否右键单击
				if (er.getClickCount() == 1
						&& SwingUtilities.isRightMouseButton(er)) {
					TreePath path = tree.getPathForLocation(er.getX(), er
							.getY());
					if (path == null)
						return;

					tree.setSelectionPath(path);
					DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode) tree
							.getLastSelectedPathComponent();

					Object ob = selectNode.getUserObject();

					if (ob instanceof IconJTreeObject) {
						IconJTreeObject si = ((IconJTreeObject) ob);
						// System.out.println(si.name + "---" + si.getObject());
						if (si.getObject() instanceof JPopupMenu) {
							// System.out.println("instanceof");
							// jPopupMenu.removeAll();
							JPopupMenu jPo = (JPopupMenu) si.getObject();
							if(jPo!=null){
								//右键菜单添加JPopupMenu,JPopupMenu 添加另一个 JPopupMenu
								
								for ( MenuElement me : jPopupMenu.getSubElements()) {
									jPo.add( me.getComponent() );
								}
								tree.add(jPo);
//								System.out.println(jPopupMenu.countComponents());
								// tree.setPopupSize(new Dimension(333,333)) ;
								jPo.show(tree, er.getX(), er.getY());
								return;
							}
						}
					}
					// JPopupMenu jPo=tree.getUI();
					System.out.println(tree.getUI());
					tree.add(jPopupMenu);
					System.out.println(jPopupMenu.countComponents());
					jPopupMenu.show(tree, er.getX(), er.getY());
				}
			}
		});
	}
	
	
	public void readSetting()  {
		
		try {
			newFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 无配置文件则自动新建文件
		reFile();// 读取配置文件
//		setCon();// 载入驱动
		setComBobox();
	}

	@SuppressWarnings("unchecked")
	public void setComBobox(){
        Set<String> key = konnfiguMap.keySet();
        Iterator it = key.iterator();
        
		for (int i = 0; it.hasNext(); i++) {
			String s=(String) it.next();
			ConnectionKonnfigu ck = (ConnectionKonnfigu) konnfiguMap.get(s);
			comboBox.addItem(new StringIcon(s,IconSigenn.DATABASE));
			IconJTreeObject ijto=new IconJTreeObject(s){
				public void mouseReleased(MouseEvent er,JTree tree, TreePath path,
						DefaultMutableTreeNode selectNode){
					if(er.getClickCount() == 2
							&& SwingUtilities.isLeftMouseButton(er)){
						ijtoMausuDaburukurikku(er, tree, path, selectNode);
					}
				}

			}; 		
			ijto.setObject(ck);//保存该节点的ConnectionKonnfigu
			ijto.setKurikkuCollapsed(false);//关闭单击折叠
			ijto.setKurikkuExpanded(false);//关闭单击展开
			Vector<JMenuItem> jMenuItemVector=new Vector();
			JMenuItemJTree jmi=new JMenuItemJTree("断开",ijto){	
				/**
				 * actionPerformed监听,需要重写
				 * public void actionPerformed(ActionEvent e,JMenuItemJTree jmijt,
				 * 			IconJTreeObject ijto, ConnectionKonnfigu ck)
				 */
				public void actionPerformed(ActionEvent e,JMenuItemJTree jmijt,
						IconJTreeObject ijto, ConnectionKonnfigu ck,DefaultMutableTreeNode dmtn){
					printlnConsole("actionPerformed----"+jmijt.getIconJTreeObject().name);
					try {
						if(conMap.get(ck)==null){
							return;
						}
						conMap.get(ck).close();
						conMap.remove(ck);
						dmtn.removeAllChildren();
						((IconJTreeObject) dmtn.getUserObject()).setTreeRootLeafIcon(IconSigenn.DATABASE,IconSigenn.DATABASE);
						tree.updateUI();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						println("***************************",true);
						println("***********断开失败***********",true);
						println("***************************",true);
						println(e1.getMessage(),true);
					}
				}
			};
			jmi.setConnectionKonnfigu(ck);
			jMenuItemVector.add(jmi);
			ijto.setJMenuItem(jMenuItemVector);
			DefaultMutableTreeNode dmtn=new DefaultMutableTreeNode(ijto);
			jmi.setDefaultMutableTreeNode(dmtn);
			root.add(dmtn);
		}
		sennyouNoMaximo.getJGTuruba().rifuressyu();

		if(tree.getRowCount()>=0){
			/**
			 * 自动展开root 根
			 */
			tree.expandRow(0);
		}
		
	}
	
	public void ijtoMausuDaburukurikku(MouseEvent er,JTree tree, TreePath path,
			DefaultMutableTreeNode selectNode){
		//TODO 左键双击
		tree.setSelectionPath(path);
		if (selectNode.isLeaf()) {
			Object ob = selectNode.getUserObject();

			if (ob instanceof IconJTreeObject) {
				IconJTreeObject si = ((IconJTreeObject) ob);
				System.out.println(si.name);
				ck=konnfiguMap.get(si.name);
					if(ck!=null){
						
						setConRunnable(ck,path,selectNode);
					}
				return;
			}
		}
	}
	
	public void newFile() throws IOException {
		// 新建文件
		if (!txtfile.exists()) {
			textPane.addLastLine("正在创建文件" + txtfile.getPath());
			try {
				String age0 = wrString();
				sousaFile.writeFile(textPane,txtfile, age0,true);
			} catch (Exception e) {
				// TODO: handle exception
				textPane.addLastLine(e.getMessage());
			}
		} else {
			textPane.addLastLine("发现配置文件" + fileName + "!");
		}
	}
	/**
	 * 默认值
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String wrString() throws UnsupportedEncodingException {
		String s = "";
		s = s + "#注释符号为#\r\n";
		s = s + "#注意要区分大小写\r\n";
		s = s + "#连接数据库参数\r\n";
		s = s + "#{需要单独一行,只取第一个字符\r\n";
		s = s + "#defuorutomeishou默认显示名\r\n";
		s = s + "#konnfigumeishou设置名\r\n";
		s = s + "defuorutomeishou=默认\r\n";
		s = s + "\r\n";
		s = s + "konnfigumeishou=默认\r\n";
		s = s + "{\r\n";
		s = s + "url=jdbc:oracle:thin:@127.0.0.1:1521:orcl\r\n";
		s = s + "driver=oracle.jdbc.OracleDriver\r\n";
		s = s + "user=maximo75\r\n";
		s = s + "password=maximo75\r\n";
		s = s + "}\r\n";
		
		return s;
	}
	

	private void reFile(){
		// 读取文件
		textPane.addLastLine("\n\n读取文件!");
		try {
			comboBox.removeAllItems();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		try {
			String code = CharacterEncoding.getLocalteFileEncode(fileName);

			FileInputStream in = new FileInputStream(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(in,
					code));
			/**
			 * 解决win记事本保存UTF-8文件后文件头???问题, bom信息=EFBBBF
			 */
			br = new BufferedReader(new UnicodeReader(in, code));

			printlnConsole("code=" + code);

			String sr = null;
			String a = null;
			String b = null;
			String konnfigumeishou=null;
			
			url="";
			user="";
			driver="";
			password="";
			while ((sr = br.readLine()) != null) {
				textPane.addLastLine(sr);
				if (sr.isEmpty()) {
					continue;
				}
				a = sr.substring(0, 1);
				printlnConsole(a);
				printlnConsole(sr);
				if (a.equals("#")) {
					continue;
				}
				if (a.equals("{")) {
					url="";
					driver="";
					user="";
					password="";
					continue;
				}
				if (a.equals("}")) {
					ck=new ConnectionKonnfigu(url, driver, user, password);
					if(konnfiguMap.get(konnfigumeishou)==null){
						konnfiguMap.put(konnfigumeishou,new ConnectionKonnfigu(url, driver, user, password) );
					}else{
						println(konnfigumeishou+"配置名称重复");
					}
					continue;
				}
				// 取等号位置
				int value = sr.indexOf("=");
				printlnConsole(value);
				if (value < 0) {
					continue;
				}
				a = sr.substring(0, value).trim();// =号前面取首尾空
				b = sr.substring(value + 1, sr.length()).trim();// =号后面取首尾空
				if(a.equals("defuorutomeishou")){
					defuorutomeishou=b;
					textPane.addLastLine("defuorutomeishou="+defuorutomeishou,true);
					continue;
				}
				if(a.equals("konnfigumeishou")){
					konnfigumeishou=b;
//					comboBox.addItem(konnfigumeishou);
					continue;
				}
				if (a.equals("url")) {
					url = b;
					continue;
				}
				if (a.equals("driver")) {
					driver = b;
					continue;
				}
				if (a.equals("user")) {
					user = b;
					continue;
				}
				if (a.equals("password")) {
					password = b;
					continue;
				}
			}
//			comboBox.setSelectedItem(defuorutomeishou);
//			this.setCon(konnfiguMap.get(defuorutomeishou));
			
		} catch (IOException e) {
			e.printStackTrace();
			textPane.addLastLine(e.getMessage());
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setConRunnable( final ConnectionKonnfigu ck, final TreePath path,
			 final DefaultMutableTreeNode selectNode){

		new Timer() .schedule(new TimerTask() {
			@Override
			public void run() {
				IconJTreeObject ito=new IconJTreeObject("正在连接....", true);
				System.out.println(ito);
				DefaultMutableTreeNode dmtn=new DefaultMutableTreeNode(ito);
				System.out.println(dmtn);
				selectNode.add(dmtn);
				tree.expandPath(path);
				try {
//					System.out.println("setConRunnable");
					String eJyouhou=setCon(ck);
					System.out.println(eJyouhou);
					if(eJyouhou==null){
						((IconJTreeObject) selectNode.getUserObject()).setTreeRootLeafIcon(IconSigenn.DBCONN,IconSigenn.DBCONN);
						ito.setName("已连接.");
						for (DefaultMutableTreeNode dmtnapp : appDataBesuVector) {
							DefaultMutableTreeNode cpdmtn= (DefaultMutableTreeNode)dmtnapp.clone();
							selectNode.add(cpdmtn); 
						}
					}else{
						ito.setName("连接失败?"+eJyouhou);
					     JOptionPane.showMessageDialog(sennyouNoMaximo.getContentPane(),
					       "连接失败?\n"+eJyouhou, "连接失败?", JOptionPane.ERROR_MESSAGE);
							
					}
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ito.setName("连接失败?"+e.getMessage());
				}
				selectNode.remove(dmtn);
				tree.updateUI();//JTree刷新
			}
		}, 10);
	}
	public String setCon(ConnectionKonnfigu ck) throws BadLocationException {
		try {

			println(ck.toString());
			 this.url=ck.getUrl();
			 this.driver=ck.getDriver();
			 this.user=ck.getUser();
			 this.password=ck.getPassword();
			// （1）装载并注册数据库的JDBC驱动程序
			// 载入JDBC驱动：oracle安装目录下的jdbc\lib\classes12.jar
			textPane.addLastLine("正在试图加载驱动程序 " + driver);
			Class.forName(driver);
			textPane.addLastLine("驱动程序已加载");
			// 注册JDBC驱动：有些地方可不用此句
			textPane.addLastLine("url=" + url);
			textPane.addLastLine("user=" + user);
			textPane.addLastLine("password=" + password);
			textPane.addLastLine("正在试图连接数据库--------");
			java.sql.DriverManager
					.registerDriver(new oracle.jdbc.driver.OracleDriver());
			DriverManager.setLoginTimeout(5) ;//登录时间限制，以秒为单位；0 表示没有限制
			printlnConsole("获取驱动程序试图登录到某一数据库时可以等待的最长时间，以秒为单位."+DriverManager.getLoginTimeout()); 
			con = DriverManager.getConnection(url, user, password);
			textPane.addLastLine("OK,成功连接到数据库");

			conMap.put(ck, con);
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			textPane.addLastLine(ex.getMessage(), true);
			
			return ex.getMessage();
		}
		
	}
	
	public void printConnection() throws BadLocationException{
		textPane.addLastLine("url=" + url, true);
		textPane.addLastLine("driver=" + driver, true);
		textPane.addLastLine("user=" + user, true);
		textPane.addLastLine("password=" + password, true);
	}	

	
	
	
	
	@Override
	public Map<String, ConnectionKonnfigu> getConnectionKonnfigu() {
		// TODO Auto-generated method stub
		return konnfiguMap;
	}
	@Override
	public String getKonnfiguMeishou(ConnectionKonnfigu ck){
		Set<String> key = konnfiguMap.keySet();
		Iterator it = key.iterator();
		for (int i=1; it.hasNext();i++) {
			String s = (String) it.next();
			if(konnfiguMap.get(s).equals(ck)){
				return s;
			}
		}
		return null;
	}
	@Override
	public ConnectionKonnfigu getConnectionKonnfigu(String konnfigumeishou){
		return konnfiguMap.get(konnfigumeishou);
	}
	@Override
	public Connection getConnection(String konnfigumeishou) {
		// TODO Auto-generated method stub
		if(konnfiguMap.get(konnfigumeishou)!=null){
			return getConnection(konnfiguMap.get(konnfigumeishou));
		}
		return null;
	}
	@Override
	public Connection getConnection(ConnectionKonnfigu ck) {
		// TODO Auto-generated method stub
		if(conMap.get(ck)==null){
			try {
				setCon(ck);
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return conMap.get(ck);
	}
	@Override
	public void addAppDatabesu(DefaultMutableTreeNode[] app){
		for (DefaultMutableTreeNode defaultMutableTreeNode : app) {
			appDataBesuVector.add(defaultMutableTreeNode);
		}
	}
	@Override
	public ConnectionKonnfigu getSelectDatabesu() {
		// TODO Auto-generated method stub
		 DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();

		 while (selectNode.getParent() !=null) {
			 Object ob=selectNode.getUserObject();
			 if(ob instanceof IconJTreeObject){
				 ob=((IconJTreeObject)ob).getObject();
				 if(ob instanceof ConnectionKonnfigu){
				        Set<String> key = konnfiguMap.keySet();
				        Iterator it = key.iterator();
						for (int i = 0; it.hasNext(); i++) {
							String s=(String) it.next();
							ConnectionKonnfigu ck = (ConnectionKonnfigu) konnfiguMap.get(s);
							if(ck.equals(ob)){
								try {
									if(conMap.get(ck)==null){
										setCon(ck);
									}
									return ck;
								} catch (BadLocationException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									println(e.getMessage(),true);
								}
							}
						}
				 }
			 }
			 selectNode=(DefaultMutableTreeNode) selectNode.getParent() ;
			 if(selectNode==null)return null;
		}
		return null;
	}
	
}


