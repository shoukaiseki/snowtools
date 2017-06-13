package org.tomaximo.other;

import java.io.PrintStream;

import org.shoukaiseki.maximo.anngoukafukugou.MXCipherX;
import org.shoukaiseki.maximo.anngoukafukugou.PropertiesUtil;
import org.shoukaiseki.syso.TerminalCursorControl;

public class MaximoAF
{
	static PropertiesUtil pu = new PropertiesUtil();
	static String password = "170DB79B4BEE2D9670C8C117EE7862D7";
	static String user = "123456";
	static TerminalCursorControl tcc=new TerminalCursorControl();
	static String osName=System.getProperty("os.name");

	public static void main(String[] paramArrayOfString)
	{
		if (paramArrayOfString.length == 0) {
			help();
		}
		else if (paramArrayOfString.length != 2) {
			help();
		} else {
			MXCipherX localMXCipherX;
			if (paramArrayOfString[0].equals("-c")) {
				user = paramArrayOfString[1];

				localMXCipherX = new MXCipherX(true, pu); 
				password = localMXCipherX.encData(user);
				System.out.print("password=");
				println(password);
			} else if (paramArrayOfString[0].equals("-x")) {
				password = paramArrayOfString[1];

				localMXCipherX = new MXCipherX(false, pu);
				user = localMXCipherX.decData(password);
				System.out.print("user=");
				println(user);
			}
			else {
				test();
			}
		}
	}

	public static void println(String paramString) {
		if(osName.equalsIgnoreCase("linux")){
			System.out.println("\033[1;30;31m" + paramString + "\033[0m");
		}else{
			System.out.println(paramString);
		}
		
	}

	public static void print(String paramString) {
		if(osName.equalsIgnoreCase("linux")){
			System.out.print("\033[1;30;31m" + paramString + "\033[0m");
		}else{
			System.out.print(paramString);
		}
	}

	public static void help() {
		System.out.println("help");
		System.out.println("-c \t\t加密");
		System.out.println("-x\t\t解密");
		println("例如\tjava maximo -c 123456");
		println("\t\tjava maximo -x 170DB79B4BEE2D9670C8C117EE7862D7");
	}

	public static void test() {
		System.out.print("user=");
		println(user);
		System.out.print("password=");
		println(password);

		MXCipherX localMXCipherX1 = new MXCipherX(true, pu);
		String str = localMXCipherX1.encData(user);
		System.out.println(str);

		MXCipherX localMXCipherX2 = new MXCipherX(false, pu);

		System.out.println(localMXCipherX2.decData(password));
	}
}



/**

---conf.properties---{{{1
//----------------------------------------------------------------------------
// Security properties
//----------------------------------------------------------------------------
// mxe.security.provider=
// mxe.security.crypto.mode=
// mxe.security.crypto.padding=
// mxe.security.crypto.key=
// mxe.security.crypto.spec=
//用于maximo7.5的解密，屏蔽参数则用于maximo6.2
 mxe.security.cryptox.mode=CBC
 mxe.security.cryptox.padding=PKCS5Padding
 mxe.security.cryptox.key=Sa#qk5usfmMI-@2dbZP9`jL3
 mxe.security.cryptox.spec=beLd7$lB

//  String algorithm = "DESede";
//  String mode = "CBC";
//  String padding = "PKCS5Padding";
//  String key = "Sa#qk5usfmMI-@2dbZP9`jL3";
//  String spec = "beLd7$lB";
//  String modulus = "";


---mxsecurity.bat---{{{1
@CLS
@echo ***************************************************
@echo **           Maximo    解密程序                  **
@echo **                                               **
@echo **开发公司：北京数途科技有限公司                 **
@echo **开发者：？？？                                 **
@echo **地址：华润电力湖南分公司涟源电厂CRPHN_EAM    　**
@echo **电话：                 　                      **
@echo ****************************************************
@echo.
@echo off 
@java -jar MXSecurity.jar %1=%2 %3=%4 %5=%6 %7=%8
@pause




---mxsec.bat---{{{1
@ECHO maximo6.2的解密 @mxsecurity.bat enc=123456 dec=D58EBBEB449C78EE800167B02D99404A2FF4AAA8DD01CAE0

@ECHO maimo7.5的解密
@mxsecurity.bat enc=111111 dec=44839CA3293D8FFC1F248CD3E02EC12A
@echo.

@pause



---readme.txt---{{{1


@date 2012-06-11
@author liuzhu ganzenghui

1.编辑mxsec.bat修改加密解密参数
执行mxsec.bat即可

2.config.properties 为配置加密、解密的参数
maxio6.2中这些参数不用配置，maximo7中需要这些参数


=========================================================
调用示例：

package org.jxkj.util.test;
import com.jxkj.util.*;
public class Test {
    public static void main(String[] args){
        PropertiesUtil pu = new PropertiesUtil();
        //测试加密
        MXCipherX mx = new MXCipherX(true,pu);
        String spwd = mx.encData("hpspsadmin");
        System.out.println(spwd);

        //解密测试
        MXCipherX dmx = new MXCipherX(false,pu);
        System.out.println(dmx.decData(spwd));
    }
}

**/