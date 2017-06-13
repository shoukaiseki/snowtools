package com.shoukaiseki.birt;



import nu.xom.Element;
import nu.xom.Node;

import com.shoukaiseki.birt.ttable.BirtTTableCell;
import com.shoukaiseki.birt.ttable.BirtTTableColumn;
import com.shoukaiseki.birt.ttable.BirtTTableRow;
import com.shoukaiseki.file.xom.ElementResult;

public   class BirtTable extends BirtProperty implements BirtHyou{
	
//	protected LinkedList<BirtTTableRow> headerRows = new LinkedList<BirtTTableRow>(); 
//	protected LinkedList<BirtTTableColumn> tableColumns = new LinkedList<BirtTTableColumn>(); 
//	protected Element colSpanElement=null;
//	protected Element rowSpanElement=null;
	
	Element[][] eles=new Element[1][1];
	Element header=null;
	
	public  BirtTable(int columns,int headers,String dataSet) {
		// TODO Auto-generated constructor stub
		super("table");
		addProperty("dataSet", dataSet);
		addColumn(columns);
		addHeaderRow(headers);
		
	}
	
	public BirtTable(Element element){
		super(element);
		int rowKaunnto=0;
		ElementResult er=new ElementResult(element);
		er.toukeiElements("header");
		er.next();
		header=er.getElementKo();
//		Elements elements = element.getChildElements();
//		for (int i = 0; i < element.getChildCount(); i++) {
//			System.out.println(i);
//			Element ele = elements.get(i);
////			Element ele=(Element) element.getChild(i);
//			if(ele.getLocalName().equals("header")){
//				header=ele;
//				break;
//			}
//
//		}
	}
	
	public BirtCell getBirtSeru(int row,int column){
		BirtTTableRow tableRow = getHeaderTableRow(row);
		BirtCell tableCell = tableRow.getTableCell(column);
		return tableCell;
	}
	
	/**セルの結合しでいますか
	 * @param columnFirst
	 * @param columnLast
	 * @param rowFirst
	 * @param rowLast
	 */
	public void setNumMergedRegions(int columnFirst,int columnLast,int rowFirst,int rowLast){
//		System.out.println(row.getElement().toXML());
		logger.debug(columnFirst+","+columnLast+","+rowFirst+","+rowLast);
		for (int i = rowLast; i >= rowFirst; i--) {
			BirtTTableRow row = getHeaderTableRow(i);
			for (int j = columnLast; j >= columnFirst; j--) {
				if(j!=columnFirst||i!=rowFirst){
//					System.out.println("del  "+i+"  "+j);
					BirtTTableCell cell = row.getTableCell(j);
//					System.out.println(cell.getElement().toXML());
					row.removeElement(cell.getElement());
				}
			}
		}
		BirtTTableRow row = getHeaderTableRow(rowFirst);
		BirtTTableCell cell = row.getTableCell(columnFirst);
//		System.out.println(cell.getElement());
		cell.addProperty("colSpan", columnLast-columnFirst+1);
		cell.addProperty("rowSpan", rowLast-rowFirst+1);
	}
	
	public void removeColumn(int count){
		
	}
	
	/**添加N列
	 * @param count
	 */
	public void addColumn(int count){
		for (int i = 0; i < count; i++) {
			addColumnLast();
		}
	}
	
	/**添加一列
	 * @return
	 */
	public BirtTTableColumn addColumnLast(){
		BirtTTableColumn column = null;
		boolean b=true;
		for(int j=0;j<element.getChildCount();j++){
			Element child = (Element) element.getChild(j);
//			System.out.println("addColumnLast"+child.getLocalName());
			//插入在header  detail footer之前
			if(child.getLocalName().matches("header|detail|footer")){
				if(j!=0){
					j--;
				}
				column = new BirtTTableColumn();
				Element ele=column.getElement();
				element.insertChild(ele, j);
				b=false;
				break;
			}
		}
		//无子节点或者无header|detail|footer
		if(b){
			column = new BirtTTableColumn();
			Element ele=column.getElement();
			element.appendChild(ele);
		}
		
//		tableColumns.add(column);
		
		
		return column;
	}
	
	/**表头添加幾行
	 * @param count
	 */
	public void addHeaderRow(int count){
		for (int i = 0; i < count; i++) {
			addHeaderRowLast();
		}
	}
	
	/**表头添加一行
	 * @return
	 */
	public BirtTTableRow addHeaderRowLast(){
		BirtTTableRow row = sinnkiHeaderRow();
//		headerRows.addLast(row);
		header.appendChild(row.getElement());
		return row;
	}
	
//	public BirtTTableRow addTableRowLast(){
//		BirtTTableRow row = sinnkiHeaderRow();
//		headerRows.addLast(row);
//		header.appendChild(row.getElement());
//		return row;
//	}
	
	public BirtTTableRow sinnkiHeaderRow(){
		if(header==null){
			header=new Element("header");
			element.appendChild(header);
		}
		BirtTTableRow row=new BirtTTableRow(getColumnCount());
		return row;
	}
	
	public BirtTTableRow insertHeaderRow(int index){
		BirtTTableRow row=sinnkiHeaderRow();
		header.appendChild(row.getElement());
//		headerRows.add(index,row);
		return row;
	}
	
	public BirtTTableColumn addTableColumnLast(){
		BirtTTableColumn birtTableColumn =addColumnLast();
//		tableColumns.addLast(birtTableColumn);
		
		return birtTableColumn;
	}
     
     
	public BirtTTableRow getHeaderTableRow(int index){
//		BirtTTableRow row=null;
		int j=0;
		ElementResult er=new ElementResult(header);
		er.toukeiElements("row");
		Element elementKo = er.getElementKo(index);
		if(elementKo==null)
			return null;
		BirtTTableRow row=new BirtTTableRow(elementKo);
//		if(header!=null){
//			for(int i=0;i<header.getChildCount();i++){
//				Element ele=(Element) header.getChild(i);
//				if(ele.getLocalName().equals("row")){
//					if(j==index){
//						row=new BirtTTableRow(ele);
//						return row;
//					}
//					j++;
//				}
//			}
//		}
//		return headerRows.get(index);
		return row;
	}
     
	
	public int getColumnCount(){
		int count=0;
//		System.out.println("coCount="+element.getChildCount());
		ElementResult er=new ElementResult(element);
		er.toukeiElements("column");
		count=er.getCount();
//		for(int j=0;j<element.getChildCount();j++){ for(int j=0;j<element.getChildCount();j++){
//			Element child = (Element) element.getChild(j);
//			System.out.println(child.getLocalName());
//			//插入在header  detail footer之前
//			if(child.getLocalName().equals("column")){
//				count++;
//			}
//		}
		return count;
	}

	public int getHeaderRowCount() {
		// TODO Auto-generated method stub
		int count=0;
		ElementResult er=new ElementResult(header);
		er.toukeiElements("row");
		count=er.getCount();
//		System.out.println("coCount="+header.getChildCount());
//		for(int j=0;j<header.getChildCount();j++){
//			Element child = (Element) header.getChild(j);
//			System.out.println(child.getLocalName());
//			//插入在header  detail footer之前
//			if(child.getLocalName().equals("row")){
//				count++;
//			}
//		}
		return count;
	}
	
	
//	public Element getElement(){
//		for (BirtTTableColumn column : tableColumns) {
//			element.appendChild(column.getElement());
//		}
//		
//		if(headerRows.size()>0){
////			Element header=new Element("header");
//			element.appendChild(header);
//			for (BirtTTableRow row : headerRows) {
//				header.appendChild(row.getElement());
//			}
//		}
//		
//		return element;
//	}
}


