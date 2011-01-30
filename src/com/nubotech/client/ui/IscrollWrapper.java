/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.ui;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadElement;
import com.google.gwt.dom.client.ScriptElement;
import com.nubotech.client.resources.Resources;

/**
 *
 * @author jonnakkerud
 */
public class IscrollWrapper {

    static HeadElement head;
    JavaScriptObject iscroll;

    private static IscrollWrapper instance = new IscrollWrapper();

    public static IscrollWrapper getInstance() {
        return instance;
    }

    private IscrollWrapper() {
        //inject(Resources.INSTANCE.iscroll().getText());
        iscroll = createJso("scroller");
    }

    void inject(String javascript) {
        HeadElement h = getHead();
        ScriptElement element = createScriptElement();
        element.setText(javascript);

        attachOnload(element, this, "scroller");

        h.appendChild(element);
    }

    private static ScriptElement createScriptElement() {
        ScriptElement script = Document.get().createScriptElement();
        script.setAttribute("language", "javascript");
        return script;
    }

    private static HeadElement getHead() {
        if (head == null) {
            Element element = Document.get().getElementsByTagName("head").getItem(0);
            assert element != null : "HTML Head element required";
            HeadElement h = HeadElement.as(element);
            head = h;
        }
        return head;
    }

    private static native JavaScriptObject createJso(String scroller_id) /*-{
        new $wnd.iScroll(scroller_id);
    }-*/;

    public static native void attachOnload(Element el, IscrollWrapper instance, String scroller_id) /*-{
        el.onload = function() {
          $wnd.console.log("attachOnLoad.loaded");
          instance.@com.nubotech.client.ui.IscrollWrapper::iscroll = new $wnd.iScroll(scroller_id);
          $wnd.console.log(instance.@com.nubotech.client.ui.IscrollWrapper::iscroll);
        }
    }-*/;


}
