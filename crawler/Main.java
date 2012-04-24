package crawler;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		ListMonitor mon = new ListMonitor();
		mon.AddRemainingURL(Settings.START_URL);
		
		long start = System.currentTimeMillis();
		Thread th[] = new Thread[Settings.N_THREADS];
		for(int i = 0; i < Settings.N_THREADS; i++){
			th[i] = new Thread(new Crawler(mon));
			th[i].start();
		}
		
		for(int i = 0; i < Settings.N_THREADS; i++){
			th[i].join();
		}
		
		long time = (System.currentTimeMillis()-start)/1000;
		
		for(String s : mon.url){
			System.out.println(s);
		}
		
		for(String s : mon.urn){
			System.out.println(s);
		}
		
		System.out.println("urns: " + mon.urn.size());
		System.out.println("urls: " + mon.url.size());
		System.out.println("Remaining: " + mon.remainingURLs.size());
		System.out.println("TIME: " + time);
		
	}
}
