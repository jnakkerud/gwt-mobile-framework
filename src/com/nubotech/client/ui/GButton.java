/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ButtonBase;
import com.nubotech.client.resources.Resources;

/**
 *
 * @author jonnakkerud
 *
 * Google style button
 */
public class GButton extends ButtonBase {
    public GButton(String text) {
        super(Document.get().createPushButtonElement());
        setStyleName(Resources.INSTANCE.appearanceCss().gButton());

        //<button type="button" class="btn"><span><span><b>&nbsp;</b><u>button</u></span></span></button>
        setHTML("<span><span><b>&nbsp;</b><u>" + text + "</u></span></span>");
    }
}
