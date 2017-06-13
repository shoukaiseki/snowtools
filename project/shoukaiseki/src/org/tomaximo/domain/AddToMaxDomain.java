package org.tomaximo.domain;


import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;

import org.shoukaiseki.gui.jtable.ExcelAdapter;
import org.shoukaiseki.gui.jtable.JTableOperating;
import org.shoukaiseki.gui.jtextfield.NumOnlyDocument;
import org.shoukaiseki.sql.ConnectionKonnfigu;
import org.tomaximo.finddetabash.ListApurikeshonnKannkei;
import org.tomaximo.tuuyouclass.KonntenntuOut;


public class AddToMaxDomain extends JFrame{
	/**
	 * 关闭時是否退出程序,默认時退出,只作为子程序时不退出
	 */
	private boolean windowclose = true;
	private boolean conCOR=false;//是否有提交数据状态
	private JTableOperating table;
	private JTextField bTextField;
	private KonntenntuOut JF_ConsoleOut;
	private JButton addRowButton,updateButton,commitButton,rollbackButton ;
	private JButton jb_cleanTable;
	private JToggleButton toggleButton;
	

	private String sql = "";
	private Connection con = null;
	private String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String user = "orclzhjq";
	private String password = "orclzhjq";
	
	public AddToMaxDomain() {
		this(null, true);
	}

	public AddToMaxDomain(boolean windowclose) {
		this(null, windowclose);
	}

	public AddToMaxDomain(Connection con) {
		this(con, true);
	}

	public AddToMaxDomain(Connection con, boolean windowclose) {
		try {
			JF_ConsoleOut = new KonntenntuOut(false);
			JF_ConsoleOut.setVisible(false);
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		init();
		this.con = con;
		String[] columnNames = { "域名(DOMAINID)", "值(VALUE)", "描述(DESCRIPTION)",
				"地点(SITEID)", "组织(ORGID)", "编号(NUM)", "错误提示" }; // 列名
		table = new JTableOperating(new DefaultTableModel(columnNames, 155));
		JScrollPane scrollPane = new JScrollPane(table); // 支持滚动
		// jdk1.6
		// 排序:
		// table.setRowSorter(new TableRowSorter(tableModel));
		table.init();
		ExcelAdapter myAd = new ExcelAdapter(table);
		// table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //单选

		scrollPane.setViewportView(table);
		Box panel = Box.createHorizontalBox(); // 横结构
		panel.add(new JLabel("需增加的行数: "));
		panel.add(Box.createHorizontalStrut(8));// 间距
		bTextField = new JTextField(new NumOnlyDocument(),"opamdop", 30);
		panel.add(bTextField);
		panel.add(Box.createHorizontalStrut(8));// 间距

		addRowButton = new JButton("增加行"); // 修改按钮
		addRowButton.addActionListener(new ActionListener() {// 添加事件
					public void actionPerformed(ActionEvent e) {
						new Timer().schedule(new TimerTask() {
							@Override
							public void run() {// 实例中的的方法
								if(!bTextField.getText().isEmpty()){
									int addRow=Integer.parseInt(bTextField.getText());

									for (int i = 0; i < addRow; i++) {
										table.addRow(new Object[] {});
									}
								}
							}
						}, 10);
					}
				});
		updateButton = new JButton("実行"); // 修改按钮
		updateButton.addActionListener(new ActionListener() {// 添加事件
					public void actionPerformed(ActionEvent e) {
						new Timer().schedule(new TimerTask() {
							@Override
							public void run() {// 实例中的的方法
								AddToAlnDomain();
								// System.out.println("冻干粉");
							}
						}, 10);
					}
				});
		 commitButton = new JButton("提交更改"); // 修改按钮
		 commitButton.addActionListener(new ActionListener() {// 添加事件
					public void actionPerformed(ActionEvent e) {
						conCommit();
					}
				});

		 rollbackButton = new JButton("回退提交"); // 修改按钮
		 rollbackButton.addActionListener(new ActionListener() {// 添加事件
					public void actionPerformed(ActionEvent e) {
							conRollback();
					}
				});
		toggleButton = new JToggleButton("情報");
		toggleButton.addActionListener(new ActionListener() {// 添加事件
					public void actionPerformed(ActionEvent e) {
						if (toggleButton.isSelected()) {
//							buttonstatus = true;
							setConsoleOutIsRight();
							table.setRowSelectionInterval(1, 2);
						} else {
							JF_ConsoleOut.setVisible(false);
//							buttonstatus = false;
						}
					}
				});
		panel.add(addRowButton);
		panel.add(Box.createHorizontalStrut(8));// 间距
		panel.add(updateButton);
		panel.add(Box.createHorizontalStrut(8));// 间距
		panel.add(commitButton);
		panel.add(Box.createHorizontalStrut(8));// 间距
		panel.add(rollbackButton);
		panel.add(Box.createHorizontalStrut(8));// 间距
		panel.add(toggleButton);

		jb_cleanTable = new JButton("清空表格"); // 修改按钮
		jb_cleanTable.addActionListener(new ActionListener() { // 插入文字的事件
			public void actionPerformed(ActionEvent e) {
				table.clearRowsAll();
			}
		});
		
		
		
		JLabel jl1 = new JLabel(
				"java -version "
						+ System.getProperty("java.version")
						+ "        Chiang Kai-shek(shoukaiseki) <jiang28555@gmail.com>  "
						+ "  2012-02-20 Tokyo japan ", SwingConstants.CENTER);

		Box boxS01 = Box.createVerticalBox(); // 竖结构
		Box boxH01 = Box.createHorizontalBox(); // 横结构
		Box boxH02 = Box.createHorizontalBox(); // 横结构
		
		boxH01.add(jl1);
		
		/**
		 * 按钮组
		 */
		boxH02.add(jb_cleanTable);
		boxH02.add(Box.createHorizontalStrut(8));// 间距
		
		
		boxS01.add(boxH01);
		boxS01.add(Box.createVerticalStrut(8)); // 两行的间距
		boxS01.add(boxH02);
		
		
		getContentPane().add(boxS01, BorderLayout.NORTH);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		getContentPane().add(panel, BorderLayout.SOUTH);
		this.setSize(700, 500);
		this.setLocation(200, 150);
		this.setTitle("添加数据到域中,域名得在maximo中指定!");
		this.setVisible(true);
		this.windowclose=windowclose;
		if (windowclose) {
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		}
	}

	/**
     * 
     */
	public void AddToAlnDomain() {
		
		
		try {
			int alndomainid=0;
			PreparedStatement pst = null;
			Statement st = con.createStatement();
				sql = "select alndomainid from alndomain order by alndomainid desc  ";
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				alndomainid = rs.getInt("alndomainid");
			}

			for (int row = 0; row < table.getRowCount(); row++) {
				table.setValueAt("", row, 6);
			}
			for (int row = 0; row < table.getRowCount(); row++) {

				/**
				 * 前6列的数据
				 *"域名(DOMAINID)", "值(VALUE)", "描述(DESCRIPTION)",
				 *"地点(SITEID)", "组织(ORGID)", "编号(NUM)", "错误提示"
				 */
					
				try {
					String domainid=(String)table.getValueAt(row, 0);
					String value=(String)table.getValueAt(row, 1);
					String description=(String)table.getValueAt(row, 2);
					String siteid=(String)table.getValueAt(row, 3);
					String orgid=(String)table.getValueAt(row, 4);
					String num=(String)table.getValueAt(row, 5);
					System.out.println("domainid="+domainid+",value="+value);
					if (!(domainid==null||value==null)) {
						String valueid=domainid+"|"+value;
						if(description==null){
							description="";
						}
						if(siteid==null){  
							siteid="";
						}
						if(orgid==null){
							orgid="";
						}else{
							valueid=domainid+"|"+value+"|"+orgid+"|"+siteid;
						}

						alndomainid++;
						
						sql = " insert into alndomain" +
								"(DOMAINID,VALUE,DESCRIPTION,SITEID,ORGID,NUM,alndomainid,valueid) " +
								"values('" +
								domainid+"','"+value+"','"+description+"','"+siteid+"','"
								+orgid+"','"+num+"',ALNDOMAINSEQ.NEXTVAL,'"+valueid+"')" ;
						pst = con.prepareStatement(sql);
						pst.execute();
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					table.setValueAt(e.getMessage(), row, 6);
				}
				
			}
			pst.close();
			printlnSeikou();
			conCOR=true;


		} catch (Exception e) {
			conCOR=false;
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}//异常则不更新
			e.printStackTrace();
			System.out.println(sql);
			println();
			println(sql);
			println(e.getMessage(),true);
			printlnSippai();
			// TODO: handle exception
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AddToMaxDomain atmd = new AddToMaxDomain();
		atmd.setDefuorutoConnection();

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


//		 url = "jdbc:oracle:thin:@10.59.67.22:1521:test22";
//		 user = "shoukaiseki";
//		 password = "shoukaiseki";				
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
			/**
			 * 关闭自动更新
			 */
			con.setAutoCommit(false);
			println("OK,成功连接到数据库");
			printlnSeikou();
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
			/**
			 * 关闭自动更新
			 */
			con.setAutoCommit(false);
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
		JF_ConsoleOut.println("------------命令成功执行完毕!-----------");
		setConsoleOutIsRight();
	}

	/**
	 * 输出SQL命令失败信息
	 * 
	 * @throws BadLocationException
	 */
	public void printlnSippai() {
		JF_ConsoleOut.println("------------命令执行错误??????-----------", true);
		setConsoleOutIsRight();
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
	/**
	 * con提交更改
	 * 要事先con.setAutoCommit(false);自动更新关闭掉
	 */
	public void conCommit(){
		try {
			if(conCOR){
				int suteetasu = JOptionPane.showConfirmDialog(null,
						"确定要提交更改吗?\n取消后将不提交,但是也不会取消更改.", "提示!!",
						JOptionPane.YES_NO_OPTION);
				if (suteetasu == 0) {
					con.commit();
					println("**********提交成功!**********",true);
					conCOR=false;
				}else{
					println("**********已取消提交!**********",true);
				}
			}else{
				println("**********无可提交更改数据!**********",true);
				setConsoleOutIsRight();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			
			conRollback();
			e.printStackTrace();
		}
	}
	/**
	 * 
	 */
	public void conRollback() {
			try {
				if(conCOR){
					con.rollback();
					println("**********本地更改已清除!**********",true);
					setConsoleOutIsRight();
				}else{
					println("**********无可清除数据!**********",true);
					setConsoleOutIsRight();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			conCOR=false;
	}
	public void printConnection() throws BadLocationException{
		println("url=" + url, true);
		println("driver=" + driver, true);
		println("user=" + user, true);
		println("password=" + password, true);
	}
}

