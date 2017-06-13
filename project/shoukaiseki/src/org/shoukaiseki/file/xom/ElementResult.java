package org.shoukaiseki.file.xom;

import nu.xom.Element;
import nu.xom.Elements;

/** 以SQL中 Result形式返回数据
 * @author 蒋カイセキ    Japan-Tokyo  2012-5-25
 * @ブログ http://shoukaiseki.blog.163.com/
 * @E-メール jiang28555@Gmail.com
 */
public class ElementResult {
	Element element=null;
	Element elementKo=null;
	int count=0;
	int kaunnto=0;
	String name=null;
	
	public ElementResult(Element element){
		this.element=element;
	}

	/**统计某節點個數
	 * @param name
	 * @return
	 */
	public int toukeiElements(String name){
		kaunnto=0;
//		System.out.println("coCount="+element.getChildCount());
		this.name=name;
//		System.out.println(element);
		Elements elements = element.getChildElements();
		for (int i = 0; i < elements.size(); i++) {
			Element child = (Element) elements.get(i);
//			System.out.println(child.getLocalName());
			//插入在header  detail footer之前
			if(child.getLocalName().equals(name)){
//				System.out.println("count++ "+count);
				count++;
			}
		}
//		System.out.println(name+" count="+count);
		return count;
	}
	
	public boolean next(){
		int j=0;
		if(element!=null){
		Elements elements = element.getChildElements();
		for (int i = 0; i < elements.size(); i++) {
			Element ele = (Element) elements.get(i);
				if(ele.getLocalName().equals(name)){
					if(j==kaunnto){
						elementKo=ele;
						kaunnto++;
						return true;
					}
					j++;
				}
			}
		}
		return false;
	}
	
	public Element getElementKo(){
		return elementKo;
	}
	
	public Element getElementKo(int index){
		ElementResult er=new ElementResult(element);
		er.toukeiElements(name);
		int i=0;
		while(er.next()){
			if(i==index){
				return er.getElementKo();
			}
			i++;
		}
		
		return null;
	}
	
	public int getCount(){
		return count;
	}
}


