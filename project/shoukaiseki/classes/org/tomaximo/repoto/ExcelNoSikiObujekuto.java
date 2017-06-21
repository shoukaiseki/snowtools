package org.tomaximo.repoto;

public class ExcelNoSikiObujekuto {
	public String seru = null;
	public String taipu = null;
	public String atai = null;

	public ExcelNoSikiObujekuto(String seru, String taipu, String atai) {
		this.seru=seru;
		this.taipu=taipu;
		this.atai=atai;

	}
	
	public String toString(){
		
		return "{"+seru+","+taipu+","+atai+"}";
		
	}
}


