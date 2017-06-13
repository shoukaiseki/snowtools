package com.shoukaiseki.birt;

import nu.xom.Attribute;
import nu.xom.Element;

import com.shoukaiseki.birt.BirtElement;
import com.shoukaiseki.birt.BirtProperty;
import com.shoukaiseki.birt.text.BirtTextProperty;

public class BirtLabel extends BirtProperty implements BirtElement{

	BirtTextProperty textProperty=new BirtTextProperty("text");
	public BirtLabel() {
		// TODO Auto-generated constructor stub
		super("label");
		Element ele = textProperty.getElement();
		element.appendChild(ele);
	}
	
	public void setText(String text){
		textProperty.setText(text);
	}
	
}


