package org.shoukaiseki.file;

import java.io.*;
import org.shoukaiseki.characterdetector.CharacterEncoding;
import org.shoukaiseki.constantlib.CharacterEncodingName;
import org.shoukaiseki.tuuyou.logger.PrintLogs;

public class SousaFairu extends PrintLogs{
	
	public SousaFairu() {
		level=7;
	}
	public SousaFairu(boolean no) {
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
			debug("文件字数为=" + in.available(),7);

			byte[] bytes = new byte[in.available()];
			while ((in.read(bytes)) != -1)
				;

			content = new String(bytes, codeName).replaceAll("\r", "");// 按照文件编码格式进行转换为系统编码
			debug("文件内容为\n" + content,7);
			in.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return content;
	}

	

	/**
	 * 
	 * @param file			要写入的文件
	 * @param konntenntu	要写入的内容
	 * @return
	 */
	public boolean writeFile(File file,String konntenntu)  {
		
		debug("正在创建文件" + file.getPath());
		try {
			mkdir(file.getParent());
			newFile(file);
			// 常量类:各编码名称
			CharacterEncodingName ce = new CharacterEncodingName();
			FileOutputStream o = new FileOutputStream(file);
			// 采用UTF-8编码格式输出
			OutputStreamWriter out = new OutputStreamWriter(o, ce.UTF_8);
			out.write(konntenntu);
			debug("文件创建写入成功");
			out.close();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			debug(e.getMessage());
			return false;
		}
	}

	public  void newFile(File file) throws IOException{
		// 新建文件
		if (!file.exists()) {
			if (file.createNewFile()){
				debug("成功创建一个新文件");
			}else{
				debug("创建新文件失败,请检查是否有权限!");
			}
		} 
	}
	public  void mkdir(String dhirekutori) {
		File file = new File(dhirekutori);
		String d=dhirekutori;
		if (!file.isDirectory()) {
			file.mkdirs();
			debug("成功创建一个文件夹" + file + "！");
		}
	}
		
}
