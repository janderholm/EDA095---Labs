package rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Set;

public class Client {

    private Client() {}

    public static void main(String[] args) {

        //String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(30000);
            RemoteInterface stub = (RemoteInterface) registry.lookup("Remote");
            stub.start();
            
            Thread.sleep(10000);
            System.out.println("FETCHING");
            Set<String>[] lists = stub.fetch();
    		System.out.println("urls: " + lists[0].size());
    		System.out.println("Remaining: " + lists[1].size());
    		Thread.sleep(10000);
    		stub.fetchAndSet(lists[0], lists[1]);
    		lists = stub.fetch();
    		 System.out.println("urls: " + lists[0].size());
     		System.out.println("Remaining: " + lists[1].size());
     		registry.unbind("Remote");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
