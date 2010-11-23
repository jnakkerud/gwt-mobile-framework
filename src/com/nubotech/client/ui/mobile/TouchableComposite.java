/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.ui.mobile;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.nubotech.client.ui.event.HasTouchHandlers;
import com.nubotech.client.ui.event.TouchCancelEvent;
import com.nubotech.client.ui.event.TouchCancelHandler;
import com.nubotech.client.ui.event.TouchEndEvent;
import com.nubotech.client.ui.event.TouchEndHandler;
import com.nubotech.client.ui.event.TouchMoveEvent;
import com.nubotech.client.ui.event.TouchMoveHandler;
import com.nubotech.client.ui.event.TouchStartEvent;
import com.nubotech.client.ui.event.TouchStartHandler;

/**
 *
 * @author jonnakkerud
 */
public class TouchableComposite extends Composite implements HasTouchHandlers {

    public TouchableComposite() {
        super();
    }

    public TouchableComposite(Widget w) {
        initWidget(w);
    }

    public HandlerRegistration addTouchStartHandler(TouchStartHandler handler) {
        return addDomHandler(handler, TouchStartEvent.getType());
    }

    public HandlerRegistration addTouchMoveHandler(TouchMoveHandler handler) {
        return addDomHandler(handler, TouchMoveEvent.getType());
    }

    public HandlerRegistration addTouchEndHandler(TouchEndHandler handler) {
        return addDomHandler(handler, TouchEndEvent.getType());
    }

    public HandlerRegistration addTouchCancelHandler(TouchCancelHandler handler) {
        return addDomHandler(handler, TouchCancelEvent.getType());
    }

}
