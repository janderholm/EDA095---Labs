package crawler;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import rmi.RemoteFunctions;
import rmi.RemoteInterface;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		ListMonitor mon = new ListMonitor();
		mon.AddRemainingURL(Settings.START_URL);
		
		System.setProperty("java.rmi.server.codebase", 
				RemoteInterface.class.getProtectionDomain().
				getCodeSource().getLocation().toString());
		
		try {
			RemoteFunctions obj = new RemoteFunctions(mon);
			RemoteInterface stub = (RemoteInterface) UnicastRemoteObject
					.exportObject(obj, 3000);

			// Bind the remote object's stub in the registry
			Registry registry = LocateRegistry.getRegistry(30000);
			registry.bind("Remote", stub);

			System.err.println("Server ready");
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
	}

}
