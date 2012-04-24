package uri;

import javax.swing.text.html.*;
import javax.swing.text.*;

import crawler.ListMonitor;

import java.io.*;
import java.net.*;

public class LinkGetter extends HTMLEditorKit.ParserCallback {

    private Writer out;
    static String baseURI;
    //static String startURL = "http://cs.lth.se/";
    ListMonitor mon;
    
    public LinkGetter(ListMonitor mon) {
    	super();
    	this.mon = mon;
    }

    public LinkGetter(Writer out) {
    	super();
        this.out = out;
    }

    @Override
    public void handleStartTag(HTML.Tag tag, MutableAttributeSet attributes, int position) {

        //System.out.println("Start: " + tag + " Position: " + position);
        if (tag == HTML.Tag.A) {
            String href = (String) attributes.getAttribute(HTML.Attribute.HREF);
            //System.out.println("Extracted link: " + href);
            try {
               // System.out.println("\tAbsolute link: " + new URL(new URL(baseURI), href));
            	URL url = new URL(new URL(baseURI), href);
                if(url.toURI().isOpaque()){
                	mon.addURN(url.toString());
                }else{
                	mon.AddRemainingURL(url.toString());
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void handleEndTag(HTML.Tag tag, int position) {
        //System.out.println("End: " + tag + " Position: " + position);
    }

    @Override
    public void handleSimpleTag(HTML.Tag tag, MutableAttributeSet attributes, int pos) {
        //System.out.println("Simple: " + t + " Position: " + pos);
        if (tag == HTML.Tag.BASE) {
            String href = (String) attributes.getAttribute(HTML.Attribute.HREF);
            baseURI = href;
            //System.out.println("Base URL: " + href);
        }
        if (tag == HTML.Tag.IMG) {
            String href = (String) attributes.getAttribute(HTML.Attribute.SRC);
            //System.out.println("Image: " + href);
        }
        if (tag == HTML.Tag.FRAME) {
            String href = (String) attributes.getAttribute(HTML.Attribute.SRC);
            //System.out.println("Frame: " + href);
        }
    }

    @Override
    public void handleText(char[] text, int position) {
    }

}