/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.http;

import com.google.gwt.core.client.JavaScriptObject;

/**
 *
 * @author jonnakkerud
 */
public interface JsonCallback {
    public void onSuccess(JavaScriptObject jso);
}