package org.shoukaiseki.birt;

import java.util.LinkedList;

import org.shoukaiseki.birt.ttable.BirtTTableCell;
import org.shoukaiseki.file.xom.ElementResult;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.ParentNode;

public  class BirtRow extends BirtProperty{
//	LinkedList<BirtTTableCell> tableCell = new LinkedList<BirtTTableCell>(); 
	
	public BirtRow(int column) {
		// TODO Auto-generated constructor stub
		super("row");
//		System.out.println("column="+column);
//		System.out.println("row int ele"+element.toXML());
		for (int i = 0; i < column; i++) {
			BirtTTableCell cell = new BirtTTableCell();
//			tableCell.add(cell);
			element.appendChild(cell.getElement());
//			System.out.println("row int"+element.toXML());
		}
	}
		
	public BirtRow(Element ele) {
		// TODO Auto-generated constructor stub
		super(ele);
//		System.out.println("\n\n\n");
//		System.out.println(ele.toXML());
	}

	public BirtTTableCell getTableCell(int index){
		Element parent = (Element) element.getParent();
		BirtTTableCell tableCell=null;
		if (parent == null)
			return null;
		parent = (Element) parent.getParent();
		BirtTable bt = new BirtTable(parent);
		int headerRowCount = bt.getHeaderRowCount();
		int columnCount = bt.getColumnCount();
		// System.out.println("33333  3"+headerRowCount+"\t"+columnCount);
		Element[][] eles = new Element[headerRowCount][columnCount];

		// System.out.println("element="+element.toXML());
		for (int i = 0; i < headerRowCount; i++) {
			Element ele = bt.getHeaderTableRow(i).getElement();
			ElementResult birtRow = new ElementResult(ele);
			ElementResult er = new ElementResult(ele);
			int count = er.toukeiElements("cell");

			for (int i1 = 0; er.next(); i1++) {
				// System.out.println(i+" =i,i1=  "+i1);
				while (eles[i][i1] != null) {
					i1++;
				}
				Element cell = er.getElementKo();
				BirtProperty bp = new BirtProperty(cell);
				// System.out.println(cell);
				int colSpan = bp.getPropertyInt("colSpan");
				int rowSpan = bp.getPropertyInt("rowSpan");
				// System.out.println(colSpan+"\t"+rowSpan);
				for (int j = 0; j < rowSpan; j++) {
					if (colSpan > 0) {
						for (int j1 = 0; j1 < colSpan; j1++) {
							// System.out.println(i+j+"       "+j1);
							eles[i + j][j1+i1] = cell;
							// System.out.println(i+j+"  "+j1);
						}
					}

				}
				if (eles[i][i1] == null) {
					eles[i][i1] = cell;
					// System.out.println(i+"  "+i1);
				}
				// System.out.println(eles[i][i1].toXML());
			}
			Element[] ele3=eles[i];
			for (Element element : ele3) {
				if (element == null)
//					System.out.println("element+" + element);
					;
			}

			// birtRow.getElementKo(index);
			if (ele.equals(element)) {
				// System.out.println("element="+element.toXML());
				// System.out.println("lel="+ele.toXML());
				// System.out.println(eles[i][index].toXML());
				if (eles[i][index] == null) {
//					 System.out.println(birtRow.getElementKo());
				}
				tableCell = new BirtTTableCell(eles[i][index]);
			}
		}

		for (Element[] ele1 : eles) {
			for (Element element : ele1) {
//				if (element == null)
//					System.out.println("element+" + element);
			}
		}
		return tableCell;
	}
	
	
//	public BirtTTableCell getTableCell(int index){
//		BirtTTableCell cell=null;
////		System.out.println(element.getChildCount());
//		int j=0;
//		for(int i=0;i<element.getChildCount();i++){
//			Element ele=(Element) element.getChild(i);
//			
//			if(ele.getLocalName().equals("cell")){
//				if(j==index){
//					cell=new BirtTTableCell(ele);
//					return cell;
//				}
//				j++;
//			}
////			System.out.println(cell);
//		}
////		return tableCell.get(index);
//		
//		return cell;
//	}
	
}


