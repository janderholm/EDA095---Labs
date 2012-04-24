package crawler;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class ListMonitor {
	public Set<String> urn;
	public Set<String> url;
	public Set<String> hashRemainingURLs; // ok ugly but fast
	public LinkedList<String> remainingURLs;
	private int waitingThreads = 0;
	private int progress = 0;

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

	synchronized public void AddRemainingURL(String s) {
		if (!url.contains(s) && !hashRemainingURLs.contains(s)) {
			remainingURLs.add(s);
			hashRemainingURLs.add(s);
			//System.out.println("urns: " + urn.size());
			//System.out.println("urls: " + url.size());
			//System.out.println("Remaining: " + remainingURLs.size());
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
		
		if(progress*(Settings.LIMIT/100) < url.size()){
			System.out.println(progress + "%");
			progress++;
		}
		
		
		String rurl = remainingURLs.pop();
		hashRemainingURLs.remove(rurl);
		url.add(rurl); 
		waitingThreads--;
		return rurl;
	}

}
