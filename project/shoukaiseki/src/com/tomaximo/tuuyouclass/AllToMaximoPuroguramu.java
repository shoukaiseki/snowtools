package com.tomaximo.tuuyouclass;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;

import oracle.javatools.ui.layout.VerticalFlowLayout;

import com.shoukaiseki.characterdetector.CharacterEncoding;
import com.shoukaiseki.characterdetector.utf.UnicodeReader;
import com.shoukaiseki.constantlib.CharacterEncodingName;
import com.shoukaiseki.gui.flowlayout.ModifiedFlowLayout;
import com.shoukaiseki.gui.jinternalframe.MCOCInternalFrameK;
import com.shoukaiseki.gui.jtextpane.JTextPaneDoc;
import com.shoukaiseki.sql.ConnectionKonnfigu;
import com.tomaximo.domain.AddToMaxDomain;
import com.tomaximo.finddetabash.DomeinnNoAtai;
import com.tomaximo.finddetabash.FindTheField;
import com.tomaximo.finddetabash.ListApurikeshonnKannkei;
import com.tomaximo.kks.ImpKksGui;




public class AllToMaximoPuroguramu extends JFrame{

	private JTabbedPane tabTop = new JTabbedPane(JTabbedPane.TOP);
    private JTabbedPane tabBottom = new JTabbedPane(JTabbedPane.BOTTOM);
    private JTabbedPane tabLeft = new JTabbedPane(JTabbedPane.LEFT);
	private JTabbedPane tabLeft2 = new JTabbedPane(JTabbedPane.TOP);
	private JSplitPane jspLeft,jspBotton;
	/**
	 * 关闭時是否退出程序,默认時退出,只作为子程序时不退出
	 */
	private boolean windowclose = true;

	private String fileName = "./AllToMaximoPuroguramu.ini";
	public File txtfile = new File(fileName);
	
	private JPanel jp;
	private JButton jb_cleantext,jb_readsetting;// 清空记录,重读配置
	private JButton JComboBox[]=new JButton[6]; 
	private JTextPaneDoc textPane;

	private Connection con = null;
	private String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String user = "orclzhjq";
	private String password = "orclzhjq";
	private JComboBox comboBox ;
	private Map<String, ConnectionKonnfigu> konnfigumap=new HashMap();
	private ConnectionKonnfigu ck=null;
	private String defuorutomeishou="";
	
	
	public AllToMaximoPuroguramu() throws BadLocationException {
    	this(true);
	}
	public AllToMaximoPuroguramu(boolean windowclose){
		this.windowclose=windowclose;
		jspBotton=new JSplitPane(JSplitPane.VERTICAL_SPLIT,tabTop,tabBottom);
		jspLeft= new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
        		tabLeft,jspBotton);
		tabLeft.add(tabLeft2);
		jspBotton.setDividerLocation(300);
		jspLeft.setDividerLocation(300);

		Box jp = Box.createVerticalBox(); // 竖结构
		Container c = this.getContentPane();
		String str1="<html><body align=\"center\">ImpKksGui<br>导入KKS</body></html>";
		JComboBox[0]=new JButton("ImpKksGui");
		JComboBox[0]=new JButton(str1);
		jp.add(JComboBox[0]);
		
		str1="<html><body align=\"center\">AddToMaxDomain<br>添加到域</body></html>";
		JComboBox[1]=new JButton(str1);
		jp.add(JComboBox[1]);

		str1="<html><body align=\"center\">ListApurikeshonnKannkei<br>列出应用对应信息</body></html>";
		JComboBox[2]=new JButton(str1);
		jp.add(JComboBox[2]);

		str1="<html><body align=\"center\">FindTheField<br>查找数据库</body></html>";
		JComboBox[3]=new JButton(str1);
		jp.add(JComboBox[3]);

		str1="<html><body align=\"center\">DomeinnNoAtai<br></body></html>";
		JComboBox[4]=new JButton(str1);
		jp.add(JComboBox[4]);
		
		JComboBox[5]=new JButton("5");
		


		textPane = new JTextPaneDoc();
		textPane.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		textPane.setText("信息输出窗口!");
		textPane.setFont(new Font("宋体", Font.BOLD, 13));
		textPane.setLayout(new ModifiedFlowLayout());// 加不加都感觉有效果,如果一段英文无空格就会出现不会自动换行
		JScrollPane jsp = new JScrollPane(textPane);// 新建一个滚动条界面，将文本框传入
		// 滚动条
		jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		

		JLabel jl1 = new JLabel(
				"java -version "
						+ System.getProperty("java.version")
						+ "        Chiang Kai-shek(shoukaiseki) <jiang28555@gmail.com>  "
						+ "  2012-02-20 Tokyo japan ",SwingConstants.CENTER);

		Box box = Box.createVerticalBox(); // 竖结构

		Box box11 = Box.createHorizontalBox(); // 横结构 
		
		JPanel jp1=new JPanel();
		comboBox = new JComboBox();
		comboBox.setToolTipText("配置名称");

		jb_cleantext=new JButton("清空");
		jb_readsetting=new JButton("重新读入配置");
		
		
		
		box11.add(new JLabel("配置名称:"));
		box11.add(Box.createHorizontalStrut(8)); 
		box11.add(comboBox);
		box11.add(Box.createHorizontalStrut(8)); 
		box11.add(jb_readsetting);
		box11.add(Box.createHorizontalStrut(8)); 
		box11.add(jb_cleantext);
		
		
		jp1.add(jl1);
		
		box.add(jp1);
		box.add(Box.createVerticalStrut(8)); // 两行的间距
		box.add(jp);
		box.add(Box.createVerticalStrut(8)); // 两行的间距
		box.add(box11);

//		c.add(box,BorderLayout.NORTH);
		
		
		VerticalFlowLayout vfl=new VerticalFlowLayout();
//		as.addLayoutComponent("as",jp1);
		
		MCOCInternalFrameK mc=new MCOCInternalFrameK();
		mc.setSize(300, 200);
		mc.setTitle("流程信息");
		mc.setVisible(true);
		Container mcc=mc.getContentPane();
		mcc.add(jp1);
		mcc.add(jp);
		mcc.add(box11);
		
		mc.hideNorthPanel();
		
		mcc.setLayout(vfl);
		tabLeft.add(mcc,"程序");

		c.add(jp1, BorderLayout.NORTH);
		tabBottom.add(jsp,"情報");
		c.add(jspLeft, BorderLayout.CENTER);
		this.setSize(700,500);
		this.setLocation(200,150);
		this.setTitle("运行maximo程序合集");
		this.setVisible(true);
		

  		if (this.windowclose) { 
  			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
  		}
		textPane.cleanText();
		
	}
	public static void main(String ages[]) throws BadLocationException, IOException{
		AllToMaximoPuroguramu amp=new AllToMaximoPuroguramu();
		amp.readSetting();
		amp.callJcomboBox();
	}
	public void callJcomboBox(){
		JComboBox[0].addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {

				new Timer().schedule (new TimerTask() {
					@Override
					public void run(){//实例中的的方法
						try {
							ImpKksGui kks =new ImpKksGui(false);
							kks.readSetting();
							kks.setConnection(konnfigumap.get(defuorutomeishou));
							
							// TODO Auto-generated method stub
							BufferedReader input = new BufferedReader(new InputStreamReader(
									System.in));
						} catch (BadLocationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, 10);
			}
		});

		JComboBox[1].addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {

				new Timer().schedule (new TimerTask() {
					@Override
					public void run(){//实例中的的方法
						AddToMaxDomain atmd = new AddToMaxDomain(false);
						try {
							atmd.setConnection(konnfigumap.get(defuorutomeishou));
						} catch (BadLocationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, 10);
			}
		});

		JComboBox[2].addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {

				new Timer().schedule (new TimerTask() {
					@Override
					public void run(){//实例中的的方法
						
						ListApurikeshonnKannkei lak = new ListApurikeshonnKannkei(false);
						try {
							lak.setConnection(konnfigumap.get(defuorutomeishou));
						} catch (BadLocationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, 10);
			}
		});

		JComboBox[3].addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {

				new Timer().schedule (new TimerTask() {
					@Override
					public void run(){//实例中的的方法
						FindTheField ftf=new FindTheField(false);
//						ftf.setConnection(con);
						try {
							ftf.setConnection(konnfigumap.get(defuorutomeishou));
						} catch (BadLocationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ftf.setComboBox();
					}
				}, 10);
			}
		});

		JComboBox[4].addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {

				new Timer().schedule (new TimerTask() {
					@Override
					public void run(){//实例中的的方法
						
						DomeinnNoAtai dna = new DomeinnNoAtai(false);
						try {
							dna.setConnection(konnfigumap.get(defuorutomeishou));
						} catch (BadLocationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, 10);
			}
		});
		JComboBox[5].addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {

				new Timer().schedule (new TimerTask() {
					@Override
					public void run(){//实例中的的方法
					}
				}, 10);
			}
		});
		

		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = ((JComboBox) e.getSource()).getSelectedIndex();// 获取到选中项的索引
				defuorutomeishou = ((JComboBox) e.getSource()).getSelectedItem()
						.toString();// 获取到项

				if(defuorutomeishou==null){
					return;
				}
				new Timer().schedule (new TimerTask() {
					@Override
					public void run(){//实例中的的方法
						// TODO Auto-generated method stub
						try {
							setCon(konnfigumap.get(defuorutomeishou));
						} catch (BadLocationException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}, 10);

			}
		});
		/**
		 * 清空内容
		 */
		jb_cleantext.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				textPane.cleanText();
			}
		});
		/**
		 * 重新读入配置
		 */
		jb_readsetting.addActionListener(new ActionListener() { 
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
		
	}
	
	

	/**
	 * 
	 */
	public void println(String konntenntu) {
		println(konntenntu, false);
	}
	/**
	 * 
	 */
	public void println(String konntenntu,boolean b) {
		try {
			textPane.addLastLine(konntenntu, b);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void readSetting() throws IOException, BadLocationException {
		
		newFile();// 无配置文件则自动新建文件
		reFile();// 读取配置文件
//		setCon();// 载入驱动
	}

	public void newFile() throws IOException {
		// 新建文件
		if (!txtfile.exists()) {
			if (txtfile.createNewFile()) {
				textPane.addLastLine("配置文件创建成功!");
				wrFile();// 写入文件
			} else {
				textPane.addLastLine("创建新文件失败!");
			}
		} else {
			textPane.addLastLine("发现配置文件" + fileName + "!");
		}
	}

	public void wrFile() {
		textPane.addLastLine("正在创建文件" + txtfile.getPath());
		try {
			String age0 = wrString();
			// 常量类:各编码名称
			CharacterEncodingName ce = new CharacterEncodingName();
			FileOutputStream o = new FileOutputStream(txtfile);
			// 采用UTF-8编码格式输出
			OutputStreamWriter out = new OutputStreamWriter(o, ce.UTF_8);
			out.write(age0);
			textPane.addLastLine("文件创建写入成功");
			out.close();
		} catch (Exception e) {
			// TODO: handle exception
			textPane.addLastLine(e.getMessage());
		}
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
					konnfigumap.put(konnfigumeishou,new ConnectionKonnfigu(url, driver, user, password) );
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
					comboBox.addItem(konnfigumeishou);
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
			comboBox.setSelectedItem(defuorutomeishou);
			this.setCon(konnfigumap.get(defuorutomeishou));
			
		} catch (IOException e) {
			e.printStackTrace();
			textPane.addLastLine(e.getMessage());
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	public void setCon() throws BadLocationException {
		try {
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
	}
	public void setCon(ConnectionKonnfigu ck) throws BadLocationException {
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
	}
	
	public void printConnection() throws BadLocationException{
		textPane.addLastLine("url=" + url, true);
		textPane.addLastLine("driver=" + driver, true);
		textPane.addLastLine("user=" + user, true);
		textPane.addLastLine("password=" + password, true);
	}

	public void printlnConsole(int i){
		System.out.println(""+i);
	}
	
	public void printlnConsole(String s){
		System.out.println(s);
	}
}







