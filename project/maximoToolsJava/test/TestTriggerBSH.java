import org.maximo.tools.impxml.bsh.TriggerBSH;

import org.maximo.app.MTException;
import org.maximo.app.OutMessage;
import org.maximo.tools.impxml.task.*;
import org.maximo.tools.impxml.*;
import java.sql.*;


public class TestTriggerBSH extends TriggerBSH{

	/**  如果為 false 則該行不會添加進數據庫.
	 * @return 默認為 true;
	 * @throws MTException
	 */
	public Object execute() throws MTException {
		boolean b = true;
		if (ixrow.isBeginNullsAuto(new String[] { "OPTICKETPROJECT","H"})) {
			return false;
		}
		//如果B列為空則設置值為上次添加的行B列的值
		String[] dbAttNames = new String[] { "DESCRIPTION" };
		ixrow.paste(ixrow.getMergedRegionValue(dbAttNames), dbAttNames, false);
		dbAttNames = new String[] { "C" };
		ixrow.paste(ixrow.getMergedRegionValue(dbAttNames), dbAttNames, false);
		dbAttNames = new String[] { "D" };
		ixrow.paste(ixrow.getMergedRegionValue(dbAttNames), dbAttNames, false);
		dbAttNames = new String[] { "E" };
		ixrow.paste(ixrow.getMergedRegionValue(dbAttNames), dbAttNames, false);


		QuerySet lei = ixrow.getTable().getQuerySet("opticket");
		lei.setObject(1, ixrow.getString("DESCRIPTION"));

		b=!lei.hasNext();
		if(!b){
			ixrow.setValue("OPTICKETNUM", lei.getObject("OPTICKETNUM"));
		}else{
			Long lo=(Long) ixt.getVariableValue("a");
			ixrow.setValue("OPTICKETNUM",new java.text.DecimalFormat("HXBZ0000").format(lo));
			lo++;
			ixt.setVariableValue("a",new Long(lo));

			//新操作票的操作排序置為1
			ixt.setVariableValue("l_seqnum",new Long(1));


		}
		if("操作步骤".equals(ixrow.getStringAuto("H"))){
			PreparedStatementSet listPs = ixt.getPreparedStatement("opticketline");
			if (!ixrow.isBeginNullsAuto(new String[]{"OPTICKETPROJECT"})) {
					 try {
						//該操作NUM
						Long lo=(Long) ixt.getVariableValue("a");
						listPs.setObject(1, "HX"+lo);
						lo++;
						ixt.setVariableValue("a",new Long(lo));


						listPs.setObject(2, ixrow.getStringAuto("OPTICKETNUM"));

						listPs.setObject(3,ixrow.getString("OPTICKETPROJECT"));
						Long l_seqnum=(Long) ixt.getVariableValue("l_seqnum");
						listPs.setObject(4,""+l_seqnum);
						l_seqnum++;
						ixt.setVariableValue("l_seqnum",new Long(l_seqnum));
						listPs.addBatchAndExecuteBatch();
					 } catch (SQLException e) {
						 // TODO Auto-generated catch block
						 String message=ixrow.getStringAuto("JPNUM")+"\t"+ixrow.getStringAuto("JPNUM")+"\t"+ixrow.getStringAuto("DESCRIPTION")+"\t"+ixrow.getStringAuto("F")+"\t"+ixrow.getStringAuto("H");
						 om.error(message,e );
						 return e;
					 }
			}
		}
		//om.info(ixrow.getStringAuto("OPTICKETNUM"));
		return b;
	}
}
