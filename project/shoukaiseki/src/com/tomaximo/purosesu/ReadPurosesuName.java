package com.tomaximo.purosesu;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.shoukaiseki.gui.jinternalframe.MCOCInternalFrameK;
import com.shoukaiseki.gui.jtable.ExcelAdapter;
import com.shoukaiseki.gui.jtable.JTableOperating;
import com.shoukaiseki.gui.jtable.JTableRowHeader;
import com.shoukaiseki.gui.jtextfield.AutoCompletionField;
import com.shoukaiseki.gui.jtextfield.DefaultCompletionFilter;
import com.shoukaiseki.gui.jtextpane.JTextPaneDoc;
import com.shoukaiseki.sql.ConnectionKonnfigu;
import com.shoukaiseki.string.ConCatLineBreaks;
import com.tomaximo.tuuyouclass.AllMaximoKonnfigu;
import com.tomaximo.tuuyouclass.Konnfigu;
import com.tomaximo.tuuyouclass.KonntenntuOut;
import com.tomaximo.tuuyouclass.KonntenntuOutToJInternalFrame;



public class ReadPurosesuName extends MCOCInternalFrameK {
	private String defuorutobTextField="OPTSDLYD_GZ";
	/**
	 * 关闭時是否退出程序,默认時退出,只作为子程序时不退出
	 */
	private JFrame  frame=null;
	private boolean printsql=false;
	private boolean windowclose = true;

	private boolean buttonstatus = true;
	/**
	 * JTableOperating默认提供JScrollPane,拥有行号
	 */
	private JTableOperating table;
	private AutoCompletionField bTextField;//自动补全JTextField
	private JComboBox comboBox;//过程修订
	private KonntenntuOutToJInternalFrame JF_ConsoleOut;

	private JToggleButton toggleButton;
	private JCheckBox domeinnTrim,betubetunihyouji; 

	private String[] konntenntu;
	private ResultSet r ;
	

	
	private AllMaximoKonnfigu amk=new AllMaximoKonnfigu();
	private ConCatLineBreaks saveAction=new ConCatLineBreaks();
	private ConCatLineBreaks saveActionGrup=new ConCatLineBreaks();

	private Map<String,Konnfigu> actionKonnfigu=new HashMap();
	private Map<String,Konnfigu> actionGrupKonnfigu=new HashMap();
	private String actionMap;
	private String actionGrupMap;
	
	private ConCatLineBreaks savesql=new ConCatLineBreaks();
	private String sql = "";
	private Connection con = null;
	private String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String user = "orclzhjq";
	private String password = "orclzhjq";
	
	public ReadPurosesuName() {
		this(null, true);
	}

	public ReadPurosesuName(boolean windowclose) {
		this(null, windowclose);
	}

	public ReadPurosesuName(Connection con) {
		this(con, true);
	}

	public ReadPurosesuName(Connection con, boolean windowclose){
			JF_ConsoleOut = new KonntenntuOutToJInternalFrame();
//			JF_ConsoleOut.setVisible(false);//使用后报错

		this.con = con;
//		String[] columnNames = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
//				"J", "K", "L" }; // 列名
		String[] columnNames = { "A", "B"}; // 列名

		columnNames= new String[]{ "域名(DOMAINID)", "值(VALUE)", "描述(DESCRIPTION)",
				"地点(SITEID)", "组织(ORGID)", "编号(NUM)" ,"G","H","I","J"};
		
		table = new JTableOperating(new DefaultTableModel(columnNames, 55));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		ExcelAdapter myAd = new ExcelAdapter(table);
		// table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //单选

		domeinnTrim = new JCheckBox("域名字首尾空",true);
		betubetunihyouji=new JCheckBox("分开显示");
		
		/**
		 * 显示行号
		 */
        table.init();
        
        
		Box panel = Box.createHorizontalBox(); // 横结构
		panel.add(new JLabel("流程名: "));
		panel.add(Box.createHorizontalStrut(8));// 间距
		bTextField = new AutoCompletionField(defuorutobTextField, 30);
		panel.add(bTextField);
		panel.add(Box.createHorizontalStrut(8));// 间距
		panel.add(new JLabel("过程修订: "));
		panel.add(Box.createHorizontalStrut(8));// 间距
		comboBox = new JComboBox();
		panel.add(comboBox);
		panel.add(Box.createHorizontalStrut(8));// 间距
		panel.add(betubetunihyouji);
		panel.add(Box.createHorizontalStrut(8));// 间距
		panel.add(domeinnTrim);
		panel.add(Box.createHorizontalStrut(8));// 间距
		Document   doc   =   bTextField.getDocument();   
		doc.addDocumentListener(new  javax.swing.event.DocumentListener(){
            
            public void changedUpdate(DocumentEvent documentEvent)   {
           }

           public void insertUpdate(DocumentEvent documentEvent)   {
        	   setComboBox();
           }

           public void removeUpdate(DocumentEvent documentEvent)   {
        	   setComboBox();
           }
                       
});

		final JButton updateButton = new JButton("実行"); // 修改按钮
		updateButton.addActionListener(new ActionListener() {// 添加事件
					public void actionPerformed(ActionEvent e) {
						new Timer().schedule(new TimerTask() {
							@Override
							public void run() {// 实例中的的方法
								listPurosesu();
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
		this.add(table.getJScrollPane(), BorderLayout.CENTER);
		this.add(panel, BorderLayout.SOUTH);
		this.setSize(700, 500);
//		this.setLocation(200, 150);
		this.setVisible(true);
		if (windowclose) {
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		}
		init();
//		JInternalFrame(String title, boolean resizable, boolean closable, 
		//boolean maximizable, boolean iconifiable) 
//        创建具有指定标题、可调整、可关闭、可最大化和可图标化的 JInternalFrame。
		this.setTitle("流程信息");
		this.setResizable(true);
//		this.setClosable(true);
//		this.setMaximizable(true);
//		this.setIconifiable(true);
//		this.pack();
		
	}

	/**
     * 
     */
	public void listPurosesu() {
		String[] domeinnNamae = bTextField.getText().toUpperCase().split(",");
		 konntenntu = new String[10];
		try {
			savesql.setContent("");
			Statement st = con.createStatement();
			String tableName="";
			String processName=bTextField.getText();//流程名称
			Object processrevName=comboBox.getSelectedItem();//修订名称
			
			sql="select  * from " +
					"WFPROCESS WHERE PROCESSNAME='"+processName+"'" +
							" and PROCESSREV='"+processrevName+"'";

			ResultSet r = st.executeQuery(sql);
			setPrintSQL(true);
			printSQL(sql);
			if(r.next()){
				tableName=r.getString("OBJECTNAME");
			}else{
				   JOptionPane.showMessageDialog( frame.getContentPane(),
					       "无效的流程名称!", "系统信息", JOptionPane.WARNING_MESSAGE);
				   return;
			}
			table.removeRowsAll();// 清空内容
			r.close();

			amk.addKonnfigu(tableName, processrevName, processName);
			sigotoPurosesuAll(amk.getKonnfigu(processName, AllMaximoKonnfigu.SOUSA_PROCESSNAME));

			System.out.println(saveAction.getContent());
			System.out.println(saveActionGrup.getContent());

			
			for (int i = 0; i < domeinnNamae.length; i++) {
				if (domeinnTrim.isSelected()&&domeinnNamae[i]!=null) {
					domeinnNamae[i]=domeinnNamae[i].trim();
				}
				if(betubetunihyouji.isSelected()){
					table.addRow(new Object[] { "域名字",domeinnNamae[i] });
				}
				domeinnWoYomu(st, domeinnNamae[i]);
				if(betubetunihyouji.isSelected()){
					table.addRow(new Object[] {});
					table.addRow(new Object[] {});
				}
			}
			
		
			
			for (int i = 0; i < 20; i++) {
				table.addRow(new Object[] {});
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(sql);
			println(sql,true);
			setConsoleOutIsRight();
			// TODO: handle exception
		}
	}
	/**
	 * 比较复杂的一种遍历在这里，呵呵~~他很暴力哦，它的灵活性太强了，想得到什么就能得到什么~~
	 * @param map
	 */
    public static void workByEntry(Map map) {
        Set<Map.Entry<String, String>> set = map.entrySet();
        int i=1;
        for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();i++) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
        
			System.out.println(i+"\tkey="+entry.getKey()+"----->"+entry.getValue());
        }
    }
	public void sigotoPurosesuAll(Konnfigu[] kon){
			try {

				actionGrupMap="";
				actionMap="";
				
				saveAction=new ConCatLineBreaks();
				saveActionGrup=new ConCatLineBreaks();
				
				Statement st = con.createStatement();
				ResultSet r = null;
				String s="";

				Vector vs=readTableValue("ACTION");//读取表的所有字段
				for (int i = 0; i < vs.size(); i++) {
					if(!s.isEmpty()){
						s+=",";
					}
					s=s+vs.get(i);
				}

				
				/**
				 * tableName	插入的表名
				 * valueName	插入时需要的字段名
				 * &AUTOADD&	插入时按照该表中拥有的该字段的最大值递增
				 * &IGNORE&		 插入时忽略该字段
				 * 'ZH'			插入时始终采用该值
				 * {}			有效范围
				 */
				saveAction.addLastLine("tableName:ACTION");
				saveAction.addLastLine("valueName:"+s);
				saveAction.addLastLine("setValue:ACTIONID=&AUTOADD&");
				saveAction.addLastLine("setValue:ROWSTAMP=&IGNORE&");
				saveAction.addLastLine("setValue:LANGCODE='ZH'");
				saveAction.addLastLine("{");

				/**
				 * ACTIONGROUP  组"操作"中操作组包含的值
				 */
				Vector vs1=readTableValue("ACTIONGROUP");//读取表的所有字段
				s="";
				for (int i = 0; i < vs1.size(); i++) {
					if(!s.isEmpty()){
						s+=",";
					}
					s=s+vs1.get(i);
				}
				saveActionGrup.addLastLine("tableName:ACTIONGROUP");
				saveActionGrup.addLastLine("valueName:"+s);
				saveActionGrup.addLastLine("setValue:ACTIONGROUPID=&AUTOADD&");
				saveActionGrup.addLastLine("setValue:ROWSTAMP=&IGNORE&");
				saveActionGrup.addLastLine("{");
				
				

				table.addRow(new Object[] {});
				table.addRow(new Object[] { "工作流程列表" });
				table.addRow(new Object[] { "序号",  "工作流程名称", "备注" });
				Vector vs2=new Vector();
				vs2.add("PROCESSNAME");
				vs2.add("DESCRIPTION");
				int row=0;
				for (int i = 0; i < kon.length; i++) {
					sigotoPurosesu(kon[i], vs, vs1);
					sql="SELECT * FROM WFPROCESS WHERE PROCESSNAME='"+kon[i].getProcessName()+"'";
					printSQL(sql);
					Statement st1 = con.createStatement();
					ResultSet rs=st1.executeQuery(sql);
					if (rs.next()) {
						String[] vs3=readTableData(rs, vs2).split("\t",-1);
						table.addRow(new Object[] { ++row,  vs3[1], vs3[2] });
					}
					rs.close();
					st1.close();
				}

				actionGrupMap=delLastKonma(actionGrupMap);
				actionMap=delLastKonma(actionMap);

				

				table.addRow(new Object[] {});
				table.addRow(new Object[] {});
				table.addRow(new Object[] { "工作流程状态" });
				table.addRow(new Object[] { "序号",  "名称","状态名称", "所在流程名称" });
				
				sql="SELECT  ACTION.*  FROM ACTION WHERE ACTIONID IN("+actionMap+")";
				printSQL(sql);
				r = st.executeQuery(sql);
				vs2=new Vector();
				vs2.add("ACTION");
				vs2.add("VALUE2");

				Vector vs3=new Vector();
				vs3.add("MEMBER");
				vs3.add("VALUE2");
				row=0;
				for (int i = 0;r.next(); i++) {
					String actionid=r.getString("ACTIONID");
					String action=r.getString("ACTION");
					String s1=readTableData(r, vs);
					saveAction.addLastLine(s1);
					String processName=actionKonnfigu.get(actionid).getProcessName();
					String[] s3=readTableData(r, vs2).split("\t",-1);
					table.addRow(new Object[] { ++row,  s3[1], s3[2],processName });
					sql="SELECT ACTIONGROUP.MEMBER,ACTION.* FROM ACTIONGROUP LEFT OUTER JOIN  " +
							"ACTION ON  ACTIONGROUP.MEMBER =ACTION.ACTION  " +
							" WHERE ACTIONGROUP. ACTION ='"+action+"'";
					printSQL(sql);
					Statement st2 = con.createStatement();
					ResultSet rs = st2.executeQuery(sql);
					while(rs.next()){
						s3=readTableData(rs, vs3).split("\t",-1);
						if(s!=null){
							table.addRow(new Object[] { "",  s3[1], s3[2],processName });
						}
						s1=readTableData(rs, vs);
						saveAction.addLastLine(s1);
					}
					rs.close();
					st2.close();
				}
				r.close();
				

				sql="SELECT  ACTIONGROUP.*  FROM ACTIONGROUP WHERE ACTIONGROUPID IN ("+actionGrupMap+")";
				printSQL(sql);
				r = st.executeQuery(sql);
				for (int i = 0;r.next(); i++) {
					String s1=readTableData(r, vs1);
					saveActionGrup.addLastLine(s1);
				}
				r.close();
				
				saveActionGrup.addLastLine("}");
				saveActionGrup.addLastLineBreaks();
				saveAction.addLastLine("}");
				saveAction.addLastLineBreaks();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(sql);
				println(sql,true);
				setConsoleOutIsRight();
			}
	}
	/**
	 * 工作流程
	 * @param st 
	 * @param appName 
	 * @param tableName 
	 * @throws SQLException 
	 */
	public void sigotoPurosesu(Konnfigu kon,Vector vs,Vector vs1) throws SQLException{
		
		String processName= kon.getProcessName();
		String tableName= kon.getTableName();
		String appName= kon.getAppName();
		String processrevName= kon.getProcessrevName();
		Statement st = con.createStatement();
		sql="SELECT DISTINCT ACTION.*,"+
		  " B.PROCESSNAME"+
		  " FROM ACTION,"+
		    " (SELECT WFACTION.*"+
		    " FROM WFACTION"+
		    " LEFT OUTER JOIN WFPROCESS"+
		    " ON (WFPROCESS.OBJECTNAME  ='"+tableName+"')"+
		    " WHERE WFACTION.PROCESSNAME=WFPROCESS.PROCESSNAME"+
		    ") B"+
		  " WHERE (ACTION.ACTION=B.ACTION AND B.PROCESSNAME='"+processName+"')"+
		  " ORDER BY ACTION.ACTIONID";

		ResultSet r = st.executeQuery(sql);
		printSQL(sql);
		String s2="";
		while(r.next()){
			String s=r.getString("ACTIONID");
			if(s!=null&&!s.isEmpty()){
				actionMap=actionMap+"'"+s+"',";
//				Konnfigu ko=new Konnfigu(tableName, processrevName, processName, appName);
				actionKonnfigu.put(s, new Konnfigu(tableName, processrevName, processName, appName));
//				System.out.println("s="+s);
			}
			String s1=r.getString("ACTION");
			if(s1!=null&&!s1.isEmpty()){
				s2=s2+"'"+s1+"',";
			}
			
		}
		r.close();
		s2=delLastKonma(s2);
		sql="SELECT * FROM ACTIONGROUP WHERE ACTION IN ("+s2+")";
		printSQL(sql);
		 st = con.createStatement();
		 r = st.executeQuery(sql);
		while(r.next()){
			String s=r.getString("ACTIONGROUPID");
			if(s!=null){
				actionGrupMap=actionGrupMap+"'"+s+"',";
				actionGrupKonnfigu.put(s, new Konnfigu(tableName, processrevName, processName, appName));
			}
		}
		r.close();

	}	
	/**
	 * 删除逗号
	 * @param s
	 * @return
	 */
	public static String delLastKonma(String s){
		String str=s;
		if(s!=null){
			int i=s.lastIndexOf(",");
//			System.out.println(i+"--->"+s.length());
			if(i==s.length()-1){
				str=s.substring(0,i);
			}
		}
		return str;
	}
	
	
	/**
	 * 返回r数据集对应的vs字段集的内容
	 * @param r
	 * @param vs
	 * @return
	 * @throws SQLException
	 */
	public String readTableData(ResultSet r,Vector vs) throws SQLException{
		String s1="";
		for (int i = 0; i < vs.size(); i++) {
			String s2=r.getString(""+vs.get(i));
			if(s2==null){
				s2="";
			}
//			else{
//				if(s2.indexOf("\t")>-1){
//					s2="\""+s2+"\"";//包含换行符则加双引号
//				}
//			}
			s1=s1+"\t"+s2;
		}
		return s1;
	}
	
	public Vector readTableValue(String tableName){
		Vector ob = new Vector();
		sql="SELECT * FROM USER_TAB_COLUMNS WHERE TABLE_NAME='"+tableName+"'";
		ResultSet r;
		try {
			Statement st = con.createStatement();

			r = st.executeQuery(sql);
			
			while(r.next()){
				ob.add(r.getString("COLUMN_NAME"));
			}
			r.close();
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ob;
	}
	/**
	 * 读取对应的域
	 * @param st
	 * @param domeinnNamae
	 * @throws SQLException
	 */
	public void domeinnWoYomu(Statement st, String domeinnNamae) throws SQLException{
		
		sql="SELECT * FROM ALNDOMAIN WHERE " +
		" upper(domainid)='"+domeinnNamae+"' ";
		ResultSet r = st.executeQuery(sql);
		for (int i = 1; r.next(); i++) {
			konntenntu[0] = r.getString("DOMAINID");
			konntenntu[1] = r.getString("VALUE");
			konntenntu[2] = r.getString("DESCRIPTION");
			konntenntu[3] = r.getString("SITEID");
			konntenntu[4] = r.getString("ORGID");
			konntenntu[5] = r.getString("NUM");
			table.addRow(new Object[] { konntenntu[0],
					konntenntu[1], konntenntu[2], konntenntu[3],
					konntenntu[4], konntenntu[5] });

		}
	}
	
	/**
	 * @param args
	 * @throws BadLocationException 
	 */
	public static void main(String[] args) throws BadLocationException {

        JTabbedPane tab = new JTabbedPane(JTabbedPane.TOP);
		ReadPurosesuName rpn = new ReadPurosesuName();
		JFrame frame = new JFrame("标题");
		rpn.setFrame(frame);
		Container c = frame.getContentPane();
		JLabel jl1 = new JLabel(
				"java -version "
						+ System.getProperty("java.version")
						+ "        Chiang Kai-shek(shoukaiseki) <jiang28555@gmail.com>  "
						+ "  2012-02-20 Tokyo japan ",SwingConstants.CENTER);

		JDesktopPane desktop =new JDesktopPane();
		JDesktopPane desktop1 =new JDesktopPane();
		JPanel jp=new JPanel();
		
		KonntenntuOutToJInternalFrame kotjf=new KonntenntuOutToJInternalFrame();

		rpn.setKonntenntuOutToJInternalFrame(kotjf);
//		jp.add(kotjf);
//		jp.add(rpn);
//		desktop.add(rpn);
//		desktop.add(kotjf);
		desktop1.add(kotjf);
		/**
		 * JInternalFrame放JTabbedPane中需要关闭最大化
		 */
        tab.addTab("流程信息",rpn);
        rpn.hideNorthPanel();
        try {
			rpn.setSelected(true);
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        tab.setPreferredSize(new Dimension(500,600));
		/**
		 * 分隔条
		 */
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
        		tab,desktop1);
        splitPane.setDividerLocation(300);
//		c.add(jl1,BorderLayout.NORTH);
//		c.add(new JButton("sdfsdf"),BorderLayout.EAST);
//		c.add(new JButton("sdfsdf"),BorderLayout.WEST);
//		c.add(desktop,BorderLayout.CENTER);
//		c.add(desktop1,BorderLayout.SOUTH);
		

		c.add(jl1,BorderLayout.NORTH);
//		c.add(desktop,BorderLayout.CENTER);
		c.add(splitPane,BorderLayout.CENTER);
		frame.setSize(700, 500);
		// frame.setLocation(200, 150);
		/**
		 * 设置显示为中心
		 */
		frame.setLocationRelativeTo(null);
		/**
		 * frame.show();失效后采用这个
		 */
		frame.setVisible(true);
		/**
		 * 关闭后终止进程=彻底退出
		 */

		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		rpn.setDefuorutoConnection();
		/**
		 * 设置补全
		 */
		rpn.setAutoComKonntenntu();
		try {
			/**
			 * JInternalFrame放JTabbedPane中需要关闭最大化
			 */
			kotjf.setMaximum(true);
			/**
			 * JInternalFrame放JTabbedPane中需要关闭最大化
			 */
//			rpn.setMaximum(true);
		} catch (PropertyVetoException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
	public KonntenntuOutToJInternalFrame getKonntenntuOutToJInternalFrame() {

		return JF_ConsoleOut;
	}

	public void setKonntenntuOutToJInternalFrame(KonntenntuOutToJInternalFrame kotjf) {

		 this.JF_ConsoleOut=kotjf;
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
//				JF_ConsoleOut.setBounds(getWidth() + getX(), getY(), 500,
//						getHeight());
				

			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
//				JF_ConsoleOut.setBounds(getWidth() + getX(), getY(), 500,
//						getHeight());
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
	 */
	public void printlnSeikou() {
		JF_ConsoleOut.println("------------成功执行更新完毕!-----------");
	}

	/**
	 * 输出SQL命令失败信息
	 * 
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

	public void printSQL(String sql){
		if(printsql){
			println();
			println(sql+";");
		}
	}
	public void setPrintSQL(boolean printsql){
		this.printsql=printsql;
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
	
	public void setFrame(JFrame frame){
		this.frame=frame;
	}
	
	public void setAutoComKonntenntu(){
		try {
			sql="select DISTINCT PROCESSNAME from WFPROCESS";
			Statement st = con.createStatement();
			ResultSet r = st.executeQuery(sql);
	        Vector tempvector = new Vector();
			while(r.next()){
				tempvector.add(r.getString("PROCESSNAME"));
			}
			r.close();
			st.close();
			bTextField.setFilter(new DefaultCompletionFilter(tempvector));
			setComboBox();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	   public void setComboBox(){
		   try {
			   if(bTextField.getText().isEmpty()){
				   return;
			   }
			   sql="select DISTINCT * from WFPROCESS WHERE PROCESSNAME='"
				   +bTextField.getText()+"'";
				
				Statement st = con.createStatement();
				ResultSet r = st.executeQuery(sql);
				comboBox.removeAllItems();
				while (r.next()) {
					String s = r.getString("PROCESSREV");// 相关工艺码
					comboBox.addItem(s);
				}
				r.close();
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(sql);
				// TODO: handle exception
			}
	   }
	   
}

