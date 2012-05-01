package rmi;

import java.rmi.RemoteException;
import java.util.Set;


import crawler.ListMonitor;
import crawler.Starter;

public class RemoteFunctions implements RemoteInterface{
	private ListMonitor mon;
	public RemoteFunctions(ListMonitor mon) {
		this.mon = mon;
	}
	
	@Override
	public void suspend() throws RemoteException {
		mon.suspendThreads();
	}

	@Override
	public void start() throws RemoteException {
		mon.unSuspendThreads();
	}

	@Override
	public void stop() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<String>[] fetch() throws RemoteException {
		return 	mon.fetch();
	}

	public void set(Set<String> traversed,Set<String> remaining) throws RemoteException {
		mon.set(traversed, remaining);
	}
	
	public void addUrl(String url) throws RemoteException {
		mon.addURL(url);
	}


}
