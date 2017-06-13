package org.tomaximo.other;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.shoukaiseki.constantlib.CharacterEncodingName;
import org.shoukaiseki.file.SousaFile;
import org.shoukaiseki.gui.jinternalframe.MCOCInternalFrameK;
import org.shoukaiseki.gui.jtable.ExcelAdapter;
import org.shoukaiseki.gui.jtable.JTableOperating;
import org.shoukaiseki.gui.jtextfield.AutoCompletionField;
import org.shoukaiseki.gui.jtextfield.DefaultCompletionFilter;
import org.shoukaiseki.sql.ConnectionKonnfigu;
import org.shoukaiseki.string.ConCatLineBreaks;
import org.tomaximo.purosesu.ReadPurosesuName;
import org.tomaximo.tuuyouclass.AllMaximoKonnfigu;
import org.tomaximo.tuuyouclass.Konnfigu;
import org.tomaximo.tuuyouclass.KonntenntuOutToJInternalFrame;


public class AddToDetaBesu extends MCOCInternalFrameK {
	private String defuorutobTextField="OPTSDLYD_GZ";
	private JFrame  frame=null;
	/**
	 * 操作文件类
	 */
	SousaFile sousaFile=new SousaFile(false);

	
	
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

	private JButton commitButton,rollbackButton ;
	private ConCatLineBreaks savesql=new ConCatLineBreaks();
	private String sql = "";
	private Connection con = null;
	private String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String user = "orclzhjq";
	private String password = "orclzhjq";
	
	public AddToDetaBesu() {
		this(null, true);
	}

	public AddToDetaBesu(boolean windowclose) {
		this(null, windowclose);
	}

	public AddToDetaBesu(Connection con) {
		this(con, true);
	}

	public AddToDetaBesu(Connection con, boolean windowclose){
			JF_ConsoleOut = new KonntenntuOutToJInternalFrame();
//			JF_ConsoleOut.setVisible(false);//使用后报错

		this.con = con;
//		String[] columnNames = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
//				"J", "K", "L" }; // 列名
		String[] columnNames = { "A", "B"}; // 列名

		columnNames= new String[]{ "APPLICATIONAUTHID","GROUPNAME","APP","OPTIONNAME","SQL","F" ,"G","H","I","J"};
		
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

		commitButton=new JButton("提交更改");
		rollbackButton = new JButton("回退提交"); 
		rollbackButton.addActionListener(new ActionListener() {// 添加事件
			public void actionPerformed(ActionEvent e) {
					conRollback();
			}
		});

		 commitButton.addActionListener(new ActionListener() {// 添加事件
					public void actionPerformed(ActionEvent e) {
						conCommit();
					}
				});
        
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
		panel.add(Box.createHorizontalStrut(8));// 间距
		panel.add(commitButton);
		panel.add(Box.createHorizontalStrut(8));// 间距
		panel.add(rollbackButton);
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

		commitButton.setEnabled(false);
		rollbackButton.setEnabled(false);
	}

	/**
     * 
     */
	public void listPurosesu() {
		String[] domeinnNamae = bTextField.getText().toUpperCase().split(",");
		 konntenntu = new String[10];
		try {
			PreparedStatement pst = null;
			String[] groupname={"GZ1070","GZ1071","GZ1072",
					"GZ1073","GZ1010","GZ1011","GZ1012",
					"GZ1013","GZ1014","GZ1015","GZ1016",
					"GZ1017","GZ1018","GZ1019","GZ1020",
					"GZ1023","GZ1024","GZ1025","GZ1026",
					"GZ1027","GZ1028","GZ1029","GZ1030",
					"GZ1031","GZ1032","GZ1033","GZ1034",
					"GZ1035","GZHX1005",
					"GZ1010","GZ1012","GZ1016","GZ1019","GZ1095"};
			String[] appname={"OPGLPZJLB", "OPDYBHTTDJ", "OPDYBHXGDJ", 
					"OPDXZCDJ", "OPDQWFZYYS", "OPSBWHJL", "OPDQSBJYCS", 
					"OPRGBHXGDJ", "OPGLSSCLFM"};
			
			Map<String,String> musiOptionName=new HashMap();
			Map<String,String> onlyOptionName=new HashMap();
			String[] only=new String[]{};//有效權限,爲空時將授予所有權限
			String[] musi=new String[]{};//在有效權限上忽略的權限.
			musi=new String[]{"DELETE"};

//			musi=new String[]{"ASSIGNWF","BOOKMARK","CLEAR","DELETE",
//					"DUPLICATE","HELPWF","HISTORYWF","INSERT","NEXT",
//					"PREVIOUS","ROUTEWF","SAVE","SEARCHBOOK",
//					"SEARCHMORE","SEARCHSQRY","SEARCHTIPS","SEARCHVMQR",
//					"SEARCHWHER","STOPWF","VIEWWF"};
//			musi=new String[]{"ASSIGNWF","BOOKMARK","CLEAR","DELETE",
//					"DUPLICATE","HELPWF","HISTORYWF","INSERT","NEXT",
//					"PREVIOUS","ROUTEWF","SAVE","SEARCHVMQR","STOPWF","VIEWWF"};
			appname=new String[]{"OPYBDYJLB","OPBLQDZJL"};
//			appname=new String[]{"WOTRACK","CODEREQITM","ACTIONSCFG","BBOARD","CALENDR","DOMAINADM","FAILURE","FINCNTRL","INTMSGTRK","KPILCONFIG","LABOR","LABREP","LMO","PROPMAINT","RECEIPTS","RELATION","REPORT","RSCONFIG","TERMCOND","TOOL","WFFIELDCTL","WSREGISTRY","OPPM","OPBLQDZJL","OPDYBHXGDJ","MASTERPM","INVISSUE","LOGGING","NDASSET","RCNASTLINK","RELEASE","SCHEDULER","PAYREQ","CHANGEPSWD","CHRTACCT","DESIGNER","IM","INBXCONFIG","JOBPLAN","SAFEPLAN","SRCOUNT","TKTEMPLATE","WFDESIGN","CONTSFW","SFWLICVIEW","RCNCIRSLT","CONTLABOR","CONTLEASE","CREATEDR","CREATEINT","CRONTASK","DEPLCOLLS","DEPLGROUPS","DM","FORGOTPSWD","INTSRV","INVOKE","LAUNCH","PUBLISH","SRVCOMMOD","TOOLINV","VIEWSR","WFADMIN","PM","OPDXZCDJ","METERGRP","ACTUALCI","FEATURE","MULTISITE","PROBLEM","CODEREQ","INVOICE","CI","CREATESR","ESCALATION","EXCHANGE","FACONFIG","HAZARDS","INVUSAGE","LOCATION","ROLE","RPTOUTPUT","SECURGROUP","SLA","TAGLOCKS","USER","OPLOG","OPPMWO","OPRGBHXGDJ","OPSBWHJL","METER","PERSON","PLUSCTMPLT","INCIDENT","CONTPURCH","PR","COMPANY","AUTOSCRIPT","CONTMASTER","CRAFT","EXTSYSTEM","INVENTOR","ISSUETRANS","ITEM","PRECAUTN","ROUTES","STARTCNTR","VIEWTMPL","WORKVIEW","OPDQSBJYCS","CHANGEPWD","OPBYQFJTJL","TLOAMSWCTG","AUTO","PO","PURASSIGN","ACTION","CITYPE","COMPMASTER","CURRENCY","MANAGEINT","SBYD","STOREROOM","WORKMAN","OPTICKET","OPGLSSCLFM","PERSONGR","CHANGE","NPASSET","SOLUTION","OPTSDLYD","ASSETCAT","COLLECTION","COND","CONDCODE","CONFIGUR","ENDPOINT","INTERROR","INTOBJECT","ISSUE","KPIGCONFIG","QUICKREP","SHIPREC","SR","VIEWDR","OPLOGEVENT","PMWO","OPDQWFZYYS","OPYBDYJLB","DPLDASSET","SHIFT","RFQ","ACTIVITY","ASSET","COMMTMPLT","CONDEXPMGR","CONTWARRTY","DEPARTMENT","EMAILSTNER","KPI","QUAL","SCCONFIG","SELFREG","SETS","SRVITEM","VIEWDRFT","OPDYBHTTDJ","OPGLPZJLB","OPCONFIG","OPLOGINIT","IBMCONTENT","OPAMDOP"};
			appname=new String[]{"ACTION","ACTIONSCFG","ACTIVITY","ACTUALCI","ASSET","ASSETCAT","AUTO","AUTOSCRIPT","BBOARD","CALENDR","CHANGE","CHANGEPSWD","CHRTACCT","CI","CITYPE","CODEREQ","COLLECTION","COMMTMPLT","COMPANY","COMPMASTER","COND","CONDCODE","CONDEXPMGR","CONFIGUR","CONTLABOR","CONTLEASE","CONTMASTER","CONTPURCH","CONTSFW","CONTWARRTY","CRAFT","CREATEDR","CREATEINT","CREATESR","CRONTASK","CURRENCY","DEPARTMENT","DEPLCOLLS","DEPLGROUPS","DESIGNER","DM","DOMAINADM","DPAMADPT","DPAMMANU","DPAMOS","DPAMPROC","DPAMSW","DPAMSWS","DPAMSWUSG","DPLDASSET","ECOMMADAPT","EMAILSTNER","ENDPOINT","ESCALATION","EXCHANGE","EXTSYSTEM","FACONFIG","FAILURE","FEATURE","FINCNTRL","FORGOTPSWD","HAZARDS","IBMCONTENT","IM","IMICONF","INBXCONFIG","INCIDENT","INTERROR","INTMSGTRK","INTOBJECT","INTSRV","INVENTOR","INVISSUE","INVOICE","INVOKE","INVUSAGE","ISSUE","ITEM","JOBPLAN","KPI","KPIGCONFIG","KPILCONFIG","LABOR","LABREP","LAUNCH","LMO","LOCATION","LOGGING","MANAGEINT","MASTERPM","METER","METERGRP","MULTISITE","NDASSET","NPASSET","OPAMDOP","OPBLQDZJL","OPBYQFJTJL","OPCONFIG","OPDQSBJYCS","OPDQWFZYYS","OPDXZCDJ","OPDYBHTTDJ","OPDYBHXGDJ","OPGLPZJLB","OPGLSSCLFM","OPLOG","OPLOGEVENT","OPLOGINIT","OPPM","OPPMWO","OPRGBHXGDJ","OPSBWHJL","OPTICKET","OPTSDLYD","OPYBDYJLB","PAYREQ","PERSON","PERSONGR","PLUSCTMPLT","PLUSDSPLAN","PM","PMWO","PO","PR","PRECAUTN","PROBLEM","PROPMAINT","PUBLISH","PURASSIGN","QUAL","QUICKREP","RCNASTLINK","RCNASTRSLT","RCNCILINK","RCNCIRSLT","RCNCMPRULE","RCNLNKRULE","RCNTSKFLTR","RECEIPTS","RECONTASK","RELATION","RELEASE","REPLISTCFG","REPORT","RFQ","ROLE","ROUTES","RPTOUTPUT","RSCONFIG","SAFEPLAN","SBYD","SCCONFIG","SCHEDULER","SEARCHSOL","SECURGROUP","SELFREG","SETS","SFWLICVIEW","SHIFT","SHIPREC","SLA","SOLUTION","SR","SRCOUNT","SRVCOMMOD","SRVITEM","STARTCNTR","STOREROOM","TAGLOCKS","TERMCOND","TEST","TESTASUS","TKTEMPLATE","TLOAMSWCTG","TOOL","TOOLINV","USER","VIEWDR","VIEWDRFT","VIEWSR","VIEWTMPL","WFADMIN","WFDESIGN","WFFIELDCTL","WORKMAN","WORKVIEW","WOTRACK","WSREGISTRY"};

//			appname=new String[]{"OPYJCYST","OPSQPZJD","OPHXCLTJ","OPYPLLTJ","OPGLSSCLFM","OPBYQFJTJL"};
			appname=new String[]{"OPHCZS"};
			appname=new String[]{"OPUPFILE"};
//			appname=new String[]{"SCCONFIG"};

			appname=new String[]{"OPGLSSCL"};
//			groupname=new String[]{"MAXADMIN"};
//			groupname=new String[]{"GZ1010","GZ1016"};
			
			//報表權限賦予廠領導
			groupname=new String[]{"FGS1037","FGS1042","GZ1010","GZ1016","GZ1036","XN1012","XN1016","XN1022","GZ1063","GZ1066","FGS1001","FGS1002","FGS1008","FGS1009","FGS1010","FGS1011","FGS1023","FGS1024","FGS1032","FGS1036"};
			only=new String[]{"RUNREPORTS","READ"};
			appname=new String[]{"TERMCOND","CONTPURCH","INVOICE","PO","PR","RFQ","COMPANY","COMPMASTER","SHIPREC","PRLINE","PURASSIGN","PAYREQ"};
			
			for (String name:musi) {
				musiOptionName.put(name, name);
			}
			for (String name:only) {
				onlyOptionName.put(name, name);
			}
			musiOptionName.put("NOPORTLET","NOPORTLET");//忽略隐藏权限
//			setPrintSQL(true);
			savesql.setContent("");
			table.removeRowsAll();// 清空内容
			int i=0;
			for (String aname:appname) {
				sql="SELECT * FROM SIGOPTION WHERE APP='"+aname+"'";
				Statement st = con.createStatement();
				ResultSet r = st.executeQuery(sql);
				printSQL(sql);
				Vector optionname=new Vector();//授权名称
				while(r.next()){
					if(onlyOptionName.isEmpty()){
						optionname.add(r.getString("OPTIONNAME"));
					}else{
						if(onlyOptionName.get(r.getString("OPTIONNAME"))!=null){
							optionname.add(r.getString("OPTIONNAME"));
						}
					}
				}
				for (String gname : groupname) {
					for (Object oname : optionname) {
						if (musiOptionName.get(oname)!=null) {
							continue;
						}
						sql="SELECT * FROM APPLICATIONAUTH WHERE" +
						" GROUPNAME='"+gname+"' AND APP='"+aname+"'"+
						 " AND OPTIONNAME='"+oname+"'";
						printSQL(sql);
						Statement stt = con.createStatement();
						ResultSet rs = stt.executeQuery(sql);
						if(!rs.next()){
							sql="SELECT APPLICATIONAUTHSEQ.NEXTVAL FROM DUAL";
							Statement stt1 = con.createStatement();
							ResultSet rs1 = stt1.executeQuery(sql);
							rs1.next();
							String seq=rs1.getString(1);
							rs1.close();
							stt1.close();
							sql = " INSERT INTO APPLICATIONAUTH" +
								"(GROUPNAME,APP,OPTIONNAME,APPLICATIONAUTHID) " +
								"VALUES('" +
								gname+"','"+aname+"','"+oname+"','"+seq+"')" ;
							
							table.addRow(new Object[] {seq,gname,aname,oname,sql});
							printSQL(sql);
							pst = con.prepareStatement(sql);
							pst.execute();
							savesql.addLastLine(sql+";");
							pst.close();
							i++;
						}
						rs.close();
						stt.close();
						JF_ConsoleOut.printLabel("GROUPNAME="+gname+",APP="+aname+"已插入"+(i));
					}
				}
				r.close();
				st.close();
				
			}
			JF_ConsoleOut.printLabel("已完成."+"共插入"+(i));
//			System.out.println(savesql.getContent());
			SimpleDateFormat bartDateFormat = new SimpleDateFormat(
            "yyyyMMddHHmmss");

            java.util.Date data = new java.util.Date();
			 String fileName = "./sqlmeirei/AddToDataBesu"+bartDateFormat.format(data)+".sql";
			 File txtfile = new File(fileName);
			 sousaFile.writeFile(JF_ConsoleOut, txtfile, savesql.getContent(), true);
			 
			 
			for (int j = 0; j < 20; j++) {
				table.addRow(new Object[] {});
			}
			if(i>0){
				commitButton.setEnabled(true);
				rollbackButton.setEnabled(true);
			}
		} catch (Exception e) {
			commitButton.setEnabled(false);
			rollbackButton.setEnabled(false);
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}//异常则不更新
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
	/**
	 * @param args
	 * @throws BadLocationException 
	 */
	public static void main(String[] args) throws BadLocationException {

        JTabbedPane tab = new JTabbedPane(JTabbedPane.TOP);
        AddToDetaBesu rpn = new AddToDetaBesu();
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
		 url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
		 driver = "oracle.jdbc.driver.OracleDriver";
		 user = "maximo75";
		 password = "maximo75";					
		 url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=10.59.67.6)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=10.59.67.7)(PORT=1521)))(CONNECT_DATA=(SERVER=dedicated)(SERVICE_NAME=eam)))";
		 user = "maximo";
		 password = "maximo";					
		 url = "jdbc:oracle:thin:@10.59.67.22:1521:test22";
		 user = "shoukaiseki";
		 password = "shoukaiseki";					
		 
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
			/**
			 * 关闭自动更新
			 */
			con.setAutoCommit(false);
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
}

