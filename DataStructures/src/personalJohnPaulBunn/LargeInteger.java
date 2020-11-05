package personalJohnPaulBunn;
import ch06.lists.*;

/**
 * An Implementation of LargeInteger that can add and subtract from other LargeIntegers.
 *   
 * @author John Paul Bunn
 * March 26 2020
 *
 */

public class LargeInteger {

	//Fields
	private ABList<Character> num = new ABList<Character>();
	private int sign = 0;
	
	//Constructor
	public LargeInteger(String s) {
		int startIndex = 0;
		
		//If there is a minus sign at the front of the number, change the startIndex to 1 and change sign to -1
		if (s.charAt(0) == '-') {
			startIndex = 1;
			sign = -1;
		} else if (s.charAt(s.length()-1) == '0' && s.length() == 1) {
			sign = 0;
		} else {
			sign = 1;
		}
		
		//If the char at the startIndex is a 0, and it's not JUST a zero, move up the startIndex (a number can't start with the digit 0)
		while ((s.charAt(startIndex) == '0') && ((s.length() - startIndex) != 1)) {
			startIndex++;
		}
		
		//Add the chars from the string starting at the startIndex to the ABList
		for (int i = startIndex; i < s.length(); i++) {
			num.add(s.charAt(i));
		}
		
	}
	
	//Getters
	public ABList<Character> getNum() {
		return this.num;
	}
	
	public int getSign() {
		return this.sign;
	}
	
	//ToString
	public String toString() {	
		String returnString = "";
		
		if (this.getSign() == -1) {
			returnString += "-";			
		}
		
		for (int i = 0; i < this.getNum().size(); i++) {
			returnString += this.getNum().get(i);			
		}
		
		return returnString;
	}
	
	//CLASS SPECIFIC
	public int compareTo(LargeInteger y) {
		int compare = 0;
		// 1 means this is bigger
		// -1 means y is bigger
		// 0 means they are both equal
		
		//Preset to avoid errors
		LargeInteger x = new LargeInteger(this.toString());
		
		//Deep copy
		LargeInteger normalX = new LargeInteger(this.toString());
		LargeInteger normalY = new LargeInteger(y.toString());

		if ((normalX.getNum().size() > normalY.getNum().size())) {
			x = this.padWithZeroes(this);
			y = this.padWithZeroes(y);			
		} else  if ((normalX.getNum().size() < normalY.getNum().size())) {
			x = this.padWithZeroes(y);
			y = y.padWithZeroes(y);	
		} else {
			x = this.padWithZeroes(this);
			y = this.padWithZeroes(y);	
		}

		for (int i = y.getNum().size()-1; i != -1; i--) {
			if (x.getNum().get(i) > y.getNum().get(i))
				compare = 1;
			else if (x.getNum().get(i) < y.getNum().get(i))
				compare = -1;
		}
		
		//Resets the variables to be equal to what they were before they were padded with zeroes
		this.num = normalX.getNum();
		y.num = normalY.getNum();
		
		int compareOperator = (this.getSign() == -1 || y.getSign() == -1) ? -1 : 1;
		
		return (compare * compareOperator);
	}
	
	private LargeInteger padWithZeroes(LargeInteger y) {
		//If the numbers aren't of the same length, pad them with 0s.
		if (this.getNum().size() > y.getNum().size()) {
			
			while (this.getNum().size() != y.getNum().size()) {
				y.getNum().add(0, '0');
			}
			
			return y;
			
		} else if (this.getNum().size() < y.getNum().size()) {
			
			while (y.getNum().size() != this.getNum().size()) {
				this.getNum().add(0, '0');
			}
			
			return this;
			
		} else {
			
			return y;
			
		}
	}
	
	public LargeInteger sum(LargeInteger y) {
		//Presetting variables
		StringBuilder sum = new StringBuilder();
		int pre_sum;
		int carry = 0;
		LargeInteger x = this;
		
		//If only one of the variables has a subtraction sign, the problem becomes a subtraction problem
		if ((y.getSign() == -1 && this.getSign() != -1)) {
			return (this.subtract(y));
		} else if (y.getSign() != -1 && this.getSign() == -1) {
			return (y.subtract(this));
		}
		
		//Padding inputs with 0s dependent on which item is larger in length
		if ((this.getNum().size() > y.getNum().size())) {
			x = this.padWithZeroes(this);
			y = this.padWithZeroes(y);			
		} else {
			x = this.padWithZeroes(y);
			y = y.padWithZeroes(y);	
		}
		
		//Loops through the list backwards, appending 
		for (int i = y.getNum().size()-1; i != -1; i--) {
			pre_sum = (Character.getNumericValue(x.getNum().get(i))) + (Character.getNumericValue(y.getNum().get(i))) + carry;
			carry = pre_sum / 10;
			
			sum.append(pre_sum % 10);
		}
		
		//If there is still a carry after the code executes, then add a 1 to the end of the string
		if (carry == 1)
			sum.append("1");
		
		//If both numbers are negative, add a minus sign at the end
		if (y.getSign() == -1 && this.getSign() == -1) {
			sum.append('-');			
		}
			
		return (new LargeInteger(sum.reverse().toString()));			
		
	}
	
	private LargeInteger subtract(LargeInteger y) {
		//Sets the size of the post_subtract array to be the same as whichever list is greater
		final int MAXSIZE = (y.getNum().size() >= this.getNum().size()) ? y.getNum().size() : this.getNum().size();
		int[] borrow = new int[MAXSIZE];
		for (int i = 0; i < MAXSIZE; i++) {borrow[i] = 0;}
		int presum;
		StringBuilder sum = new StringBuilder();
		LargeInteger larger = this;
		LargeInteger smaller = y;
		boolean reloop = false;
		
		//Sets the larger and smaller, respectively
		if (this.compareTo(y) == 1) {
			larger = y;
			smaller = this;
		} else if (this.compareTo(y) == -1) {
			larger = this;
			smaller = y;
		} else if (this.compareTo(y) == 0) {
			return new LargeInteger("0");
		}
		
		larger = new LargeInteger(larger.toString());
		smaller = new LargeInteger(smaller.toString());
		
		if (smaller.getNum().size() != MAXSIZE) {
			smaller.padWithZeroes(larger);
		}
		
		for (int i = MAXSIZE-1; i != -1; i--) {
			
			if ((larger.getNum().get(i) >= smaller.getNum().get(i)) && (borrow[i] != -1 || reloop == true)) {
				reloop = false;
			} else {
				
				if (borrow[i] == 0) {
					borrow[i] = 10;
				}
					
				
				//If i-1 is a valid index
				if ((i-1) >= 0) {
					borrow[i-1] = -1;
					reloop = true;
					
					//If the number at an index is 0, then borrow
					if (larger.getNum().get(i-1).equals('0')) {
						borrow[i-1] = 9;
						borrow[i-2] = -1;
					}
				}
			}
			
			presum = larger.getNum().get(i) - smaller.getNum().get(i) + borrow[i];
			
			if (presum < 0) {
				presum = 9;				
			}
			sum.append(presum);
		}
		
		if (larger.getSign() == -1) {
			sum.append("-");
		}
		
		return (new LargeInteger(sum.reverse().toString()));
	}
	
}
