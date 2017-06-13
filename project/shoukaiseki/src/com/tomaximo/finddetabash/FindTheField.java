package com.tomaximo.finddetabash;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;

import com.shoukaiseki.characterdetector.CharacterEncoding;
import com.shoukaiseki.characterdetector.utf.UnicodeReader;
import com.shoukaiseki.constantlib.CharacterEncodingName;
import com.shoukaiseki.gui.flowlayout.ModifiedFlowLayout;
import com.shoukaiseki.gui.jtextpane.JTextPaneDoc;
import com.shoukaiseki.sql.ConnectionKonnfigu;




public class FindTheField extends JFrame {

	/**
	 * 关闭時是否退出程序,默认時退出,只作为子程序时不退出
	 */
	private boolean windowclose = true;
	private JikkouSql ssqltable;
	private DefaultTableModel tableModel;
	private JTable table;
	private JComboBox comboBox ;
	private String sql="";
	
	private String fileName="./FindTheField.ini";
	public  File txtfile = new File(fileName);
	
	private SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private String[] parent;
	private String parentOne;
	private String parentSepareta="//";//parent多个的分隔符

	private Box box ;
	private static JTextPaneDoc textPane;
	private JLabel jl;

	private JButton jb_find,jb_cleantext,jb_readsetting; // 清空记录,重读配置

	private JLabel jl_user, jl_password,jl_url,jl_driver;
	private JTextField jt_user, jt_password,jt_url,jt_driver;
	/**
	 * 索引最终值设置,插入LoCanCesTor表时的查找是否有与parent同名的location字段,
	 * 如果没有则以该数据为基准,使用//分隔符隔开,可匹配多个字符
	 */
	private JTextField jt_parent;
	private JTextField jl_parent;
//	private JTextField jt_loginurl,jt_loginuser,jt_loginpassword;

	//
	private DecimalFormat df = new DecimalFormat("00.00%"); // " "内写格式的模式
															// 如保留2位就用"0.00"即可
	private int error = 0;// 记录数据更新失败记录
	private int endtj = 0;// 统计总更新数据条数
	private Connection con = null;
	private String fuirudo;
	private String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
//	private String url = "jdbc:oracle:thin:@192.168.2.101:1521:zhjqmaximo";
	private String driver="oracle.jdbc.driver.OracleDriver";
	private String user = "maximo75";
	private String password = "maximo75";

	public FindTheField() {
		this(true);
	}
	public FindTheField(boolean windowclose){

		jl_user = new JLabel("  user:", SwingConstants.LEFT);
		jt_user = new JTextField(user, 8);
		jl_password = new JLabel("password:", SwingConstants.LEFT);
		jt_password = new JTextField(password, 8);

		jl_url = new JLabel("      url:", SwingConstants.LEFT);
		jt_url = new JTextField(url, 8);
		jl_driver = new JLabel("driver:", SwingConstants.LEFT);
		jt_driver = new JTextField(driver, 8);
		
		textPane = new JTextPaneDoc();
		textPane.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		textPane.setText("这里设置文本框内容!");
		textPane.setFont(new Font("宋体", Font.BOLD, 13));
		textPane.setLayout(new ModifiedFlowLayout());// 加不加都感觉有效果,如果一段英文无空格就会出现不会自动换行

		jb_find=new JButton("查找");
		jb_cleantext=new JButton("清空");
		jb_readsetting=new JButton("重新读入配置");
		/**
		 * 查找
		 */
		jb_find.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {

				new Timer().schedule (new TimerTask() {
					@Override
					public void run(){//实例中的的方法
						kaishiFind();
					}
				}, 10);
			}
		});
		jb_cleantext.addActionListener(new ActionListener() { // 插入文字的事件
			public void actionPerformed(ActionEvent e) {
				textPane.cleanText();
			}
		});
		jb_readsetting.addActionListener(new ActionListener() { // 插入文字的事件
			public void actionPerformed(ActionEvent e) {
				textPane.cleanText();
					try {
						readSetting();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}
		});

		jl = new JLabel("状态无异常!", SwingConstants.LEFT);
		comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// TODO Auto-generated method stub
				int index = ((JComboBox) e.getSource()).getSelectedIndex();// 获取到选中项的索引
				fuirudo = ((JComboBox) e.getSource()).getSelectedItem()
						.toString();// 获取到项
				jl.setText("查找选中项为:" + index + ",内容为:" + fuirudo);

			}
		});
		// 设置主框架的布局
		Container c = getContentPane();
		// c.setLayout(new BorderLayout());

		JScrollPane jsp = new JScrollPane(textPane);// 新建一个滚动条界面，将文本框传入
		// 滚动条
		jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		JLabel jl1 = new JLabel(
				"java -version "
						+ System.getProperty("java.version")
						+ "        Chiang Kai-shek(shoukaiseki) <jiang28555@gmail.com>  "
						+ "  2012-02-20 Tokyo japan ");
		// jl1.setBorder(BorderFactory.createEtchedBorder());//分界线
		jl1.setFont(new Font("标楷体 ", Font.BOLD, 13)); 
		Box box = Box.createVerticalBox(); // 竖结构
		Box box11 = Box.createHorizontalBox(); // 横结构 
		Box boxurl = Box.createHorizontalBox(); // 横结构
		Box boxdriver = Box.createHorizontalBox(); // 横结构
		Box boxfrom = Box.createHorizontalBox(); // 横结构
		Box boxparent=Box.createHorizontalBox(); // 横结构



		
		
		box11.add(jl1);

		
		box.add(box11);
		box.add(Box.createVerticalStrut(8)); // 两行的间距
		box.add(boxurl);
		box.add(Box.createVerticalStrut(8)); // 两行的间距
		box.add(boxdriver);
		box.add(Box.createVerticalStrut(8)); // 两行的间距
		box.add(boxfrom);
		box.add(Box.createVerticalStrut(8)); // 两行的间距
		box.add(boxparent);

		box.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8)); // 8个的边距


		boxurl.add(jl_url);
		boxurl.add(Box.createHorizontalStrut(8));// 间距
		boxurl.add(jt_url);
		
		boxdriver.add(jl_driver);
		boxdriver.add(Box.createHorizontalStrut(8));// 间距
		boxdriver.add(jt_driver);
		
		boxfrom.add(jl_user);
		boxfrom.add(Box.createHorizontalStrut(8));// 间距
		boxfrom.add(jt_user);
		boxfrom.add(Box.createHorizontalStrut(8));// 间距
		boxfrom.add(jl_password);
		boxfrom.add(Box.createHorizontalStrut(8));// 间距
		boxfrom.add(jt_password);
		
		
		jt_parent=new JTextField("%Fedora%");
		jt_parent.setFont(new Font("新宋体", Font.BOLD, 13));//Courier New 微软雅黑
		jl_parent=new JTextField("状态");
		jl_parent.setEditable(false);
		
		comboBox.setToolTipText("BLOB类型只能单独查找");

		
		boxparent.add(new JLabel("查找内容:"));
		boxparent.add(Box.createHorizontalStrut(8)); 
		boxparent.add(jt_parent);
		boxparent.add(Box.createHorizontalStrut(8)); 
		boxparent.add(new JLabel("字段类型:"));
		boxparent.add(Box.createHorizontalStrut(8)); 
		boxparent.add(comboBox);
		boxparent.add(Box.createHorizontalStrut(8)); 
		boxparent.add(jb_find);
		boxparent.add(Box.createHorizontalStrut(8)); 
		boxparent.add(jb_readsetting);
		boxparent.add(Box.createHorizontalStrut(8)); 
		boxparent.add(jb_cleantext);
		
		box.add(Box.createVerticalStrut(8)); 
		box.add(jl_parent); 

		Box box2 = Box.createHorizontalBox(); // 横结构
		box2.add(new JLabel("状态栏:", SwingConstants.LEFT));
		box2.add(Box.createHorizontalStrut(8));// 间距
		box2.add(jl);

		jsp.setBorder(BorderFactory.createEtchedBorder());// 分界线

		c.add(box, BorderLayout.NORTH);
		c.add(jsp, BorderLayout.CENTER);
		c.add(box2, BorderLayout.SOUTH);
		
		
		
		this.setSize(700,500);
		this.setLocation(200,150);
		this.setTitle("查找数据库内容");
		this.setVisible(true);
		this.windowclose=windowclose;
		if (windowclose) {
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		}
	}
	/**
	 * @param args
	 * @throws BadLocationException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, BadLocationException {
		// TODO Auto-generated method stub
		FindTheField ftf=new FindTheField();
		ftf.readSetting();
		ftf.setDefuorutoConnection();//载入驱动
		ftf.setComboBox();
	}
	public Connection getCon(){
		return con;
		
	}
	public void setConnection(Connection con){
		this.con=con;
	}
	public void setConnection(ConnectionKonnfigu ck) throws BadLocationException{
		try {
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

			con = DriverManager.getConnection(url, user, password);
			textPane.addLastLine("OK,成功连接到数据库");

			printConnection();
		} catch (Exception ex) {
			ex.printStackTrace();
			textPane.addLastLine(ex.getMessage(), true);
		}
		setSetting();
	}
	public void setDefuorutoConnection() throws BadLocationException {
		try {
			// （1）装载并注册数据库的JDBC驱动程序
			// 载入JDBC驱动：oracle安装目录下的jdbc\lib\classes12.jar
			textPane.addLastLine("正在试图加载驱动程序 "+driver);
			Class.forName(driver);
			textPane.addLastLine("驱动程序已加载");
			// 注册JDBC驱动：有些地方可不用此句
			textPane.addLastLine("url=" + url);
			textPane.addLastLine("user=" + user);
			textPane.addLastLine("password=" + password);
			textPane.addLastLine("正在试图连接数据库--------");
			java.sql.DriverManager
					.registerDriver(new oracle.jdbc.driver.OracleDriver());

			con = DriverManager.getConnection(url, user, password);
			textPane.addLastLine("OK,成功连接到数据库");

		} catch (Exception ex) {
			ex.printStackTrace();
			textPane.addLastLine(ex.getMessage(),true);
		}
	}
	/**
	 * 设置选择类型
	 */
	public void setComboBox(){
		try {
			sql = "SELECT DISTINCT DATA_TYPE FROM  USER_TAB_COLUMNS " ;
			
			Statement st = con.createStatement();
			ResultSet r = st.executeQuery(sql);
			comboBox.removeAllItems();
			comboBox.addItem("所有字段");
			while (r.next()) {
				error=0;
				String s = r.getString("data_type");// 相关工艺码
				comboBox.addItem(s);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			textPane.addLastLine(e.getMessage());
			textPane.addLastLine(sql);
			printlnSippai();
			// TODO: handle exception
		}
	}
	public void kaishiFind(){
		try {
			
			parentOne=jt_parent.getText();
			if(fuirudo.equalsIgnoreCase("所有字段")){
				sql = "select TABLE_NAME,column_name from user_tab_columns where data_type not like 'BLOB'" ;
				
			}else{
				sql = "select TABLE_NAME,column_name from user_tab_columns" +
						" where data_type = '"+fuirudo+"'" ;
			
			}
			Statement tablebfb = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			textPane.addLastLine(sql);
			ResultSet ablebr = tablebfb.executeQuery(sql);
			ablebr.last();
			int tablebc = ablebr.getRow(); // 获得当前行号：此处即为最大记录数 ,无效时为0

			ssqltable=new JikkouSql(con,false);
//			sakuseiTable();
			ssqltable.removeRowTabel();

			
			
			Statement st = con.createStatement();
			ResultSet r = st.executeQuery(sql);

			textPane.addLastLine("需要查找的总记录数总共:" + tablebc);
			jl.setText("需要查找的总记录数总共:" + tablebc);
			endtj = 0;// 统计总更新数据条数

			Calendar cal = Calendar.getInstance();
			java.util.Date data = new java.util.Date();
			cal.setTime(data);// 设置时间为当前时间
			long timeOne = cal.getTimeInMillis();
			textPane.addLastLine("开始时间为:" + bartDateFormat.format(data));

			int rownum = 0;

//			ssqltable.addRowTable(new String[] { ""+rownum, sql });
			while (r.next()) {
				error=0;
				String tableName = r.getString("table_name");
				String columnName  = r.getString("column_name");
				sql=" select \""+columnName+"\" shoukaiseki,\""+tableName+"\".* from \""+tableName
				+"\" where upper(\""+columnName+"\") like "+"'"+parentOne.toUpperCase()+"'";
				int i=0;
				try {
					jl_parent.setText(sql);
					ResultSet rs = tablebfb.executeQuery(sql);
					rs.last();
					 i= rs.getRow(); 
					if(i>0){
						rownum++;
						if(tableModel!=null){
							tableModel.addRow(new String[] { ""+rownum, sql });
						}
						ssqltable.addRowTable(new String[] { ""+rownum, sql });
						System.out.println(sql);
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					textPane.addLastLine(i+sql);
					textPane.addLastLine(e.getMessage());
				}
				endtj++;
				jl.setText("已查找:"+endtj+"条"+df.format((float)endtj/tablebc));
				
			}

			textPane.addLastLine("查找结束!");
			data = new java.util.Date();
			cal.setTime(data);// 设置时间
			long timeTwo = cal.getTimeInMillis();
			long daysapart = (timeTwo - timeOne) / (1000 * 60);// 分钟
			long daysaparts = (timeTwo - timeOne) / 1000 % 60;// 获得总秒数后取除以60的余数即为秒数
			textPane.addLastLine("结束时间为:" + bartDateFormat.format(data));
			textPane.addLastLine("共花费时间为" + daysapart + "分" + daysaparts + "秒");
			printlnSeikou();
		} catch (Exception e) {
			e.printStackTrace();
			textPane.addLastLine(e.getMessage());
			textPane.addLastLine(sql);
			printlnSippai();
			// TODO: handle exception
		}
	}
	public void printlnSeikou() {
		try {
			textPane.addLastLine("------------成功执行更新完毕!-----------",true);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 输出SQL命令失败信息
	 * @throws BadLocationException 
	 */
	public void printlnSippai() {
		textPane.setFont(new Font("标楷体 ", Font.BOLD, 13));
		try {
			textPane.addLastLine("------------更新错误??????-----------",true);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		textPane.setFont(new Font("宋体 ", Font.BOLD, 13));
	}
	
	  public void readSetting() throws IOException, BadLocationException {
			newFile();//无配置文件则自动新建文件
			reFile();//读取配置文件
			setSetting();//
	  }

		public  void newFile() throws IOException{
			// 新建文件
			if (!txtfile.exists()) {
				if (txtfile.createNewFile()){
					textPane.addLastLine("配置文件创建成功!");
					wrFile();//写入文件
				}else{
					textPane.addLastLine("创建新文件失败!");
				}
			} else {
				textPane.addLastLine("发现配置文件"+fileName+"!");
			}
		}

		public  void wrFile(){
			textPane.addLastLine("正在创建文件"+txtfile.getPath());
			try {
				String age0=wrString();
				//常量类:各编码名称
				CharacterEncodingName ce=new CharacterEncodingName();
				FileOutputStream o=new FileOutputStream(txtfile);
				//采用UTF-8编码格式输出
				OutputStreamWriter out =new OutputStreamWriter(o, ce.UTF_8);
				out.write(age0);
				textPane.addLastLine("文件创建写入成功");
				out.close();
			} catch (Exception e) {
				// TODO: handle exception
				textPane.addLastLine(e.getMessage());
			}
		}

	private void reFile() throws BadLocationException {
		// 读取文件
		textPane.addLastLine("\n\n读取文件!");
		try {
			String code = CharacterEncoding.getLocalteFileEncode(fileName);

			 FileInputStream in = new FileInputStream(fileName);  
			BufferedReader br=new BufferedReader(new InputStreamReader(in, code));
			/**
			 * 解决win记事本保存UTF-8文件后文件头???问题, bom信息=EFBBBF
			 */
			br = new BufferedReader(new UnicodeReader(in, code));
			
			
			System.out.println("code="+code);
			ConnectionKonnfigu sk=new ConnectionKonnfigu();
			String sr = null;
			String a = null; 
			String b = null;
			while ((sr = br.readLine()) != null) {
				textPane.addLastLine(sr);
				if (sr.isEmpty()) {
					continue;
				}
				a = sr.substring(0, 1);
				System.out.println(a);
				System.out.println(sr);
				if (a.equals("#")) {
					continue;
				}
				// 取等号位置
				int value = sr.indexOf("=");
				System.out.println(value);
				if (value<0) {
					continue;
				}
				a = sr.substring(0, value).trim();// =号前面取首尾空
				b = sr.substring(value + 1, sr.length()).trim();// =号后面取首尾空
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
				if (a.equals("parent")) {
					parentOne = b;
					continue;
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
			textPane.addLastLine(e.getMessage());
		}
		textPane.addLastLine("url=" + url,true);
		textPane.addLastLine("driver=" + driver,true);
		textPane.addLastLine("user=" + user,true);
		textPane.addLastLine("password=" + password,true);
		textPane.addLastLine("parent=" + parentOne,true);
	}

		/**
		 * 默认值
		 * @return
		 * @throws UnsupportedEncodingException
		 */
		public  String wrString() throws UnsupportedEncodingException{
			String s="";
			s=s+"#注释符号为#\r\n"; 
			s=s+"#注意要区分大小写\r\n";
			s=s+"#连接数据库参数\r\n";
			s=s+"url=jdbc:oracle:thin:@127.0.0.1:1521:orcl\r\n";
			s=s+"driver=oracle.jdbc.OracleDriver\r\n";
			s=s+"user=maximo75\r\n";
			s=s+"password=maximo75\r\n";
			return s; 
		}
		public void setSetting(){
			jt_user.setText(user);
			jt_password.setText(password);
			jt_url.setText(url);
			jt_driver.setText(driver);
		}

		public void sakuseiTable(){
			JFrame jf=new JFrame("查询结果");
			Object rows[][] ={};
			String columns[] = { "序号", "内容"};

			tableModel=new DefaultTableModel(rows, columns);;

			table = new JTable(tableModel);

			JScrollPane jScrollPane = new JScrollPane(table);
			
			jf.add(jScrollPane);
			jf.setBounds(100, 100, 500, 300);
			jf.setVisible(false);
		}

		public void printConnection() throws BadLocationException{
			textPane.addLastLine("url=" + url, true);
			textPane.addLastLine("driver=" + driver, true);
			textPane.addLastLine("user=" + user, true);
			textPane.addLastLine("password=" + password, true);
		}
}










