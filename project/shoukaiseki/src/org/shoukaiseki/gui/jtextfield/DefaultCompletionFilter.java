package org.shoukaiseki.gui.jtextfield;

import java.util.ArrayList;
import java.util.Vector;

public class DefaultCompletionFilter implements CompletionFilter {
	/**
	 * 设置补全分隔符,默认为null则全文匹配
	 */
	private String separeta=null;
    private Vector vector;

    public DefaultCompletionFilter() {
        vector = new Vector();
    }

    public DefaultCompletionFilter(Vector v) {
        vector = v;
    }
    /**
     * 
     * @param v			补全String组
     * @param separeta	设置补全分隔符,默认为null则全文匹配
     */
    public DefaultCompletionFilter(Vector v,String separeta) {
        vector = v;
        this.separeta=separeta;
    }
    
    public ArrayList filter(String text) {
        ArrayList list=new ArrayList();
        String txt=text;
        if(separeta!=null){
            int index=text.lastIndexOf(separeta);
            if(index!=-1){
            	txt=text.substring(index+1,text.length());
            }
        }
        txt=txt.trim();
//        System.out.println(txt);
        int length=txt.length();
        for(int i=0;i<vector.size();i++){
            Object o=vector.get(i);
            String str=o.toString();
            /**
             * 忽略大小写toLowerCase()
             */
            if(length==0||str.toLowerCase().startsWith(txt.toLowerCase()))   //输入内容与记录前端比较
                list.add(o);
        } 
        return list;
    }
    /**
     * 设置补全分隔符,默认为null则全文匹配
     * @param separeta
     */
    public void setSepareta(String separeta){
    	this.separeta=separeta;
    }
    /**
     * 
     * @return 补全分隔符,默认为null则全文匹配
     */
    public String getSepareta(){
    	return this.separeta;
    }
}


