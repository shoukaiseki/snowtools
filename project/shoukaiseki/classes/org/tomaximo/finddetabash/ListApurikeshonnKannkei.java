package org.tomaximo.finddetabash;

//java tomaximo.finddetabash.ListApurikeshonnKannkei
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.TabExpander;

import org.shoukaiseki.gui.jtable.ExcelAdapter;
import org.shoukaiseki.gui.jtable.JTableOperating;
import org.shoukaiseki.gui.jtextpane.JTextPaneDoc;
import org.shoukaiseki.sql.ConnectionKonnfigu;
import org.tomaximo.tuuyouclass.KonntenntuOut;



public class ListApurikeshonnKannkei extends JFrame {
	/**
	 * 关闭時是否退出程序,默认時退出,只作为子程序时不退出
	 */
	private boolean windowclose = true;

	private boolean buttonstatus = true;
	private JTableOperating table;
	private JTextField bTextField;
	private KonntenntuOut JF_ConsoleOut;

	private JToggleButton toggleButton;
	

	private String[] konntenntu;
	private ResultSet r ;
	

	private String sql = "";
	private Connection con = null;
	private String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String user = "orclzhjq";
	private String password = "orclzhjq";
	
	public ListApurikeshonnKannkei() {
		this(null, true);
	}

	public ListApurikeshonnKannkei(boolean windowclose) {
		this(null, windowclose);
	}

	public ListApurikeshonnKannkei(Connection con) {
		this(con, true);
	}

	public ListApurikeshonnKannkei(Connection con, boolean windowclose) {
		try {
			JF_ConsoleOut = new KonntenntuOut(false);
//			JF_ConsoleOut.setVisible(false);//使用后报错
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		init();
		this.con = con;
		String[] columnNames = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
				"J", "K", "L" }; // 列名
//		columnNames =new String[] { "A", "B"}; // 列名
		table = new JTableOperating(new DefaultTableModel(columnNames, 55));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JScrollPane scrollPane = new JScrollPane(table); // 支持滚动
		// jdk1.6
		// 排序:
		// table.setRowSorter(new TableRowSorter(tableModel));
		table.init();
		ExcelAdapter myAd = new ExcelAdapter(table);
		// table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //单选

		scrollPane.setViewportView(table);
		Box panel = Box.createHorizontalBox(); // 横结构
		panel.add(new JLabel("应用名: "));
		panel.add(Box.createHorizontalStrut(8));// 间距
		bTextField = new JTextField("opamdop", 30);
		panel.add(bTextField);
		panel.add(Box.createHorizontalStrut(8));// 间距

		final JButton updateButton = new JButton("実行"); // 修改按钮
		updateButton.addActionListener(new ActionListener() {// 添加事件
					public void actionPerformed(ActionEvent e) {
						new Timer().schedule(new TimerTask() {
							@Override
							public void run() {// 实例中的的方法
								selectSQL();
								// System.out.println("冻干粉");
							}
						}, 10);
					}
				});

		toggleButton = new JToggleButton("情報");
		toggleButton.addActionListener(new ActionListener() {// 添加事件
					public void actionPerformed(ActionEvent e) {
						new Timer().schedule(new TimerTask() {
							@Override
							public void run() {// 实例中的的方法
								if (toggleButton.isSelected()) {
									buttonstatus = true;
									setConsoleOutIsRight();
									table.setRowSelectionInterval(1, 2);
								} else {
									JF_ConsoleOut.setVisible(false);
									buttonstatus = false;
								}
							}
						}, 10);
					}
				});

		panel.add(updateButton);
		panel.add(Box.createHorizontalStrut(8));// 间距
		panel.add(toggleButton);

		JLabel jl1 = new JLabel(
				"java -version "
						+ System.getProperty("java.version")
						+ "        Chiang Kai-shek(shoukaiseki) <jiang28555@gmail.com>  "
						+ "  2012-02-20 Tokyo japan ", SwingConstants.CENTER);

		getContentPane().add(jl1, BorderLayout.NORTH);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		getContentPane().add(panel, BorderLayout.SOUTH);
		this.setSize(700, 500);
		this.setLocation(200, 150);
		this.setTitle("列出应用名对应的关系");
		this.setVisible(true);
		if (windowclose) {
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		}
	}

	/**
     * 
     */
	public void selectSQL() {
		table.removeRowsAll();// 清空内容
		// for (int i = 0; i < 9999999; i++) {
		// for (int j = 0; j < 10; j++) {
		//				
		// }
		// ;
		// }
		String appName = bTextField.getText().toUpperCase();
		String tableName = "";
		 konntenntu = new String[100];
		try {
			Statement st = con.createStatement();

			/**
			 * 应用中对应的表
			 */
			tableName=appTaiouNoHyou(st, appName);
			
			
			

			/**
			 * 触发器,因关系复杂性原因,只能提供本表的
			 */
			toriga(st, appName, tableName);
			
			/**
			 * 各元素的详细属性
			 */
			yousoNoShousaiNaPuropathi(st, appName, tableName);
			
			/**
			 *  签名选项设计
			 */
			shomeioOpushonnDezainn(st, appName, tableName);
			

			/**
			 * 选择操作设计
			 */
			senntakuSousaDezainn(st, appName, tableName);
			
			

			/**
			 * 工具栏设计
			 */
			turubaDezainn(st, appName, tableName);
			
			

			/**
			 * 搜索菜单设计
			 */
			kennsakuMenyuSekkei(st, appName, tableName);
			

			/**
			 * Mbo类 数据库绑定类
			 */
			mboDetabesuClass(st, appName, tableName);
			
			

			/**
			 * Fld类:主对象中各字段的关联类
			 */
			fldClass(st, appName, tableName);
			


			/**
			 * 工作流程相关
			 */
			sigotoPurosesu(st, appName, tableName);
			
			

			/**
			 * 页面中的信息
			 */
			printClob(con, tableName, tableName);
			
			/**
			 * Action类
			 */
			actionClass(st, appName, tableName);
			
			

			/**
			 * 对应的域
			 */
			taiouSuruDomeinn(st, appName, tableName);
			
			/**
			 * 权限组
			 */
			kyokaGurupu(st, appName, tableName);
			
			
			/**
			 * 页面自定义标签
			 */
//			sql = "SELECT * FROM MAXLABELS WHERE APP='"+appName+"'";
//			r = st.executeQuery(sql);
//			println("\n\n页面自定义标签");
//			println(sql); 
//			table.addRow(new Object[] {});
//			table.addRow(new Object[] {});
//			table.addRow(new Object[] { "页面自定义标签" });
//			table.addRow(new Object[] { "序号",  "属性", "描述","ID" });
//			for (int i = 1; r.next(); i++) {
//				konntenntu[0] = r.getString("PROPERTY"); 
//				konntenntu[1] = r.getString("VALUE");
//				konntenntu[2] = r.getString("ID");
//				table.addRow(new Object[] { "" + i, konntenntu[0],
//						konntenntu[1],konntenntu[2] });
//			}
//
//			r.close();
			

			
			for (int i = 0; i < 20; i++) {
				table.addRow(new Object[] {});
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(sql);
			println(sql);
			setConsoleOutIsRight();
			// TODO: handle exception
		}
	}

	/**
	 * 权限组
	 */
	public void kyokaGurupu(Statement st, String appName, String tableName) throws SQLException{
		sql = "SELECT A.*,B.DESCRIPTION,NVL((SELECT MAXGROUP.DESCRIPTION FROM " +
				"MAXGROUP WHERE MAXGROUP.GROUPNAME=a.GROUPNAME)," +
				"GROUPNAME) MAXGROUPDESCRIPTION FROM APPLICATIONAUTH A " +
				" LEFT OUTER JOIN SIGOPTION B ON (A.APP=B.APP AND " +
				"A.OPTIONNAME=B.OPTIONNAME) WHERE A.APP='"+appName+"' " +
						" and a.GROUPNAME<>'MAXADMIN' ORDER BY GROUPNAME";
		r = st.executeQuery(sql);
		println("\n\n 权限组");
		println(sql); 
		println();
		table.addRow(new Object[] {});
		table.addRow(new Object[] {});
		table.addRow(new Object[] { "权限组" });
		table.addRow(new Object[] { "序号", "职位或角色", "权限"});
		for (int i = 1; r.next(); i++) {
			konntenntu[0] = r.getString("MAXGROUPDESCRIPTION"); 
			konntenntu[1] = r.getString("DESCRIPTION");
			konntenntu[2] = r.getString("OPTIONNAME");
			table.addRow(new Object[] { "" + i, konntenntu[0],
					konntenntu[1] });
		}

		r.close();
	}
	
	/**
	 * 对应的域
	 */
	public void taiouSuruDomeinn(Statement st, String appName, String tableName) throws SQLException{
		sql="SELECT MAXDOMAIN.* FROM MAXDOMAIN " +
				"LEFT OUTER JOIN MAXATTRIBUTECFG ON " +
				"(MAXATTRIBUTECFG.OBJECTNAME='"+tableName+"' AND" +
				" MAXATTRIBUTECFG.DOMAINID IS NOT NULL ) " +
				"WHERE MAXDOMAIN.DOMAINID=MAXATTRIBUTECFG.DOMAINID ";
		r = st.executeQuery(sql);
//		System.out.println(sql);
		table.addRow(new Object[] {});
		table.addRow(new Object[] {});
		table.addRow(new Object[] { "Action类" });
		table.addRow(new Object[] { "序号",  "域值","描述","域类型", "说明" });
		int i=0;
		while (r.next()) {
			String domaindomainid=r.getString("DOMAINID");
			String domaindescription=r.getString("DESCRIPTION");
			String domaindomaintype=r.getString("DOMAINTYPE");
			String domainmaxtype=r.getString("MAXTYPE");
			String domainlength=r.getString("LENGTH");
			i++;
			table.addRow(new Object[] {  ""+i,domaindomainid,
					domaindescription,domaindomaintype, domainmaxtype+"("+domainlength+")"});
		}
	}
	
	/**
	 * Action类
	 * @param appName 
	 * @param st 
	 * @throws SQLException 
	 * @throws ArrayIndexOutOfBoundsException 
	 */
	public void actionClass(Statement st, String appName, String tableName) throws SQLException{
		sql = "select DISTINCT a.*,b.PROCESSNAME from ACTION a, " +
				"(SELECT WFACTION.* FROM WFACTION " +
				"LEFT OUTER JOIN WFPROCESS on (WFPROCESS.OBJECTNAME  ='"+appName+"')" +
				" WHERE WFACTION.PROCESSNAME=WFPROCESS.PROCESSNAME) b where " +
				"(a.ACTION=b.ACTION) and a.ACTION='定制' order by a.actionid";
		r = st.executeQuery(sql);
		println("\n\nAction类");
		println(sql); 
		table.addRow(new Object[] {});
		table.addRow(new Object[] {});
		table.addRow(new Object[] { "Action类" });
		table.addRow(new Object[] { "序号",  "类名","描述","操作名称", "备注" });
		for (int i = 1; r.next(); i++) {
			konntenntu[0] = r.getString("VALUE"); 
			konntenntu[1] = r.getString("ACTION"); 
			konntenntu[2] = r.getString("DESCRIPTION"); 
			System.out.println(konntenntu[0]);
			table.addRow(new Object[] { "" + i, konntenntu[0],
					konntenntu[1],konntenntu[2] });
		}

		r.close();
	}
	
	/**
	 * 工作流程
	 * @param st 
	 * @param appName 
	 * @param tableName 
	 * @throws SQLException 
	 */
	public void sigotoPurosesu(Statement st, String appName, String tableName) throws SQLException{
		/**
		 * 工作流程列表
		 */
		sql = "SELECT * FROM WFPROCESS WHERE OBJECTNAME='"+appName+"'";
		r = st.executeQuery(sql);
		println("\n\n工作流程列表");
		println(sql); 
		table.addRow(new Object[] {});
		table.addRow(new Object[] {});
		table.addRow(new Object[] { "工作流程列表" });
		table.addRow(new Object[] { "序号",  "工作流程名称", "备注" });
		for (int i = 1; r.next(); i++) {
			konntenntu[0] = r.getString("PROCESSNAME"); 
			konntenntu[1] = r.getString("DESCRIPTION"); 
			table.addRow(new Object[] { "" + i, konntenntu[0],
					konntenntu[1] });
		}

		r.close();
		
		/**
		 * 工作流程状态
		 */
		sql = "select DISTINCT a.*,b.PROCESSNAME from ACTION a, " +
				"(SELECT WFACTION.* FROM WFACTION " +
				"LEFT OUTER JOIN WFPROCESS on (WFPROCESS.OBJECTNAME  ='"+appName+"')" +
				" WHERE WFACTION.PROCESSNAME=WFPROCESS.PROCESSNAME) b where " +
				"(a.ACTION=b.ACTION) order by a.actionid";
		r = st.executeQuery(sql);
		println("\n\n工作流程状态");
		println(sql); 
		table.addRow(new Object[] {});
		table.addRow(new Object[] {});
		table.addRow(new Object[] { "工作流程状态" });
		table.addRow(new Object[] { "序号",  "名称","状态名称", "所在流程名称" });
		for (int i = 1; r.next(); i++) {
			konntenntu[0] = r.getString("ACTION"); 
			konntenntu[1] = r.getString("VALUE2"); 
			konntenntu[2] = r.getString("PROCESSNAME"); 
			table.addRow(new Object[] { "" + i, konntenntu[0],
					konntenntu[1],konntenntu[2] });
		}

		r.close();
		


		/**
		 * 工作流程节点
		 */
		sql = "SELECT (SELECT CONDITION FROM WFCONDITION C " +
				"WHERE A.PROCESSNAME=C.PROCESSNAME AND" +
				" A.NODEID=C.NODEID) CONDITION," +
				"A.* FROM WFNODE A LEFT OUTER JOIN WFPROCESS B ON " +
				"(B.OBJECTNAME='"+appName+"') WHERE A.PROCESSNAME=B.PROCESSNAME " +
						"ORDER BY A.PROCESSNAME";
		r = st.executeQuery(sql);
		println("\n\n工作流程节点");
		println(sql); 
		table.addRow(new Object[] {});
		table.addRow(new Object[] {});
		table.addRow(new Object[] { "工作流程节点" });
		table.addRow(new Object[] { "序号",  "节点名称", "节点类型" , "角色名称", "Where条件", "正向", "反向", "状态","流程名称"});
		for (int i = 1; r.next(); i++) {

			Statement st01 = con.createStatement();

			String processname=r.getString("PROCESSNAME");//流程名
			String nodeid=r.getString("NODEID");//流程节点编号
			konntenntu[0] = r.getString("TITLE"); 
			konntenntu[1] = r.getString("NODETYPE"); 
			konntenntu[3] = r.getString("CONDITION"); 
			
			/**
			 * 正向节点,反向节点,状态
			 */
			sql=" SELECT (select WFNODE.TITLE from WFNODE " +
					"where WFNODE.PROCESSNAME=WFACTION.PROCESSNAME and" +
					"  WFACTION.membernodeid=WFNODE.nodeid ) TITLE," +
					"(select ACTION.description from ACTION where" +
					" ACTION.OBJECTNAME='"+tableName+"' and" +
					" WFACTION.ACTION=ACTION.ACTION and ACTION.TYPE='设置值') status," +
					"WFACTION.* FROM WFACTION " +
					"WHERE  WFACTION.OWNERNODEID='"+nodeid +"' AND " +
					" WFACTION.PROCESSNAME='"+processname+"'";
			println("正向节点,反向节点,状态"); 
			println(sql); 
			ResultSet rs = st01.executeQuery(sql);
			 konntenntu[4] = "";
			 konntenntu[5] = "";
			 konntenntu[6] = "";
			while(rs.next()){
				if(rs.getInt("ISPOSITIVE")==1){
					//正向节点名称
					konntenntu[4] = rs.getString("TITLE"); 
					konntenntu[6] = rs.getString("status"); 
				}else{
					//反向节点名称
					konntenntu[5] = rs.getString("TITLE"); 
				}
			}
			
			
			sql = " SELECT (SELECT DESCRIPTION FROM  MAXROLE WHERE" +
					" MAXROLE=WFASSIGNMENT.ROLEID) DESCRIPTIONS,WFASSIGNMENT.* FROM WFASSIGNMENT "
			+" WHERE NODEID = '"
					+ nodeid + "' AND PROCESSNAME='"+processname+"'" +
							" AND ASSIGNSTATUS='缺值' AND APP='"+appName+"'";
			 rs = st01.executeQuery(sql);
			 konntenntu[2] = "";
			if(rs.next()){
				//角色名称
				konntenntu[2] = rs.getString("DESCRIPTIONS"); 

			}
			println("角色名称"); 
			println(sql); 
			table.addRow(new Object[] { "" + i, konntenntu[0],
					konntenntu[1],konntenntu[2],konntenntu[3],konntenntu[4],
					konntenntu[5],konntenntu[6],processname });
			while(rs.next()){
				konntenntu = new String[100];
					//角色名称
					konntenntu[2] = rs.getString("DESCRIPTIONS"); 
					table.addRow(new Object[] { "" + i, konntenntu[0],
							konntenntu[1],konntenntu[2],konntenntu[3],konntenntu[4],
							konntenntu[5],konntenntu[6],processname });
			}
			st01.close();
			rs.close();
		}

		r.close();
	}
	
	/**
	 * Fld类:主对象中各字段的关联类
	 */
	public void fldClass(Statement st,String appName,String tableName) throws SQLException{
		sql = "SELECT * FROM MAXATTRIBUTECFG WHERE CLASSNAME IS NOT NULL AND OBJECTNAME='"+appName+"'";
		r = st.executeQuery(sql);
		println("\n\nFld类:主对象中各字段的关联类");
		println(sql); 
		table.addRow(new Object[] {});
		table.addRow(new Object[] {});
		table.addRow(new Object[] { "Fld类:主对象中各字段的关联类" });
		table.addRow(new Object[] { "序号",  "类名", "描述","字段标识","备注" });
		for (int i = 1; r.next(); i++) {
			konntenntu[0] = r.getString("CLASSNAME"); 
			konntenntu[1] = r.getString("TITLE"); 
			konntenntu[2] = r.getString("ATTRIBUTENAME"); 
			konntenntu[3] = r.getString("REMARKS");
			table.addRow(new Object[] { "" + i, konntenntu[0],
					konntenntu[1],konntenntu[2],konntenntu[3] });
		}

		r.close();
	}
	
	/**
	 * Mbo类 数据库绑定类
	 */
	public void mboDetabesuClass(Statement st,String appName,String tableName) throws SQLException{
		sql = "SELECT * FROM MAXOBJECTCFG WHERE OBJECTNAME='"+appName+"'";
		r = st.executeQuery(sql);
		println("\n\nMbo类 数据库绑定类");
		println(sql); 
		table.addRow(new Object[] {});
		table.addRow(new Object[] {});
		table.addRow(new Object[] { "Mbo类 数据库绑定类" });
		table.addRow(new Object[] { "序号",  "类名", "描述","备注" });
		for (int i = 1; r.next(); i++) {
			konntenntu[0] = r.getString("CLASSNAME"); 
			konntenntu[1] = r.getString("DESCRIPTION");
			table.addRow(new Object[] { "" + i, konntenntu[0],
					konntenntu[1] });
		}

		r.close();
	}
	/**
	 * 搜索菜单设计
	 */
	public void kennsakuMenyuSekkei(Statement st,String appName,String tableName) throws SQLException{
		sql = "SELECT *  FROM MAXMENU WHERE MODULEAPP='" + appName
				+ "' AND MENUTYPE !='MODULE' AND MENUTYPE='SEARCHMENU'";
		r = st.executeQuery(sql);
		println("\n\n搜索菜单设计");
		println(sql);
		println();
		table.addRow(new Object[] {});
		table.addRow(new Object[] {});
		table.addRow(new Object[] { "搜索菜单设计" });
		table.addRow(new Object[] { "序号", "元素类型", "键值", "标题描述", "职位",
				"下级职位", "URL", "图像", "可见", "访问键", "选项卡" });
		for (int i = 1; r.next(); i++) {
			konntenntu[0] = r.getString("ELEMENTTYPE");
			konntenntu[1] = r.getString("KEYVALUE");
			konntenntu[2] = r.getString("HEADERDESCRIPTION");
			konntenntu[3] = r.getString("POSITION");
			konntenntu[4] = r.getString("SUBPOSITION");
			konntenntu[5] = r.getString("URL");
			konntenntu[6] = r.getString("IMAGE");
			if (r.getBoolean("VISIBLE")) {
				konntenntu[7] = "Y";
			} else {
				konntenntu[7] = "N";
			}
			konntenntu[8] = r.getString("ACCESSKEY");
			konntenntu[9] = r.getString("TABDISPLAY");
			table.addRow(new Object[] { "" + i, konntenntu[0],
					konntenntu[1], konntenntu[2], konntenntu[3],
					konntenntu[4], konntenntu[5], konntenntu[6],
					konntenntu[7], konntenntu[8], konntenntu[9] });
		}

		r.close();
	}
	/**
	 * 工具栏设计
	 */
	public void turubaDezainn(Statement st,String appName,String tableName) throws SQLException{
		sql = "SELECT *  FROM MAXMENU WHERE MODULEAPP='" + appName
				+ "' AND MENUTYPE !='MODULE' AND MENUTYPE='APPTOOL'";
		r = st.executeQuery(sql);
		println("\n\n工具栏设计");
		println(sql);
		println();
		table.addRow(new Object[] {});
		table.addRow(new Object[] {});
		table.addRow(new Object[] { "工具栏设计" });
		table.addRow(new Object[] { "序号", "元素类型", "键值", "标题描述", "职位",
				"下级职位", "URL", "图像", "可见", "访问键", "选项卡" });
		for (int i = 1; r.next(); i++) {
			konntenntu[0] = r.getString("ELEMENTTYPE");
			konntenntu[1] = r.getString("KEYVALUE");
			konntenntu[2] = r.getString("HEADERDESCRIPTION");
			konntenntu[3] = r.getString("POSITION");
			konntenntu[4] = r.getString("SUBPOSITION");
			konntenntu[5] = r.getString("URL");
			konntenntu[6] = r.getString("IMAGE");
			if (r.getBoolean("VISIBLE")) {
				konntenntu[7] = "Y";
			} else {
				konntenntu[7] = "N";
			}
			konntenntu[8] = r.getString("ACCESSKEY");
			konntenntu[9] = r.getString("TABDISPLAY");
			table.addRow(new Object[] { "" + i, konntenntu[0],
					konntenntu[1], konntenntu[2], konntenntu[3],
					konntenntu[4], konntenntu[5], konntenntu[6],
					konntenntu[7], konntenntu[8], konntenntu[9] });
		}
		r.close();
	}
	/**
	 * 选择操作设计
	 */
	public void senntakuSousaDezainn(Statement st,String appName,String tableName) throws SQLException{
		sql = "SELECT *  FROM MAXMENU WHERE MODULEAPP='" + appName
				+ "' AND MENUTYPE !='MODULE' AND MENUTYPE='APPMENU'";
		r = st.executeQuery(sql);
		println("\n\n选择操作设计");
		println(sql);
		println();
		table.addRow(new Object[] {});
		table.addRow(new Object[] {});
		table.addRow(new Object[] { "选择操作设计" });
		table.addRow(new Object[] { "序号", "元素类型", "键值", "标题描述", "职位",
				"下级职位", "URL", "图像", "可见", "选项卡" });
		for (int i = 1; r.next(); i++) {
			konntenntu[0] = r.getString("ELEMENTTYPE");
			konntenntu[1] = r.getString("KEYVALUE");
			konntenntu[2] = r.getString("HEADERDESCRIPTION");
			konntenntu[3] = r.getString("POSITION");
			konntenntu[4] = r.getString("SUBPOSITION");
			konntenntu[5] = r.getString("URL");
			konntenntu[6] = r.getString("IMAGE");
			if (r.getBoolean("VISIBLE")) {
				konntenntu[7] = "Y";
			} else {
				konntenntu[7] = "N";
			}
			konntenntu[8] = r.getString("TABDISPLAY");
			table.addRow(new Object[] { "" + i, konntenntu[0],
					konntenntu[1], konntenntu[2], konntenntu[3],
					konntenntu[4], konntenntu[5], konntenntu[6],
					konntenntu[7], konntenntu[8] });

		}
		r.close();
	}

	/**
	 *  签名选项设计
	 */
	public void shomeioOpushonnDezainn(Statement st,String appName,String tableName) throws SQLException{
		sql = "select *  from sigoption where app='" + appName + "'";
		r = st.executeQuery(sql);
		println("\n\n 签名选项设计");
		println(sql);
		println();
		table.addRow(new Object[] {});
		table.addRow(new Object[] {});
		table.addRow(new Object[] { "签名选项设计" });
		table.addRow(new Object[] { "序号", "选项", "描述", "选项也授予", "选项也撤销",
				"先决条件", "电子签名已启用？", "可见" });
		for (int i = 1; r.next(); i++) {
			konntenntu[0] = r.getString("OPTIONNAME");
			konntenntu[1] = r.getString("DESCRIPTION");
			konntenntu[2] = r.getString("ALSOGRANTS");
			konntenntu[3] = r.getString("ALSOREVOKES");
			konntenntu[4] = r.getString("PREREQUISITE");
			if (r.getBoolean("ESIGENABLED")) {
				konntenntu[5] = "Y";
			} else {
				konntenntu[5] = "N";
			}
			if (r.getBoolean("VISIBLE")) {
				konntenntu[6] = "Y";
			} else {
				konntenntu[6] = "N";
			}
			table.addRow(new Object[] { "" + i, konntenntu[0],
					konntenntu[1], konntenntu[2], konntenntu[3],
					konntenntu[4], konntenntu[5], konntenntu[6] });
		}
		r.close();
		
	}
	
	/**
	 * 各元素的详细属性
	 */
	public void yousoNoShousaiNaPuropathi(Statement st,String appName,String tableName) throws SQLException{
		sql = "select B.DATA_TYPE,B.DATA_LENGTH,a.* "
				+ "from MAXATTRIBUTECFG a ,USER_TAB_COLUMNS b"
				+ " where  a.ATTRIBUTENAME=b.COLUMN_NAME(+) and"
				+ " a.OBJECTNAME=B.TABLE_NAME(+) and " + "a.OBJECTNAME='"
				+ tableName + "'";
		r = st.executeQuery(sql);
		println("\n\n各元素的详细属性");
		println(sql);
		println();
		table.addRow(new Object[] {});
		table.addRow(new Object[] {});
		table.addRow(new Object[] { "各元素的详细属性" });
		table.addRow(new Object[] { "序号", //"元素名称",
				"字段名称", "对象名称", "ibm类型",
				//"oracle类型", 
				"数据来源", "必填", "备注" });
		for (int i = 1; r.next(); i++) {
			konntenntu[0] = r.getString("TITLE");//TITLE REMARKS
			konntenntu[1] = r.getString("ATTRIBUTENAME");
			konntenntu[2] = r.getString("MAXTYPE") + "("
					+ r.getString("LENGTH") + ")";
			konntenntu[3] = r.getString("DATA_TYPE") + "("
					+ r.getString("DATA_LENGTH") + ")";
			if (r.getBoolean("REQUIRED")) {
				konntenntu[4] = "Y";
			} else {
				konntenntu[4] = "N";
			}
			konntenntu[5] = r.getString("REMARKS");

			table.addRow(new Object[] { "" + i,// konntenntu[0],
					konntenntu[1], tableName, konntenntu[2], //konntenntu[3],
					"", konntenntu[4], konntenntu[5] });
		}
		r.close();
	}
	
	/**
	 * 触发器,因关系复杂性原因,只能提供本表的
	 */
	public void toriga(Statement st,String appName,String tableName) throws SQLException{
		sql = "SELECT * FROM USER_TRIGGERS where table_name='" + tableName
				+ "'";
		r = st.executeQuery(sql);
		println("\n\n触发器,因关系复杂性原因,只能提供本表的");
		println(sql);
		println();
		table.addRow(new Object[] {});
		table.addRow(new Object[] {});
		table.addRow(new Object[] { "触发器,因关系复杂性原因,只能提供本表的" });
		table
				.addRow(new Object[] { "序号", "触发器名称", "用于数据库表", "功能描述",
						"备注" });

		for (int i = 1; r.next(); i++) {
			konntenntu[0] = r.getString("TRIGGER_NAME");
			table.addRow(new Object[] { "" + i, konntenntu[0], tableName });
		}
		r.close();
	}

	
	/**
	 * 应用中对应的表
	 * 
	 * @throws SQLException
	 */
	public String appTaiouNoHyou(Statement st, String appName)
			throws SQLException {
		String tableName = "";
		sql = " select * from maxapps " + " where app like " + "'" + appName
				+ "'";
		r = st.executeQuery(sql);
		table.addRow(new Object[] { "应用中对应的表" });
		table.addRow(new Object[] { "应用名", "所属表对象", "菜单", "二级菜单" });

		println("\n\n应用中对应的表");
		println(sql);
		if (r.next()) {
			konntenntu[0] = r.getString("description");
			konntenntu[1] = r.getString("maintbname");
			tableName = konntenntu[1].toUpperCase();
		}
		r.close();

		/**
		 * 所属菜单
		 */
		sql = "SELECT MAXMODULES.*  FROM MAXMENU,MAXMODULES "
				+ "WHERE MAXMENU.KEYVALUE = '" + appName + "' AND "
				+ "MAXMENU.ELEMENTTYPE='APP' AND "
				+ "MAXMENU.MENUTYPE='MODULE' AND "
				+ "MAXMENU.MODULEAPP=MAXMODULES.MODULE";
		r = st.executeQuery(sql);
		println("\n\n所属菜单");
		println(sql);
		if (r.next()) {
			konntenntu[2] = r.getString("description") + "("
					+ r.getString("module") + ")";
		}
		r.close();

		/**
		 * 二级菜单
		 */
		sql = "SELECT A.*  FROM MAXMENU A WHERE "
				+ "A.ELEMENTTYPE='HEADER' AND "
				+ "EXISTS (SELECT B.*  FROM MAXMENU B WHERE " + "KEYVALUE = '"
				+ appName + "' AND " + "A.POSITION=B.POSITION AND"
				+ " A.MAXMENUID<>B.MAXMENUID )";
		r = st.executeQuery(sql);
		println("\n\n二级菜单");
		println(sql);
		println();
		if (r.next()) {
			konntenntu[3] = r.getString("headerdescription") + "("
					+ r.getString("KEYVALUE") + ")";
		}
		table.addRow(new Object[] { konntenntu[0], konntenntu[1],
				konntenntu[2], konntenntu[3] });

		r.close();

		return tableName;
	}
	
	public void printClob(Connection con,String tableName,String appName){
		OracleClob jc = new OracleClob(con, "maxpresentation",
				"app", "'"+appName+"'", "presentation", "<font>你好</font>");
		Vector<String> databean=new Vector<String>();
		try {
			String s=jc.read();
			String[] st1 = s.split("<",-1);//分割符为\n
			int lookupBanngo=0;
			int whereBanngo=0;
			
			for (int i = 0; i < st1.length; i++) {
				String s2="";
				st1[i]=st1[i].trim();
				String raberu=getHeadString(st1[i]);//该行的类型标签

				s2="relationship";//关系
				String relationship=getNakaString(st1[i],s2);
				s2="lookup";//查找
				String lookup=getNakaString(st1[i],s2);
				s2="id";
				String id=getNakaString(st1[i],s2);
				s2="label";//标签
				String label=getNakaString(st1[i],s2);
				s2="beanclass";
				String beanclass=getNakaString(st1[i],s2);
				s2="dataattribute";//属性
				String dataattribute=getNakaString(st1[i],s2);
				s2="apprestrictions";//应用程序限制
				String apprestrictions=getNakaString(st1[i],s2);
				s2="whereclause";//应用where子句
				String whereclause=getNakaString(st1[i],s2);
				/**
				 * lookup
				 */
				if(!lookup.isEmpty()){
					lookupBanngo++;
					String s3="";
					String label2=label;
					if(label2.isEmpty()){
						Statement st = con.createStatement();
						sql="select * from MAXATTRIBUTECFG where " +
							" objectname='"+tableName+"' and" +
							" upper(attributename)=upper('"+dataattribute+"')";
						ResultSet r = st.executeQuery(sql);
						if (r.next()) {
							label2=r.getString("TITLE");
						}
					}
					if(lookup.equalsIgnoreCase("DATELOOKUP")){
						s3="选择时间";
					}else if(lookup.equalsIgnoreCase("PERSON")){
						s3="选择人员";
					}else if(lookup.equalsIgnoreCase("VALUELIST")){
						s3="选择域";
					}

//					table.addRow(new Object[] { "序号", "Lookups标识", "描述", "备注" });
//					table.addRow(new Object[]{lookupBanngo,lookup,label2,s3});
//					System.out.println(lookupBanngo+"\t"+lookup+"\t"+label2+"\t"+s3);
				}
				/** 应用限制与的默认where
				 * where and apprestrictions
				 */
				if((!apprestrictions.isEmpty())&&(raberu.equalsIgnoreCase("table")
						||raberu.equalsIgnoreCase("presentation"))){
					whereBanngo++;
//						System.out.println(whereBanngo+"\t"+label+"\t"+apprestrictions+"\t"+"\t"+"应用限制");
				}
				if((!whereclause.isEmpty())&&(raberu.equalsIgnoreCase("table")
						||raberu.equalsIgnoreCase("presentation"))){
					whereBanngo++;
//						System.out.println(whereBanngo+"\t"+label+"\t"+whereclause+"\t"+"\t"+"where限制");
				}
				/**
				 * 关系对应where
				 */
				if((!relationship.isEmpty())&&(raberu.equalsIgnoreCase("table")||
						raberu.equalsIgnoreCase("dialog"))){
					whereBanngo++;
					String label2=label;
					if(label2.isEmpty()){
						Statement st = con.createStatement();
						sql="select * from MAXATTRIBUTECFG where " +
							" objectname='"+tableName+"' and" +
							" upper(attributename)=upper('"+dataattribute+"')";
						ResultSet r = st.executeQuery(sql);
						if (r.next()) {
							label2=r.getString("TITLE");
						}
					}
					Statement st = con.createStatement();
					sql="SELECT * FROM MAXRELATIONSHIP WHERE " +
						" PARENT='"+tableName+"' AND" +
						" UPPER(NAME)=UPPER('"+relationship+"')";
					ResultSet r = st.executeQuery(sql);
//					System.out.println(sql);
					if (r.next()) {
						String konntenntu=r.getString("WHERECLAUSE");
						if(konntenntu!=null){
							konntenntu=konntenntu.replace("\r", " ").replace("\n", " ");
						}else{
							konntenntu="";
						}
//						System.out.println(whereBanngo+"\t"+label+"\t"+konntenntu+"\t"+"\t"+"关系限制"+relationship);
					}
				}
				
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(sql);
			println(sql);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(sql);
			println(sql);
		}
	}
	/**
	 * 返回该组件名称
	 * @param age 
	 * @param fromIndex
	 * @return 
	 */
	public String getHeadString(String age){	
		String s=age;
		if(age==null||age.isEmpty()){
			return "";
		}
		int j=s.indexOf(" ");
		if(j>-1){
			s=s.substring(0,j);
		}else{
			return "";
		}
		return s;
	}
	/**
	 * 返回fromIndex="toIndex"中toIndex的值
	 * @param age 
	 * @param fromIndex
	 * @return 
	 */
	public String getNakaString(String age,String fromIndex){	
		String s=age;
		if(age==null||age.isEmpty()){
			return "";
		}
		int j=s.indexOf(" "+fromIndex);
		if(j>-1){
			s=s.substring(j,s.length());
			s=s.replace(" ", "");
			String fIndex=fromIndex+"=\"";
			j=s.indexOf(fIndex);
			if (j>-1) {
				int j2=s.indexOf("\"",j+fIndex.length());
				if(j>-1){
					s=s.substring(j+fIndex.length(),j2 );
				}else{
					return "";
				}
			}else{
				return "";
			}
		}else{
			return "";
		}
		return s;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ListApurikeshonnKannkei lak = new ListApurikeshonnKannkei();
		lak.setDefuorutoConnection();
	}
	public void setDefuorutoConnection(){
		// TODO Auto-generated method stub
		String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
		 String driver = "oracle.jdbc.driver.OracleDriver";
		String user = "maximo75";
		String password = "maximo75";					
		
//		url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=10.59.67.6)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=10.59.67.7)(PORT=1521)))(CONNECT_DATA=(SERVER=dedicated)(SERVICE_NAME=eam)))";
//		driver = "oracle.jdbc.driver.OracleDriver";
//		user = "maximo";
//		password = "maximo";
		 con = null;
		try {
			// （1）装载并注册数据库的JDBC驱动程序
			// 载入JDBC驱动：oracle安装目录下的jdbc\lib\classes12.jar
			println("正在试图加载驱动程序 " + driver);
			Class.forName(driver);
			println("驱动程序已加载");
			// 注册JDBC驱动：有些地方可不用此句
			println("url=" + url, true);
			println("user=" + user, true);
			println("password=" + password, true);
			println("正在试图连接数据库--------");
			java.sql.DriverManager
					.registerDriver(new oracle.jdbc.driver.OracleDriver());

			con = DriverManager.getConnection(url, user, password);
			println("OK,成功连接到数据库");
			printlnSeikou();
			setConsoleOutIsRight();
		} catch (Exception ex) {
			ex.printStackTrace();
			printlnSippai();
			println();
			setConsoleOutIsRight();
		}
	}
	public void setConnection(Connection con) {
		this.con = con;
	}

	public void setConnection(ConnectionKonnfigu ck) throws BadLocationException{
		try {
			 this.url=ck.getUrl();
			 this.driver=ck.getDriver();
			 this.user=ck.getUser();
			 this.password=ck.getPassword();
			// （1）装载并注册数据库的JDBC驱动程序
			// 载入JDBC驱动：oracle安装目录下的jdbc\lib\classes12.jar
			println("正在试图加载驱动程序 " + driver);
			Class.forName(driver);
			println("驱动程序已加载");
			// 注册JDBC驱动：有些地方可不用此句
			println("url=" + url);
			println("user=" + user);
			println("password=" + password);
			println("正在试图连接数据库--------");
			java.sql.DriverManager
					.registerDriver(new oracle.jdbc.driver.OracleDriver());

			con = DriverManager.getConnection(url, user, password);
			println("OK,成功连接到数据库");

			printConnection();
		} catch (Exception ex) {
			ex.printStackTrace();
			println(ex.getMessage(), true);
		}
	}
	
	public JFrame getConsoleOut() {

		return JF_ConsoleOut;
	}

	public void init() {
		this.addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentResized(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				JF_ConsoleOut.setBounds(getWidth() + getX(), getY(), 500,
						getHeight());
			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 输出SQL命令成功信息
	 * 
	 * @throws BadLocationException
	 */
	public void printlnSeikou() {
		JF_ConsoleOut.println("------------成功执行更新完毕!-----------");
	}

	/**
	 * 输出SQL命令失败信息
	 * 
	 * @throws BadLocationException
	 */
	public void printlnSippai() {
		JF_ConsoleOut.println("------------更新错误??????-----------", true);
		JF_ConsoleOut.setFont(new Font("宋体 ", Font.BOLD, 13));
	}

	public void println() {
		JF_ConsoleOut.println("");
	}

	public void println(int age) {
		JF_ConsoleOut.println("" + age);
	}

	public void println(String age) {
		JF_ConsoleOut.println(age, false);
	}

	public void println(String age, boolean b) {
		JF_ConsoleOut.println(age, b);
	}

	/**
	 * 让信息窗口显示在右边
	 */
	public void setConsoleOutIsRight() {
		// JF_ConsoleOut.setBounds(getWidth()+getX(), getY(),500, getHeight());
		JF_ConsoleOut.setVisible(true);
		toggleButton.setSelected(true);
	}

	public void printConnection() throws BadLocationException{
		println("url=" + url, true);
		println("driver=" + driver, true);
		println("user=" + user, true);
		println("password=" + password, true);
	}
}


