package com.shoukaiseki.gui.jtable;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.shoukaiseki.gui.tuuyou.StringIcon;
import com.tomaximo.puroguramu.tuuyou.IconSigenn;

public class TableCellRendererIcon extends DefaultTableCellRenderer {

	public TableCellRendererIcon() {
		setOpaque(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		// TODO Auto-generated method stub
		if (value != null) {
			if (value instanceof StringIcon) {
				if(((StringIcon) value).name==null){
					setText("");
				}else{
					setText(((StringIcon) value).name);
				}
				setIcon(((StringIcon) value).icon);

				// System.out.println("----"+((StringIcon) value).name);
			} else {
//				setText((String) value);
			}
		}
		if(table.isCellSelected(row, column)){
			setBackground(table.getSelectionBackground());
			setForeground(table.getSelectionForeground());
		}else{
			setBackground(table.getBackground());
			setForeground(table.getForeground());
		}
		
		return this;
	}

}


