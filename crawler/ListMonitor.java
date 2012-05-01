package crawler;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;


public class ListMonitor {
	public Set<String> urn;
	public Set<String> url;
	public Set<String> hashRemainingURLs; // ok ugly but fast
	public LinkedList<String> remainingURLs;
	private int waitingThreads = 0;
	private int progress = 0;
	private boolean suspend = true;

	public ListMonitor() {
		urn = new HashSet<String>();
		url = new HashSet<String>();
		remainingURLs = new LinkedList<String>();
		hashRemainingURLs = new HashSet<String>();
	}

	synchronized public void addURN(String s) {
		if (!urn.contains(s)) {
			urn.add(s);
		}
	}

	synchronized public void addURL(String s) {
		url.add(s);
	}

	synchronized public int urlSize() {
		return url.size();
	}

	synchronized public int urnSize() {
		return urn.size();
	}

	synchronized public int remainingSize() {
		return remainingURLs.size();
	}
	
	@SuppressWarnings("unchecked")
	synchronized public Set<String>[] fetch() {
		Set<String> tTraversed = new HashSet<String>();
		Set<String> tRemaining =  new HashSet<String>();
		tTraversed.addAll(url);
		tRemaining.addAll(hashRemainingURLs);
		return (Set<String>[]) (new Set[]{tTraversed,tRemaining});
	}
	
	synchronized public void set(Set<String> traversed, Set<String> remaining) {
		System.out.println("CHANGE");
		url = traversed;
		hashRemainingURLs = remaining;
		remainingURLs = new LinkedList<String>();
		remainingURLs.addAll(remaining);
		double calc = (traversed.size()/(double)Settings.LIMIT)*100;
		progress = (int) calc;
	}

	synchronized public void AddRemainingURL(String s) {
		if (!url.contains(s) && !hashRemainingURLs.contains(s)) {
			remainingURLs.add(s);
			hashRemainingURLs.add(s);
		}
		notifyAll();
	}

	synchronized public String getRemainingURL() {
		waitingThreads++;
		while (remainingURLs.isEmpty()) {
			if (waitingThreads == Settings.N_THREADS) {
				notifyAll();
				return null;
			}
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		Random rand = new Random();
		if(rand.nextInt()%3 == 1){
			System.out.print("\r /");
		}else if(rand.nextInt()%3 == 2){
			System.out.print("\r \\");
		}else{
			System.out.print("\r |");
		}
		
		
		if(progress*(Settings.LIMIT/100) < url.size()){
			System.out.print("\r"+progress + "%  " + url.size());
			progress++;
		}
		
		
		String rurl = remainingURLs.pop();
		hashRemainingURLs.remove(rurl);
		url.add(rurl);
		waitingThreads--;
		return rurl;
	}
	
	synchronized public void suspendThreads() {
		suspend = true;
	}
	
	synchronized public void unSuspendThreads() {
		suspend = false;
		notifyAll();
	}

	synchronized public void suspend() {
		while(suspend){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
