package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

class Participant implements Runnable {	
	private Sentry sentry;
	private LinkedBlockingQueue<String> mailbox;
	private Socket connection;
	
	public Participant(Socket connection) throws IOException {
		this.connection = connection;
		this.mailbox = new LinkedBlockingQueue<String>();
		this.sentry = new Sentry(this, connection);
		ThreadMonitor.getInstance().register(this);
	}

	public void placeInQueue(String msg) {
		try {
			this.mailbox.put(msg);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		Thread sentrythread = new Thread(this.sentry);
		sentrythread.start();

		String msg;
		while (true) {
			try {
				msg = mailbox.take();
				if (msg == "DIE") {
					try {
						connection.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				} else {
					sentry.sendMsg(msg);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ThreadMonitor.getInstance().deregister(this);
	}

	private class Sentry implements Runnable {
		private BufferedReader in;
		private OutputStreamWriter out;
		private Participant parent;
		
		public Sentry(Participant parent, Socket connection) throws IOException {
			this.parent = parent;
			this.in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			this.out = new OutputStreamWriter(connection.getOutputStream());
		}
		
		public void sendMsg(String msg) {
			try {
				this.out.write(msg + "\r\n");
				this.out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			String line;	
			while (true) {
				try {
					line = in.readLine();
					if (line == null || line.startsWith(":q")) {
						parent.placeInQueue("DIE");
						break;
					} else if (line.startsWith(":e ")) {
						parent.placeInQueue(line.substring(3));
					} else if (line.startsWith(":m ")) {
						ThreadMonitor.getInstance().sendMessage(line.substring(3));
					}

				} catch (SocketException e) {
					// Parent closed the socket
					break;		
				} catch (IOException e) {
					// TODO Auto-generated catch block
					parent.placeInQueue("DIE");
					e.printStackTrace();
					break;
				} 
			}
		}
	}
}

class ThreadMonitor {
	
	private static ThreadMonitor instance;
	private ArrayList<Participant> participants;
	
	private ThreadMonitor() {
		this.participants = new ArrayList<Participant>();
	}
	
	public synchronized void sendMessage(String msg) {
		for (Participant p : participants) {
			p.placeInQueue(msg);
		}
	}
	
	public synchronized void register(Participant p) {
		participants.add(p);
	}
	
	public synchronized void deregister(Participant p) {
		participants.remove(p);
	}
	
	public static synchronized ThreadMonitor getInstance() {
		if (instance == null) {
			instance = new ThreadMonitor();
		}
		return instance;	
	}
	
}

public class ChatServer {
	public static void main(String args[]) throws IOException {
		ServerSocket listen = new ServerSocket(30000);

		while (true) {
			Socket socket = listen.accept();
			System.out.println("Got connection " + socket.getInetAddress());
			new Thread(new Participant(socket)).start();
		}

	}
}
