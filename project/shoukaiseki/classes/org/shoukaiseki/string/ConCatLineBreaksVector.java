package org.shoukaiseki.string;

import java.util.ArrayList;
import java.util.Vector;

public class ConCatLineBreaksVector {
	
	private String lineBreaks="\r\n";
	private ArrayList vector=new ArrayList();
	public ConCatLineBreaksVector(){
		
	}
	public ConCatLineBreaksVector(String content){
		vector.clear();
		this.vector.add(content);
	}
	public ConCatLineBreaksVector(String lineBreaks,String content){
		vector.clear();
		this.lineBreaks=lineBreaks;
		this.vector.add(content);
	}

	/**
	 * 在content末尾加入一行内容age0,如果age0内容为空时则直接取消在String concat前的LineBreaks
	 * @param age0
	 * 			加入的行内容
	 * @return content
	 */
	public void addLastLine(String age0){
		vector.add(age0+lineBreaks);
	}

	public void setContent(String age0){
		vector.clear();
		vector.add(age0);
	}
	public String getContent(){
		return vector.toString();
	}
	public Object getLength() {
		// TODO Auto-generated method stub
		return vector.size();
	}

}


