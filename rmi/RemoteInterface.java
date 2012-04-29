package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;
public interface RemoteInterface extends Remote {
	void suspend() throws RemoteException;
	void start() throws RemoteException;
	void stop() throws RemoteException;
	Set<String>[] fetchAndSet(Set<String> traversed, Set<String> remaining) throws RemoteException;
	Set<String>[] fetch() throws RemoteException;
}
