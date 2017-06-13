package com.shoukaiseki.birt;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.shoukaiseki.file.xom.CDATAFactory;
import com.shoukaiseki.file.xom.ElementResult;
import com.shoukaiseki.tuuyou.logger.PrintLogs;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Text;

public  class BirtProperty extends PrintLogs implements BirtElement,BirtPuropathi{
	protected Element element;
	public static final String textAlign="textAlign";
//	protected Map<String,Attribute> attributeMap=new HashMap();
//	protected Map<String,String> propertyMap=new HashMap();
//	protected Map<String,Text> methodMap=new HashMap();
//	protected int id=0;
//	protected Attribute idAttribute=null;
	
	
	protected BirtProperty(String name){
		element=new Element(name);
//		id=BirtRepoto.getID();
		addAttribute("id",""+BirtRepoto.getID());
	}
	
	public BirtProperty(Element ele) {
		// TODO Auto-generated constructor stub
		this.element=ele;
	}
	
	public void addProperty(String name,String value){
		Element ele=new Element("property");
		ele.addAttribute(new Attribute("name",name));
		Text text=new Text(value);
		ele.appendChild(text);
		element.appendChild(ele);
	}
	
	public void addProperty(String name, int value) {
		// TODO Auto-generated method stub
		Element ele=new Element("property");
		ele.addAttribute(new Attribute("name",name));
		Text text=new Text(""+value);
		ele.appendChild(text);
		element.appendChild(ele);
		
	}
	
	public int getPropertyInt(String string) {
		// TODO Auto-generated method stub
		int c=-1;
		ElementResult er=new ElementResult(element);
		er.toukeiElements("property");
		while(er.next()){
			Element ele=er.getElementKo();
			if(ele.getLocalName().equals("property")){
				Attribute att = ele.getAttribute("name");
				if(att.getValue()!=null&&att.getValue().equals(string)){
					return Integer.parseInt(ele.getValue());
				}
			}
		}
//		for(int i=0;i<element.getChildCount();i++){
//			Element ele = (Element) element.getChild(i);
//			if(ele.getLocalName().equals("property")){
//				Attribute att = ele.getAttribute("name");
//				if(att.getLocalName().equals(string)){
//					return Integer.parseInt(att.getValue());
//				}
//			}
//			
//		}
		return c;
		
	}
	public void addAttribute(String name,String value){
		Attribute attribute = new Attribute(name,value);
		element.addAttribute(attribute);
	}
	
	
	
	public void addScriptMethod(String name,String js){
        Text te = CDATAFactory.makeCDATASection(js);
//		methodMap.put(name,te);
		Element ele=new Element("method");
		ele.addAttribute(new Attribute("name",name));
		ele.appendChild(te);
		element.appendChild(ele);
	}
	
//	protected Element putAllAttributeToElement(Element element){
//		Set<String> key = attributeMap.keySet();
//        Iterator it = key.iterator();
//        while(it.hasNext()) {
//			String s=(String) it.next();
//			element.addAttribute(attributeMap.get(s));
//		}
//        
//       key=methodMap.keySet();
//       it = key.iterator();
//       while(it.hasNext()) {
//    	   String s=(String) it.next();
//    	   Element ele=new Element("method");
//    	   ele.addAttribute(addAttribute("name",s));
//    	   Text text=methodMap.get(s);
//    	   ele.appendChild(text);
//    	   element.appendChild(ele);
//       }
//       key=propertyMap.keySet();
//       it = key.iterator();
//       while(it.hasNext()) {
//    	   String s=(String) it.next();
//    	   Element ele=new Element("property");
//    	   ele.addAttribute(addAttribute("name",s));
//    	   Text text=new Text(propertyMap.get(s));
//    	   ele.appendChild(text);
//    	   element.appendChild(ele);
//       }
//
//
//       return element;
//	}
	
	

	public Element getElement(){
		return this.element;
	}
	
	protected void removeAttribute(String key){
		for (int i=0;i<element.getAttributeCount();i++) {
			Attribute attribute = element.getAttribute(i);
			if(attribute.getLocalName().equals(key)){
				element.removeAttribute(attribute);
			}
		}
	}

	
	public void removeElement(Element ele){
//		System.out.println(ele.toXML());
		for(int i=0;i<element.getChildCount();i++){
			Node child = element.getChild(i);
//			System.out.println(child.toXML());
			if(child.equals(ele)){
//				System.out.println("同じ(onaji)[一样]");
				element.removeChild(i);
			}
		}
	}
	
	
}


