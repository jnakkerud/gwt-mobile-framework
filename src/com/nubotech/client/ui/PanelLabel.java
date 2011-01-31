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

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.nubotech.client.resources.MobileResources;

/**
 * A widget that represents a single label. A label might represent a feed, and
 * entry in a feed, or some other named action.
 */
public class PanelLabel extends SimplePanel implements HasText {

    final Command primary;

    public PanelLabel(String text) {
        this(text, false, null);
    }

    public PanelLabel(String text, Command primary) {
        this(text, false, primary);
    }

    public PanelLabel(String text, boolean html) {
        this(text, html, null);
    }

    public PanelLabel(String text, boolean html, Command primary) {
        this(html ? new UnsunkLabel(text, true) : new UnsunkLabel(text), primary);
    }

    public PanelLabel(Widget widget, Command primary) {
        this.primary = primary;
        //this.widget = widget;

        setWidget(widget);
        addStyleName(MobileResources.INSTANCE.appearanceCss().PanelLabel());

        if (primary != null) {
            sinkEvents(Event.ONCLICK);
            addStyleName(MobileResources.INSTANCE.appearanceCss().hasCommand());
        }
    }

    public String getText() {
        if (getWidget() instanceof HasText) {
            return ((HasText) getWidget()).getText();
        } else {
            return getWidget().toString();
        }
    }

    @Override
    public void onBrowserEvent(Event e) {
        switch (DOM.eventGetType(e)) {
            case Event.ONCLICK: {
                addStyleName(MobileResources.INSTANCE.appearanceCss().clicked());
                primary.execute();
            }
        }
    }

    public void setText(String text) {
        if (getWidget() instanceof HasText) {
            ((HasText) getWidget()).setText(text);
        }
    }


}
