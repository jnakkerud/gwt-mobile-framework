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

package com.nubotech.client.ui.event;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.shared.EventHandler;
import java.util.Date;

/**
 * Common code for all touch events.
 *
 * @author amoffat Alex Moffat
 */
public abstract class TouchEvent<H extends EventHandler> extends DomEvent<H> {

    public JsArray<Touch> changedTouches() {
        return changedTouches(getNativeEvent());
    }

    private native JsArray<Touch> changedTouches(NativeEvent nativeEvent) /*-{
      return nativeEvent.changedTouches;
    }-*/;

    public JsArray<Touch> targetTouches() {
        return targetTouches(getNativeEvent());
    }

    private native JsArray<Touch> targetTouches(NativeEvent nativeEvent) /*-{
      return nativeEvent.targetTouches;
    }-*/;

    public JsArray<Touch> touches() {
        return touches(getNativeEvent());
    }

    private native JsArray<Touch> touches(NativeEvent nativeEvent) /*-{
      return nativeEvent.touches;
    }-*/;


    public Date timeStamp() {
        return timeStamp(getNativeEvent());
    }

    private native Date timeStamp(NativeEvent nativeEvent) /*-{
        return @com.nubotech.client.Utils::createDate(D)(nativeEvent.timeStamp);
    }-*/;

}
