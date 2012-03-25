import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.CharBuffer;

public class EchoTCP1 {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		ServerSocket server = new ServerSocket(30000);
		while (true) {
			Socket connection = server.accept();
			System.out.println(connection.getInetAddress().toString());
		
			InputStream inStream = connection.getInputStream();
			
			while(inStream.read()){
				
			}
			
			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream());
			out.write("You've connected to this server. Bye-bye now.\r\n");
			connection.close();
		}
	}

}
