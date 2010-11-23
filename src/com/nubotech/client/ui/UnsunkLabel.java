/*
 * Copyright 2007 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.nubotech.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

/**
 * A Label/HTML replacement that does not sink events. By default, the standard
 * Label and HTML widgets call sinkEvents() in their constructors. On the
 * iPhone, this causes the browser to render these elements with a dark
 * background and display an "action bubble" when the user performs a
 * tap-and-hold gesture on these elements. The UnsunkLabel exists to replace the
 * basic functions of Label and HTML without adding this unwanted UI behavior.
 */
public class UnsunkLabel extends Widget implements HasText, HasHTML {

    public UnsunkLabel() {
        setElement(DOM.createDiv());
    }

    public UnsunkLabel(String contents) {
        this();
        setText(contents);
    }

    public UnsunkLabel(String contents, boolean asHTML) {
        this();
        if (asHTML) {
            setHTML(contents);
        } else {
            setText(contents);
        }
    }

    public String getHTML() {
        return DOM.getInnerHTML(getElement());
    }

    public String getText() {
        return DOM.getInnerText(getElement());
    }

    public void setHTML(String html) {
        DOM.setInnerHTML(getElement(), html);
    }

    public void setText(String text) {
        DOM.setInnerText(getElement(), text);
    }
}
