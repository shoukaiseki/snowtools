package org.shoukaiseki.string;
//字符串
public class ConCatLineBreaks {
	
	private String lineBreaks="\r\n";
	private StringBuilder content=new StringBuilder();
	public ConCatLineBreaks(){
		content=new StringBuilder();
	}
	public ConCatLineBreaks(String content){
		this.content=new StringBuilder(content);
	}
	public ConCatLineBreaks(String lineBreaks,String content){
		this.lineBreaks=lineBreaks;
		this.content=new StringBuilder(content);
	}
	/**
	 * 在content开始加入一行内容age0,如果age0内容为空时则直接取消在String concat前的LineBreaks
	 * @param age0
	 * 			加入的行内容
	 * @return content
	 */
	public String addHeadLine(String age0){
		if (age0==null) {
			return content.toString();
		}
		if (content.length()==0) {
			content.append(age0);
			return content.toString();
		}if(content.toString().trim().isEmpty()){
			this.content=new StringBuilder(age0);
			return content.toString();
		}
		content.insert(0,lineBreaks+age0);
		return content.toString();
	}

	/**
	 * 在content末尾加入一行内容age0,如果age0内容为空时则直接取消在String concat前的LineBreaks
	 * @param age0
	 * 			加入的行内容
	 * @return content
	 */
	public String addLastText(String age0){
		if (age0==null) {
			return content.toString();
		}
		if(content.toString().trim().isEmpty()){
			content.append(age0);
			return content.toString();
		}
		content.append(age0);
		return content.toString();
	}
	/**
	 * 在content末尾加入一行内容age0,如果age0内容为空时则直接取消在String concat前的LineBreaks
	 * @param age0
	 * 			加入的行内容
	 * @return content
	 */
	public String addLastLine(String age0){
		if (age0==null) {
			return content.toString();
		}
		if (content.length()==0) {
			content.append(age0);
			return content.toString();
		}if(content.toString().trim().isEmpty()){
			content.append(age0);
			return content.toString();
		}
		content.append(lineBreaks+age0);
		return content.toString();
	}
	/**
	 * 删除最开始一行
	 * @return
	 */
	public String delHeadLine(){
		int i=content.indexOf(lineBreaks)+lineBreaks.length();//位置+换行符长度
		content=content.delete(0, i);
		return content.toString();
	}
	/**
	 * 删除最末尾一行
	 * @return
	 */
	public String delLastLine(){
		int i=content.lastIndexOf(lineBreaks);//回车键位置
		content=content.delete(0,i);
		return content.toString();
	}
	/**
	 * 在开始加入空行
	 * @return content
	 */
	public String addHeadLineBreaks(){
		content.insert(0,lineBreaks);
		return content.toString();
	}
	/**
	 * 在末尾加入空行
	 * @return content
	 */
	public String addLastLineBreaks(){
		content.append(lineBreaks);
		return content.toString();
	}
	public String getContent(){
		return content.toString();
	}
	public String setContent(String age0){
		content.delete( 0, content.length() );
		content.append(age0);
		return content.toString();
	}
	public String getLineBreaks(){
		return lineBreaks;
	}
	public String setLineBreaks(String age0){
		lineBreaks=age0;
		return lineBreaks;
	}
	public int getLength(){
		return content.length();
	}
	public StringBuilder getStringBuilder(){
		return this.content;
	}
}


