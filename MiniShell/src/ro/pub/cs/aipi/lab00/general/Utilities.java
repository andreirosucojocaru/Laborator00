package ro.pub.cs.aipi.lab00.general;

public class Utilities {
	
	public static int numberOfDigits(long number) {
		if (number == 0)
			return 1;
		int result = 0, index = 0;
		while(number != 0) {
			result++;
			index++;
			number /= 10;
			if (index % 3 == 0 && number != 0)
				result++;
		}
		return result;
	}
	
	public static String format(long number) {
		if (number == 0)
			return "";
		String result = new String();
		int index = 0;
		while(number != 0) {
			result = (number%10) + result;
			index++;
			number /= 10;
			if (index % 3 == 0 && number != 0)
				result = "," + result;
		} 
		return result;
	}

}
