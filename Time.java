import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


public class Time {
	public static void main(String[] args) {
		Date date = new Date();
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.LONG, Locale.GERMAN);
		String s = df.format(date);
		System.out.println(s);
		System.out.println(date);
	}
}
