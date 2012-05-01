package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;
public interface RemoteInterface extends Remote {
	void suspend() throws RemoteException;
	void start() throws RemoteException;
	void stop() throws RemoteException;
	Set<String>[] fetch() throws RemoteException;
	void set(Set<String> traversed, Set<String> remaining)
			throws RemoteException;
	public void addUrl(String url) throws RemoteException;
}
