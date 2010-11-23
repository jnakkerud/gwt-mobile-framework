/*
 * Copyright (c) 2010 Alex Moffat
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.nubotech.client.ui.mobile;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
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
 * Extension of composite to add touch handlers.
 *
 * @author amoffat Alex Moffat
 */
public class TouchableFlowPanel extends FlowPanel implements HasTouchHandlers {

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
