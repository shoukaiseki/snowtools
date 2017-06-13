package com.shoukaiseki.file.excel;

public class ExcelStaticHouhou {
	
	
	

	/**将excel的AA1转换成 int{列,行},0开始
	 * @param cell
	 * @return
	 */
	public static int[] rowCoumnIntArray(String cell){
		int[] i={-1,-1};
		String str="";
		String in="";
		byte[] b=cell.getBytes();
		boolean bo=false;
		for(byte c:b){
			if(c>64&&c<91){
				if(bo){
					//A-Z只能在数字前面
					return i;
				}
				str+=(char)c;
			}else{
				bo=true;
				in+=(char)c;
			}
		}
		try {
			i[0]=columnInt(str);
			i[1]=Integer.parseInt(in);
		} catch (Exception e) {
			// TODO: handle exception
			i[0]=-1;
			System.out.println("cell="+cell+"\tcolumn="+i[0]+"\trow="+i[1]);
		}
		return i;
	}
	
	/**将excel A-Z AA-AZ,BA-BZ格式转换为列号,0开始,无效返回-1
	 * @param object
	 * @return
	 */
	public static int columnInt(String object) {
		// TODO Auto-generated method stub
		for (int i = 0; i < 1000; i++) {
			String string = columnEgo(i);
//			System.out.println(string+"====="+object+"    "+string.equalsIgnoreCase(object));
//			StringToASCII.toASCII(object);
			if (string.equalsIgnoreCase(object)) {
				return i;
			}
		}
		
		return -1;
	}
	/**将列号変換为excle A-Z AA-AZ,BA-BZ格式
     * @param col 要转换的数字
     * @return
     */
    public static String columnEgo(int column){
    	int col=column+1;
    	if(col<1)return "復帰(ふっき)[返回]null";
    	int iti=col/26;
    	if(col%26==0){
    		iti--;
    	}
    	char saishou=(char) (iti+65-1);
    	int ninn=col%26;
    	ninn=ninn==0?26:ninn;
    	char nii=(char)(ninn+64);
    	if(col<27){
    		return ""+nii;
    	}else{
    		return ""+saishou+nii;
    	}
    }
    
    public static void main(String[] args) {
    	String s="AA2";
    	System.out.println(rowCoumnIntArray(s)[0]);
    	System.out.println(rowCoumnIntArray(s)[1]);
	}
    
}


