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

package com.google.gwt.user.client.impl;

import com.google.gwt.user.client.Element;

/**
 * Add handling for touch events to standard DOM impl used by Safari.
 *
 * @author amoffat Alex Moffat
 */
class DOMImplMobileSafari extends DOMImplSafari {

  public native int eventGetTypeInt(String eventType) /*-{
    switch (eventType) {
    case "blur": return 0x01000;
    case "change": return 0x00400;
    case "click": return 0x00001;
    case "dblclick": return 0x00002;
    case "focus": return 0x00800;
    case "keydown": return 0x00080;
    case "keypress": return 0x00100;
    case "keyup": return 0x00200;
    case "load": return 0x08000;
    case "losecapture": return 0x02000;
    case "mousedown": return 0x00004;
    case "mousemove": return 0x00040;
    case "mouseout": return 0x00020;
    case "mouseover": return 0x00010;
    case "mouseup": return 0x00008;
    case "scroll": return 0x04000;
    case "error": return 0x10000;
    case "mousewheel": return 0x20000;
    case "DOMMouseScroll": return 0x20000;
    case "contextmenu": return 0x40000;
    case "paste": return 0x80000;
    
    // Handle touch events.
    case "touchstart": return 0x100000;
    case "touchmove": return 0x200000;
    case "touchcancel": return 0x400000;
    case "touchend": return 0x800000;
    }
  }-*/;

  protected native void sinkEventsImpl(Element elem, int bits) /*-{
    var chMask = (elem.__eventBits || 0) ^ bits;
    elem.__eventBits = bits;
    if (!chMask) return;

    if (chMask & 0x00001) elem.onclick       = (bits & 0x00001) ?
        @com.google.gwt.user.client.impl.DOMImplStandard::dispatchEvent : null;
    if (chMask & 0x00002) elem.ondblclick    = (bits & 0x00002) ?
        @com.google.gwt.user.client.impl.DOMImplStandard::dispatchEvent : null;
    if (chMask & 0x00004) elem.onmousedown   = (bits & 0x00004) ?
        @com.google.gwt.user.client.impl.DOMImplStandard::dispatchEvent : null;
    if (chMask & 0x00008) elem.onmouseup     = (bits & 0x00008) ?
        @com.google.gwt.user.client.impl.DOMImplStandard::dispatchEvent : null;
    if (chMask & 0x00010) elem.onmouseover   = (bits & 0x00010) ?
        @com.google.gwt.user.client.impl.DOMImplStandard::dispatchEvent : null;
    if (chMask & 0x00020) elem.onmouseout    = (bits & 0x00020) ?
        @com.google.gwt.user.client.impl.DOMImplStandard::dispatchEvent : null;
    if (chMask & 0x00040) elem.onmousemove   = (bits & 0x00040) ?
        @com.google.gwt.user.client.impl.DOMImplStandard::dispatchEvent : null;
    if (chMask & 0x00080) elem.onkeydown     = (bits & 0x00080) ?
        @com.google.gwt.user.client.impl.DOMImplStandard::dispatchEvent : null;
    if (chMask & 0x00100) elem.onkeypress    = (bits & 0x00100) ?
        @com.google.gwt.user.client.impl.DOMImplStandard::dispatchEvent : null;
    if (chMask & 0x00200) elem.onkeyup       = (bits & 0x00200) ?
        @com.google.gwt.user.client.impl.DOMImplStandard::dispatchEvent : null;
    if (chMask & 0x00400) elem.onchange      = (bits & 0x00400) ?
        @com.google.gwt.user.client.impl.DOMImplStandard::dispatchEvent : null;
    if (chMask & 0x00800) elem.onfocus       = (bits & 0x00800) ?
        @com.google.gwt.user.client.impl.DOMImplStandard::dispatchEvent : null;
    if (chMask & 0x01000) elem.onblur        = (bits & 0x01000) ?
        @com.google.gwt.user.client.impl.DOMImplStandard::dispatchEvent : null;
    if (chMask & 0x02000) elem.onlosecapture = (bits & 0x02000) ?
        @com.google.gwt.user.client.impl.DOMImplStandard::dispatchEvent : null;
    if (chMask & 0x04000) elem.onscroll      = (bits & 0x04000) ?
        @com.google.gwt.user.client.impl.DOMImplStandard::dispatchEvent : null;
    if (chMask & 0x08000) elem.onload        = (bits & 0x08000) ?
        @com.google.gwt.user.client.impl.DOMImplStandard::dispatchEvent : null;
    if (chMask & 0x10000) elem.onerror       = (bits & 0x10000) ?
        @com.google.gwt.user.client.impl.DOMImplStandard::dispatchEvent : null;
    if (chMask & 0x20000) elem.onmousewheel  = (bits & 0x20000) ?
        @com.google.gwt.user.client.impl.DOMImplStandard::dispatchEvent : null;
    if (chMask & 0x40000) elem.oncontextmenu = (bits & 0x40000) ?
        @com.google.gwt.user.client.impl.DOMImplStandard::dispatchEvent : null;
    if (chMask & 0x80000) elem.onpaste       = (bits & 0x80000) ?
        @com.google.gwt.user.client.impl.DOMImplStandard::dispatchEvent : null;

    // Handle touch events.
    if (chMask & 0x100000) elem.ontouchstart = (bits & 0x100000) ?
        @com.google.gwt.user.client.impl.DOMImplStandard::dispatchEvent : null;
    if (chMask & 0x200000) elem.ontouchmove = (bits & 0x200000) ?
        @com.google.gwt.user.client.impl.DOMImplStandard::dispatchEvent : null;
    if (chMask & 0x400000) elem.ontouchcancel = (bits & 0x400000) ?
        @com.google.gwt.user.client.impl.DOMImplStandard::dispatchEvent : null;
    if (chMask & 0x800000) elem.ontouchend = (bits & 0x800000) ?
        @com.google.gwt.user.client.impl.DOMImplStandard::dispatchEvent : null;
  }-*/;
}
