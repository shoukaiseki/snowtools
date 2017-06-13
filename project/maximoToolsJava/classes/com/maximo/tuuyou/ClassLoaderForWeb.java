package com.maximo.tuuyou;

public class ClassLoaderForWeb {
	
	public ClassLoaderForWeb() {
	}
	
	public  static Class<?> loadClass(String className)
			throws ClassNotFoundException {
		Class<?> newClass=null;
		ClassLoader loader = (Thread.currentThread().getContextClassLoader());
		newClass = loader.loadClass(className);
		return newClass;
	}
	
	public static void main(String[] args) throws Exception {
		ClassLoaderForWeb clf=new ClassLoaderForWeb();
		Class<?> loadClass = clf.loadClass("hello");
		loadClass.newInstance();
		
	}


}
