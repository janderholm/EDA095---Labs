import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


public class TimeServerUDP {
	public static void main(String[] args) throws IOException {
		int port = 30000;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		}
		
		DatagramSocket socket = new DatagramSocket(port);
		byte buf[] = new byte[65507];
		DatagramPacket dp = new DatagramPacket(buf,buf.length);
		
		while(true) {
			socket.receive(dp);
			String in = new String(buf,0,dp.getLength());
			Date date = new Date();
			DateFormat df;
			String s = "";
			if("DATE?".equals(in)) {
				df = DateFormat.getDateInstance(DateFormat.LONG);
				s = df.format(date) + "\n";
			}else if("TIME?".equals(in)){
				df = DateFormat.getTimeInstance(DateFormat.LONG);
				s = df.format(date) + "\n";
			}else{
				s = "Unsupported\n";
			}
			socket.send(new DatagramPacket(s.getBytes(), 0, s.getBytes().length, dp.getSocketAddress()));
		}
	}
}
