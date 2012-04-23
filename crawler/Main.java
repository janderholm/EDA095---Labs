package crawler;

public class Main {
	public static void main(String[] args) {
		String startURL = "http://cs.lth.se/EDA095/";
		// static String startURL = "http://cs.lth.se/pierre_nugues/";
		ListMonitor mon = new ListMonitor();
		mon.AddRemainingURL(startURL);
		Thread th = new Thread(new Crawler(mon));
		th.start();
		try {
			th.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
