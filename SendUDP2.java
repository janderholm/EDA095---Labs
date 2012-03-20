import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;


public class SendUDP2 {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		//java SendUDP machine port command.
		//if (args.length != 3) {
		//	System.out.println("Usage: SendUDP hostname port");
		//}
		
		byte[] who = "WHOAREYOU?".getBytes();
		
		byte buf[] = new byte[65507];
	    MulticastSocket ms = new MulticastSocket();
	    ms.setTimeToLive(1);
	    InetAddress ia = InetAddress.getByName("experiment.mcast.net");
	    DatagramPacket dp = new DatagramPacket(who, who.length, ia, 4099);
	    ms.send(dp);

	    dp = new DatagramPacket(buf, buf.length);
	    ms.receive(dp);
	    
		String machine = new String(dp.getData(), 0, dp.getData().length);
		int port = 30000;//Integer.parseInt(args[1]);
		String command = args[0];
		
		InetAddress address = InetAddress.getByName(machine);
		DatagramPacket p1 = new DatagramPacket(command.getBytes(), 0, command.getBytes().length, address, port);
		dp = new DatagramPacket(buf, buf.length);
		DatagramSocket socket = new DatagramSocket();
		socket.send(p1);
		socket.receive(dp);
		System.out.println(new String(dp.getData()));
	}

}
