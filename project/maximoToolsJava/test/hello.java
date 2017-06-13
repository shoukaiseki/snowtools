import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.maximo.app.MTException;
import com.maximo.app.OutMessage;
import com.maximo.tools.impxml.task.*;


public class hello {
	public static void main(String[] args) throws MTException {
		
		BigDecimal[] bds=new BigDecimal[5];
		bds[0]=new BigDecimal("112");
		System.out.println(bds[0]);
	}
}