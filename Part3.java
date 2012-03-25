import java.util.*;

interface Mailbox {
	public String fetch() throws InterruptedException;

	public void store(String msg) throws InterruptedException;
}

class BufferedMailbox implements Mailbox {

	public static final int SIZE = 5;
	private String[] buffer = new String[SIZE];
	private int first = 0;
	private int last = 0;
	private int n = 0;

	@Override
	public synchronized String fetch() throws InterruptedException {
		while (n == 0) {
			wait();
		}
		String msg = buffer[first];
		buffer[first] = null;

		n -= 1;
		first = (first + 1) % SIZE;

		notifyAll();

		return msg;
	}

	@Override
	public synchronized void store(String msg) throws InterruptedException {
		while (n == SIZE) {
			wait();
		}
		buffer[last] = msg;

		n += 1;
		last = (last + 1) % SIZE;

		notifyAll();
	}
}

class SimpleMailbox implements Mailbox {
	String msg = null;

	@Override
	public synchronized String fetch() throws InterruptedException {
		while (msg == null) {
			wait();
		}
		String msg = this.msg;
		this.msg = null;
		notifyAll();
		return msg;
	}

	@Override
	public synchronized void store(String msg) throws InterruptedException {
		while (this.msg != null) {
			wait();
		}
		this.msg = msg;
		notifyAll();
	}
}

class Listener implements Runnable {
	private Mailbox mailbox;

	public Listener(Mailbox mailbox) {
		this.mailbox = mailbox;
	}

	@Override
	public void run() {
		String msg;
		while (true) {
			try {
				msg = mailbox.fetch();
				System.out.println(msg);
				if ("KILL".equals(msg)) {
					break;
				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}

class Worker implements Runnable {

	private String name;
	private Random rand;
	private Mailbox mailbox;

	public Worker(String name, Random rand, Mailbox mailbox) {
		this.name = name;
		this.rand = rand;
		this.mailbox = mailbox;
	}

	@Override
	public void run() {
		for (int i = 0; i < 5; ++i) {
			try {
				mailbox.store("Thread " + name + " awake at "
						+ System.currentTimeMillis());
				Thread.sleep(rand.nextInt(10000));
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}

public class Part3 {
	public static void main(String[] args) {
		final int nthreads = 10;
		Random rand = new Random();
		Mailbox mailbox = new BufferedMailbox();

		Thread[] sender = new Thread[nthreads];
		Thread listener = new Thread(new Listener(mailbox));
		listener.start();

		for (int i = 0; i < nthreads; ++i) {
			sender[i] = new Thread(new Worker("" + (char) ('A' + i), rand,
					mailbox));
			sender[i].start();
		}

		for (int i = 0; i < nthreads; ++i) {
			try {
				sender[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			mailbox.store("KILL");
			listener.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
