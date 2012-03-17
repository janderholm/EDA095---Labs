import java.net.*;
import java.io.*;

public class MCReader {

    public static void main(String args[]) {
	try {
	    MulticastSocket ms = new MulticastSocket(4099);
	    InetAddress ia = InetAddress.getByName("experiment.mcast.net");
	    ms.joinGroup(ia);
	    while(true) {
		byte[] buf = new byte[65536];
		DatagramPacket dp = new DatagramPacket(buf,buf.length);
		ms.receive(dp);
		String s = new String(dp.getData(),0,dp.getLength());
		System.out.println("Received: "+s);
	    }
	} catch(IOException e) {
	    System.out.println("Exception:"+e);
	}
    }

}
