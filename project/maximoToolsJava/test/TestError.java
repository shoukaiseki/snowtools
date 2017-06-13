import bsh.EvalError;
import bsh.Interpreter;

import org.maximo.app.MTException;


public class TestError {
	
	public static void main(String[] args) throws MTException {
//		test1();
//		test2();
//		test3();
		try {
			test4();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("eee");
		}
		
		int age=27;
		String you= (age%3==0?age%(age/3-1)-1:age%3==1?age%3+1:age%3)+""+(char)66;
		System.out.println("董昱材的別名為\023"+you);

	}
	
	public static void test4() throws MTException {
		Interpreter i = new Interpreter();  // Construct an interpreter
		String bsh="int as=12;return  new MTException(\"測試報錯\");;";
					try {
						i.eval("import org.maximo.app.MTException;");
						Object eval = i.eval(bsh);
						if(eval!=null){
							if(eval instanceof MTException){
								throw (MTException)eval;
							}
						}
					} catch (EvalError e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	}
	
	public static void test3() throws MTException {
		try {
			Integer.parseInt("2a");
		} catch (NumberFormatException e) {
			// TODO: handle exception
			MTException mtException = new MTException(e);
//			System.out.println(mtException.getMessage());
//			mtException.printStackTrace();
			throw mtException;
		}
	}
	
	public static void test2() throws MTException{
		throw new MTException(("測試報錯"));
	}
	
	public static void test1() throws MTException{
		throw new MTException(new Exception("測試報錯"));
	}

}
