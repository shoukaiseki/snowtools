package org.shoukaiseki.gui.jtextpane.autocompletion;

import java.util.ArrayList;
import java.util.Vector;

import org.shoukaiseki.string.StringHouhou;

public class DefaultCompletionJTextPaneFileter implements CompletionJTextPaneFilter{
	/**
	 * 设置补全分隔符,默认为null则全文匹配
	 */
	private String indexString="";//补全查找时的内容
	private String separeta=null;
    private Vector vector;

    public DefaultCompletionJTextPaneFileter() {
        vector = new Vector();
    }

    public DefaultCompletionJTextPaneFileter(Vector v) {
        vector = v;
    }
    /**
     * 
     * @param v			补全String组
     * @param separeta	设置补全分隔符,默认为null则全文匹配
     */
    public DefaultCompletionJTextPaneFileter(Vector v,String separeta) {
        vector = v;
        this.separeta=separeta;
    }
    
    public ArrayList filter(String text) {
        ArrayList list=new ArrayList();
        String txt=text;
        System.out.println("text = "+text);
        int index=-1;
        if(separeta!=null){
            index=text.lastIndexOf(separeta);
            if(index!=-1){
            	txt=text.substring(index+1,text.length());
            }
        }
        index=txt.lastIndexOf("\n");
        if(index!=-1){
        	txt=txt.substring(index+1,txt.length());
        }
        txt=StringHouhou.trimLeft(txt);
        indexString=txt;
        System.out.println("filter = "+txt);
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

    /**
     * 获取补全时搜索的字符
     */
	@Override
	public String getIndexString() {
		// TODO Auto-generated method stub
		return indexString;
	}
}

