package org.tomaximo.finddetabash;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.shoukaiseki.gui.jtable.ExcelAdapter;
import org.shoukaiseki.gui.jtable.JTableOperating;


class JikkouSql extends JFrame{
	/**
	 * 关闭時是否退出程序,默认時退出,只作为子程序时不退出
	 */
	private boolean windowclose = true;

	private String sql="";
	private Connection con = null;
    private DefaultTableModel tableModel;   //表格模型对象
    private JTableOperating table;
    private JTextField bTextField;

    private DefaultTableModel konntenntuTableModel;   //表格模型对象
    private JTableOperating konntenntuTable;
    JikkouSql()
    {
    	this(null,true);
    }
    JikkouSql(boolean windowclose) {
    	this(null,windowclose);
	}
     JikkouSql(Connection con)
    {
    	this(con,true);
    }
     JikkouSql(Connection con,boolean windowclose){
    	 	this.con=con;
          String[] columnNames = { "序号", "内容"};   //列名
          String [][]tableVales={{"A1","B1"},{"A2","B2"},{"A3","B3"},{"A4","B4"},{"A5","B5"}}; //数据
          tableModel = new DefaultTableModel(tableVales,columnNames);
          table = new JTableOperating(tableModel);  
          JScrollPane scrollPane = new JScrollPane(table);   //支持滚动
          getContentPane().add(scrollPane,BorderLayout.CENTER);
          //jdk1.6
          //排序:
          //table.setRowSorter(new TableRowSorter(tableModel));
          table.init();
  			ExcelAdapter myAd = new ExcelAdapter(table);
//          table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //单选
          table.addMouseListener(new MouseAdapter(){    //鼠标事件
              public void mouseClicked(MouseEvent e){
                  int selectedRow = table.getSelectedRow(); //获得选中行索引
                  Object ob = tableModel.getValueAt(selectedRow, 1);
                  bTextField.setText(ob.toString());
                  sql=ob.toString();
              }
          });
          scrollPane.setViewportView(table);
  		Box panel = Box.createHorizontalBox(); // 横结构
          getContentPane().add(panel,BorderLayout.SOUTH);
          panel.add(new JLabel("sql语句: "));
          bTextField = new JTextField("select sysdate from dual",30);
          panel.add(bTextField);
//          final JButton addButton = new JButton("添加");   //添加按钮
//          addButton.addActionListener(new ActionListener(){//添加事件
//              public void actionPerformed(ActionEvent e){
//                  String []rowValues = {aTextField.getText(),bTextField.getText()};
//                  tableModel.addRow(rowValues);  //添加一行
//                  int rowCount = table.getRowCount() +1;   //行数加上1
//                  aTextField.setText("A"+rowCount);
//                  bTextField.setText("B"+rowCount);
//              }
//          });
//          panel.add(addButton);  
          
          final JButton updateButton = new JButton("按此句执行");   //修改按钮
          updateButton.addActionListener(new ActionListener(){//添加事件
              public void actionPerformed(ActionEvent e){
  				new Timer().schedule (new TimerTask() {
  					@Override
  					public void run(){//实例中的的方法
  						selectSQL();
  					}
  				}, 10);
              }
          });
          panel.add(updateButton);
          
//          final JButton delButton = new JButton("删除");
//          delButton.addActionListener(new ActionListener(){//添加事件
//              public void actionPerformed(ActionEvent e){
//                  int selectedRow = table.getSelectedRow();//获得选中行的索引
//                  if(selectedRow!=-1)  //存在选中行
//                  {
//                      tableModel.removeRow(selectedRow);  //删除行
//                  }
//              }
//          });
//          panel.add(delButton);
          String[] s1 = { "序号", "内容"};   //列名
          String [][]s2={}; //数据

          konntenntuTableModel = new DefaultTableModel(s2,s1);
          konntenntuTable = new JTableOperating(konntenntuTableModel);
          konntenntuTable.init();
          myAd = new ExcelAdapter(konntenntuTable);
          JScrollPane jsp = new JScrollPane(konntenntuTable);   //支持滚动
          JPanel jp=new JPanel();
          jp.add(jsp);
          Dimension d = new Dimension(300, 400);
//          konntenntuTable.setPreferredSize(d);
          jsp.setPreferredSize(d);
//          jp.setPreferredSize(d);
          jsp.setBorder(BorderFactory.createTitledBorder("内容"));
          getContentPane().add(jp,BorderLayout.EAST);

  		this.setSize(700,500);
  		this.setLocation(200,150);
  		this.setTitle("执行SQL语句");
  		this.setVisible(true);
  		if (windowclose) {
  			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
  		}
    }
    /**
     * 清空表格
     */
    public void removeRowTabel(){
    	 for (int index = table.getModel().getRowCount() - 1; index >= 0; index--) {
    		 tableModel.removeRow(index);
         }
    }
    /**
     * 加入一行
     * @param s1
     * @param s2
     */
    public void addRowTable(String[] rowValues){
    	tableModel.addRow(rowValues);  //添加一行
    }
    /**
     * 碰到CLOB类型字段的会报错
     */
    public void selectSQL(){
    	try {	
			sql=bTextField.getText();
			
			Statement st = con.createStatement();
			ResultSet r = st.executeQuery(sql);

			int endtj = 0;// 统计总更新数据条数

			Calendar cal = Calendar.getInstance();
			java.util.Date data = new java.util.Date();
			cal.setTime(data);// 设置时间为当前时间
			long timeOne = cal.getTimeInMillis();

			int rownum = 0;

	    	 for (int index = konntenntuTable.getModel().getRowCount() - 1; index >= 0; index--) {
	    		 konntenntuTableModel.removeRow(index);
	         }
			while (r.next()) {
				endtj++;
				String s=r.getString(1);
				konntenntuTableModel.addRow(new String[]{""+endtj,s});
				
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(sql);
			// TODO: handle exception
		}
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
//		 String url = "jdbc:oracle:thin:@192.168.2.101:1521:zhjqmaximo";
		 String driver="oracle.jdbc.driver.OracleDriver";
		 String user = "maximo75";
		 String password = "maximo75";
		 Connection con = null;
			try {
				// （1）装载并注册数据库的JDBC驱动程序
				// 载入JDBC驱动：oracle安装目录下的jdbc\lib\classes12.jar
				System.out.println("正在试图加载驱动程序 "+driver);
				Class.forName(driver);
				System.out.println("驱动程序已加载");
				// 注册JDBC驱动：有些地方可不用此句
				System.out.println("url=" + url);
				System.out.println("user=" + user);
				System.out.println("password=" + password);
				System.out.println("正在试图连接数据库--------");
				java.sql.DriverManager
						.registerDriver(new oracle.jdbc.driver.OracleDriver());

				con = DriverManager.getConnection(url, user, password);
				System.out.println("OK,成功连接到数据库");

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			new JikkouSql(con,true);
	}
}

