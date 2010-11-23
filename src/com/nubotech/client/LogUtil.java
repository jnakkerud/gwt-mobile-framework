/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client;

/**
 *
 * @author jonnakkerud
 */
public class LogUtil {

    public static void log(String message) {
        if (isFirebug()) {
            nativeFirebugLog(message);
        }
        else if (Utils.isWebKit()) {
            nativeSafariLog(message);
        }
    }

    public final static native boolean isFirebug() /*-{
        return (typeof $wnd.console != "undefined" && typeof $wnd.console.firebug != "undefined");
    }-*/;

    public final static native void nativeFirebugLog(String message) /*-{
        $wnd.console.log(message);
    }-*/;
    
    public final static native void nativeSafariLog(String message) /*-{
        $wnd.console.log(message);
    }-*/;
    
}
