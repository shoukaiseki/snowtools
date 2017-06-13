package org.shoukaiseki.tuuyou.logger.config;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//<!--tomcat 服务一启动就会加载-->
//<servlet>
//	  <servlet-name>SetConfigPathInWebService</servlet-name>
//	  <servlet-class>org.shoukaiseki.tuuyou.logger.config.SetConfigPathInWebService</servlet-class>
//	  <load-on-startup>0</load-on-startup>
//</servlet>
//


/**
 * 設置 ConfigPath 運行與 Web Service
 * 繼承 HttpServletResponse 纔會才 tomcat 加載的時候 執行 init
 * org.shoukaiseki.tuuyou.logger.config.SetConfigPathInWebService
 * @author 蒋カイセキ    Japan-Tokyo 2012-7-24 下午11:21:32
 * @ブログ http://shoukaiseki.blog.163.com/
 * @E-メール jiang28555@Gmail.com
 */
public class SetConfigPathInWebService extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SetConfigPathInWebService() {
		// TODO Auto-generated constructor stub
		super();
		System.out.println(this.getClass()+" 實例化");
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
		System.out.println(this.getClass()+".destroy");
	}
	
	
	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	public void doGet(HttpServletRequest request, HttpServletResponse response){
	}
	
	

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response){
		System.out.println(this.getClass()+".doPost");
	}

	/**
	 * Initialization of the servlet. <br>
	 */
	public void init() {
		new ConfigPath(true);
		System.out.println(this.getClass()+".init");
		// Put your code here
	}

}
