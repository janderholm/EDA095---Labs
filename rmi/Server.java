package rmi;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import crawler.ListMonitor;
import crawler.Settings;
import crawler.Starter;

public class Server {
	public static void main(String[] args) throws InterruptedException {
		
		
		System.setProperty("java.rmi.server.codebase", 
				RemoteInterface.class.getProtectionDomain().
				getCodeSource().getLocation().toString());
		
		int rmisocket = 3000;
		int registrysocket = 30000;
		String rref = "Remote";
		if(args.length == 3){
			rmisocket = Integer.parseInt(args[0]);
			registrysocket = Integer.parseInt(args[1]);
			rref = args[2];
		}
		
		try {
			Thread th = null;
			ListMonitor mon = new ListMonitor(th);
			th = new Thread(new Starter(mon));
			mon.AddRemainingURL(Settings.START_URL);
			th.start(); //start the thread.
			
			RemoteFunctions obj = new RemoteFunctions(mon);
			RemoteInterface stub = (RemoteInterface) UnicastRemoteObject
					.exportObject(obj, rmisocket);

			// Bind the remote object's stub in the registry
			Registry registry = LocateRegistry.getRegistry(registrysocket);
			registry.bind("Remote", stub);

			System.out.println("Server ready");
			
			BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));

	        while (true){
	        	System.out.print("server> ");
	        	String command = cin.readLine();
	        	if(command.equals("close")){
	        		registry.unbind(rref);
	        		System.out.println("Server closed");
	        		System.exit(1);
	        	}else if(command.equals("help")){
	        		System.out.println("close - closes the server and unbinds the RMI registry");
	        		System.out.println("start");
	        		System.out.println("suspend");
	        	}else if(command.equals("start")){
	        		mon.unSuspendThreads();
	        		System.out.println("Started crawlers");
	        	}else if(command.equals("suspend")){
	        		mon.suspendThreads();
	        		System.out.println("Suspended crawlers");
	        	}else{
	        		System.out.println("Command does not exist");
	        	}
	        }

		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
	}

}

