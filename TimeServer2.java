import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


public class TimeServer2 {
	public static void main(String[] args) throws IOException {
		int len;
		byte buf[] = new byte[100];
		while((len = System.in.read(buf)) != -1) {
			String in = new String(buf,0,len - 1,"UTF-8");
			Date date = new Date();
			DateFormat df;
			String s = "";
			if("DATE?".equals(in)) {
				df = DateFormat.getDateInstance(DateFormat.LONG);
				s = df.format(date);
			}else if("TIME?".equals(in)){
				df = DateFormat.getTimeInstance(DateFormat.LONG);
				s = df.format(date);
			}else{
				System.out.println("Unsupported");
			}
			System.out.println(s);
		}
	}
}
