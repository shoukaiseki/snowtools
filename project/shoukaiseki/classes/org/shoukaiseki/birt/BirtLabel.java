package org.shoukaiseki.birt;

import nu.xom.Attribute;
import nu.xom.Element;

import org.shoukaiseki.birt.BirtElement;
import org.shoukaiseki.birt.BirtProperty;
import org.shoukaiseki.birt.text.BirtTextProperty;

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


