package org.shoukaiseki.tuuyou.logger.config;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
//<listener>
//<listener-class>test.startup.MyServletContextListener</listener-class>
//</listener>
/**
 * org.shoukaiseki.tuuyou.logger.config.SetConfigPathnWebServletContextListener
 * @author 蒋カイセキ    Japan-Tokyo 2012-7-26 下午10:30:25
 * @ブログ http://shoukaiseki.blog.163.com/
 * @E-メール jiang28555@Gmail.com
 */
public class SetConfigPathnWebServletContextListener implements ServletContextListener 
{

	public SetConfigPathnWebServletContextListener() {
		// TODO Auto-generated constructor stub
		System.out.println(this.getClass()+" 實例化");
	}

	public void contextDestroyed(ServletContextEvent arg0) 
	{

	}

	public void contextInitialized(ServletContextEvent arg0) 
	{
		System.out.println(this.getClass()+".contextInitialized");
		new ConfigPath(true);
	}
}