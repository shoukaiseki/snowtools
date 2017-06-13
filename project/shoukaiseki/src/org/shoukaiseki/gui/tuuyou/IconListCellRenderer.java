package org.shoukaiseki.gui.tuuyou;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;


public class IconListCellRenderer extends JLabel implements ListCellRenderer {
	
	public IconListCellRenderer() {
		setOpaque(true);
	}

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		if (value != null) {
			if(value instanceof StringIcon){
				setText(((StringIcon) value).name);
				setIcon(((StringIcon) value).icon);
//				System.out.println("----"+((StringIcon) value).name);
			}else{
				setText((String) value);
			}
		}

		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		return this;
	}

}


