package com.maximo.app;

import java.rmi.RemoteException;
import java.sql.SQLException;

import psdi.util.MXException;

public interface CopyMaximo {
	
	public void copyJdbcToMXServer(String fromProcessName,int fromProcessRev,String toProcessName) throws MXException, RemoteException, SQLException;

}
