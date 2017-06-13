package org.tomaximo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.shoukaiseki.math.PrintPercent;


public class PrintOneData {

	// ͳ������ٷֱȶ���
	public static PrintPercent pp = new PrintPercent(true);// ���Ϊ����̨ģʽ
	//
	public static DecimalFormat df = new DecimalFormat("0.0000"); // " "��д��ʽ��ģʽ
	// �籣��2λ����"0.00"����
	public static int error = 0;// ��¼��ݸ���ʧ�ܼ�¼
	public static int endtj = 0;// ͳ���ܸ����������

	public static void main(String[] args) {
		Connection con = null;
		String command = null;
		String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
		// mxe.db.driver=oracle.jdbc.OracleDriver
		String user = "asus";
		String password = "asus";
		try {
			// ��1��װ�ز�ע����ݿ��JDBC�����
			// ����JDBC��oracle��װĿ¼�µ�jdbc\lib\classes12.jar
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("������Ѽ���");
			// ע��JDBC����Щ�ط��ɲ��ô˾�
			java.sql.DriverManager
					.registerDriver(new oracle.jdbc.driver.OracleDriver());

			con = DriverManager.getConnection(url, user, password);
			System.out.println("OK,�ɹ����ӵ���ݿ�");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			String ssupername = "test12dcc";// ������,�����test12,test12dcc

			// �ٷֱ�
			Statement tablebfb = con
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);

			command = "select   ID,SBMC " + " from " + ssupername + " a "
					+ " ORDER BY sbmc";
			System.out.println(command);
			ResultSet ablebr = tablebfb.executeQuery(command);
			ablebr.last();
			int tablebc = ablebr.getRow(); // ��õ�ǰ�кţ��˴���Ϊ����¼�� ,��ЧʱΪ0

			pp.setTotal(tablebc);// ��Ҫ���µ�������
			System.out.println("��Ҫ���µ��ܼ�¼���ܹ�:" + tablebc);
			System.out.println("ÿ�ٷ�һ������:" + pp.getPercent());
			// �ٷֱ�
			// int supernameint=20;//�ӱ���
			Statement st = con.createStatement();

			System.out.println(command);
			ResultSet r = st.executeQuery(command);

			SimpleDateFormat bartDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			java.util.Date data = new java.util.Date();
			cal.setTime(data);// ����ʱ��Ϊ��ǰʱ��
			long timeOne = cal.getTimeInMillis();
			System.out.println("��ʼʱ��Ϊ:" + bartDateFormat.format(data));

			String sbmcbak = "";
			System.out.print("in (");
			int a = 0;
			while (r.next()) {
				String sbmc = r.getString(2);// �豸���
				if (!sbmc.equals(sbmcbak)) {
					if (a != 0) {
						System.out.print(",");
					}
					System.out.print("'" + sbmc + "'");
					a++;
				}
				sbmcbak = "";
				if (sbmc != null) {
					sbmcbak = sbmc.substring(0, sbmc.length());
				}
			}
			System.out.println(")\n\n���ظ���һ����" + a + "��!");

			data = new java.util.Date();
			cal.setTime(data);// ����ʱ��
			long timeTwo = cal.getTimeInMillis();
			long daysapart = (timeTwo - timeOne) / (1000 * 60);// ����
			long daysaparts = (timeTwo - timeOne) / 1000 % 60;// ����������ȡ����60������Ϊ����
			System.out.println("����ʱ��Ϊ:" + bartDateFormat.format(data));
			System.out.println("������ʱ��Ϊ" + daysapart + "��" + daysaparts + "��");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

