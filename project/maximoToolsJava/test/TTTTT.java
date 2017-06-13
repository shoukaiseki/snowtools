import java.math.BigDecimal;
import java.util.Set;

import com.shoukaiseki.lottery.LotteryBonusCalculation;


public class TTTTT extends LotteryBonusCalculation{
	
	
	public StringBuffer calculate(String s){
		StringBuffer sb=new StringBuffer("開獎SP值=");
		if(s==null){
			sb=new StringBuffer("無效的輸入");
		}
		String par=s.replace("\r", "\r\n");
		String[] split = par.split("\n");
		StringBuffer sbZh=null;
		if(split==null||split.length<2||split[0]==null){
			sb=new StringBuffer("無效的輸入");
		}

		BigDecimal[] bds=new BigDecimal[split.length-1];
		for (int i = 1; i < split.length; i++) {
			par=split[i];
			if(!par.isEmpty()){
				bds[i-1]=new BigDecimal(par);
				sb.append(bds[i-1]).append(",");
			}
		}

		sb.append("\n");

		par=split[0].replace("，", ",");;
		BigDecimal bs=null;
		int count=0;
		for (String widget : par.split(",")) {
			clearLotteryMap();
			lottery(bds, Integer.parseInt(widget));
			Set<Set<BigDecimal>> lotteryMap=getLotteryMap();

			BigDecimal bd=null;
			StringBuffer sbtemp=new StringBuffer("\n");
			for (Set<BigDecimal> set : lotteryMap) {
				BigDecimal bd2=null;
				for (BigDecimal bigDecimal : set) {
					if(bd2==null){
						bd2=new BigDecimal(String.valueOf(bigDecimal));
						sbtemp.append(bigDecimal);
					}else{
						bd2=bd2.multiply(bigDecimal);
						sbtemp.append("*").append(bigDecimal);
					}
				}
				if(bd==null){
					bd=bd2;
				}else{
					bd=bd.add(bd2);
				}
				sbtemp.append("=").append(bd2).append("\n");
			}
			sb.append(widget).append("串1").append("=").append(lotteryMap.size()).append("注,中獎金額").append(bd).append("；");

			if(bs==null){
				bs=new BigDecimal(String.valueOf(bd)); 
			}else{
				bs=bs.add(bd);
			}
			if(sbZh==null){
				sbZh=new StringBuffer("中獎信息為：\n"); 
				sbZh.append(widget).append("串1").append("=").append(lotteryMap.size()).append("注")
				.append(",中獎金額 ").append(bd).append("元");
				;
			}else{
				sbZh.append("").append(widget).append("串1").append("=").append(lotteryMap.size()).append("注")
				.append(",中獎金額 ").append(bd).append("元");
			}
			sbZh.append(sbtemp);
			count+=lotteryMap.size();
		}
		BigDecimal buhanshuijine = bs.multiply(new BigDecimal("0.65"));
		sb.append("\n合計總金額=").append(bs).append("元,去65%稅后金額=").append(buhanshuijine).append("元")
		.append("，稅款=").append(bs.subtract(buhanshuijine)).append("元");
		sb.append("\n具體中獎詳細如下:\n");
		sb.append(sbZh);
		
		return new StringBuffer("一共中").append(count).append("注\n").append(sb);
	}

	public static void main(String[] args) {
		TTTTT t=new TTTTT();
		String s=null;
		s="2,3,4\n2\n3\n4\n5\n6";
		StringBuffer sb = t.calculate(s);
		System.out.println(sb.toString());
		
	}

	public static void main1(String[] args) {
		LotteryBonusCalculation lbc=new LotteryBonusCalculation();
		BigDecimal bd1=new BigDecimal("2");
		BigDecimal bd2=new BigDecimal("3");
		BigDecimal bd3=new BigDecimal("4");
		BigDecimal bd4=new BigDecimal("5");
		BigDecimal[] bds=new BigDecimal[]{bd1,bd2,bd3,bd4};

		lbc.lottery(bds, 4);
		lbc.lottery(bds, 3);

		Set<Set<BigDecimal>> lotteryMap = lbc.getLotteryMap();
		for (Set<BigDecimal> set : lotteryMap) {
			BigDecimal bd=new BigDecimal("1");
			StringBuffer sb=null;
			for (BigDecimal bigDecimal : set) {
				bd=bd.multiply(bigDecimal);
				if(sb==null){
					sb=new StringBuffer().append(bigDecimal);
				}else{
					sb.append("*").append(bigDecimal);
				}
			}
			sb.append("=").append(bd);
			System.out.println(sb.toString());
		}


	}


}
