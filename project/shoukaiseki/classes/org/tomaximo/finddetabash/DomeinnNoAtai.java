package org.tomaximo.finddetabash;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;

import org.shoukaiseki.gui.jtable.ExcelAdapter;
import org.shoukaiseki.gui.jtable.JTableOperating;
import org.shoukaiseki.sql.ConnectionKonnfigu;
import org.tomaximo.tuuyouclass.KonntenntuOut;


public class DomeinnNoAtai extends JFrame {
	/**
	 * 关闭時是否退出程序,默认時退出,只作为子程序时不退出
	 */
	private boolean windowclose = true;

	private boolean buttonstatus = true;
	private JTableOperating table;
	private JTextField bTextField;
	private KonntenntuOut JF_ConsoleOut;

	private JToggleButton toggleButton;
	private JCheckBox domeinnTrim,betubetunihyouji; 

	private String[] konntenntu;
	private ResultSet r ;
	

	private String sql = "";
	private Connection con = null;
	private String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String user = "orclzhjq";
	private String password = "orclzhjq";
	
	
	public DomeinnNoAtai() {
		this(null, true);
	}

	public DomeinnNoAtai(boolean windowclose) {
		this(null, windowclose);
	}

	public DomeinnNoAtai(Connection con) {
		this(con, true);
	}

	public DomeinnNoAtai(Connection con, boolean windowclose) {
		try {
			JF_ConsoleOut = new KonntenntuOut(false);
//			JF_ConsoleOut.setVisible(false);//使用后报错
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		this.con = con;
//		String[] columnNames = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
//				"J", "K", "L" }; // 列名
		String[] columnNames = { "A", "B"}; // 列名

		columnNames= new String[]{ "域名(DOMAINID)", "值(VALUE)", "描述(DESCRIPTION)",
				"地点(SITEID)", "组织(ORGID)", "编号(NUM)" ,"G","H","I","J"};
		
		table = new JTableOperating(new DefaultTableModel(columnNames, 55));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JScrollPane scrollPane = new JScrollPane(table); // 支持滚动
//		scrollPane.setViewportView(table);
		
		// jdk1.6
		// 排序:
//		 table.setRowSorter(new TableRowSorter(tableModel));
		
		ExcelAdapter myAd = new ExcelAdapter(table);
		// table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //单选

		domeinnTrim = new JCheckBox("域名字首尾空",true);
		betubetunihyouji=new JCheckBox("分开显示");
		
		scrollPane.setViewportView(table);
		Box panel = Box.createHorizontalBox(); // 横结构
		panel.add(new JLabel("应用名: "));
		panel.add(Box.createHorizontalStrut(8));// 间距
		bTextField = new JTextField("opamdop", 30);
		panel.add(bTextField);
		panel.add(Box.createHorizontalStrut(8));// 间距
		panel.add(betubetunihyouji);
		panel.add(Box.createHorizontalStrut(8));// 间距
		panel.add(domeinnTrim);
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
		this.setTitle("列出指定域内所有的值");
		this.setVisible(true);
		if (windowclose) {
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		}
		init();
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
		String[] domeinnNamae = bTextField.getText().toUpperCase().split(",");
		 konntenntu = new String[10];
		try {
			Statement st = con.createStatement();
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
			println(sql);
			setConsoleOutIsRight();
			// TODO: handle exception
		}
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
	 */
	public static void main(String[] args) {
		DomeinnNoAtai dna = new DomeinnNoAtai();
		dna.setDefuorutoConnection();
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


