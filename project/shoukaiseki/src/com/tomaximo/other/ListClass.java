package com.tomaximo.other;

import java.io.File;
import java.util.Map;
import java.util.Vector;

import com.shoukaiseki.file.FindFile;
import com.shoukaiseki.file.SousaFile;
import com.shoukaiseki.file.md5.DiffFileMD5;

public class ListClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String source="e:/bea10_3/WorkspaceHuarun/huarun/WebRoot/WEB-INF/classes";
		String type="*.class";
		
		
		//無視のファイル
		String[] musi={"FindClass.class","FindClass$1.class","GetAllMaximoClassPath.class","getSystemFukugou.class","MaximoFukugouka.class","TheCost.class",
				"QbeBeanbak.class","MboRemotebak.class","WebClientRuntimebak.class",
				"FldPurOrderQty.class",
				"Dialog.class","JxsqApproveDateBean.class",
				"PurAssignDialogDataBean.class","CRFQAppBean.class",
				"delOPOpamdopName.class","FldRespPartyGroup.class",
				"FldPersonGroupTeamCalnum.class"};
		Vector<String> vector=new Vector();
		for (String string : musi) {
			vector.add(string);
		}
		
		//Bean相关的类包含的关键字,fileName.
		String[] webClass={"Bean","Dialog"};
		
		Vector<String> webVector=new Vector();
		for (String string : webClass) {
			webVector.add(string);
		}
		
		
		FindFile findFile = new FindFile();
		File[] files = findFile.getFiles(source, type);// 查找运行目录下的所有文件
		StringBuffer sbBean=new StringBuffer();
		StringBuffer sbOther=new StringBuffer();
		loop:for (int i = 0; i < files.length; i++) {
			String filePath = files[i].getPath();
			System.out.println(filePath);
			String name=files[i].getName();
			filePath=name;
			if (vector.contains(name)) {
				continue;
			}
			
			for (String string : webVector) {
				if(name.indexOf(string)!=-1){
					sbBean.append(filePath).append("\n");
					continue loop;
				}
			}
			sbOther.append(filePath).append("\n");
			
		}
		System.out.println(sbBean.toString());
		System.out.println(sbOther.toString());
		SousaFile sousaFile=new SousaFile();
		String path="W:/listBeanClass.txt";
		sousaFile.writeFile(new File(path), sbBean.toString());
		path="W:/listOtherClass.txt";
		sousaFile.writeFile(new File(path), sbOther.toString());

	}

	public static void hyoujiFiles(
			Map<String, DiffFileMD5.FileJyouhouKonnfigu> map, String path) {

	}

}


