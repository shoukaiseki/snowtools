package org.tomaximo.tuuyouclass;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.shoukaiseki.sql.ConnectionKonnfigu;


public class AllMaximoKonnfigu {

	public static final  int SOUSA_APPNAME=1;
	public static final  int SOUSA_TABLENAME=2;
	public static final  int SOUSA_PROCESSNAME=4;
	public static final  int SOUSA_PROCESSREVNAME=8;
	
	int autokey=0;
	private Konnfigu konnfigu;
	private Map<String, Konnfigu> appnameMap=new HashMap();
	public void addAppName(String appName){
		konnfigu.setAppName(appName);
	}

	public void setTableName(String tableName){
		konnfigu.setTableName(tableName);
	}
	public void setProcessrevName(String processrevName){
		konnfigu.setProcessrevName(processrevName);
	}
	public void setProcessName(String processName){
		konnfigu.setProcessName(processName);
	}
	public void setAppName(String appName){
		konnfigu.setAppName(appName);
	}
	
	public void save(){
		appnameMap.put(""+autokey++, konnfigu);
	}
	
	public void addKonnfigu(String tableName,String processrevName,String processName,String appName){
		konnfigu=new Konnfigu();
		konnfigu.setAppName(appName);
		konnfigu.setProcessName(processName);
		konnfigu.setProcessrevName(processrevName);
		konnfigu.setTableName(tableName);
		appnameMap.put(""+autokey++, konnfigu);
	}
	public void addKonnfigu(String tableName,String processrevName,String processName){
		konnfigu=new Konnfigu();
		konnfigu.setProcessName(processName);
		konnfigu.setProcessrevName(processrevName);
		konnfigu.setTableName(tableName);
		appnameMap.put(""+autokey++, konnfigu);
	}
	public Konnfigu[] getKonnfigu(String name,int type){
		int j=0;
		Konnfigu[] kon=new Konnfigu[]{};
        Set<String> key = appnameMap.keySet();
        Iterator it = key.iterator();
        for (int i=1; it.hasNext();i++) {
            String s = (String) it.next();
            Konnfigu ko=appnameMap.get(s);
            if(!ko.getAppName().equals(name)&&(type&SOUSA_APPNAME)>0){
            	continue;
            }
            if(!ko.getTableName().equals(name)&&(type&SOUSA_TABLENAME)>0){
            	continue;
            }
            if(!ko.getProcessName().equals(name)&&(type&SOUSA_PROCESSNAME)>0){
            	continue;
            }
            if(!ko.getProcessrevName().equals(name)&&(type&SOUSA_PROCESSREVNAME)>0){
            	continue;
            }
			kon=  AddKonnfiguMember(kon,ko);
        }
		return kon;
	}
	/**
	 * @desc 数组加入成员
	 * @param a源数组
	 * @return 加入后的数组
	 */
	public Konnfigu[] AddKonnfiguMember(Object[] a,Konnfigu o) {
		Class cl = a.getClass();
		if (!cl.isArray())
			return null;
		Class componentType = cl.getComponentType();
		int length = Array.getLength(a);
		int newLength = length +1;

		Object newArray = Array.newInstance(componentType, newLength);
		System.arraycopy(a, 0, newArray, 0, length);
		Konnfigu[] ko=(Konnfigu[]) newArray;
		ko[length]=o;
		return ko;
	}
	
	public void clear(){
		appnameMap.clear();
	}
	
	public static void main(String ages[]){
		AllMaximoKonnfigu amk=new AllMaximoKonnfigu();
		System.out.println(amk.getKonnfigu("1", 2).length);
		amk.addKonnfigu("1", "a", "A");
		System.out.println(amk.getKonnfigu("1", 2).length);
		amk.addKonnfigu("1", "b", "B");
		System.out.println(amk.getKonnfigu("1", 2).length);
		amk.clear();
		System.out.println(amk.getKonnfigu("1", 2).length);
	}

	public void addKonnfigu(String tableName, Object processrevName,
			String processName) {
		// TODO Auto-generated method stub
		addKonnfigu(tableName, (String)processrevName, processName);
	}
 }


