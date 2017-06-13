package com.shoukaiseki.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileJyouhou {
	// 创建时间
	public final static int SAKUSEIJIKANN = 0;
	// 修改时间
	public final static int HENNKOUJIKANN = 1;
	// 访问时间
	public final static int AKUSESUJIKANN = 2;

	public final static double B=1D;
	public final static double KB=1024D*B;
	public final static double MB=1024D*KB;
	public final static double GB=1024D*MB;
	public final static double TB=1024D*GB;
	/**
	 * 获取文件更改时间
	 * 
	 * @param file
	 * @return
	 */
	public static Date getFileHennkouJikann(File file) {
		long modifiedTime = file.lastModified();
		Date hennkouJikann = new Date(modifiedTime);// 返回此抽象路径名表示的文件最后一次被修改的时间。
		return hennkouJikann;
	}

	/**
	 * 获取文件更改时间
	 * 
	 * @param file
	 * @return
	 */
	public static String getFileHennkouJikannFormat(File file, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(getFileHennkouJikann(file));
	}

	/**
	 * 復帰には時間がフォーマットされている
	 * 
	 * @param file
	 * @param format
	 *            書式文字列
	 * @return
	 * @throws IOException
	 */
	public static String getFileSakuseiJikannFormat(File file, String format)
			throws IOException {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(getFileSakuseiJikann(file));

	}

	/**
	 * 读取文件创建时间
	 * 
	 * @return
	 * @throws IOException
	 */
	public static Date getFileSakuseiJikann(File file) throws IOException {
		String strTime = null;
		String cmd = null;
		Date date = null;
		cmd = "cmd /C dir " + file.getAbsolutePath() + " /tc";
		Process p = Runtime.getRuntime().exec(cmd);
		InputStream is = p.getInputStream();

		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line;
		for (int i = 0; i < 5; i++) {
			br.readLine();
		}
		line = br.readLine();
		if (line != null) {
			strTime = line.substring(0, 17);
		}
		SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-ddhh:mm");
		try {
			if (strTime != null)
				date = d.parse(strTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(cmd+"  创建时间	" + date);
		return date;
	}

	/**
	 * 
	 * @param file
	 * @param jikannTaipu
	 *            　　復帰時間の型
	 * @return
	 * @throws IOException
	 */
	public static Date getJikannJdk7u2(File file, int jikannTaipu)
			throws IOException {
		Date date = null;
		BasicFileAttributes fAttr = Files.readAttributes(file.toPath(),
				BasicFileAttributes.class);
		if (jikannTaipu == SAKUSEIJIKANN) {
			date = new Date(fAttr.creationTime().toMillis());// 取得文件创建时间
		} else if (jikannTaipu == HENNKOUJIKANN) {
			date = new Date(fAttr.lastModifiedTime().toMillis());// 取得文件最后修改时间
		} else if (jikannTaipu == AKUSESUJIKANN) {
			date = new Date(fAttr.lastAccessTime().toMillis());// 取得文件最后访问时间
		}
		return date;
	}

	/**
	 * 
	 * @param file
	 * @param jikannTaipu
	 *            復帰時間の型
	 * @param format
	 *            SimpleDateFormat格式化字符
	 * @return
	 * @throws IOException
	 */
	public static String getJikannFormatJdk7u2(File file, int jikannTaipu,
			String format) throws IOException {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(getJikannJdk7u2(file, jikannTaipu));
	}

	/**
	 * 设置文件的最后访问时间
	 * 
	 * @param file
	 * @param date
	 * @throws IOException
	 */
	public static void setJikannJdk7u2(File file, int jikannTaipu, Date date)
			throws IOException {
		BasicFileAttributeView fView = Files.getFileAttributeView(
				file.toPath(), BasicFileAttributeView.class);

		if (jikannTaipu == SAKUSEIJIKANN) {
			fView.setTimes(null,null, FileTime.fromMillis(date.getTime()) ); // 设置文件的最后访问时间
		} else if (jikannTaipu == HENNKOUJIKANN) {
			fView.setTimes( FileTime.fromMillis(date.getTime()), null,null); // 设置文件的最后访问时间
		} else if (jikannTaipu == AKUSESUJIKANN) {
			// 设置文件的最后访问时间
			fView.setTimes(null, FileTime.fromMillis(date.getTime()), null); // 设置文件的最后访问时间
		}
	}

	/**
	 * ファイルのサイズを返します
	 * @param file
	 * @param taipu		FileJyouhou.B||KB||MB||GB||TG
	 * @return
	 * @throws IOException
	 */
	public static double getSizeJdk7u2(File file,double taipu)
			throws IOException {
		double size = 0D;
		BasicFileAttributes fAttr = Files.readAttributes(file.toPath(),
				BasicFileAttributes.class);
			size = (double)fAttr.size()/taipu;// 取文件大小
		return size;
	}
}


















