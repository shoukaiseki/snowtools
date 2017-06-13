package org.maximo.app;

import java.rmi.RemoteException;
import java.sql.SQLException;

import psdi.util.MXException;
import psdi.util.MXSession;

public class MTException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String msg=null;
	private Throwable detail=null;


	public MTException() {
		// TODO Auto-generated constructor stub
	}

	
	public MTException(String msg){
		super(msg);
		this.msg=msg;
	}
	
	public MTException(Throwable t){
		super(t);
		detail=t;
		if(t instanceof MXException){
			GetMXExcepionMessage gm=new GetMXExcepionMessage(getThisMXSession());
			msg=gm.getMessage((MXException)t);
		}else if(t instanceof RemoteException){
			msg=t.getMessage();
		}else if(t instanceof SQLException){
			msg=t.getMessage();
		}else if(t instanceof NumberFormatException){
			msg="String 轉換為 number 錯誤:"+t.getMessage();
		}else{ 
			msg=t.getMessage();
		}
	}
	
    public MTException(String message, Throwable t) {
		// TODO Auto-generated constructor stub
    	this(t);
    	msg=message;
	}


	public boolean hasDetail()
    {
        return detail != null;
    }
	
    public Throwable getDetail()
    {
        return detail;
    }
    
    public String getMessage(){
    	return msg;
    }


	private MXSession getThisMXSession() {
		// TODO Auto-generated method stub
		MXSession session = MXSession.getSession();
		return session;
	}
}
