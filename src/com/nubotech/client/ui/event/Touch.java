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

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;

/**
 * Simple access to Touch objects.
 *
 * @author amoffat Alex Moffat
 */
public class Touch extends JavaScriptObject {

    protected Touch() {
    }

    public final int getRelativeX(Element target) {
        return getClientX() - target.getAbsoluteLeft() + target.getScrollLeft() +
                target.getOwnerDocument().getScrollLeft();
    }

    public final int getRelativeY(Element target) {
        return getClientY() - target.getAbsoluteTop() + target.getScrollTop() +
                target.getOwnerDocument().getScrollTop();
    }

    public final native int getClientX() /*-{
        return this.clientX;
    }-*/;

    public final native int getClientY() /*-{
        return this.clientY;
    }-*/;

    public final native Long identifier() /*-{
        return this.identifier;
    }-*/;

    public final native int pageX() /*-{
        return this.pageX;
    }-*/;

    public final native int pageY() /*-{
        return this.pageY;
    }-*/;

    public final native int screenX() /*-{
        return this.screenX;
    }-*/;

    public final native int screenY() /*-{
        return this.screenY;
    }-*/;

    public final native JavaScriptObject target() /*-{
        return this.target;
    }-*/;
}
