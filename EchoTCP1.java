import java.io.*;
import java.net.*;

public class EchoTCP1 {
	public static void main(String[] args) throws IOException {
		ServerSocket listen = new ServerSocket(30000);

		while (true) {
			Socket socket = listen.accept();
			System.out.println("Got connection " + socket.getInetAddress());

			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();

			final int bufsize = 5;
			int offset = 0;
			int msgend;
			byte[] buf = new byte[bufsize];

			while ((offset += in.read(buf, offset, bufsize)) != -1) {
				String msg = new String(buf, 0, offset);
				if ((msgend = msg.indexOf("\r\n")) != -1) {
					// Got echo:
					System.out.println(msg.substring(0, msgend));
					// Include \r\n
					msgend += 2;
					out.write(msg.getBytes(), 0, msgend);
					out.flush();

					offset = 0;

					byte[] oldbuf = buf;
					buf = new byte[bufsize];
					System.arraycopy(oldbuf, msgend, buf, 0, oldbuf.length - msgend);
				} else {
					byte[] oldbuf = buf;
					buf = new byte[offset + bufsize];
					System.arraycopy(oldbuf, 0, buf, 0, oldbuf.length);
				}
			}
			socket.close();
		}
	}
}