package crawler;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.text.html.HTMLEditorKit;

import uri.LinkGetter;
import uri.ParseGetter;

public class Crawler implements Runnable{
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
		while(true){
			String startURL = mon.getRemainingURL();
			try {
				URL url = new URL(startURL);
				InputStream in = new BufferedInputStream(url.openStream());
				InputStreamReader r = new InputStreamReader(in);
				parser.parse(r, callback, true);
			} catch (IOException ex) {
				ex.printStackTrace();
				System.err.println(ex);
			}
		}
	}
}
