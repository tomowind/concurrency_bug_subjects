package benchmarks.hedc;

import java.text.ParseException;
import java.util.Date;

public class Tester_RandomDate {
	
	public static void main(String[] args) {
		String dateString = "2000/02/02 22:06:47";
//		String dateString = "2000/02/06 21:23:16";
		Date date = null;
		try {
			date = RandomDate.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("parsed date: "+date);
	}

}
