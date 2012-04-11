package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

class IOHandler implements Runnable {

	private BufferedReader in;
	private OutputStreamWriter out;

	public IOHandler(InputStream in, OutputStream out) {
		this.in = new BufferedReader(new InputStreamReader(in));
		this.out = new OutputStreamWriter(out);
	}

	@Override
	public void run() {
		String line;
		try {
			while (true) {
				line = in.readLine();
				if (line == null) {
					break;
				} else {
					out.write(line + "\r\n");
					out.flush();
					if (line.equals(":q")) {
						break;
					}
				}
			}
		} catch (SocketException e) {
			System.out.println("Connection Closed");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}

public class ChatClient {
	public static void main(String args[]) throws UnknownHostException,
			IOException {

		if (args.length != 2) {
			System.out.println("Usage: java ChatClient HOST PORT");
			System.exit(1);
		}

		String host = args[0];
		int port = Integer.parseInt(args[1]);

		Socket connection = new Socket(host, port);

		System.out.println("Connected");
		
		IOHandler writer = new IOHandler(System.in,
				connection.getOutputStream());

		IOHandler listener = new IOHandler(connection.getInputStream(),
				System.out);

		Thread wt = new Thread(writer);
		Thread lt = new Thread(listener);

		lt.start();
		wt.start();

	}
}
