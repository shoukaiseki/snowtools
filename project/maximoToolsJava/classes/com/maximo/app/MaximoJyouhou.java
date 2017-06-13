package com.maximo.app;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.shoukaiseki.sql.oracle.OracleSqlDetabese;

public class MaximoJyouhou {
	private Connection con;
	public MaximoJyouhou(OracleSqlDetabese osd) {
		// TODO Auto-generated constructor stub
		this.con=osd.getConnection();
	}
	public MaximoJyouhou(Connection con) {
		// TODO Auto-generated constructor stub
		this.con=con;
	}
	
	public MaxSequenceJyouhou getUniqueColumnName(String objectName) throws SQLException{
//		MaxSequence
		OracleSqlDetabese osd=new OracleSqlDetabese(con);
		osd.setTableRoot("MAXSEQUENCE");
		osd.setWhere("TBNAME='"+objectName+"'");
		osd.format();
		ResultSet rs = osd.executeQuery();
		MaxSequenceJyouhou msj=new MaxSequenceJyouhou();
		if(rs.next()){
			msj.setName(rs.getString("NAME"));
			msj.setMaxReserved( rs.getString("MAXRESERVED"));
			msj.setMaxValue( rs.getString("MAXVALUE"));
			msj.setRange( rs.getString("RANGE"));
			msj.setSequenceName (rs.getString("SEQUENCENAME"));
		}
		osd.close();
		return msj;
	}

}
