package rmi;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Client {

	public static void main(String[] args) throws IOException,
			InterruptedException, NotBoundException {

		ArrayList<String> hosts = new ArrayList<String>();
		ArrayList<String> startUrls = new ArrayList<String>();
		FileInputStream fstream = new FileInputStream("hosts.txt");
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		while ((strLine = br.readLine()) != null) {
			hosts.add(strLine.split(" ")[0]);
			startUrls.add(strLine.split(" ")[1]);
		}
		in.close();

		Set<String> traversed = new HashSet<String>();
		Set<String> remaining = new HashSet<String>();
		int i = 0;

		Registry registry = LocateRegistry.getRegistry(30000);

		RemoteInterface stubs[] = new RemoteInterface[hosts.size()];
		for (i = 0; i < hosts.size(); ++i) {
			stubs[i] = (RemoteInterface) registry.lookup(hosts.get(i));
		}

		for (i = 0; i < hosts.size(); ++i) {
			stubs[i].addUrl(startUrls.get(i));
			stubs[i].start();
		}

		// String host = (args.length < 1) ? null : args[0];
		while (true) {

			Thread.sleep(180000);

			for (i = 0; i < hosts.size(); ++i) {
				stubs[i].suspend();
			}

			System.out.println("FETCHING");
			for (i = 0; i < hosts.size(); ++i) {
				Set<String>[] lists = stubs[i].fetch();
				System.out.println("urls: " + lists[0].size());
				System.out.println("Remaining: " + lists[1].size());
				traversed.addAll(lists[0]);
				remaining.addAll(lists[1]);
			}

			i = 0;
			int iList = 0;
			HashSet<String> nRemaining = new HashSet<String>();
			for (String s : remaining) {
				i++;
				nRemaining.add(s);
				if (i == (iList+1) * (remaining.size() / hosts.size())) {
					System.out.println("SET");
					stubs[iList].set(traversed, nRemaining);
					nRemaining = new HashSet<String>();
					++iList;
				}
			}

			for (i = 0; i < hosts.size(); ++i) {
				stubs[i].start();
			}

		}
	}
}
