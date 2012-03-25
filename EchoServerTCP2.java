import java.io.*;
import java.net.*;

class Connection implements Runnable {
	Socket connection;

	public Connection(Socket connection) {
		this.connection = connection;
	}

	public void run() {
		try {
			InputStream in = connection.getInputStream();
			OutputStream out = connection.getOutputStream();

			final int bufsize = 5;
			int len;
			int offset = 0;
			int msgend;
			byte[] buf = new byte[bufsize];

			while ((len = in.read(buf, offset, bufsize)) != -1) {
				offset += len;
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
					System.arraycopy(oldbuf, msgend, buf, 0, oldbuf.length
							- msgend);
				} else {
					byte[] oldbuf = buf;
					buf = new byte[offset + bufsize];
					System.arraycopy(oldbuf, 0, buf, 0, oldbuf.length);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				System.out.println("Close connection");
				connection.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

public class EchoServerTCP2 {
	public static void main(String[] args) throws IOException {
		ServerSocket listen = new ServerSocket(30000);

		while (true) {
			Socket socket = listen.accept();
			System.out.println("Got connection " + socket.getInetAddress());

			new Thread(new Connection(socket)).start();
		}
	}
}