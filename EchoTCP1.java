import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoTCP1 {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		ServerSocket server = new ServerSocket(30000);
		int buffSize = 500;
		byte[] buff    = new byte[500];
		int bytesRead   = 0;
		int result;
		Socket connection;
		OutputStream out = null;
		InputStream inStream;
		while(true){
			connection = server.accept();
			System.out.println("Connection from: " + connection.getInetAddress().toString());
			inStream = connection.getInputStream();
			out = connection.getOutputStream();
			bytesRead = 0; //reset counter
			buffSize = 500; //reset size
			while (bytesRead < buffSize) {
				result = inStream.read(buff, bytesRead, buffSize - bytesRead);
				if (result == -1) break;
				bytesRead += result;
				if(bytesRead >= buffSize){
					byte[] tbuff = buff;
					buffSize += bytesRead;
					buff = new byte[buffSize];
					System.arraycopy(tbuff, 0, buff, 0, bytesRead);
				}
		        //System.out.println(bytesRead);
			}
			System.out.println(new String(buff,0,bytesRead));
			out.write(buff,0,bytesRead);
			out.flush();
			connection.close();
		}
	}
}
