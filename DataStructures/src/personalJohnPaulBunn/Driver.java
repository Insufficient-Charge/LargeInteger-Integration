package personalJohnPaulBunn;
import personalJohnPaulBunn.LargeInteger;

public class Driver {

	public static void main(String[] args) {
		
		LargeInteger test1 = new LargeInteger("92000000000000000");
		LargeInteger test2 = new LargeInteger("123123456787654334");
		
		test1 = test1.sum(test2);
		
		System.out.println(test1);
		
	}

}
