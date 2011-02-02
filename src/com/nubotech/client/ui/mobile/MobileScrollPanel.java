/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.ui.mobile;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.SimplePanel;
import com.nubotech.client.Utils;

/**
 *
 * @author jonnakkerud
 */
public class MobileScrollPanel extends SimplePanel {

    private IScroller scroller;
    private Element container;

    public MobileScrollPanel() {
        container = Document.get().createDivElement();
        getElement().appendChild(container);
        //getElement().getStyle().setOverflow(Overflow.AUTO);

        container.setClassName("scroller");
    }

    public void refresh() {
        if (scroller != null) {
            scroller.refresh();
        }
    }

    @Override
    protected void onLoad() {
        // Only turn on the touch-scroll implementation if we're on a touch device.
        if (Utils.isMobile()) {
            scroller = new IScroller(container);
        }
    }


    @Override
    protected com.google.gwt.user.client.Element getContainerElement() {
        return container.cast();
    }

}
