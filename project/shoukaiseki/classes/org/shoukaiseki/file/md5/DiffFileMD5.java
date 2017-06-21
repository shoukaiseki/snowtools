package org.shoukaiseki.file.md5;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.poi.hssf.record.FileSharingRecord;

import org.shoukaiseki.file.FindFile;
import org.shoukaiseki.map.MapHouhou;

public class DiffFileMD5 {

	private String source, destination, type;
	// (源目录,目标目录)映射,因比较的是相同目录下的文件
	private HashMap<String, String> mappinnguDhirekutori = new HashMap<String, String>();
	// 源目录MD5一样的文件
	private Map<String, FileJyouhouKonnfigu> sourceOnajiNoFairu = new HashMap();
	// 目标目录MD5一样的文件
	private Map<String, FileJyouhouKonnfigu> destinationOnajiNoFairu = new HashMap();

	// 源目录MD5不同的文件
	private Map<String, FileJyouhouKonnfigu> sourceTigauNoFairu = new HashMap();
	// 目标目录MD5不同的文件
	private Map<String, FileJyouhouKonnfigu> destinationTigauNoFairu = new HashMap();

	/**
	 * 定义源目录,目标目录
	 * 
	 * @param source
	 * @param destination
	 * @param type
	 *            类型,支持通配符,*为所有文件
	 */
	public DiffFileMD5(String source, String destination, String type) {
		this.source = source;
		this.destination = destination;
		addMappinnguDhirekutori(source, destination);
		this.type = type;
	}

	/**
	 * 增加源目录,目标目录映射
	 * 
	 * @param source
	 * @param destination
	 */
	public void addMappinnguDhirekutori(String source, String destination) {

		mappinnguDhirekutori.put(
				FormatPath(source.replace(this.source, this.destination)),
				FormatPath(destination));
	}

	/**
	 * 执行MD5验证
	 * 
	 * @throws IOException
	 */
	public void execute() throws IOException {
		FindFile findFile = new FindFile();
		File[] files = findFile.getFiles(source, type);// 查找运行目录下的所有文件
		for (int i = 0; i < files.length; i++) {
			String filePath = files[i].getPath();
			Set<String> set = mappinnguDhirekutori.keySet();
			Object[] key = set.toArray();
			for (Object object : key) {
				String mappinnguPath = mappinnguDhirekutori.get(object);
				if (mappinnguPath != null) {
					filePath = FormatPath(filePath)
							.replace(source, destination).replace(
									((String) object), mappinnguPath);
				}
			}
			String fileName = FormatPath(filePath + "/" + files[i].getName());
			FileJyouhouKonnfigu fj = new FileJyouhouKonnfigu();
			fj.file = files[i];
			fj.mappinnguAbsolutePath = fileName;
			fj.set();
			sourceTigauNoFairu.put(fileName, fj);
			// System.out.println(fileName+"\n"+fj.md5);
		}

		files = findFile.getFiles(destination, type);// 查找运行目录下的所有文件
		for (int i = 0; i < files.length; i++) {
			String filePath = files[i].getPath();
			String fileName = FormatPath(filePath + "/" + files[i].getName());
			FileJyouhouKonnfigu fj = new FileJyouhouKonnfigu();
			fj.file = files[i];
			fj.mappinnguAbsolutePath = fileName;
			fj.set();
			destinationTigauNoFairu.put(fileName, fj);
			// System.out.println("destination="+fileName+"\n"+fj.md5);
		}

		Set<String> key = MapHouhou.sort(sourceTigauNoFairu);// 排序
		Iterator it = key.iterator();
		for (int i = 1; it.hasNext(); i++) {
			String s = (String) it.next();
			FileJyouhouKonnfigu sourceFairu = sourceTigauNoFairu.get(s);
			FileJyouhouKonnfigu destinationFairu = destinationTigauNoFairu
					.get(s);
			if (sourceFairu != null && destinationFairu != null) {
				String sourceMD5 = sourceFairu.md5;
				String destinationMD5 = destinationFairu.md5;
				if (sourceMD5 != null && destinationMD5 != null) {
					if (sourceMD5.equals(destinationMD5)) {
						sourceOnajiNoFairu.put(s, sourceFairu);
						destinationOnajiNoFairu.put(s, destinationFairu);
						sourceTigauNoFairu.remove(s);
						destinationTigauNoFairu.remove(s);

					}
				}
			}
		}
		key = MapHouhou.sort(sourceTigauNoFairu);// 排序
		it = key.iterator();
		for (int i = 1; it.hasNext(); i++) {
			String s = (String) it.next();
			System.out.println(i + "\tkey=" + s + "----->"
					+ destinationTigauNoFairu.get(s));
		}
	}

	public String FormatPath(String path) {

		return path.replace("\\", "/").replace("/./", "/").replace("//", "/")
				.replace("//", "/");
	}

	/**
	 * 源ディレクトリ中に同じのファイル
	 * 
	 * @return
	 */
	public Map<String, FileJyouhouKonnfigu> getSourceOnajiNoFairu() {
		return this.sourceOnajiNoFairu;
	}

	/**
	 * 目标ディレクトリ中に同じのファイル
	 * 
	 * @return
	 */
	public Map<String, FileJyouhouKonnfigu> getDestinationOnajiNoFairu() {
		return this.destinationOnajiNoFairu;
	}

	/**
	 * 源ディレクトリ中に違うのファイル
	 * 
	 * @return
	 */
	public Map<String, FileJyouhouKonnfigu> getSourceTigauNoFairu() {
		return this.sourceTigauNoFairu;
	}

	/**
	 * 目标しレクトリ中に違うのファイル
	 * 
	 * @return
	 */
	public Map<String, FileJyouhouKonnfigu> getDestinationTigauNoFairu() {
		return this.destinationTigauNoFairu;
	}

	public class FileJyouhouKonnfigu {
		public File file = null;
		// 文件的md5
		public String md5 = null;
		// 映射后的目录全路径
		public String mappinnguAbsolutePath = null;
		// 文件修改时间
		public String fileDate = null;

		public void set() throws IOException {
			md5 = FileMD5.getFileMD5String(file);
		}
	}

}
















