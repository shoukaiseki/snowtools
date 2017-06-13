package com.shoukaiseki.birt.text;

import com.shoukaiseki.birt.BirtProperty;

import nu.xom.Attribute;
import nu.xom.Element;


public class BirtTextProperty extends BirtProperty{
	Attribute textProperty;
	public BirtTextProperty(String type) {
		// TODO Auto-generated constructor stub
		super("text-property");
		removeAttribute("id");
		textProperty= new Attribute("name", type);
		element.addAttribute(textProperty);
	}
	
	public void setText(String text){
		element.appendChild(text);
	}
	
	
}


