package com.tomaximo.kks.impkksgui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import com.shoukaiseki.characterdetector.CharacterEncoding;
import com.shoukaiseki.gui.flowlayout.ModifiedFlowLayout;
import com.shoukaiseki.gui.jtextpane.JTextPaneOperating;


public class ImpKksGui_Setting extends JFrame implements ScrollPaneConstants {
	private String fileName="./ImpKksGui.ini";
	
	private SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private JTextField shoukaiseki_insert_kks;// 新增KKS编码存放至该表shoukaiseki_insert_kks
	private JTextField locsystem ;
	private JTextField locations ; 
	private JTextField locstatus ;
	private JTextField locoper ;
	private JTextField lochierarchy ;
	private JTextField locancestor;

	private Box box = null;
	private JTextPaneOperating textPane;
	private JLabel jl;
	private JButton jb_apurikesyonn = null, jb_kettei = null, jb_kyannseru = null,
			jb_defuoruto = null; // 应用按钮;确定按钮;取消按钮;默认按钮
	/**
	 * 连接名,用户名,口令,主机名,端口,连接符标识
	 */
	private JTextField jtname,jtuser,jtlocalhost,jtport,jtsid;
	private JPasswordField jtpassword;



	/**
	 * 
	 */
	public ImpKksGui_Setting() {


		shoukaiseki_insert_kks =new JTextField("shoukaiseki_insert_kks", 8);
		locsystem = new JTextField("locsystem", 8);
		locations  = new JTextField("locations", 8);
		locstatus  = new JTextField("locstatus", 8);
		locoper  = new JTextField("locoper", 8);
		lochierarchy  = new JTextField("lochierarchy", 8);
		locancestor = new JTextField("locancestor", 8);


		textPane = new JTextPaneOperating();
		textPane.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		textPane.setText("这里设置文本框内容!");
		textPane.setFont(new Font("宋体", Font.BOLD, 13));
		textPane.setLayout(new ModifiedFlowLayout());// 加不加都感觉有效果,如果一段英文无空格就会出现不会自动换行

		jb_apurikesyonn = new JButton("应用");
		jb_kettei = new JButton("确定");
		jb_kyannseru = new JButton("取消");
		jb_defuoruto = new JButton("默认");
		jb_apurikesyonn.addActionListener(new ActionListener() { // 插入文字的事件
					public void actionPerformed(ActionEvent e) {
						
						readFile();
					}
				});
		jb_kettei.addActionListener(new ActionListener() { // 插入文字的事件
					public void actionPerformed(ActionEvent e) {
					}
				});

		jb_kyannseru.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				delHyou();
			}
		});

		jb_defuoruto.addActionListener(new ActionListener() { // 插入文字的事件
					public void actionPerformed(ActionEvent e) {

						int suteetasu=JOptionPane.showConfirmDialog(null,"确定要恢复默认值吗?", "提示!!", 
								JOptionPane.YES_NO_OPTION);
						if(suteetasu==0){
							new Timer().schedule(new TimerTask() {
								@Override
								public void run() {//实例中的的方法
									setDefuoruto();//定时器到后执行的方法,一般在定时器到后的内容具体在外面写
								}
							}, 10);
						}
					}
				});

		// 设置主框架的布局
		Container c = getContentPane();
		// c.setLayout(new BorderLayout());
		
		JScrollPane jsp = new JScrollPane(textPane);// 新建一个滚动条界面，将文本框传入
		// 滚动条
		jsp.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);


		jl = new JLabel("状态无异常!", SwingConstants.LEFT);
		JLabel jl1 = new JLabel(
				"java -version "
						+ System.getProperty("java.version")
						+ "        Chiang Kai-shek(shoukaiseki) <jiang28555@gmail.com>  "
						+ "  2012-02-20 Tokyo japan ");
		// jl1.setBorder(BorderFactory.createEtchedBorder());//分界线
		jl1.setFont(new Font("标楷体 ", Font.BOLD, 13)); 
		Box box = Box.createVerticalBox(); // 竖结构
		Box box1 = Box.createHorizontalBox(); // 横结构
		Box box10 = Box.createHorizontalBox(); // 横结构
		Box box11 = Box.createHorizontalBox(); // 横结构

		
		
		box11.add(jl1);

		box.add(box11);
		box.add(Box.createVerticalStrut(8)); // 两行的间距
		box.add(box1);
		box.add(Box.createVerticalStrut(8)); // 两行的间距
		box.add(box10);
		
		box.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8)); // 8个的边距


		jtname=new JTextField("jtname", 8);
		jtuser=new JTextField("jtuser", 8);
		jtpassword=new JPasswordField("jtpassword", 8);
		jtlocalhost=new JTextField("jtlocalhost", 8);
		jtport=new JTextField("jtport", 8);
		jtsid=new JTextField("jtsid", 8);
		

		JPanel jp01a = new JPanel();
		JPanel jp01b = new JPanel();
		Box box01a = Box.createVerticalBox(); // 竖结构
		Box box01b = Box.createVerticalBox(); // 竖结构
		Box box01c = Box.createVerticalBox(); // 竖结构
		Box box01d = Box.createVerticalBox(); // 竖结构
		
		int i1=12;
		box01a.add(new JLabel("连接名"));
		box01a.add(Box.createVerticalStrut(i1)); // 两行的间距
		box01a.add(new JLabel("用户名"));
		box01a.add(Box.createVerticalStrut(i1)); // 两行的间距
		box01a.add(new JLabel("口令"));
		box01a.add(Box.createVerticalStrut(i1)); // 两行的间距
		box01a.add(new JLabel("主机名"));
		box01a.add(Box.createVerticalStrut(i1)); // 两行的间距
		box01a.add(new JLabel("端口"));
		box01a.add(Box.createVerticalStrut(i1)); // 两行的间距
		box01a.add(new JLabel("SID"));
		
		box01b.add(jtname);
		box01b.add(Box.createVerticalStrut(8)); // 两行的间距
		box01b.add(jtuser);
		box01b.add(Box.createVerticalStrut(8)); // 两行的间距
		box01b.add(jtpassword);
		box01b.add(Box.createVerticalStrut(8)); // 两行的间距
		box01b.add(jtlocalhost);
		box01b.add(Box.createVerticalStrut(8)); // 两行的间距
		box01b.add(jtport);
		box01b.add(Box.createVerticalStrut(8)); // 两行的间距
		box01b.add(jtsid);
		
		i1=12;
		box01c.add(new JLabel("locsystem "));
		box01c.add(Box.createVerticalStrut(i1)); // 两行的间距
		box01c.add(new JLabel("locations "));
		box01c.add(Box.createVerticalStrut(i1)); // 两行的间距
		box01c.add(new JLabel("locstatus "));
		box01c.add(Box.createVerticalStrut(i1)); // 两行的间距
		box01c.add(new JLabel("locoper"));
		box01c.add(Box.createVerticalStrut(i1)); // 两行的间距
		box01c.add(new JLabel("lochierarchy"));
		box01c.add(Box.createVerticalStrut(i1)); // 两行的间距
		box01c.add(new JLabel("locancestor"));


		box01d.add(locsystem );
		box01d.add(Box.createVerticalStrut(8)); // 两行的间距
		box01d.add(locations);
		box01d.add(Box.createVerticalStrut(8)); // 两行的间距
		box01d.add(locstatus );
		box01d.add(Box.createVerticalStrut(8)); // 两行的间距
		box01d.add(locoper);
		box01d.add(Box.createVerticalStrut(8)); // 两行的间距
		box01d.add(lochierarchy);
		box01d.add(Box.createVerticalStrut(8)); // 两行的间距
		box01d.add(locancestor);

		box1.add(Box.createHorizontalStrut(12));// 间距
		box1.add(box01a);
		box1.add(Box.createHorizontalStrut(12));// 间距
		box1.add(box01b);
		box1.add(Box.createHorizontalStrut(22));// 间距
		box1.add(box01c);
		box1.add(Box.createHorizontalStrut(12));// 间距
		box1.add(box01d);
		box1.add(Box.createHorizontalStrut(12));// 间距
		

		box10.add(Box.createHorizontalStrut(12));// 间距
		box10.add(new JLabel("KKS编码存放表"));
		box10.add(Box.createHorizontalStrut(12));// 间距
		box10.add(shoukaiseki_insert_kks);
		box10.add(Box.createHorizontalStrut(12));// 间距
		box10.add(jb_defuoruto);
		box10.add(Box.createHorizontalStrut(8));// 间距
		box10.add(jb_kettei);
		box10.add(Box.createHorizontalStrut(8));// 间距
		box10.add(jb_kyannseru);
		box10.add(Box.createHorizontalStrut(8));// 间距
		box10.add(jb_apurikesyonn);

		

		Box box2 = Box.createHorizontalBox(); // 横结构
		box2.add(new JLabel("状态栏:", SwingConstants.LEFT));
		box2.add(Box.createHorizontalStrut(8));// 间距
		box2.add(jl);

		textPane.setBorder(BorderFactory.createEtchedBorder());// 分界线
		jsp.setBorder(BorderFactory.createEtchedBorder());// 分界线
		jsp.setBorder(BorderFactory
				.createTitledBorder("説明:"));
		c.add(box, BorderLayout.NORTH);
		c.add(jsp, BorderLayout.CENTER);
		c.add(box2, BorderLayout.SOUTH);

		setSize(700, 500);
		// 隐藏frame的标题栏,此功暂时关闭，以方便使用window事件
		setLocation(200, 150);

		setTitle("Kks导入程序-参数设置");
		show();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ImpKksGui_Setting kks = new ImpKksGui_Setting();
		// TODO Auto-generated method stub
		BufferedReader input = new BufferedReader(new InputStreamReader(
				System.in));
		kks.setCon();
	}

	public void setCon() {
	}

	/**
	 * 新建子表
	 */
	public String readFile() {

		String content="";
		try {
			FileInputStream in = new FileInputStream(fileName);
			System.out.println("文件字数为="+in.available());
			
			byte[] bytes = new byte[in.available()];
			while((in.read(bytes)) != -1);
			String code=CharacterEncoding.getLocalteFileEncode(fileName);
			System.out.println("文件编码格式为"+code); 
			
			content= new String(bytes,code);//按照文件编码格式进行转换为系统编码
//			System.out.println("文件内容为\n"+content); 
//			printText.addLastLine(content);
			in.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return content;
	}

	/**
	 * 恢复默认值
	 */
	public void setDefuoruto() {

		shoukaiseki_insert_kks.setText("shoukaiseki_insert_kks");
		locsystem.setText("locsystem");
		locations.setText("locations");
		locstatus.setText("locstatus");
		locoper.setText("locoper");
		lochierarchy.setText("lochierarchy");
		locancestor.setText("locancestor");
		jtname.setText("Linux Fedora");
		jtuser.setText("maximo");
		jtpassword.setText("maximo");
		jtlocalhost.setText("localhost");
		jtport.setText("1521");
		jtsid.setText("orcl");
	}
	
	/**
	 * 删除表按钮执行方法
	 */
	public void delHyou() {
		
	}
	
	/**
	 * 
	 */
	public void expToHyou() {

	}

	public void impToHyou() {
		

	}

}


