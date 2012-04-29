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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() throws RemoteException {
		new Thread(new Starter(mon)).start();
	}

	@Override
	public void stop() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<String>[] fetch() throws RemoteException {
		return 	mon.fetch();
	}

	@Override
	public Set<String>[] fetchAndSet(Set<String> traversed,
			Set<String> remaining) throws RemoteException {
		return mon.fetchAndSet(traversed, remaining);
	}


}
