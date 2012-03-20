import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


public class TimeServer1 {
	public static void main(String[] args) {
		Date date = new Date();
		DateFormat df;
		String s = "";
		if (args[0].equals("DATE?")) {
			df = DateFormat.getDateInstance(DateFormat.LONG);
			s = df.format(date);
		} else if (args[0].equals("TIME?")) {
			df = DateFormat.getTimeInstance(DateFormat.LONG);
			s = df.format(date);
		} else {
		}
		System.out.println(s);
	}
}
