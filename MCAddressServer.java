import java.io.*;
import java.net.*;

public class MCAddressServer {
    public static void main(String args[]) throws IOException {
        MulticastSocket ms = new MulticastSocket(4099);
        InetAddress ia = InetAddress.getByName("experiment.mcast.net");
        ms.joinGroup(ia);
	
        byte[] buf = new byte[65536];
        while (true) {
            DatagramPacket dp = new DatagramPacket(buf, buf.length);
            System.out.println("Receiving.");
            ms.receive(dp);
            System.out.println("Received.");
            String s = new String(dp.getData(), 0, dp.getLength());
            System.out.println("Received: " + s);
            if ("WHOAREYOU?".equals(s)) {
                s = InetAddress.getLocalHost().getHostName();
                dp = new DatagramPacket(s.getBytes(), s.getBytes().length,
                       dp.getSocketAddress());
                ms.send(dp);
            }
        }
    }
}
