package org.tomaximo.repoto;

import java.util.ArrayList;
import java.util.HashMap;

public class CopyOfExcelNoSiki{
		//無効
		public static final int column=1;
		public static final int row=2;
		public static final int cell=3;
		public static final int string=4;
		//セパレーター
		public static final String separeta="separeta";
		public HashMap<String,HashMap<String,ArrayList>> hm=new HashMap();
		public CopyOfExcelNoSiki(){
		}
		public HashMap<String, ArrayList> getSource(String destination) {
			// TODO Auto-generated method stub
			return hm.get(destination);
			
		}
		public void addSource(String destination,String key,String value){
			System.out.println("add="+destination+","+key+","+value);
			HashMap<String, ArrayList> hashMap = hm.get(destination);
			if(hashMap==null){
				hashMap = new HashMap<String,ArrayList>();
				hm.put(destination, hashMap);
			}
			ArrayList arrayList = hashMap.get(key);
			if(arrayList==null){
				ArrayList al = new ArrayList();
				al.add(value);
				hashMap.put(key, al);
			}else{
				arrayList.add(value);
			}
		}
		public  int getTaipu(String type){
			if(type.equals("column")){
				return column;
			}else if(type.equals("row")){
				return row;
			}else if(type.equals("cell")){
				return cell;
			}else if(type.equals("string")){
				return string;
			}
			return 0;
		}
	}


