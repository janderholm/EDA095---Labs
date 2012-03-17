import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class SendUDP {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		//java SendUDP machine port command.
		if(args.length < 3){
			System.out.println("epic fail!");
		}
		String machine = args[0];
		int port = Integer.parseInt(args[1]);
		String command = args[2];
		
		InetAddress address = InetAddress.getByName(machine);
		DatagramPacket p1 = new DatagramPacket(command.getBytes(),0,command.getBytes().length, address,port);
		byte buf[] = new byte[65507];
		DatagramPacket dp = new DatagramPacket(buf,buf.length);
		DatagramSocket socket = new DatagramSocket();
		socket.send(p1);
		socket.receive(dp);
		System.out.println(new String(dp.getData()));
	}

}
