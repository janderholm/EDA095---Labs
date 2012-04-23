package crawler;

import java.util.ArrayList;

public class ListMonitor {
	public ArrayList<String> urn;
	public ArrayList<String> url;
	public ArrayList<String> remainingURLs;
	
	public ListMonitor(){
		urn = new ArrayList<String>();
		url = new ArrayList<String>();
		remainingURLs = new ArrayList<String>();
	}
	
	synchronized public void addURN(String s){
		urn.add(s);
	}
	
	synchronized public void addURL(String s){
		url.add(s);
	}
	
	synchronized public void AddRemainingURL(String s){
		remainingURLs.add(s);
		notifyAll();
	}
	
	synchronized public String getRemainingURL(){
		while(remainingURLs.isEmpty()){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return remainingURLs.remove(0);
	}
	
}
