/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.http;

import com.google.gwt.dom.client.Element;
import com.nubotech.client.Utils;

/**
 *
 * @author jonnakkerud
 */
public abstract class JsonpService {
    static int curIndex = 0; // Keeps the count of callbacks

    JsonpService() {
        curIndex++;
    }

    /*
     * Creates a function |callback| on the webpage which calls the function that
     * processes the JSON response
     */
    public native static void setup(JsonCallback cb, String callback) /*-{
      $wnd[callback] = function(data) {
        cb.@com.nubotech.client.http.JsonCallback::onSuccess(Lcom/google/gwt/core/client/JavaScriptObject;)(data);
        $wnd[callback + "done"] = true;
      }
    }-*/;

    public native static void cleanup(Element script, JsonCallback cb, String callback) /*-{
        // [4] Wait up to 5 seconds for the call to return.
        setTimeout(function() {
            if (!$wnd[callback + "done"]) {
                cb.@com.nubotech.client.http.JsonCallback::onSuccess(Lcom/google/gwt/core/client/JavaScriptObject;)(null);
            }

            // [5] Cleanup. Remove script and callback elements.
            $doc.body.removeChild(script);
            delete $wnd[callback];
            delete $wnd[callback + "done"];
        }, 5000);
    }-*/;

    public void executeQuery(JsonCallback cb) {
        // gen a unique callback name
        String callback = this.reserveCallback();

        // Generate a function in the page with the known name we just created
        setup(cb, callback);


        // Makes the request using OAuth, which calls our just created callback
        // and adds the response as a <script/> to the page
        Element script = Utils.createScript(generateRequestUrl(callback));

        // cleanup
        cleanup(script, cb, callback);
    }

    public abstract String generateRequestUrl(String callback);

    public String reserveCallback() {
        return "__gwt_callback" + curIndex++;
    }

}
