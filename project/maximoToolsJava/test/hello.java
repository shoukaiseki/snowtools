import java.math.BigDecimal;

import org.apache.log4j.Logger;


public class hello {
	
	static Logger log=Logger.getLogger("hello");
	
	public static void main(String[] args)  {
		long l=12L;
		long b=l++;
		l=12L;
		long c=++l;
		System.out.println("l="+l+",b="+b+",c="+c);
		
		BigDecimal[] bds=new BigDecimal[5];
		bds[0]=new BigDecimal("112");
		System.out.println(bds[0]);
//		asus();
	}
	
	private static void asus() {
		// TODO Auto-generated method stub
		StringBuffer sb=new StringBuffer();
		long a=System.currentTimeMillis();
//		String str="";
		for (int i = 0; i < 1000000; i++) {
			sb.append("hello world");
//			str+="hello world";
//			if(i%10000==0){
//				System.out.println(i);
//			}
			
		}
//		log.error(sb.toString());
		
		System.out.println(System.currentTimeMillis()-a);
//		System.out.println(sb.toString());

	}
}