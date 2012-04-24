package crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.text.html.HTMLEditorKit;

import uri.LinkGetter;
import uri.ParseGetter;

public class Crawler extends Thread{
	ListMonitor mon;
	HTMLEditorKit.ParserCallback callback;
	HTMLEditorKit.Parser parser;
	
	public Crawler(ListMonitor mon){
		this.mon = mon;
		ParseGetter kit = new ParseGetter();
		parser = kit.getParser();
		callback = new LinkGetter(mon);
	}
	
	public void run() {
		while(!this.isInterrupted()){
			boolean accept1 = false;
			boolean accept2 = false;
			String startURL = mon.getRemainingURL();
			if(startURL==null || mon.urlSize() > Settings.LIMIT){
				System.out.println("IM DONE");
				break;
			}
			
			if(!startURL.substring(0, startURL.indexOf(':')).equals("http")){
				continue;
			}
			
			// http://www.google.se
			// http://www.google.se/site
			String end = startURL.substring(startURL.lastIndexOf("/"),startURL.length()-1);
			if(end.indexOf(".") == -1 || startURL.indexOf("/")+1 == startURL.lastIndexOf("/")){
				accept2 = true;
			}

			if(startURL.endsWith("/") ||
					startURL.endsWith(".html") ||
					startURL.endsWith(".htm") ||
					startURL.endsWith(".shtml") ||
					startURL.endsWith(".cgi") ||
					startURL.endsWith(".jsp") ||
					startURL.endsWith(".asp") ||
					startURL.endsWith(".aspx") ||
					startURL.endsWith(".php") ||
					startURL.endsWith(".pl") ||
					startURL.endsWith(".cfm")){
				accept1 = true;
			}
			
			if(!(accept1 || accept2)){
				continue;
			}
			
			try {
				URL url = new URL(startURL);
				URLConnection c = url.openConnection();
				c.setConnectTimeout(1000);
				c.setReadTimeout(2000);
				BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
				parser.parse(in, callback, true);
			} catch (IOException ex) {
			}
		}
	}
}
