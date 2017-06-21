package org.tomaximo.kks;


/**
 * 1.树结构 LOCHIERARCHY的SYSTEMID来自LOCSYSTEM的SYSTEMID
 * 2.LOCATIONS.LOCATIONS=LOCHIERARCHY.LOCATIONS,显示的名称为LOCATIONS.DESCRIPTION
配置文件为运行目录下的ImpKksGui.ini文件

1.点击创建表格来创建KKS导入時用的表,表名默认为shoukaiseki_insert_kks
	创建完成后将EXCEL中的KKS数据导入该表中



2.将多个EXCEL文件(下文称为子EXCEL)存放至一个EXCEL(下文称为总EXCEL)文件中
主要字段描述:
ID				在EXCEL总表导入時给每位编上EXCEL中的行号
PARENT  		个人认为应该称为"依赖","索引",详见"依赖关系"按钮
CFL 			重复LOCATION值的标识,详见下面"重复编码"按钮使用
CFD
ALLCFL
ALLCFD	
SETUMEI			备用字段,个人习惯为存放"子EXCEL"名
SETUMEINUM		备用字段,个人习惯用于存放"子EXCEL"的行号
SETUME2			存放与locations表location字段重复的行的DESCRIPTION值,如果description值相同则此值为locations表中的location字段值


3.按钮説明
创建表格  		创建KKS导入時用的表,表名默认为shoukaiseki_insert_kks
删除表格		删除shoukaiseki_insert_kks表
删除空行		删除shoukaiseki_insert_kks中的空行
清首尾空		清除shoukaiseki_insert_kks表的location和description字段首尾空
重复编码		先查询shoukaiseki_insert_kks表,对location重复的行进行标识,CFL为标识位,相同的为同一编号,编号从10000开始,如果description也相同则设置CFD与CFL同值.  再查询shoukaiseki_insert_kks表的location字段与locations的location字段,ALLCFL字段为标识位,两表之间的location字段一样时对ALLCFL值进行标识,如果description值相同则将ALLCFD也进行标识.
依赖关系		即location值之间的子父级关系,形成树结构的关键,事先了解树结构.
LOCSYSTEM表的SYSTEMID为系统名称,该名默认值为"PRIMARY",下面就已SYSTEMID为"PRIMARY"为基准,显示该系统下的最高索引符合条件为:
①LOCHIERARCHY.SYSTEMID='PRIMARY'
②LOCHIERARCHY.PARENT is null
③EXISTS(SELECT a.description FROM LOCATIONS a WHERE a.location=c.location)
参考如下(最高索引只显示一条,其它SITEID等字段请按实际插入,与LOCHIERARCHY.CHILDREN字段无关,调试系统为MAXIMO7.2,仅供参考,事实为准)
select distinct C.location,(select a.DESCRIPTION from LOCATIONS a where a.location=C.location )
from LOCATIONS a, LOCSYSTEM B,LOCHIERARCHY C where C.SYSTEMID='PRIMARY' and C.parent                                                              is null
AND exists (SELECT a.description FROM LOCATIONS a WHERE a.location=c.location) and rownum=1
最高索引(假如为 ASUS)的子索引为SELECT * FROM LOCHIERARCHY WHERE LOCHIERARCHY.PARENT='ASUS' AND SYSTEMID='PRIMARY',依次类推
所以每一个索引直接都存在依赖问题,该方法的详细流程请查看流程图.

全库依赖			对parent值为空的通过查询locations表进行依赖处理
********************************************
**注:只对parent值为空的进行索引
********************************************
删除索引			清空shoukaiseki_insert_kks.parent字段
插入数据			将shoukaiseki_insert_kks的数据插入五张表中
删一重复			删根据标识位删除其中一条重复的,用于测试,插入正式表シ勿用

*/

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.JMXUtils;
import org.shoukaiseki.characterdetector.CharacterEncoding;
import org.shoukaiseki.characterdetector.utf.UnicodeReader;
import org.shoukaiseki.constantlib.CharacterEncodingName;
import org.shoukaiseki.gui.flowlayout.ModifiedFlowLayout;
import org.shoukaiseki.gui.jtextpane.JTextPaneDoc;
import org.shoukaiseki.sql.ConnectionKonnfigu;
import org.shoukaiseki.sql.oracle.OracleSqlDetabese;
import org.shoukaiseki.string.ConCatLineBreaks;
import org.shoukaiseki.string.ConCatLineBreaksVector;


public class ImpKksGui_CRPWZ extends JFrame implements ScrollPaneConstants {

	/**
	 * 关闭時是否退出程序,默认時退出,只作为子程序时不退出
	 */
	private boolean windowclose = true;
	// locancestor
	public static long locancestorid = 0;
	public static long locancestorrowstamp = 0;
	// locoper
	public static long locoperid = 0;
	public static long locoperrowstamp = 0;
	// lochierarchy
	public static long lochierarchyid = 0;
	public static long lochierarchyrowstamp = 0;
	// locstatus
	public static long locstatusid = 0;
	public static long locstatusrowstamp = 0;
	// locations
	public static long locationsid = 0;
	public static long locationsrowstamp = 0;
	public static String datetime="sysdate";//
//	public static String datetime="TO_DATE( '2011/11/27 16:16:16', 'YYYY-MM-DD HH24:MI:SS')";//
	public static String allsiteid = "温州电厂";
	public static String allorgid = "CRPWZ";
	
	public static String onesystemid = "CRPWZ";
	public static String systemid = "";//現在該值默認為空
//	public static String[] systemids = {"电气", "汽机", "锅炉", "化学", "脱硫", "起重", "消防", "脱硝", "除灰", "燃料", "热控"};
	public static String[] systemids = {"电气","锅炉","汽机","化学","脱硫","除灰除渣","热控锅炉","热控输煤","热控脱硫","热控化学","热控盘柜","输煤","热控汽机","脱硝"};

	
	
	
	
	
//	public static String allsiteid = "徐州项目";
//	public static String allorgid = "CRPXZ";
	
//	public static String systemid = "CRPXZ";
//	public static String systemid = "除灰除渣";
//	public static String systemid = "电气";
//	public static String systemid = "锅炉";
//	public static String systemid = "化学";
//	public static String systemid = "继保";
//	public static String systemid = "金属";
//	public static String systemid = "暖通";
//	public static String systemid = "汽机";
//	public static String systemid = "燃料";
//	public static String systemid = "热控";
//	public static String systemid = "土建";
//	public static String systemid = "脱硫";
//	public static String systemid = "消防";

//	public static String systemid = "TXSYSTEM";
	public static String type ="操作";		//"OPERATING"		//跟显示有关
	public static String status ="操作中";	//"OPERATING"//跟显示有关

	private int parentLength = 5;// barent最低位数,用于自动解决依赖问题的查找位数,小于此则取父值首字母
	private int parentCutLength = 2;// Location首次截取的位数后成为barent
	private int parentLowerLength = 2;// barent查询Location字段時的最小长度

	private String sql = ""; 
//	private ConCatLineBreaksVector savesql=new ConCatLineBreaksVector();
	
	private String fileName = "./ImpKksGui.ini";
	public File txtfile = new File(fileName);
	private String sqlName = "./ImpKksGui.sql";
	public File sqlNameFile = new File(sqlName);
	private String shoukaiseki_insert_kks = "shoukaiseki_insert_kks";// 新增KKS编码存放至该表shoukaiseki_insert_kks

	private SimpleDateFormat bartDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private String locsystem = "locsystem";
	private String locations = "locations";
	private String locstatus = "locstatus";
	private String locoper = "locoper";
	private String lochierarchy = "lochierarchy";
	private String locancestor = "locancestor";
	private String[] parent;
	private String parentOne;
	private String parentSepareta = "//";// parent多个的分隔符
	private String fromuser, frompassword, fromsid, touser, topassword, tosid;

	private Box box;
	private static JTextPaneDoc textPane;
	private JLabel jl;
	private JButton commitButton,rollbackButton ;
	private JButton jb_hyou, jb_delnull,// 新建表按钮,删除空数据;
			jb_kurikaesu, jb_yilai, jb_insert, jb_delkurikaesu,jb_insertteam; // 重复编号按钮;依赖按钮;插入数据按钮,删除重复的其中一条

	private JButton jb_delhyou, jb_ldtrim, jb_delyilai, jb_allyilai;// 删除表,清首位空,删除依赖,全库依赖
	private JButton jb_exphyou, jb_imphyou, jb_cleantext, jb_readsetting; // 生成导出KKS六张表cmd命令按钮,导入,清空记录,重读配置

	private JLabel jlfromuser, jlfrompassword, jltouser, jltopassword,
			jlfromsid, jltosid;
	private JTextField jtfromuser, jtfrompassword, jttouser, jttopassword,
			jtfromsid, jttosid;
	/**
	 * 索引最终值设置,插入LoCanCesTor表时的查找是否有与parent同名的location字段,
	 * 如果没有则以该数据为基准,使用//分隔符隔开,可匹配多个字符
	 */
	private JTextField jt_parent;
	private JTextField jl_parent;
	// private JTextField jt_loginurl,jt_loginuser,jt_loginpassword;

	//
	private DecimalFormat df = new DecimalFormat("00.00%"); // " "内写格式的模式
	// 如保留2位就用"0.00"即可
	private int error = 0;// 记录数据更新失败记录
	private int endtj = 0;// 统计总更新数据条数
	private boolean conCOR=false;//是否有提交数据状态

	OracleSqlDetabese osd=null;
	private PreparedStatement pst ;
	private Connection con = null;
	private Statement sm = null;
	private ResultSet rs = null;
	private String tableName = null;
	private String cName = null;
	private String result = null;
	private String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
	// private String url = "jdbc:oracle:thin:@192.168.2.101:1521:zhjqmaximo";
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String user = "orclzhjq";
	private String password = "orclzhjq";
	private PreparedStatement psSelectParent;
	private PreparedStatement psSelectLP;
	private PreparedStatement psSelectId;

	public ImpKksGui_CRPWZ() throws BadLocationException {
		this(true);
	}


	/**
	 * 
	 */
	public ImpKksGui_CRPWZ(boolean windowclose) throws BadLocationException {
		jlfromuser = new JLabel("fromuser:", SwingConstants.LEFT);
		jtfromuser = new JTextField("maximo", 8);
		jlfrompassword = new JLabel("password:", SwingConstants.LEFT);
		jtfrompassword = new JTextField("maximo", 8);
		jlfromsid = new JLabel("SID:", SwingConstants.LEFT);
		jtfromsid = new JTextField("orcl", 8);

		jltouser = new JLabel("     touser:", SwingConstants.LEFT);
		jttouser = new JTextField("maximo", 8);
		jltopassword = new JLabel("password:", SwingConstants.LEFT);
		jttopassword = new JTextField("maximo", 8);
		jltosid = new JLabel("SID:", SwingConstants.LEFT);
		jttosid = new JTextField("orcl", 8);

		textPane = new JTextPaneDoc();
		textPane.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		textPane.setText("这里设置文本框内容!");
		textPane.setFont(new Font("宋体", Font.BOLD, 13));
		textPane.setLayout(new ModifiedFlowLayout());// 加不加都感觉有效果,如果一段英文无空格就会出现不会自动换行

		jb_hyou = new JButton("创建表格");
		jb_delnull = new JButton("删除空行");
		jb_kurikaesu = new JButton("重复编码");
		jb_yilai = new JButton("依赖关系");
		jb_insert = new JButton("写入编码");
		jb_insertteam = new JButton("分組写入编码");
		commitButton=new JButton("提交更改");
		
		jb_hyou.addActionListener(new ActionListener() { // 插入文字的事件
					public void actionPerformed(ActionEvent e) {

						hyou();
					}
				});
		jb_delnull.addActionListener(new ActionListener() { // 插入文字的事件
					public void actionPerformed(ActionEvent e) {
						sql = "delete from "
								+ shoukaiseki_insert_kks
								+ " where  trim(location) is null AND  trim(description) is null";
						textPane
								.addLastLine("准备删除location字段和description字段为空的行!");
						println(sql);
						if (update(con, sql)) {
							commitButton.setEnabled(true);
							rollbackButton.setEnabled(true);
							printlnSeikou();
						} else {
							commitButton.setEnabled(false);
							rollbackButton.setEnabled(false);
							printlnSippai();
						}

					}
				});
		jb_kurikaesu.addActionListener(new ActionListener() { // 插入文字的事件
					public void actionPerformed(ActionEvent e) {
						int suteetasu = JOptionPane.showConfirmDialog(null,
								"确定更新该表重复编码错误字段吗?", "提示!!",
								JOptionPane.YES_NO_OPTION);
						if (suteetasu == 0) {
							new Timer().schedule(new TimerTask() {
								@Override
								public void run() {// 实例中的的方法
									kuRiKaeSu();// 定时器到后执行的方法,一般在定时器到后的内容具体在外面写
									kuRiKaeSuAll();
									commitButton.setEnabled(true);
									rollbackButton.setEnabled(true);
								}
							}, 10);
							println(shoukaiseki_insert_kks
									+ "更新重复编码错误字段完成!");
						} else {
							println(shoukaiseki_insert_kks
									+ "已取消操作!");
						}
					}
				});

		jb_yilai.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {// 实例中的的方法
						yilai();
						commitButton.setEnabled(true);
						rollbackButton.setEnabled(true);
					}
				}, 10);
			}
		});

		jb_insert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {// 实例中的的方法
						try {
							if (getParentString() && selectParentHaveNull()) {
//								savesql.setContent("");
								systemid=onesystemid;
								insertAllHyou(false);
								wrFileSql();
							}
						} catch (BadLocationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, 10);
			}
		});
		jb_insertteam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {// 实例中的的方法
						try {
							if (getParentString() && selectParentHaveNull()) {
//								savesql.setContent("");
								for (String str : systemids) {
									systemid=str;
									insertAllHyou(true);
									wrFileSql();
								}
							}
						} catch (BadLocationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, 10);
			}
		});

		 commitButton.addActionListener(new ActionListener() {// 添加事件
					public void actionPerformed(ActionEvent e) {
						conCommit();
					}
				});
		
		jb_delhyou = new JButton("删除表格");
		jb_ldtrim = new JButton("清首尾空");
		jb_delyilai = new JButton("删除依赖");
		jb_allyilai = new JButton("全库依赖");
		jb_delkurikaesu = new JButton("删一重复");
		rollbackButton = new JButton("回退提交"); 
		
		jb_delhyou.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				delHyou();
			}
		});
		jb_ldtrim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {// 实例中的的方法
						try {
							delLDTrim();
						} catch (BadLocationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}// 请Location,Description
					}
				}, 10);
			}
		});
		jb_delyilai.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int suteetasu = JOptionPane
						.showConfirmDialog(null, "确定清空依赖所在parent字段吗?", "提示!!",
								JOptionPane.YES_NO_OPTION);
				if (suteetasu == 0) {
					new Timer().schedule(new TimerTask() {
						@Override
						public void run() {// 实例中的的方法
							// 清空Parent字段
							sql = "UPDATE " + shoukaiseki_insert_kks
									+ " set PARENT =null";
							println("准备清空依赖所在parent字段!");
							println(sql);
							if (update(con, sql)) {
								printlnSeikou();
							} else {
								printlnSippai();
							}
							commitButton.setEnabled(true);
							rollbackButton.setEnabled(true);
						}
					}, 10);
				} else {
					println("已取消操作");
				}

			}
		});

		jb_allyilai.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {// 实例中的的方法
						allYilai();// 查找locations表的依赖值
						commitButton.setEnabled(true);
						rollbackButton.setEnabled(true);
					}
				}, 10);
			}
		});
		jb_delkurikaesu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {// 实例中的的方法
						int suteetasu = JOptionPane.showConfirmDialog(null,
								"确定删除重复编码的其中一条吗?", "提示!!",
								JOptionPane.YES_NO_OPTION);
						if (suteetasu == 0) {
							new Timer().schedule(new TimerTask() {
								@Override
								public void run() {// 实例中的的方法
									sql = "select 'delete from "
											+ shoukaiseki_insert_kks
											+ " where id='||id||';' from (select a.*, ROW_NUMBER() "
											+ " over(partition by CFL　ORDER by CFL)　RN from "
											+ shoukaiseki_insert_kks
											+ " a where cfl<>0) where RN=1 ";
									println("准备清空依赖所在parent字段!");
									println(sql);
									if (update(con, sql)) {
										printlnSeikou();
									} else {
										printlnSippai();
									}
								}
							}, 10);
						} else {
							println("已取消操作");
						}
					}
				}, 10);
			}
		});
		
		rollbackButton.addActionListener(new ActionListener() {// 添加事件
				public void actionPerformed(ActionEvent e) {
						conRollback();
				}
			});

		jb_exphyou = new JButton("导出表");
		jb_imphyou = new JButton("导入表");
		jb_cleantext = new JButton("清空");
		jb_readsetting = new JButton("重新读入配置");
		jb_exphyou.addActionListener(new ActionListener() { // 插入文字的事件
					public void actionPerformed(ActionEvent e) {
						expToHyou();
					}
				});
		jb_imphyou.addActionListener(new ActionListener() { // 插入文字的事件
					public void actionPerformed(ActionEvent e) {
						impToHyou();
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
		Box boxfrom = Box.createHorizontalBox(); // 横结构
		Box boxto = Box.createHorizontalBox(); // 横结构
		Box boxparent = Box.createHorizontalBox(); // 横结构

		JPanel jpfrom = new JPanel();
		jpfrom.add(boxfrom);
		JPanel jpto = new JPanel();
		jpto.add(boxto);

		box11.add(jl1);

		box.add(box11);
		box.add(Box.createVerticalStrut(8)); // 两行的间距
		box.add(box1);
		box.add(Box.createVerticalStrut(8)); // 两行的间距
		box.add(box10);
		box.add(Box.createVerticalStrut(8)); // 两行的间距
		box.add(jpfrom);
		box.add(Box.createVerticalStrut(8)); // 两行的间距
		box.add(jpto);
		box.add(Box.createVerticalStrut(8)); // 两行的间距
		box.add(boxparent);

		box.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8)); // 8个的边距

		box1.add(jb_hyou);
		box1.add(Box.createHorizontalStrut(8));// 间距
		box1.add(jb_delnull);
		box1.add(Box.createHorizontalStrut(8));// 间距
		box1.add(jb_kurikaesu);
		box1.add(Box.createHorizontalStrut(8));// 间距
		box1.add(jb_yilai);
		box1.add(Box.createHorizontalStrut(8));// 间距
		box1.add(jb_insert);
		box1.add(Box.createHorizontalStrut(8));// 间距
		box1.add(jb_insertteam);
		box1.add(Box.createHorizontalStrut(8));// 间距
		box1.add(commitButton);

		box10.add(jb_delhyou);
		box10.add(Box.createHorizontalStrut(8));// 间距
		box10.add(jb_ldtrim);
		box10.add(Box.createHorizontalStrut(8));// 间距
		box10.add(jb_delyilai);
		box10.add(Box.createHorizontalStrut(8));// 间距
		box10.add(jb_allyilai);
		box10.add(Box.createHorizontalStrut(8));// 间距
		box10.add(jb_delkurikaesu);
		box10.add(Box.createHorizontalStrut(8));// 间距
		box10.add(rollbackButton);

		boxfrom.add(jlfromuser);
		boxfrom.add(Box.createHorizontalStrut(8));// 间距
		boxfrom.add(jtfromuser);
		boxfrom.add(Box.createHorizontalStrut(8));// 间距
		boxfrom.add(jlfrompassword);
		boxfrom.add(Box.createHorizontalStrut(8));// 间距
		boxfrom.add(jtfrompassword);
		boxfrom.add(Box.createHorizontalStrut(8));// 间距
		boxfrom.add(jlfromsid);
		boxfrom.add(Box.createHorizontalStrut(8));// 间距
		boxfrom.add(jtfromsid);
		boxfrom.add(Box.createHorizontalStrut(8));// 间距
		// boxfrom.add(new JLabel());
		// boxfrom.add(Box.createHorizontalStrut(8));//间距
		boxfrom.add(jb_exphyou);

		boxto.add(jltouser);
		boxto.add(Box.createHorizontalStrut(8));// 间距
		boxto.add(jttouser);
		boxto.add(Box.createHorizontalStrut(8));// 间距
		boxto.add(jltopassword);
		boxto.add(Box.createHorizontalStrut(8));
		boxto.add(jttopassword);
		boxto.add(Box.createHorizontalStrut(8));
		boxto.add(jltosid);
		boxto.add(Box.createHorizontalStrut(8));
		boxto.add(jttosid);
		boxto.add(Box.createHorizontalStrut(8));
		boxto.add(jb_imphyou);

		jt_parent = new JTextField("JW" + parentSepareta + "CRPCFD");
		jt_parent.setFont(new Font("新宋体", Font.BOLD, 13));// Courier New 微软雅黑
		jl_parent = new JTextField("状态:");
		jl_parent.setEditable(false);

		boxparent.add(new JLabel("parent最终值," + "分隔符为" + parentSepareta));
		boxparent.add(Box.createHorizontalStrut(8));
		boxparent.add(jt_parent);
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

		setSize(700, 500);
		// 隐藏frame的标题栏,此功暂时关闭，以方便使用window事件
		setLocation(200, 150);

		setTitle("Kks导入程序");
		show();
		jtfromuser.requestFocus();
		jttouser.requestFocus();
		textPane.cleanText();
		this.windowclose=windowclose;

		if (windowclose) {
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		}

		commitButton.setEnabled(false);
		rollbackButton.setEnabled(false);
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws BadLocationException
	 */
	public static void main(String[] args) throws IOException,
			BadLocationException {
		ImpKksGui_CRPWZ kks = new ImpKksGui_CRPWZ();
		kks.readSetting();
		// TODO Auto-generated method stub
		BufferedReader input = new BufferedReader(new InputStreamReader(
				System.in));
		kks.setDefuorutoConnection();
		kks.println("已经开启五张表插入!2012年3月31日11时22分15秒",true);
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

			/**
			 * 关闭自动更新
			 */
			con.setAutoCommit(false);
			printConnection();
		} catch (Exception ex) {
			ex.printStackTrace();
			println(ex.getMessage(), true);
		}
		setSetting();
	}
	public void setDefuorutoConnection() throws BadLocationException {
		try {
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
//			java.sql.DriverManager .registerDriver(new oracle.jdbc.driver.OracleDriver());
//			con = DriverManager.getConnection(url, user, password);
			
			DruidDataSource dataSource = new DruidDataSource();
			
			JMXUtils.register("com.alibaba:type=DruidDataSource", dataSource);
			dataSource.setInitialSize(10);
			dataSource.setMaxActive(25);
			dataSource.setMinIdle(15);
			dataSource.setMaxIdle(30);
			dataSource.setPoolPreparedStatements(true);
			dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
			dataSource.setUrl(url);
			dataSource.setPoolPreparedStatements(true);
			dataSource.setUsername(user);
			dataSource.setPassword(password);
			dataSource.setValidationQuery("SELECT 'asus' from dual");
			dataSource.setTestOnBorrow(true);
			con=dataSource.getConnection();

 

			osd=new OracleSqlDetabese(con);	
			
				String sql = "select parent from " + shoukaiseki_insert_kks
						+ " WHERE location =? ";
				 psSelectParent = con.prepareStatement(sql);
				 
					sql = "select location,parent from " + lochierarchy
							+ " WHERE location =? ";
					psSelectLP = con.prepareStatement(sql);
					
					
			sql = "select id " + "from " + shoukaiseki_insert_kks
					+ " WHERE parent =? ";
			psSelectId = con.prepareStatement(sql);
			println("OK,成功连接到数据库");

			/**
			 * 关闭自动更新
			 */
			con.setAutoCommit(false);
		} catch (Exception ex) {
			ex.printStackTrace();
			println(ex.getMessage(), true);
		}
	}
	

	/**
	 * 新建子表
	 */
	public void hyou() {
		println("正在创建表............");
		sql = "CREATE TABLE " + shoukaiseki_insert_kks
				+ " (ID NUMBER NOT NULL ENABLE"
				+ ",LOCATION  VARCHAR2(30 CHAR)"
				+ ",DESCRIPTION  VARCHAR2(100 CHAR)"
				+ ",PARENT VARCHAR2(30 CHAR)" + " ,CFL NUMBER " // 本表编码重复
				+ " ,CFD NUMBER " // 本表名称重复
				+ " ,ALLCFL NUMBER " // 所有表编码重复
				+ " ,ALLCFD NUMBER " // 所有表名称重复
				+ ",SETUMEINUM  NUMBER"// EXCEL内编号
				+ ",SETUMEI  VARCHAR2(100 CHAR)"// 説明
				+ ",SETUMEI2  VARCHAR2(100 CHAR)"// 説明
				+ ")";
		println(sql);
		try {
			PreparedStatement pst = con.prepareStatement(sql);

			pst.execute();
			pst.close();// 更新后关闭此线程,不然更新数据多了就会异常
			printlnSeikou();
		} catch (Exception e) {
			e.printStackTrace();
			printlnSippai();
			jl.setText(shoukaiseki_insert_kks + "表创建失败");
			println("\n错误原因:\n" + e.getMessage());
			// TODO: handle exception
			return;
		}
		println(shoukaiseki_insert_kks + "表创建成功!");

	}

	public void kuRiKaeSu() {
		try {
			//
			sql = "UPDATE " + shoukaiseki_insert_kks
					+ " set CFL=0,CFD=0,ALLCFL=0,ALLCFD=0,SETUMEI2 =null";
			println(sql);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.execute();
			pst.close();// 更新后关闭此线程,不然更新数据多了就会异常
			println("重复标识字段全部置0完成!");

			sql = "select   ID,LOCATION,DESCRIPTION " + "from "
					+ shoukaiseki_insert_kks + " a " + "where exists(select * "
					+ "from " + shoukaiseki_insert_kks + " b "
					+ "where a.LOCATION=b.LOCATION " + "AND a.id<>b.id)"
					+ " ORDER BY location,description";
			Statement tablebfb = con
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			println(sql);
			ResultSet ablebr = tablebfb.executeQuery(sql);
			ablebr.last();
			int tablebc = ablebr.getRow(); // 获得当前行号：此处即为最大记录数 ,无效时为0

			Statement st = con.createStatement();
			ResultSet r = st.executeQuery(sql);

			println("需要更新的总记录数总共:" + tablebc);
			jl.setText("需要更新的总记录数总共:" + tablebc);
			endtj = 0;// 统计总更新数据条数

			Calendar cal = Calendar.getInstance();
			java.util.Date data = new java.util.Date();
			cal.setTime(data);// 设置时间为当前时间
			long timeOne = cal.getTimeInMillis();
			println("开始时间为:" + bartDateFormat.format(data));
			int intddcsm = 10000;
			int intddcsmbak = 10000;
			String xggymbak = "";// 相关工艺码对比字符
			String sbmcbak = "";
			int theerror = 0;

			while (r.next()) {
				error = 0;// 记录数据更新失败记录
				Boolean repaat = true;
				int id = r.getInt("id");// id
				String sbmc = r.getString("DESCRIPTION");// 设备名称
				if (sbmc != null) {
					sbmc = sbmc.trim();
				}
				String xggym = r.getString("LOCATION");// 相关工艺码
				if (xggym != null) {
					xggym = xggym.trim();
				}

				if (!xggym.equals(xggymbak))// 比较字符内容
				{// 与上次的相关工艺码不同,而且所在表不同,则编码+1
					intddcsm++;
					intddcsmbak = 0;
					update(con, shoukaiseki_insert_kks, id, intddcsm,
							intddcsmbak);
					// System.out.println("id="+id+"\tcfl="+intddcsm+"\tcfd="+intddcsmbak);
				} else {
					intddcsmbak = 0;
					Statement thissa = con.createStatement(
							ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
					String thiscommand = "select * " + "from "
							+ shoukaiseki_insert_kks
							+ " WHERE LOCATION like '%" + xggym + "%' "
							+ " AND DESCRIPTION like '%" + sbmc + "%'"
							+ " AND  id <> " + id;
					ResultSet thisa = thissa.executeQuery(thiscommand);
					while (thisa.next()) {
						String xggym2 = r.getString("location");// 编码
						String sbmc2 = r.getString("DESCRIPTION");// 设备名称

						if (xggym2 != null) {
							xggym2 = xggym2.trim();
						}
						if (sbmc2 != null) {
							sbmc2 = sbmc2.trim();
						}
						if (xggym2.equals(xggym)) {
							if (sbmc2.equals(sbmc) && sbmcbak.equals(sbmc)) {
								// 与上次的设备名称是否也相同
								// System.out.println(thiscommand);
								// 工艺编码相同,设备名称也相同,而且在同一个所在表中,ID不同
								intddcsmbak = intddcsm;
								repaat = true;// 本表中已有相同工艺码
								break;
							}
						}
					}
					if (repaat) {

					}
					thisa.close();// 更新后关闭此线程,不然更新数据多了就会异常
					thissa.close();// 更新后关闭此线程,不然更新数据多了就会异常
					update(con, shoukaiseki_insert_kks, id, intddcsm,
							intddcsmbak);
					// System.out.println("id="+id+"\tcfl="+intddcsm+"\tcfd="+intddcsmbak);
				}

				xggymbak = "";
				xggymbak = xggym.substring(0, xggym.length());
				sbmcbak = "";
				if (sbmc != null) {
					sbmcbak = sbmc.substring(0, sbmc.length());
				}

				if (error > 0) {
					theerror++;
				} else {
					endtj++;
					jl.setText("已成功更新:" + endtj + "条"
							+ df.format((float) endtj / tablebc));
				}
			}

			println("表  " + shoukaiseki_insert_kks + "  更新结束!");
			println("一共更新成功" + endtj + "次!");
			println("一共更新失败" + theerror + "次!");
			data = new java.util.Date();
			cal.setTime(data);// 设置时间
			long timeTwo = cal.getTimeInMillis();
			long daysapart = (timeTwo - timeOne) / (1000 * 60);// 分钟
			long daysaparts = (timeTwo - timeOne) / 1000 % 60;// 获得总秒数后取除以60的余数即为秒数
			println("结束时间为:" + bartDateFormat.format(data));
			println("共花费时间为" + daysapart + "分" + daysaparts + "秒");
			printlnSeikou();
		}

		catch (Exception ex) {
			ex.printStackTrace();
			println(ex.getMessage());
			println(sql);
			printlnSippai();
		}
	}

	public void kuRiKaeSuAll() {
		try {
			//
			int suteetasu = JOptionPane.showConfirmDialog(null,
					"是否只对location值相同的表进行标识?否则进入全表查询还将对decription值进行重复查询",
					"提示!!", JOptionPane.YES_NO_OPTION);
			if (suteetasu == 0) {
				sql = "select *  from " + shoukaiseki_insert_kks
						+ " a where  exists (select *" + " from " + locations
						+ " B" + " where trim(B.location) = trim(a.location) )";
				println("只查询" + shoukaiseki_insert_kks + "表与"
						+ locations + "表的location字段相同的行");
			} else {
				sql = "select * from " + shoukaiseki_insert_kks;
				println("进入全表查询,对" + shoukaiseki_insert_kks + "表与"
						+ locations + "表的location字段相同的和description相同的行进行标识");
			}
			Statement tablebfb = con
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			println(sql);
			ResultSet ablebr = tablebfb.executeQuery(sql);
			ablebr.last();
			int tablebc = ablebr.getRow(); // 获得当前行号：此处即为最大记录数 ,无效时为0

			Statement st = con.createStatement();
			ResultSet r = st.executeQuery(sql);

			println("需要更新的总记录数总共:" + tablebc);
			jl.setText("需要更新的总记录数总共:" + tablebc);
			error = 0;// 记录数据更新失败记录
			endtj = 0;// 统计总更新数据条数

			Calendar cal = Calendar.getInstance();
			java.util.Date data = new java.util.Date();
			cal.setTime(data);// 设置时间为当前时间
			long timeOne = cal.getTimeInMillis();
			println("开始时间为:" + bartDateFormat.format(data));
			int intddcsm = 10000;
			int intddcsmbak = 10000;
			int theerror = 0;

			while (r.next()) {
				error = 0;// 记录数据更新失败记录
				Boolean repaat = true;
				int id = r.getInt("id");// id
				String sbmc = r.getString("DESCRIPTION");// 设备名称
				if (sbmc != null) {
					sbmc = sbmc.trim();
				}
				String xggym = r.getString("LOCATION");// 相关工艺码
				if (xggym != null) {
					xggym = xggym.trim();
				}
				Statement thissa = con.createStatement(
						ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				sql = "select * " + "from " + locations
						+ " WHERE trim(LOCATION) = '" + xggym + "' ";

				ResultSet thisa = null;
				try {
					thisa = thissa.executeQuery(sql);
				} catch (Exception e) {
					println(sql);
					// TODO: handle exception
				}
				while (thisa.next()) {
					String xggym2 = thisa.getString("location");// 编码
					String sbmc2 = thisa.getString("DESCRIPTION");// 编码
					if (sbmc2 != null) {
						sbmc2 = sbmc2.trim();
					}
					if (xggym2 != null) {
						xggym2 = xggym2.trim();
					}
					if (xggym2.equals(xggym)) {
						intddcsm++; // 与上次的设备名称是否也相同
						// System.out.println(thiscommand);
						// 工艺编码相同,设备名称也相同,而且在同一个所在表中,ID不同
						updateallcfl(con, shoukaiseki_insert_kks, id, intddcsm,
								sbmc2);
						break;
					}
					// System.out.println("id="+id+"\tcfl="+intddcsm+"\tcfd="+intddcsmbak);
				}
				thisa.close();// 更新后关闭此线程,不然更新数据多了就会异常
				sql = "select * " + "from " + locations
						+ " Where trim(DESCRIPTION) ='" + sbmc + "'";
				try {
					thisa = thissa.executeQuery(sql);
				} catch (Exception e) {
					println(sql);
					// TODO: handle exception
				}
				while (thisa.next()) {
					String xggym2 = thisa.getString("location");// 编码
					String sbmc2 = thisa.getString("DESCRIPTION");// 编码
					if (xggym2 != null) {
						xggym2 = xggym2.trim();
					}
					if (sbmc2 != null) {
						sbmc2 = sbmc2.trim();
					}
					if (sbmc2.equals(sbmc)) { // 与上次的设备名称是否也相同
						// System.out.println(thiscommand);
						// 工艺编码相同,设备名称也相同,而且在同一个所在表中,ID不同
						intddcsmbak++;
						updateallcfd(con, shoukaiseki_insert_kks, id,
								intddcsmbak, xggym2);
						break;
					}
					// System.out.println("id="+id+"\tcfl="+intddcsm+"\tcfd="+intddcsmbak);
				}
				thisa.close();// 更新后关闭此线程,不然更新数据多了就会异常
				thissa.close();// 更新后关闭此线程,不然更新数据多了就会异常

				if (error > 0) {
					theerror++;
				} else {
					endtj++;
					jl.setText("已成功更新:" + endtj + "条"
							+ df.format((float) endtj / tablebc));
				}
			}

			println("表  " + shoukaiseki_insert_kks + "  更新结束!");
			println("一共更新成功" + endtj + "次!");
			println("一共更新失败" + theerror + "次!");
			data = new java.util.Date();
			cal.setTime(data);// 设置时间
			long timeTwo = cal.getTimeInMillis();
			long daysapart = (timeTwo - timeOne) / (1000 * 60);// 分钟
			long daysaparts = (timeTwo - timeOne) / 1000 % 60;// 获得总秒数后取除以60的余数即为秒数
			println("结束时间为:" + bartDateFormat.format(data));
			println("共花费时间为" + daysapart + "分" + daysaparts + "秒");
			printlnSeikou();
		}

		catch (Exception ex) {
			ex.printStackTrace();
			println(ex.getMessage());
			println(sql);
			printlnSippai();
		}
	}

	/**
	 * 更新至表
	 * 
	 * @param con
	 * @param shoukaiseki_insert_kks
	 * @param id
	 * @param cfl
	 * @param cfd
	 * @param allcfl
	 * @param allcfd
	 * @param setumei2
	 * @throws SQLException
	 */
	public void update(Connection con, String shoukaiseki_insert_kks, int id,
			int cfl, int cfd, int allcfl, int allcfd, String setumei2)
			throws SQLException {
		try {
			String command = "UPDATE " + shoukaiseki_insert_kks + " SET  "
					+ "cfl = " + cfl + ",cfd = " + cfd + ",allcfl = " + allcfl
					+ ",allcfd = " + allcfd + ",setumei2 = " + setumei2
					+ " WHERE   id=" + id;
			// System.out.println(command);
			PreparedStatement pst = con.prepareStatement(command);
			pst.execute();
			pst.close();// 更新后关闭此线程,不然更新数据多了就会异常

		} catch (Exception e) {
			e.printStackTrace();
			error++;
			println("" + error);
			println(e.getMessage());
			// TODO: handle exception
		}
	}

	/**
	 * 更新至表
	 * 
	 * @param con
	 * @param shoukaiseki_insert_kks
	 * @param id
	 * @param cfl
	 * @param cfd
	 * @throws SQLException
	 */
	public void update(Connection con, String shoukaiseki_insert_kks, int id,
			int cfl, int cfd) throws SQLException {
		String command = "";
		try {
			command = "UPDATE " + shoukaiseki_insert_kks + " SET  " + " cfl = "
					+ cfl + ",cfd = " + cfd + " WHERE   id=" + id;
			PreparedStatement pst = con.prepareStatement(command);
			pst.execute();
			pst.close();// 更新后关闭此线程,不然更新数据多了就会异常

		} catch (Exception e) {
			e.printStackTrace();
			println(command);
			error++;
			println("" + error);
			println(e.getMessage());
			// TODO: handle exception
		}
	}

	public void updateallcfd(Connection con, String shoukaiseki_insert_kks,
			int id, int allcfd, String SETUMEI2) throws SQLException {
		String command = "";
		try {
			command = "UPDATE " + shoukaiseki_insert_kks + " SET  "
					+ " allcfd = " + allcfd + ", SETUMEI2='" + SETUMEI2 + "'"
					+ " WHERE   id=" + id;
			// System.out.println(command);
			PreparedStatement pst = con.prepareStatement(command);
			pst.execute();
			pst.close();// 更新后关闭此线程,不然更新数据多了就会异常

		} catch (Exception e) {
			e.printStackTrace();
			error++;
			println(command);
			println("" + error);
			println(e.getMessage());
			// TODO: handle exception
		}
	}

	/**
	 * 更新表中的CFL字段
	 * 
	 * @param con
	 * @param shoukaiseki_insert_kks
	 * @param id
	 * @param allcfl
	 * @throws SQLException
	 */
	public void updateallcfl(Connection con, String shoukaiseki_insert_kks,
			int id, int allcfl, String SETUMEI2) throws SQLException {
		String command = "";
		try {
			command = "UPDATE " + shoukaiseki_insert_kks + " SET  "
					+ " allcfl = " + allcfl + ",SETUMEI2 ='" + SETUMEI2 + "'"
					+ " WHERE   id=" + id;
			// System.out.println(command);
			PreparedStatement pst = con.prepareStatement(command);
			pst.execute();
			pst.close();// 更新后关闭此线程,不然更新数据多了就会异常

		} catch (Exception e) {
			e.printStackTrace();
			error++;
			println(command);
			println("" + error);
			println(e.getMessage());
			// TODO: handle exception
		}
	}

	/**
	 * 删除表按钮执行方法
	 */
	public void delHyou() {
		println("正在删除表............");
		sql = "DROP TABLE  " + shoukaiseki_insert_kks;
		println(sql);
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			int suteetasu = JOptionPane.showConfirmDialog(null, "确定删除该表吗?",
					"提示!!", JOptionPane.YES_NO_OPTION);
			if (suteetasu == 0) {
				pst.execute();
				pst.close();// 更新后关闭此线程,不然更新数据多了就会异常
				println(shoukaiseki_insert_kks + "表删除成功!");
			} else {
				println(shoukaiseki_insert_kks + "表已取消删除!");
			}
			printlnSeikou();
		} catch (Exception e) {
			e.printStackTrace();
			println(sql);
			jl.setText(shoukaiseki_insert_kks + "表删除失败");
			println("\n错误原因:\n" + e.getMessage());
			printlnSippai();
			// TODO: handle exception
			return;
		}
	}

	/**
	 * 表内查询依赖关系,即寻找父索引
	 */
	public void yilai() {
		try {

			sql = "select   * " + "from " + shoukaiseki_insert_kks + " a "
					+ "where parent is null ";
			Statement tablebfb = con
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			println(sql);
			ResultSet ablebr = tablebfb.executeQuery(sql);
			ablebr.last();
			int tablebc = ablebr.getRow(); // 获得当前行号：此处即为最大记录数 ,无效时为0

			Statement st = con.createStatement();
			ResultSet r = st.executeQuery(sql);

			println("需要更新的总记录数总共:" + tablebc);
			jl.setText("需要更新的总记录数总共:" + tablebc);
			endtj = 0;// 统计总更新数据条数

			Calendar cal = Calendar.getInstance();
			java.util.Date data = new java.util.Date();
			cal.setTime(data);// 设置时间为当前时间
			long timeOne = cal.getTimeInMillis();
			println("开始时间为:" + bartDateFormat.format(data));
			int intddcsm = 10000;
			int intddcsmbak = 10000;
			String xggymbak = "";// 相关工艺码对比字符
			String sbmcbak = "";
			int theerror = 0;
			String parent = "";// 相关工艺码的父级编号

			while (r.next()) {
				int id = r.getInt("id");// id
				String xggym = r.getString("location");// 相关工艺码
				parent = xggym.substring(0, xggym.length());// 上层索引码,先置于于工艺码相同
				// int theerror = r.getInt(7);//错误标识

				Boolean theok = false;// 如果为真,保留索引名,为假则置空字符
				if (parent.length() > parentCutLength) {
					parent = xggym.substring(0, xggym.length()
							- parentCutLength);// 索引的父值比子值至少小于两位以上
					System.out.println(xggym+"--"+parent);
				}
				if (xggym.length() < parentLength) {
					parent = xggym.substring(0, 1);// 工艺码小于parentLength位则取父值首字母
					theok = true;
				} else {
					Statement thissa = con.createStatement(
							ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
					for (int i = 1; parent.length() > parentLowerLength; i++) {
						sql = "select *" + "from " + shoukaiseki_insert_kks
								+ " WHERE location ='" + parent + "' " 
										//+" and COLUMN1= '"+r.getString("COLUMN1")+"'"
										;
						 System.out.println(sql);
						ResultSet thisa = thissa.executeQuery(sql);
						thisa.last();
						int thisd = thisa.getRow();// 获得当前行号：此处即为最大记录数 ,无效时为0
						thisa.close();// 更新后关闭此线程,不然更新数据多了就会异常
						if (thisd > 0) {
							theok = true;
							break;
						}

						parent = xggym.substring(0, xggym.length() - i);
					}
					thissa.close();// 更新后关闭此线程,不然更新数据多了就会异常
				}
				if (theok == false) {
//					parent = "xxxxxxx";
				}
				updateParent(con, shoukaiseki_insert_kks, id, parent);
				// tablezb);//更新表数据
				if (error > 0) {
					theerror++;
				} else {
					endtj++;
					jl.setText("已成功更新:" + endtj + "条"
							+ df.format((float) endtj / tablebc));

				}
			}

			println("表  " + shoukaiseki_insert_kks + "  更新结束!");
			println("一共更新成功" + endtj + "次!");
			println("一共更新失败" + theerror + "次!");
			data = new java.util.Date();
			cal.setTime(data);// 设置时间
			long timeTwo = cal.getTimeInMillis();
			long daysapart = (timeTwo - timeOne) / (1000 * 60);// 分钟
			long daysaparts = (timeTwo - timeOne) / 1000 % 60;// 获得总秒数后取除以60的余数即为秒数
			println("结束时间为:" + bartDateFormat.format(data));
			println("共花费时间为" + daysapart + "分" + daysaparts + "秒");
			printlnSeikou();
		} catch (Exception e) {
			// TODO: handle exception
			println(e.getMessage());
			println(sql);
			printlnSippai();
		}
	}

	/**
	 * 更新Parent值
	 * 
	 * @param con
	 * @param shoukaiseki_insert_kks
	 * @param id
	 * @param parent
	 * @throws SQLException
	 */
	public void updateParent(Connection con, String shoukaiseki_insert_kks,
			int id, String parent) throws SQLException {
		String command = "";
		try {
			command = "UPDATE " + shoukaiseki_insert_kks + " SET  "
					+ " parent = '" + parent + "'" + " WHERE   id=" + id;
			// System.out.println(command);
			PreparedStatement pst = con.prepareStatement(command);
			pst.execute();
			pst.close();// 更新后关闭此线程,不然更新数据多了就会异常

		} catch (Exception e) {
			e.printStackTrace();
			error++;
			println(command);
			println("" + error);
			println(e.getMessage());
			// TODO: handle exception
		}
	}

	public void allYilai() {
		sql = "";
		try {

			sql = "select   * " + "from " + shoukaiseki_insert_kks + " a "
					+ "where parent is null";
			Statement tablebfb = con
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			println(sql);
			ResultSet ablebr = tablebfb.executeQuery(sql);
			ablebr.last();
			int tablebc = ablebr.getRow(); // 获得当前行号：此处即为最大记录数 ,无效时为0

			Statement st = con.createStatement();
			ResultSet r = st.executeQuery(sql);

			println("需要更新的总记录数总共:" + tablebc);
			jl.setText("需要更新的总记录数总共:" + tablebc);
			endtj = 0;// 统计总更新数据条数

			Calendar cal = Calendar.getInstance();
			java.util.Date data = new java.util.Date();
			cal.setTime(data);// 设置时间为当前时间
			long timeOne = cal.getTimeInMillis();
			println("开始时间为:" + bartDateFormat.format(data));
			int intddcsm = 10000;
			int intddcsmbak = 10000;
			String xggymbak = "";// 相关工艺码对比字符
			String sbmcbak = "";
			int theerror = 0;
			String parent = "";// 相关工艺码的父级编号

			while (r.next()) {
				int id = r.getInt("id");// id
				String xggym = r.getString("location");// 相关工艺码
				parent = xggym.substring(0, xggym.length());// 上层索引码,先置于于工艺码相同
				// int theerror = r.getInt(7);//错误标识

				Boolean theok = false;// 如果为真,保留索引名,为假则置空字符
				if (parent.length() > parentCutLength) {
					parent = xggym.substring(0, xggym.length()
							- parentCutLength);// 索引的父值比子值至少小于两位以上
				}
				if (xggym.length() < parentLength) {
					parent = xggym.substring(0, 1);// 工艺码小于parentLength位则取父值首字母
					theok = true;
				} else {
					Statement thissa = con.createStatement(
							ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
					for (int i = 1; parent.length() > parentLowerLength; i++) {
						sql = "select *" + "from " + locations
								+ " WHERE location ='" + parent + "' ";
						// System.out.println(thiscommand);
						ResultSet thisa = thissa.executeQuery(sql);
						thisa.last();
						int thisd = thisa.getRow();// 获得当前行号：此处即为最大记录数 ,无效时为0
						thisa.close();// 更新后关闭此线程,不然更新数据多了就会异常
						if (thisd > 0) {
							theok = true;
							break;
						}

						parent = xggym.substring(0, xggym.length() - i);
					}
					thissa.close();// 更新后关闭此线程,不然更新数据多了就会异常
				}
				if (theok == false) {
					parent = "xxxxxxx";
				}
				updateParent(con, shoukaiseki_insert_kks, id, parent);
				// tablezb);//更新表数据
				if (error > 0) {
					theerror++;
				} else {
					endtj++;
					jl.setText("已成功更新:" + endtj + "条"
							+ df.format((float) endtj / tablebc));

				}
			}

			println("表  " + shoukaiseki_insert_kks + "  更新结束!");
			println("一共更新成功" + endtj + "次!");
			println("一共更新失败" + theerror + "次!");
			data = new java.util.Date();
			cal.setTime(data);// 设置时间
			long timeTwo = cal.getTimeInMillis();
			long daysapart = (timeTwo - timeOne) / (1000 * 60);// 分钟
			long daysaparts = (timeTwo - timeOne) / 1000 % 60;// 获得总秒数后取除以60的余数即为秒数
			println("结束时间为:" + bartDateFormat.format(data));
			println("共花费时间为" + daysapart + "分" + daysaparts + "秒");
			printlnSeikou();
		} catch (Exception e) {
			// TODO: handle exception
			println(e.getMessage());
			println(sql);
			printlnSippai();
		}
	}

	/**
	 * 清字段首位空
	 * 
	 * @throws BadLocationException
	 */
	public void delLDTrim() throws BadLocationException {
		sql = "";
		try {

			Calendar cal = Calendar.getInstance();
			java.util.Date data = new java.util.Date();
			cal.setTime(data);// 设置时间为当前时间
			long timeOne = cal.getTimeInMillis();
			println("开始时间为:" + bartDateFormat.format(data));

			/**
			 * 清location字段首尾空
			 */
			println("清location字段首尾空");
			sql = "UPDATE " + shoukaiseki_insert_kks
					+ " SET location = trim(location) ";
			println(sql);
			update(con, sql);
			println("清location字段首尾空完成\n");
			/**
			 * 清DESCRIPTION字段首尾空
			 */
			println("清description字段首尾空");
			sql = "UPDATE " + shoukaiseki_insert_kks
					+ " SET DESCRIPTION = trim(DESCRIPTION) ";
			println(sql);
			update(con, sql);
			println("清description字段首尾空完成\n");

			println("表  " + shoukaiseki_insert_kks + "  清首尾空结束!");
			data = new java.util.Date();
			cal.setTime(data);// 设置时间
			long timeTwo = cal.getTimeInMillis();
			long daysapart = (timeTwo - timeOne) / (1000 * 60);// 分钟
			long daysaparts = (timeTwo - timeOne) / 1000 % 60;// 获得总秒数后取除以60的余数即为秒数
			println("结束时间为:" + bartDateFormat.format(data));
			println("共花费时间为" + daysapart + "分" + daysaparts + "秒");
			printlnSeikou();
		} catch (Exception e) {
			// TODO: handle exception
			println(e.getMessage());
			println(sql);
			printlnSippai();
		}

	}

	/**
	 * 更新命令,成功执行后返回true
	 * 
	 * @param con
	 * @param sql
	 */
	public boolean update(Connection con, String sql) {
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			pst.execute();
			pst.close();// 更新后关闭此线程,不然更新数据多了就会异常
		} catch (Exception e) {
			e.printStackTrace();
			error++;
			println("" + error);
			println(e.getMessage());
			println(sql);
			// TODO: handle exception
			return false;
		}
		return true;
	}

	/**
	 * 生成导出命令
	 */
	public void expToHyou() {
		String fromuser = jtfromuser.getText();
		String frompassword = jtfrompassword.getText();
		String fromsid = jtfromsid.getText();
		String touser = jttouser.getText();
		String topassword = jttopassword.getText();
		String tosid = jttosid.getText();
		SimpleDateFormat date = new SimpleDateFormat(
		"yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		java.util.Date data = new java.util.Date();
		cal.setTime(data);// 设置时间为当前时间
		long timeOne = cal.getTimeInMillis();
		
		String s = "mkdir c:\\temp\n" + "exp " + fromuser + "/" + frompassword
				+ "@" + fromsid + " file=c:\\temp\\kks_bak_" + date.format(data)
				+ ".dmp tables=" + locsystem + "," + locations + ","
				+ locstatus + "," + locoper + "," + lochierarchy + ","
				+ locancestor;
		textPane.setText(s);

	}

	/**
	 * 生成导入命令
	 */
	public void impToHyou() {
		String fromuser = jtfromuser.getText();
		String frompassword = jtfrompassword.getText();
		String fromsid = jtfromsid.getText();
		String touser = jttouser.getText();
		String topassword = jttopassword.getText();
		String tosid = jttosid.getText();
		SimpleDateFormat date = new SimpleDateFormat(
		"yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		java.util.Date data = new java.util.Date();
		cal.setTime(data);// 设置时间为当前时间
		long timeOne = cal.getTimeInMillis();

		String s = "" + "imp " + touser + "/" + topassword + "@" + tosid
				+ " fromuser=" + fromuser + " touser=" + touser
				+ "  ignore=y file=c:\\temp\\kks_bak_" + date.format(data)
				+ ".dmp tables=" + locsystem + "," + locations + ","
				+ locstatus + "," + locoper + "," + lochierarchy + ","
				+ locancestor;
		textPane.setText(s);

	}

	/**
	 * 输出SQL命令成功信息
	 * 
	 * @throws BadLocationException
	 */
	public void printlnSeikou() {
			println("------------成功执行更新完毕!-----------", true);
	}

	/**
	 * 输出SQL命令失败信息
	 * 
	 * @throws BadLocationException
	 */
	public void printlnSippai() {

			println("------------更新错误??????-----------", true);
	}

	/**
	 * 将数据插入五个表
	 * 
	 * @param a	分組
	 * @throws BadLocationException
	 */
	public void insertAllHyou(boolean a) throws BadLocationException {
		try {
			sql = "select   id,location,parent,description " + "from " + shoukaiseki_insert_kks
					+" where SETUMEINUM is null or SETUMEINUM='"+systemid+"'";
			if(!a){
				sql = "select   id,location,parent,description " + "from " + shoukaiseki_insert_kks ;
			}
			Statement tablebfb = con
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			println(sql);
			ResultSet ablebr = tablebfb.executeQuery(sql);
			ablebr.last();
			int tablebc = ablebr.getRow(); // 获得当前行号：此处即为最大记录数 ,无效时为0
			ablebr.close();
			tablebfb.close();

			Statement st = con.createStatement();
			println(sql);
			ResultSet r = st.executeQuery(sql);

			println("需要更新的总记录数总共:" + tablebc);
			jl.setText("需要更新的总记录数总共:" + tablebc);
			endtj = 0;// 统计总更新数据条数


			int theerror = 0;

			setIdRowstamp(con);
			Calendar cal = Calendar.getInstance();
			java.util.Date data = new java.util.Date();
			cal.setTime(data);// 设置时间为当前时间
			long timeOne = cal.getTimeInMillis();
			println("开始时间为:" + bartDateFormat.format(data));
			

			/**
			 * LoCanCesToer表
			 */
			sql = "insert into " + locancestor + " (" + "Location,"
					+ "Ancestor," + "SystemId," + "SiteId," + "Orgid,"
					+ "locancestorid," + "rowstamp" + " ) values(?,?,'" +
							systemid+"','"+allsiteid+"','"+allorgid+"',locancestorseq.nextval,?)"; //
			PreparedStatement pstLoCanCesToer = con.prepareStatement(sql);
			/**
			 * lochierarchy
			 */
			sql = "insert into " + lochierarchy + " (" + "Location,"
			+ "Parent," + "SystemId,"
			+ "Children,"
			+ // 父子标识,如果XGGYM为子值,则为0,父值则为1
			"SiteId," + "Orgid," + "lochierarchyid," + "rowstamp"
			+ " ) values(?,?," + "'"+systemid+"',?," + "'"
			+ allsiteid + "'," + // SiteId
			"'" + allorgid + "',lochierarchyseq.nextval,?)"; //
			PreparedStatement pstLocHiEraRchy =con.prepareStatement(sql);
			/**
			 * LoCoper
			 */
			sql = "insert into " + locoper + " (" + "Location," + "SiteId,"
			+ "Orgid," + "locoperid," + "rowstamp" + " ) values(?," + // Location
			"'" + allsiteid + "'," + // SiteId
			"'" + allorgid + "',locoperseq.nextval,?)"; //
			PreparedStatement pstLoCoper =con.prepareStatement(sql);
			/**
			 * Locstatus
			 */

			sql = "insert into " + locstatus + " (" + "Location," + "Status,"
					+ "ChangeBy," + "ChangeDate," + "Memo," + "SiteId,"
					+ "Orgid," + "LocStatusId," + "RowStamp" + " ) values(?,"
					+ // Location
					"'OPERATING',"
					+ // Status
					"'MAXADMIN',"
					+ // ChangeBy
					"TO_DATE( '2012/02/22 16:16:16', 'YYYY-MM-DD HH24:MI:SS'),"
					+ // ChangeDate
					"null," + // Memo 为空即可
					"'" + allsiteid + "'," + // SiteId
					"'" + allorgid + "',locstatusseq.nextval,?)";
			PreparedStatement pstLocstatus=con.prepareStatement(sql);
			
			
			/**
			 * Locations
			 */
			PreparedStatement pstLocations=con.prepareStatement(getLocationsSQL());
			while (r.next()) {
				error = 0;
				String id = r.getString("id");
				String location = r.getString("location");// 相关工艺码
				String parent = r.getString("parent");// 依赖,上层索引码,先置于于工艺码相同
				String description = r.getString("description");// 内容,还原SQL的'字符
				if(description!=null){
					if(!description.isEmpty()){
						description=description.replace("'", "''");
					}
				} 
				if(!a){
					updateLoCanCesTor(con, location,pstLoCanCesToer);
					updateLocstatus(con, location,pstLocstatus);
					updateLoCoper(con, location,pstLoCoper);
				}
				//之後的專業分類只插入這個表即可
				updateLocHiEraRchy(con, location, parent,pstLocHiEraRchy);
				if(!a){
					updateLocations(con, description, location,pstLocations);
				}
				
				if(endtj%1000==0){//可以设置不同的大小；如50，100，500，1000等等
					jl_parent.setText(jl_parent.getText()+"   正在将一批命令提交给数据库来执行!");
					pstLoCanCesToer.executeBatch();//将一批命令提交给数据库来执行
					pstLoCanCesToer.clearBatch();//清空此 Statement 对象的当前 SQL 命令列表;开启后效率
					pstLocHiEraRchy.executeBatch();
					pstLocHiEraRchy.clearBatch();
					pstLoCoper.executeBatch();
					pstLoCoper.clearBatch();
					pstLocstatus.executeBatch();
					pstLocstatus.clearBatch();
					pstLocations.executeBatch();
					pstLocations.clearBatch();
//					con.commit();
				}
				
				if (error > 0) {
					theerror++;
					return;
				} else {
					endtj++;
//					/**
//					 * 成功插入,删除此条记录
//					 */
//					sql = "delete from " + shoukaiseki_insert_kks + " where"
//							+ " id='" + id + "' and location='" + location
//							+ "' and parent='" + parent + "'";
//					PreparedStatement pst = con.prepareStatement(sql);
//					pst.execute();
//					pst.close();// 更新后关闭此线程,不然更新数据多了就会异常
					long timeTwo =  System.currentTimeMillis();
					long lo=(timeTwo-timeOne);//*(endtj / tablebc);
					long ko=1;
					if((lo/1000)!=0){
						ko= lo/1000;
					}
					String kouritu="<font color=\"#8A2BE2\">&nbsp;&nbsp;&nbsp;效率:"+endtj/ko+"条/秒;</font>";
					long daysapart = lo / (1000 * 60);// 分钟
					long daysaparts = lo / 1000 % 60;// 获得总秒数后取除以60的余数即为秒数
					String yahari="<font color=\"#FF7F50\">已经耗时:" + daysapart + "分" + daysaparts + "秒;</font>";
					long mada=lo*tablebc/endtj-lo;
					 daysapart = mada / (1000 * 60);// 分钟
					 daysaparts = mada / 1000 % 60;// 获得总秒数后取除以60的余数即为秒数
					 String madaJikann="<font color=\"#DC143C\">&nbsp;&nbsp;&nbsp;估计剩余时间:" + daysapart + "分" + daysaparts + "秒;</font>";
					jl.setText("<HTML><font color=\"#006400\">已成功更新:" + endtj + "条"
							+ df.format((float) endtj / tablebc)+";</font>"+kouritu+"&nbsp;&nbsp;&nbsp;"+yahari+madaJikann+"</HTML>");
				}					
			}
			pstLoCanCesToer.executeBatch();//将一批命令提交给数据库来执行
			pstLoCanCesToer.clearBatch();//清空此 Statement 对象的当前 SQL 命令列表;开启后效率
			pstLocHiEraRchy.executeBatch();
			pstLocHiEraRchy.clearBatch();
			pstLoCoper.executeBatch();
			pstLoCoper.clearBatch();
			pstLocstatus.executeBatch();
			pstLocstatus.clearBatch();
			pstLocations.executeBatch();
			pstLocations.clearBatch();
			
			
			pstLoCanCesToer.close();
			pstLocHiEraRchy.close();
			pstLoCoper.close();
			pstLocstatus.close();
			pstLocations.close();

			println("表  " + shoukaiseki_insert_kks + "  更新结束!");
			println("一共更新成功" + endtj + "次!");
			println("一共更新失败" + theerror + "次!");
			data = new java.util.Date();
			cal.setTime(data);// 设置时间
			long timeTwo = cal.getTimeInMillis();
			long daysapart = (timeTwo - timeOne) / (1000 * 60);// 分钟
			long daysaparts = (timeTwo - timeOne) / 1000 % 60;// 获得总秒数后取除以60的余数即为秒数
			println("结束时间为:" + bartDateFormat.format(data));
			println("共花费时间为" + daysapart + "分" + daysaparts + "秒");
			printlnSeikou();
			if(theerror>0){
				commitButton.setEnabled(false);
				rollbackButton.setEnabled(false);
				try {
					con.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}//异常则不更新
			}else{
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
			println(e.getMessage());
			println(sql);
			printlnSippai();
			// TODO: handle exception
		}
	}

	public void updateLoCanCesTor(Connection con, String location,PreparedStatement pst)
			throws SQLException {
		String ancestor = location;// 相关工艺码的索引,第一次写入的的索引跟相关工艺码相同
		try {
			while (1 == 1) {
//				locancestorid=osd.getSequenceNext("LOCANCESTORSEQ");
				locancestorrowstamp++;
				pst.setString(1,location);
				pst.setString(2,ancestor);
//				pst.setLong(3,locancestorid);
				pst.setLong(3,locancestorrowstamp);
				
//				savesql.addLastLine(sql);
				pst.addBatch();

				String sql = "select parent from " + shoukaiseki_insert_kks
						+ " WHERE location ='"+ancestor+"' ";
				psSelectParent.setString(1, ancestor);
				ResultSet thisa = psSelectParent.executeQuery();
				String thisscsym = "";
				if (thisa.next()) {
					thisscsym = thisa.getString("parent");
					ancestor = thisscsym.substring(0, thisscsym.length());
				}else{
					
					sql = "select location,parent from " + lochierarchy
							+ " WHERE location ='"+ancestor+"' ";
					psSelectLP.setString(1, ancestor);
					
					thisa.close();// 更新后关闭此线程,不然更新数据多了就会异常
					thisa =psSelectLP.executeQuery();
					if (thisa.next()) {
						thisscsym = thisa.getString("parent");
						ancestor = thisscsym.substring(0, thisscsym.length());
					}else {
						thisa.close();// 更新后关闭此线程,不然更新数据多了就会异常
						break;
					}
				}
				thisa.close();// 更新后关闭此线程,不然更新数据多了就会异常

				/**
				 * 检查是否为终止索引码
				 */
				for (int i = 0; i < parent.length; i++) {
					jl_parent.setText(ancestor + "?" + parent[i]);
					if (ancestor.equals(parent[i])) {
						return;
					}
				}

				if (ancestor.equals(location)) {
					println(sql);
					println("进入死循环,上层索引码与相关工艺码相同!   " + ancestor);
					println("location=" + location + "" );
					Thread.sleep(88888);
				}
			}
		} catch (Exception e) {
			error++;
			e.printStackTrace();
			println(e.getMessage());
			println(sql);
			System.out.println(sql);
			// TODO: handle exception
		}
	}

	public void updateLocHiEraRchy(Connection con, String location,
			String parent,PreparedStatement pst) throws SQLException {
//		lochierarchyid=osd.getSequenceNext("lochierarchyseq");
		lochierarchyrowstamp++;
		int Children = 0;// 父子标识,如果XGGYM为子值,则为0,父值则为1
		try {
			
			sql = "select id " + "from " + shoukaiseki_insert_kks
					+ " WHERE parent ='"+location+"' ";
			psSelectId.setString(1, location);
			ResultSet thisa = psSelectId.executeQuery();


//			sql = "select location,parent " + "from " + lochierarchy + " WHERE parent ='" + location + "' ";
			if (thisa.next()) {
				Children = 1;
			}
			thisa.close();// 更新后关闭此线程,不然更新数据多了就会异常
			
			pst.setString(1,location);
			pst.setString(2,parent);
			pst.setLong(3,Children);
//			pst.setLong(4,lochierarchyid);
			pst.setLong(4,lochierarchyrowstamp);
			pst.addBatch();
			
		} catch (Exception e) {
			error++;
			e.printStackTrace();
			println(e.getMessage());
			println(sql);
			System.out.println(sql);
			// TODO: handle exception
		}
	}

	public void updateLoCoper(Connection con, String location,PreparedStatement pst)
			throws SQLException {
//		locoperid=osd.getSequenceNext("locoperseq");
		locoperrowstamp++;
		try {
//			savesql.addLastLine(sql);
			pst.setString(1,location);
//			pst.setLong(2,locoperid);
			pst.setLong(2,locoperrowstamp);
			pst.addBatch();
		} catch (Exception e) {
			error++;
			e.printStackTrace();
			println(e.getMessage());
			println(sql);
			System.out.println(sql);
			// TODO: handle exception
		}
	}

	public void updateLocstatus(Connection con, String location,PreparedStatement pst)

	throws SQLException {
//		locstatusid=osd.getSequenceNext("locstatusseq");
		locstatusrowstamp++;
		try {
//			savesql.addLastLine(sql);
			pst.setString(1,location);
//			pst.setLong(2,locstatusid);
			pst.setLong(2,locstatusrowstamp);
			pst.addBatch();
			
		} catch (Exception e) {
			error++;
			e.printStackTrace();
			println(e.getMessage());
			println(sql);
			System.out.println(sql);
			// TODO: handle exception
		}
	}

	public String getLocationsSQL(){
		sql = "insert into "
			+ locations
			+ " (Location,Description,Type,ChangeBy,ChangeDate,"
			+ "Disabled,SiteId,Orgid,Status,Langcode,ISDEFAULT,"
			+ "LOCATIONSID,USEINPOPR,HASLD,AUTOWOGEN,STATUSDATE"
			+",ISREPAIRFACILITY,PLUSCLOOP,PLUSCPMEXTDATE"
			+" ) values( ?,?,"
			+ // Description
			"'"+type+"','MAXADMIN',"
			+ // ChangeBy
			datetime+","
			+ // ChangeDate
			"0,'" + allsiteid + "'," + // SiteId
			"'" + allorgid + "'," + // Orgid
			"'"+status+"'," + // Status
			"'ZH'," + // Langcode
			"'0'," + "locationsseq.nextval," + // locationsid
			"'0'," + "'0'," + "'0'" +
					","+datetime +
					",'0','0','0' )";
		return sql;
	}
	public void updateLocations(Connection con, String description,
			String location,PreparedStatement pst) throws SQLException {
//		locationsid=osd.getSequenceNext("locationsseq");
		locationsrowstamp++;
		try {
			pst.setString(1,location);
			pst.setString(2,description);
//			pst.setLong(3,locationsid);
			pst.addBatch();
//			savesql.addLastLine(sql);
		} catch (Exception e) {
			error++;
			e.printStackTrace();
			println(e.getMessage());
			System.out.println(sql);
			println(sql);
			// TODO: handle exception
		}
	}

	public void setIdRowstamp(Connection con) throws SQLException {
		String thiscommand = "";
		try {
			// locancestor
			Statement sql = con.createStatement();
			thiscommand = "select locancestorid from " + locancestor
					+ " order by locancestorid desc  ";
			println(thiscommand + ";");
			ResultSet rs = sql.executeQuery(thiscommand);
			if (rs.next()) {
				locancestorid = rs.getLong(1);
			}
			rs.close();
			thiscommand = "select rowstamp from " + locancestor
					+ " order by rowstamp desc  ";
			println(thiscommand + ";");
			rs = sql.executeQuery(thiscommand);
			if (rs.next()) {
				locancestorrowstamp = rs.getLong(1);
			}
			rs.close();
			// locoper
			thiscommand = "select locoperid from " + locoper
					+ " order by locoperid desc  ";
			println(thiscommand + ";");
			rs = sql.executeQuery(thiscommand);
			if (rs.next()) {
				locoperid = rs.getLong(1);
			}
			rs.close();
			thiscommand = "select rowstamp from " + locoper
					+ " order by rowstamp desc  ";
			println(thiscommand + ";");
			rs = sql.executeQuery(thiscommand);
			if (rs.next()) {
				locoperrowstamp = rs.getLong(1);
			}
			rs.close();
			// lochierarchy
			thiscommand = "select lochierarchyid from " + lochierarchy
					+ " order by lochierarchyid desc  ";
			println(thiscommand + ";");
			rs = sql.executeQuery(thiscommand);
			if (rs.next()) {
				lochierarchyid = rs.getLong(1);
			}
			rs.close();
			thiscommand = "select rowstamp from " + lochierarchy
					+ " order by rowstamp desc  ";
			println(thiscommand + ";");
			rs = sql.executeQuery(thiscommand);
			if (rs.next()) {
				lochierarchyrowstamp = rs.getLong(1);
			}
			rs.close();
			// locstatus
			thiscommand = "select locstatusid from " + locstatus
					+ " order by locstatusid desc  ";
			println(thiscommand + ";");
			rs = sql.executeQuery(thiscommand);
			if (rs.next()) {
				locstatusid = rs.getLong(1);
			}
			rs.close();
			thiscommand = "select rowstamp from " + locstatus
					+ " order by rowstamp desc  ";
			println(thiscommand + ";");
			rs = sql.executeQuery(thiscommand);
			if (rs.next()) {
				locstatusrowstamp = rs.getLong(1);
			}
			rs.close();
			// locations
			thiscommand = "select locationsid from " + locations
					+ " order by locationsid desc  ";
			println(thiscommand + ";");
			rs = sql.executeQuery(thiscommand);
			if (rs.next()) {
				locationsid = rs.getLong(1);
			}
			rs.close();
			thiscommand = "select rowstamp from " + locations
					+ " order by rowstamp desc  ";
			println(thiscommand + ";");
			rs = sql.executeQuery(thiscommand);
			if (rs.next()) {
				locationsrowstamp = rs.getLong(1);
			}
			rs.close();
			sql.close();
			println(locancestor
					+ " 表的locancestorid,locancestorrowstamp的起始值为 "
					+ locancestorid + "," + locancestorrowstamp);
			// locancestor
			// locoper
			println(locoper + " 表的locoperid,locoperrowstamp的起始值为 "
					+ locoperid + "," + locoperrowstamp);
			// lochierarchy
			println(lochierarchy
					+ " 表的lochierarchyid,lochierarchyrowstamp的起始值为 "
					+ lochierarchyid + "," + lochierarchyrowstamp);
			// locstatus
			println(locstatus
					+ " 表的locstatusid,locstatusrowstamp的起始值为 " + locstatusid
					+ "," + locstatusrowstamp);
			// locations
			println(locations
					+ " 表的locationsid,locationsrowstamp的起始值为 " + locationsid
					+ "," + locationsrowstamp);
		} catch (Exception e) {
			println(e.getMessage());
			println(thiscommand);
			// TODO: handle exception
		}
	}

	/**
	 * 查询表中的parent字段是否有空值,有返回false
	 * 
	 * @return
	 * @throws BadLocationException
	 */
	public boolean selectParentHaveNull() throws BadLocationException {
		try {
			sql = "select  * from " + shoukaiseki_insert_kks
					+ " where trim(parent) is null";
			Statement tablebfb = con
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			println(sql);
			ResultSet ablebr = tablebfb.executeQuery(sql);
			ablebr.last();
			int tablebc = ablebr.getRow(); // 获得当前行号：此处即为最大记录数 ,无效时为0
			if (tablebc > 0) {
				JOptionPane.showMessageDialog(getContentPane(), "表"
						+ shoukaiseki_insert_kks + "的parent字段含有空值,终止执行!",
						"错误提示!", JOptionPane.ERROR_MESSAGE);
				println("表" + shoukaiseki_insert_kks
						+ "的parent字段含有空值,终止执行!");
				return false;
			}
			printlnSeikou();
		} catch (Exception e) {
			println(e.getMessage());
			println(sql);
			printlnSippai();
			return false;
			// TODO: handle exception
		}

		return true;
	}

	public boolean getParentString() {
		String s = jt_parent.getText();
		if (s.isEmpty()) {
			JOptionPane.showMessageDialog(getContentPane(),
					"parent最终值为空,终止执行!", "错误提示!", JOptionPane.ERROR_MESSAGE);
			println("parent最终值为空,终止执行!");
			return false;
		} else {
			parent = s.split(parentSepareta);
			println("分隔符:" + parentSepareta);
			s = "";
			for (int i = 0; i < parent.length; i++) {
				s += "[" + parent[i] + "]\t";
			}
			println("parent最终索引码为:" + s);
			return true;
		}
	}

	public void readSetting() throws IOException, BadLocationException {
		newFile();// 无配置文件则自动新建文件
		reFile();// 读取配置文件
		setSetting();//
//		setCon();// 载入驱动
	}

	public void newFile() throws IOException {
		// 新建文件
		if (!txtfile.exists()) {
			if (txtfile.createNewFile()) {
				println("配置文件创建成功!");
				wrFile();// 写入文件
			} else {
				println("创建新文件失败!");
			}
		} else {
			println("发现配置文件" + fileName + "!");
		}
	}


	/**
	 * 生成ImpKksGui.sql
	 */
	public void wrFileSql() {
		println("正在创建文件" + sqlNameFile.getPath());
		try {
			// 常量类:各编码名称
			CharacterEncodingName ce = new CharacterEncodingName();
			FileOutputStream o = new FileOutputStream(sqlNameFile);
			// 采用UTF-8编码格式输出
			OutputStreamWriter out = new OutputStreamWriter(o, ce.UTF_8);
//			out.write(savesql.getContent());
			println("文件创建写入成功");
			out.close();
		} catch (Exception e) {
			// TODO: handle exception
			println(e.getMessage());
		}
	}
	
	public void wrFile() {
		println("正在创建文件" + txtfile.getPath());
		try {
			String age0 = wrString();
			// 常量类:各编码名称
			CharacterEncodingName ce = new CharacterEncodingName();
			FileOutputStream o = new FileOutputStream(txtfile);
			// 采用UTF-8编码格式输出
			OutputStreamWriter out = new OutputStreamWriter(o, ce.UTF_8);
			out.write(age0);
			println("文件创建写入成功");
			out.close();
		} catch (Exception e) {
			// TODO: handle exception
			println(e.getMessage());
		}
	}

	private void reFile() throws BadLocationException {
		// 读取文件
		println("\n\n读取文件!");
		try {
			String code = CharacterEncoding.getLocalteFileEncode(fileName);

			FileInputStream in = new FileInputStream(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(in,
					code));
			/**
			 * 解决win记事本保存UTF-8文件后文件头???问题, bom信息=EFBBBF
			 */
			br = new BufferedReader(new UnicodeReader(in, code));

			System.out.println("code=" + code);

			String sr = null;
			String a = null;
			String b = null;
			while ((sr = br.readLine()) != null) {
				println(sr);
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
				if (value < 0) {
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
				if (a.equals("fromuser")) {
					fromuser = b;
					continue;
				}
				if (a.equals("frompassword")) {
					frompassword = b;
					continue;
				}
				if (a.equals("fromsid")) {
					fromsid = b;
					continue;
				}
				if (a.equals("touser")) {
					touser = b;
					continue;
				}
				if (a.equals("topassword")) {
					topassword = b;
					continue;
				}
				if (a.equals("tosid")) {
					tosid = b;
					continue;
				}
				if (a.equals("parent")) {
					parentOne = b;
					continue;
				}
				if (a.equals("locations")) {
					locations = b;
					continue;
				}
				if (a.equals("locstatus")) {
					locstatus = b;
					continue;
				}
				if (a.equals("locoper")) {
					locoper = b;
					continue;
				}
				if (a.equals("lochierarchy")) {
					lochierarchy = b;
					continue;
				}
				if (a.equals("locancestor")) {
					locancestor = b;
					continue;
				}
				if (a.equals("locsystem")) {
					locsystem = b;
					continue;
				}
				if (a.equals("shoukaiseki_insert_kks")) {
					shoukaiseki_insert_kks = b;
					continue;
				}
				if (a.equals("parentLength")) {

					parentLength = Integer.parseInt(b);
					continue;
				}
				if (a.equals("parentCutLength")) {
					parentCutLength = Integer.parseInt(b);
					continue;
				}
				if (a.equals("parentLowerLength")) {
					parentLowerLength = Integer.parseInt(b);
					continue;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			println(e.getMessage());
		}
		println("url=" + url, true);
		println("driver=" + driver, true);
		println("user=" + user, true);
		println("password=" + password, true);
		println("fromuser=" + fromuser, true);
		println("frompassword=" + frompassword, true);
		println("fromsid=" + fromsid, true);
		println("touser=" + touser, true);
		println("topassword=" + topassword, true);
		println("tosid=" + tosid, true);
		println("parent=" + parentOne, true);
		println("locations=" + locations, true);
		println("locstatus=" + locstatus, true);
		println("locoper=" + locoper, true);
		println("lochierarchy=" + lochierarchy, true);
		println("locancestor=" + locancestor, true);
		println("locsystem=" + locsystem, true);
		println(
				"shoukaiseki_insert_kks=" + shoukaiseki_insert_kks, true);
		println("parentLength=" + parentLength, true);
		println("parentCutLength=" + parentCutLength, true);
		println("parentLowerLength=" + parentLowerLength, true);

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
		s = s + "url=jdbc:oracle:thin:@127.0.0.1:1521:orcl\r\n";
		s = s + "driver=oracle.jdbc.OracleDriver\r\n";
		s = s + "user=orclzhjq\r\n";
		s = s + "password=orclzhjq\r\n";
		s = s + "#导出時使用的用户名\r\n";
		s = s + "fromuser=maximo\r\n";
		s = s + "frompassword=maximo\r\n";
		s = s + "fromsid=orcl\r\n";
		s = s + "#导入時使用的用户名\r\n";
		s = s + "touser=maximo\r\n";
		s = s + "topassword=maximo\r\n";
		s = s + "tosid=orcl\r\n";
		s = s + "#parent最终标识符\r\n";
		s = s + "#索引最终值设置,插入LoCanCesTor表时的查找是否有与parent同名的location字段,\r\n";
		s = s + "#如果没有则以该数据为基准,使用//分隔符隔开,可匹配多个字符\r\n";
		s = s + "parent=JW//ZHJW\r\n";
		s = s + "#KKS导入正式库时要插入的5张表名\r\n";
		s = s + "locations=locations\r\n";
		s = s + "locstatus=locstatus\r\n";
		s = s + "locoper=locoper\r\n";
		s = s + "lochierarchy=lochierarchy\r\n";
		s = s + "locancestor=locancestor\r\n";
		s = s + "#KKS导出时的表\r\n";
		s = s + "locsystem=locsystem\r\n";
		s = s + "#KKS新增编码存放的表\r\n";
		s = s + "shoukaiseki_insert_kks=shoukaiseki_insert_kks\r\n";
		s = s + "#索引的Location最低有效位数\r\n";
		s = s + "parentLength=5\r\n";
		s = s + "#索引是的首次截取Location的位数作为Parent做查询\r\n";
		s = s + "parentCutLength=2\r\n";
		s = s + "#循环索引时Parent的最小位数\r\n";
		s = s + "parentLowerLength=2\r\n";
		return s;
	}

	public void setSetting() {
		jtfromuser.setText(fromuser);
		jtfrompassword.setText(frompassword);
		jtfromsid.setText(fromsid);
		jttouser.setText(touser);
		jttopassword.setText(topassword);
		jttosid.setText(tosid);
		jt_parent.setText(parentOne);
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

	public void println() {
		println("");
	}
	
	public void println(int age) {
		println("" + age);
	}

	public void println(String age)  {
		try {
			textPane.addLastLine(age, false);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void println(String age, boolean b) {
		try {
			textPane.addLastLine(age, b);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void printConnection() throws BadLocationException{
		println("url=" + url, true);
		println("driver=" + driver, true);
		println("user=" + user, true);
		println("password=" + password, true);
	}
}



