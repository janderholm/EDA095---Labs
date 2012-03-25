import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.net.ServerSocket;
import java.net.Socket;

//The first four bytes are an int with the message lenght, not including the int itself 
public class EchoTCP1 {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		ServerSocket server = new ServerSocket(30001);
		int buffSize = 4;
		byte[] lenbuff = new byte[4];
		byte[] buff;
		int bytesRead   = 0;
		int result;
		Socket connection;
		OutputStream out = null;
		InputStream inStream;
		DataInput in;
		
		while(true){
			connection = server.accept();
			System.out.println("Connection from: " + connection.getInetAddress().toString());
			inStream = connection.getInputStream();
			out = connection.getOutputStream();
			bytesRead=0;
			while (bytesRead < 4) {
				result = inStream.read(lenbuff, bytesRead, 4 - bytesRead);
				if (result == -1) break;
				bytesRead += result;
			}
			
			int len = buffArrayToInt(lenbuff); //messange length
			buff = new byte[len];
			System.out.println(len);
			bytesRead = 0;
			while (bytesRead < len) {
				result = inStream.read(buff, bytesRead, len - bytesRead);
				if (result == -1) break;
				bytesRead += result;
			}
			
			System.out.println(new String(buff));
			out.write(buff,0,bytesRead);
			out.flush();
			connection.close();
		}
	}
	
	static int buffArrayToInt(byte[] lenbuff) throws StreamCorruptedException{
		int len = (((lenbuff[0] & 0xff) << 24) | 
				((lenbuff[1] & 0xff) << 16) | 
				((lenbuff[2] & 0xff) << 8) | 
				(lenbuff[3] & 0xff));
		if (len < 0 || len > 1 << 24) throw new StreamCorruptedException("Invalid message length "+len);
		return len;
	}
}
