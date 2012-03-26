import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.net.ServerSocket;
import java.net.Socket;

//The first four bytes are an int with the message lenght, not including the int itself 
//the fith byte tells wheter to close or not. != 0 for close
//[4b][1b][message]
public class EchoTCP1 {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		ServerSocket server = new ServerSocket(30002);
		byte[] lenbuff = new byte[4];
		byte[] buff;
		Socket connection;
		OutputStream out = null;
		InputStream inStream;
		
		connection = server.accept();
		while(true){
			System.out.println("Connection from: " + connection.getInetAddress().toString());
			inStream = connection.getInputStream();
			out = connection.getOutputStream();

			read(inStream, lenbuff, 4);
			int len = buffArrayToInt(lenbuff); //messange length
			int close = inStream.read(); //last message?
			buff = new byte[len];
			read(inStream, buff, len);
			
			String message = new String(buff);
			System.out.println(message);
			out.write(1);
			out.flush();
			
			if(close != 0){
				System.out.println("closed");
				connection.close();
				connection = server.accept();
			}
			
		}
	}
	
	static void read(InputStream inStream, byte[] lenbuff, int bytesToRead) throws IOException {
		int bytesRead=0;
		int result = 0;
		while (bytesRead < bytesToRead) {
			result = inStream.read(lenbuff, bytesRead, bytesToRead - bytesRead);
			if (result == -1) break;
			bytesRead += result;
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
