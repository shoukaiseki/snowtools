package org.shoukaiseki.lottery;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** org.shoukaiseki.lottery.LotteryBonusCalculation
 * 彩票奖金计算,提供一个数组,按照组合方式单数组合或者2个数组合或者8个数组合等等,得到N种组合方式
 * @author 蒋カイセキ    Japan-Tokyo  2014年9月3日
 * ブログ http://shoukaiseki.blog.163.com/
 * E-メール jiang28555@Gmail.com
 */
public class LotteryBonusCalculation {
	HashSet<Set<BigDecimal>> map=null;
	
	public LotteryBonusCalculation(){
		map=new HashSet<Set<BigDecimal>>();
	}
	
	/**生成组合方式,用 getLotteryMap 获取所有组合
	 * @param bds 数组
	 * @param needed_balls 几个数据组合
	 */
	public void lottery(BigDecimal bds[],int needed_balls) {
		// TODO Auto-generated method stub
		lottery(bds, 0, bds.length-1, needed_balls, new HashSet<BigDecimal>());

	}
	
	
	/** 生成组合方式,用 getLotteryMap 获取所有组合
	 * @param bds 数组
	 * @param start_index 检索开始位置 ,用于内循环使用
	 * @param end_index   检索结束为止,用于内循环使用
	 * @param needed_balls 几个数字组合
	 * @param already_chosen 用于内循环使用
	 */
	public void lottery(BigDecimal bds[], int start_index, int end_index,
			int needed_balls, Set<BigDecimal> already_chosen) {
		if (needed_balls == 0) {
			StringBuffer sb=new StringBuffer();
			BigDecimal bdhe=new BigDecimal(0D);
			Set<BigDecimal> set=new HashSet<BigDecimal>();
			for (BigDecimal bigDecimal : already_chosen) {
				if(bigDecimal!=null){
					set.add(bigDecimal);
					bdhe=bdhe.add(bigDecimal);
					sb.append(bigDecimal).append("+");
				}
			}
			sb.append("=").append(bdhe.toString());
//			System.out.println(sb.toString());
			map.add(set);
			return;
		}
		for (int i = start_index; i <= end_index - needed_balls + 1; i++) {
			already_chosen.add(bds[i]);
			lottery(bds, i + 1, end_index, needed_balls - 1, already_chosen);
			already_chosen.remove(bds[i]);
		}
	}
	
	/**
	 * 清除现有组合集
	 */
	public void clearLotteryMap(){
		map.clear();
	}
	
	/** 获取组合集
	 * @return
	 */
	public Set<Set<BigDecimal>> getLotteryMap(){
		return map;
	}
	
	
	public static void main(String[] args) {
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
