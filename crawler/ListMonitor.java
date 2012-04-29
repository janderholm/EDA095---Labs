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
	
	@SuppressWarnings("unchecked")
	synchronized public Set<String>[] fetch() {
		Set<String> tTraversed = new HashSet<String>();
		Set<String> tRemaining =  new HashSet<String>();
		tTraversed.addAll(url);
		tRemaining.addAll(hashRemainingURLs);
		return (Set<String>[]) (new Set[]{tTraversed,tRemaining});
	}
	
	@SuppressWarnings("unchecked")
	synchronized public Set<String>[] fetchAndSet(Set<String> traversed, Set<String> remaining) {
		System.out.println("CHANGE");
		Set<String> tTraversed = url;
		Set<String> tRemaining = hashRemainingURLs;
		long time = System.currentTimeMillis();
		
		url = traversed;
		hashRemainingURLs = remaining;
		remainingURLs = new LinkedList<String>();
		remainingURLs.addAll(traversed);
		double calc = (traversed.size()/(double)Settings.LIMIT)*100;
		progress = (int) calc;
		System.out.println(progress);
		
		System.out.println(System.currentTimeMillis() - time);
		return (Set<String>[]) (new Set[]{tTraversed, tRemaining});
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
			System.out.println(progress + "%  " + url.size());
			progress++;
		}
		
		
		String rurl = remainingURLs.pop();
		hashRemainingURLs.remove(rurl);
		url.add(rurl);
		waitingThreads--;
		return rurl;
	}

}
