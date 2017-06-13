package com.tomaximo.repoto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ExcelNoSiki {
	// 無効
	public static final int column = 1;
	public static final int row = 2;
	public static final int cell = 3;
	public static final int string = 4;
	// セパレーター
	public static final String separeta = "separeta";
	public HashMap<String, LinkedList<ExcelNoSikiObujekuto>> hm = new HashMap();

	public ExcelNoSiki() {
	}

	public LinkedList<ExcelNoSikiObujekuto> getSource(String destination) {
		// TODO Auto-generated method stub
		return hm.get(destination);

	}

	public void addSource(String destination, String key, String value) {
		System.out.println("add=" + destination + "," + key + "," + value);
		 LinkedList<ExcelNoSikiObujekuto> linkedList = hm.get(destination);
		 
		if (linkedList == null) {
			linkedList = new LinkedList<ExcelNoSikiObujekuto>();
			hm.put(destination, linkedList);
		}
		ExcelNoSikiObujekuto eso = new ExcelNoSikiObujekuto(destination, key, value);
		linkedList.add(eso);
	}

	public int getTaipu(String type) {
		if (type.equals("column")) {
			return column;
		} else if (type.equals("row")) {
			return row;
		} else if (type.equals("cell")) {
			return cell;
		} else if (type.equals("string")) {
			return string;
		}
		return 0;
	}

}


