package org.shoukaiseki.sql.oracle;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SqlFormat {
	public SqlFormat setTableRoot(String tableRoot);
	
	public void setDoutouWhere(String column,String atai);
	/**同等where追加します
	 * @param column
	 * @param atai
	 */
	public void addDoutouWhere(String column,String atai);
	public String getSql();
	public void format();
	public ResultSet executeQuery() throws SQLException ;

	
	
	
	public void setWhere(String where);

}


