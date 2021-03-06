/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Node;
import com.google.gwt.i18n.client.DateTimeFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author jonnakkerud
 */
public class Utils {
    public static final String HTTP_DATE_PATTERN = "EEE, d MMM yyyy HH:mm:ss";
    public static final String HTTP_FEED_DATE_PATTERN = "yyyy-MM-ddTHH:mm:ss";

    public static final long HOURS = 1000 * 60 * 60;
    public static final long MINUTES = 1000 * 60;

    static DateTimeFormat formatter;
    static DateTimeFormat feed_formatter;

    public static String createSpan(String txt, String style) {
        StringBuilder sb = new StringBuilder();
        sb.append("<span style=\"").append(style).append("\">");
        sb.append(txt);
        sb.append("</span>");
        return sb.toString();
    }

    // find instances of http:
    public static String createUrls(String str) {
        return nativeCreateUrls(str);
    }

    public static native String nativeCreateUrls(String input) /*-{
        return input.replace(/(ftp|http|https|file):\/\/[\S]+(\b|$)/gim,'<a href="$&">$&</a>');
    }-*/;

    public static String join(List<String> ar) {
        StringBuilder sb = new StringBuilder();

        Iterator itr = ar.iterator();
        while(itr.hasNext()) {
            sb.append("'").append(itr.next()).append("'").append(itr.hasNext() ? "," : "");
        }

        return sb.toString();
    }

    public static native double parseDouble(String s) /*-{
        return parseFloat(s);
    }-*/;
    
    public static native boolean isWebKit() /*-{
        return RegExp(" AppleWebKit/").test(navigator.userAgent);
    }-*/;

    // page run as standalone (full-screen mode)
    public static native boolean isIphone() /*-{
        return (navigator.userAgent.indexOf('iPhone') != -1);
    }-*/;

    public static boolean isMobile() {
        return isIphone() || isMobileSafari();
    }

    public static native boolean isMobileSafari() /*-{
        var ua = navigator.userAgent.toLowerCase();

         if (ua.indexOf("safari") != -1 &&
             ua.indexOf("applewebkit") != -1 &&
             ua.indexOf("mobile") != -1)
         {
             return true;
         }
         else
         {
             return false;
         }
    }-*/;

    public static Date createDate(double time) {
        return new Date((long)time);
    }

    public static Date parseHTTPDate(String s) {
        // gwt date formater has a problem with TZ as GMT
        // Sun, 09 Nov 2008 02:36:32 GMT
        String ps = null;
        if (s.indexOf("+0000") > 0) {
            ps = s.substring(0, s.indexOf("+0000"));
            ps = ps.trim();
        }
        else {
            ps = s;
        }
        if (formatter == null) {
            formatter = DateTimeFormat.getFormat(HTTP_DATE_PATTERN);
        }
        return formatter.parse(ps);
    }

    public static Date parseFeedDate(String s) {
        // yyyy-MM-ddTHHmmss
        String ps = null;
        if (s.indexOf("Z") > 0) {
            ps = s.substring(0, s.indexOf("Z"));
            ps = ps.trim();
        }
        else {
            ps = s;
        }

        if (feed_formatter == null) {
            feed_formatter = DateTimeFormat.getFormat(HTTP_FEED_DATE_PATTERN);
        }
        return feed_formatter.parse(ps);
    }

    public static native NodeList<Element> getElementsByClassName(String clazz, Node ctx) /*-{
        return ctx.getElementsByClassName(clazz);
    }-*/;

}
