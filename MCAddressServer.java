import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;


public class MCAddressServer {
	public static void main(String args[]) throws IOException {
		MulticastSocket ms = new MulticastSocket(4099);
		
		InetAddress ia = InetAddress.getByName("experiment.mcast.net");
		ms.joinGroup(ia);
		
		byte[] buf = new byte[65536];
		while (true) {
			DatagramPacket dp = new DatagramPacket(buf, buf.length);
			ms.receive(dp);
			String s = new String(dp.getData(), 0, dp.getLength());
			if ("WHOAREYOU?".equals(s)) {
				s = InetAddress.getLocalHost().getHostName();
				dp = new DatagramPacket(s.getBytes(), s.getBytes().length, dp.getSocketAddress());
				
			}
			System.out.println("Received: " + s);
		}
	}
}
