package com.shoukaiseki.file.md5;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 使用getFileMD5String(fileName)获取文件的MD5
 * @author 蒋カイセキ    Japan-Tokyo  2012-4-28
 * @ブログ http://shoukaiseki.blog.163.com/
 * @E-メール jiang28555@Gmail.com
 */
public class FileMD5 {
		
	protected static char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
	protected static MessageDigest messageDigest = null;
	static{
		try{
			messageDigest = MessageDigest.getInstance("MD5");
		}catch (NoSuchAlgorithmException e) {
			System.err.println(FileMD5.class.getName()+"初始化失败，MessageDigest不支持MD5Util.");
			e.printStackTrace();
		}
	}
	
	/**
	 * 计算文件的MD5
	 * @param fileName 文件的绝对路径
	 * @return
	 * @throws IOException
	 */
	public static String getFileMD5String(String fileName) throws IOException{
		File f = new File(fileName);
		return getFileMD5String(f);
	}
	
	/**
	 * 计算文件的MD5，重载方法
	 * @param file 文件对象
	 * @return
	 * @throws IOException
	 */
	public static String getFileMD5String(File file) throws IOException{
		FileInputStream in = new FileInputStream(file);
		FileChannel ch = in.getChannel();
		MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
		messageDigest.update(byteBuffer);
		return bufferToHex(messageDigest.digest());
	}
	
	public static String bufferToHex(byte bytes[]) {
	   return bufferToHex(bytes, 0, bytes.length);
	}
	
	public static String bufferToHex(byte bytes[], int m, int n) {
	   StringBuffer stringbuffer = new StringBuffer(2 * n);
	   int k = m + n;
	   for (int l = m; l < k; l++) {
	    appendHexPair(bytes[l], stringbuffer);
	   }
	   return stringbuffer.toString();
	}
	
	public static void appendHexPair(byte bt, StringBuffer stringbuffer) {
	   char c0 = hexDigits[(bt & 0xf0) >> 4];
	   char c1 = hexDigits[bt & 0xf];
	   stringbuffer.append(c0);
	   stringbuffer.append(c1);
	}
}


