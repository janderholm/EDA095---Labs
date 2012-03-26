import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;


public class TCPclient {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket = new Socket("127.0.0.1", 30002);
		OutputStream out = socket.getOutputStream();
		
		while(true) {
			int ch;
			String s = new String();
			do {
			    ch = System.in.read();
			    if (ch!='\n') {
			    	s = s+(char)ch;
			    }
			} while(ch!='\n');
			System.out.println("Sending message: "+s);
			byte[] bytes = ByteBuffer.allocate(4).putInt(s.getBytes().length).array();
			out.write(bytes, 0, bytes.length);
			if(s.equals("close"))
				out.write(1); //send if close
			else 
				out.write(0);
			out.write(s.getBytes(), 0, s.getBytes().length);
			out.flush();
			
			InputStream in = socket.getInputStream();
			in.read();
			
			if(s.equals("close")){ //safe to close when server is finished
				System.out.println("connection closed");
				break;
			}
				
			
		}

	}

}
