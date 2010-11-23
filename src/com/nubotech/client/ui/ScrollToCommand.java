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

import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style;
import com.nubotech.client.LogUtil;
import com.nubotech.client.Utils;
import com.nubotech.client.ui.mobile.MobileSafariUtils;

/**
 * This Command is used to ensure that a Widget will be on-screen, or at least
 * the title-bar scrolled offscreen.  It is used by {@link View}.
 */
class ScrollToCommand implements ScheduledCommand {

    final int yPos;
    View view;

    public ScrollToCommand(View view) {
        this.view = view;
        Style style = view.header_panel.getContents().getElement().getStyle();
        String top_str = (style.getTop().length() == 0 ? "0" : style.getTop());
        yPos = (int) Utils.parseDouble(top_str);
    }

    public void execute() {
        if (Utils.isMobile()) {
            MobileSafariUtils.scrollToY(view.header_panel.getContents().getElement(), yPos);
        }
        else {
            nativeScrollTo(yPos);
        }
    }

    private native void nativeScrollTo(int yPos) /*-{
        //$wnd.scrollTo(0, this.@com.nubotech.client.ui.ScrollToCommand::yPos);
        $wnd.scrollTo(0, yPos);
    }-*/;
}