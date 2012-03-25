import java.io.IOException;
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
		Socket socket = new Socket("127.0.0.1", 30001);
		OutputStream out = socket.getOutputStream();
		String m = "trololololol";
		byte[] bytes = ByteBuffer.allocate(4).putInt(m.getBytes().length).array();
		for (byte b : bytes) {
			out.write(b);
		}
		bytes = m.getBytes();
		for (byte b : bytes) {
			out.write(b);
		}
		out.flush();
		socket.close();

	}

}
