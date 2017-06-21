package org.shoukaiseki.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;

import org.shoukaiseki.characterdetector.CharacterEncoding;
import org.shoukaiseki.constantlib.CharacterEncodingName;
import org.shoukaiseki.gui.jtextpane.JTextPaneDoc;
import org.tomaximo.tuuyouclass.KonntenntuOutToJInternalFrame;


public class SousaFile {
	private boolean print=true;
	private JTextPaneDoc jtpd=new JTextPaneDoc();
	public SousaFile() {
	}
	public SousaFile(boolean no) {
	}
	
	public static ByteArrayOutputStream readFileToByteArrayOutputStream(String filename){
		File file=new File(filename);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		InputStream in = null;  
		try {  
			// 根据文件创建文件的输入流  
			in = new FileInputStream(file);  
			// 创建字节数组  
			// 读取内容，放到字节数组里面  
			byte[] buffer = new byte[256];
			int n;
			while ((n = in.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
			;
		} catch (Exception e) {  
			e.printStackTrace();  
		} finally {  
			try {  
				// 关闭输入流  
				in.close();  
			} catch (Exception e) {  
				e.printStackTrace();  
			}  
		}  
		return out;
	}
	

	public String readFileToString(String fileName) {
		String code = CharacterEncoding.getLocalteFileEncode(fileName);
		return readFileToString(fileName, code);

	}

	/**文字列にファイルを読み込む
	 * @param fileName	ファイル名
	 * @param codeName	コーディング
	 * @return
	 */
	public String readFileToString(String fileName, String codeName) {
		String content = "";
		try {
			FileInputStream in = new FileInputStream(fileName);
//			System.out.println("文件字数为=" + in.available());

			byte[] bytes = new byte[in.available()];
			while ((in.read(bytes)) != -1)
				;

			content = new String(bytes, codeName).replaceAll("\r", "");// 按照文件编码格式进行转换为系统编码
//			System.out.println("文件内容为\n" + content);
			in.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return content;
	}

	public boolean writeFile(File file,String konntenntu)  {
		
		return this.writeFile(jtpd, file, konntenntu, print);
	}
	
	/**
	 * 向指定目录写入指定文件名和内容的文件
	 * 
	 * @param dirFile
	 * @param fileName
	 * @param content
	 * @throws IOException
	 * @Description:
	 */
	public static void writeFile(File dirFile, String fileName, byte[] bytes)
			throws IOException {
		// 如果文件名不存在，则创建文件夹
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		File crashFile = new File(dirFile, fileName);
		// 将错误报告写入文件
		FileOutputStream fous = new FileOutputStream(crashFile);
		fous.write(bytes,0,bytes.length);
		fous.flush(); 
		fous.close();
		
	}
	
	public boolean writeFile(KonntenntuOutToJInternalFrame kojf,File file,String konntenntu)  {
		return this.writeFile(kojf, file, konntenntu, print);
	}

	/**
	 * 
	 * @param kojf
	 * @param file			要写入的文件
	 * @param konntenntu	要写入的内容
	 * @param isPrint		是否输出状态信息
	 * @return
	 */
	public boolean writeFile(JTextPaneDoc kojf,File file,String konntenntu,boolean isPrint)  {
		
		this.print=isPrint;
		this.jtpd=kojf;
		if(print){
			try {
				jtpd.addLastLine("正在创建文件" + file.getPath(),true);
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			mkdir(file.getParent());
			newFile(file);
			// 常量类:各编码名称
			CharacterEncodingName ce = new CharacterEncodingName();
			FileOutputStream o = new FileOutputStream(file);
			// 采用UTF-8编码格式输出
			OutputStreamWriter out = new OutputStreamWriter(o, ce.UTF_8);
			out.write(konntenntu);
			if(print){
				jtpd.addLastLine("文件创建写入成功",true);
			}
			out.close();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			jtpd.addLastLine(e.getMessage());
			return false;
		}
	}

	public  void newFile(File file) throws IOException{
		// 新建文件
		if (!file.exists()) {
			if (file.createNewFile()){
				if(print){
					try {
						jtpd.addLastLine("成功创建一个新文件",true);
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}else{
				if(print){
					try {
						jtpd.addLastLine("创建新文件失败,请检查是否有权限!",true);
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} 
	}
	public  void mkdir(String dhirekutori) {
		File file = new File(dhirekutori);
		if (!file.isDirectory()) {
			file.mkdir();
			if(print){
				try {
					jtpd.addLastLine("成功创建一个文件夹" + file + "！",true);
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public boolean writeFile(KonntenntuOutToJInternalFrame textPane, File file, String konntenntu,
			boolean isPrint) {
		// TODO Auto-generated method stub
		return this.writeFile(textPane.getTextPane(), file, konntenntu, isPrint);
	}
}













