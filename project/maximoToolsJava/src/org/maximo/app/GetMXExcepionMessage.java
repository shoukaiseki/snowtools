package org.maximo.app;

import java.rmi.RemoteException;
import java.sql.Connection;

import psdi.mbo.MboSetRemote;
import psdi.util.MXException;
import psdi.util.MXSession;

import org.maximo.app.config.MaximoConfig;

public class GetMXExcepionMessage{
	MXSession mxSession=null;
	
	
	public GetMXExcepionMessage(MXSession mxSession) {
		this.mxSession=mxSession;
	}
	
	

	public String getMessage(MXException e){
		String msg=null;
		try {
			MboSetRemote mm = mxSession.getMboSet("MAXMESSAGES");
			mm.setWhere("msgkey='"+e.getErrorKey()+"' and msggroup='"+e.getErrorGroup()+"'");
			mm.reset();
			if(mm.count()>0){
				msg=mm.getMbo(0).getString("VALUE");
			}
			mm.close();
			Object[] parameters = e.getParameters();
			if(parameters!=null){
				for (Object object : parameters) {
					System.out.println(object);
				}
			}
			for (int i = 0; parameters!=null&&i < parameters.length; i++) {
				if(parameters[i] !=null){
					msg=msg.replaceAll("\\{"+i+"\\}", parameters[i].toString());
				}else{
					msg=msg.replaceAll("\\{"+i+"\\}", "");
				}
			}
		} catch (MXException e1) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			e1.printStackTrace();
            msg = (new StringBuilder()).append(e.getErrorGroup()).append("#").append(e.getErrorKey()).toString();
		} catch (RemoteException e2) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			e2.printStackTrace();
            msg = (new StringBuilder()).append(e.getErrorGroup()).append("#").append(e.getErrorKey()).toString();
			msg+=e2.getMessage();
		}
		return msg;
	}
}
