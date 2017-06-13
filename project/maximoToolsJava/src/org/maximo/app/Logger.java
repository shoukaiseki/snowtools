package org.maximo.app;

public interface Logger {
	public void logger(String log);
	public void loggerln(String log);

	public void cleanLogs();
	
	public String getSqllogs();
	public void sqllogger(String log);
	public void sqlloggerln(String log);
	
	public void cleanSqllogs();
	public String getLogs();
	public void setAutoPrintSqllogs(boolean auto);
	public void setAutoPrintLogs(boolean auto);
	public boolean isAutoPrintSqllogs();
	public boolean isAutoPrintLogs();

}
