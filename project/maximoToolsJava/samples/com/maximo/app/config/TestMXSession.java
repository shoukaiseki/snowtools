package com.maximo.app.config;

import java.rmi.RemoteException;
import java.util.Enumeration;

import psdi.mbo.KeyValue;
import psdi.mbo.MboRemote;
import psdi.mbo.MboSetRemote;
import psdi.mbo.MboValueInfo;
import psdi.util.MXException;
import psdi.util.MXSession;

import com.maximo.app.MTException;
import com.maximo.app.MessageOnTerminal;
import com.maximo.app.OutMessage;
import com.maximo.app.resources.MXServerConfig;

/**經過測試,可以建立鏈接到不同服務器的 MXSession
 *com.maximo.app.config.TestMXSession
 * @author 蒋カイセキ    Japan-Tokyo 2013-7-3
 * <P>ブログ http://shoukaiseki.blog.163.com/
 * <P>E-メール jiang28555@Gmail.com
 */
public class TestMXSession {
	OutMessage om=null;
	private MXServerConfig mxsc=null;
	MXSession mxa=null;
	MXSession mxb=null;
	public TestMXSession() {
		// TODO Auto-generated constructor stub
		om=new MessageOnTerminal();
		mxsc=new MXServerConfig();
		mxsc.setServer("172.22.48.70:13481/MXServer");
		mxsc.setServer("172.22.48.70:13473/MXServer");
		mxsc.setServer("172.22.48.100:13400/MXServer");
		mxsc.setServer("172.22.48.101:13471/MXServer");
		mxsc.setUsername("maxadmin");
		mxsc.setPassword("maxadmin");
		
		mxa = MXSession.getSession();
		System.out.println(mxsc.getServer());
		mxa.setHost(mxsc.getServer());
		mxa.setUserName(mxsc.getUsername());
		mxa.setPassword(mxsc.getPassword());
		
		mxb=MXSession.getNewSession();
		mxb.setHost("172.22.48.100:13400/MXServer");
		mxb.setUserName("maxadmin");
		mxb.setPassword("maxadmin");
		try {
			mxa.connect();
		} catch (RemoteException | MXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			mxb.connect();
		} catch (RemoteException | MXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		om.info("--------------------101--------------------");
		readMboSet(mxa);
		om.info("--------------------100--------------------");
		readMboSet(mxb);
		om.info("--------------------101--------------------");
		readMboSet(mxa);
		om.info("--------------------100--------------------");
		readMboSet(mxb);
		om.info("--------------------101--------------------");
		readMboSet(mxa);
		om.info("--------------------100--------------------");
		readMboSet(mxb);
		try {
			mxa.disconnect();
		} catch (RemoteException | MXException e) {
			// TODO Auto-generated catch block
			new MTException(e).printStackTrace();
		}
		try {
			mxb.disconnect();
		} catch (RemoteException | MXException e) {
			// TODO Auto-generated catch block
			new MTException(e).printStackTrace();
		}
	}
	
	public void readMboSet(MXSession mxs) {
		try {
			MboSetRemote mboSet = mxs.getMboSet("HAZARD");
			mboSet.reset();
			
			for (int i = 0; i < mboSet.count()&&i<2; i++) {
				MboRemote mbo = mboSet.getMbo(i);
				Enumeration enu = mboSet.getMboSetInfo().getMboValuesInfo();
				while(enu.hasMoreElements()){
					MboValueInfo mv = (MboValueInfo) enu.nextElement();
					String key=mv.getAttributeName();
					om.info("key="+key);
					if(!mbo.isNull(key)){
						om.info(key+":"+mbo.getString(key));
					}
				}
			}
		} catch (MXException | RemoteException e) {
			// TODO Auto-generated catch block
			new MTException(e).printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new TestMXSession();
	}

}
