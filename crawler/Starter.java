package crawler;

import java.rmi.registry.LocateRegistry;

public class Starter extends Thread{
	ListMonitor mon;
	public Starter(ListMonitor mon){
		this.mon = mon;
	}
	
	@Override
	public void run() {
		long start = System.currentTimeMillis();
		Thread th[] = new Thread[Settings.N_THREADS];
		for(int i = 0; i < Settings.N_THREADS; i++){
			th[i] = new Thread(new Crawler(mon));
			th[i].start();
		}
		
		for(int i = 0; i < Settings.N_THREADS; i++){
			try {
				th[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			//LocateRegistry.getRegistry(30000).unbind("Remote");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long time = (System.currentTimeMillis()-start)/1000;
		
	}
		
}
