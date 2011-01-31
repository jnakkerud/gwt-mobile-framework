/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.nubotech.client.resources.MobileResources;

/**
 *
 * @author jonnakkerud
 */
public class SingleField extends Composite {

    FlowPanel fp;
    Widget valueWidget;

    public SingleField(String name, String label, Widget valueWidget) {
        initWidget(fp = new FlowPanel());
        setStyleName(MobileResources.INSTANCE.appearanceCss().singleField());

        // create the label
        if (!(valueWidget instanceof ListBox)) {
            fp.add(new FormLabel(name, label));
        }

        // add the field widget
        valueWidget.getElement().setAttribute("name", name);
        fp.add(valueWidget);
    }

    class FormLabel extends Widget {
        public FormLabel(String name, String text) {
            setElement(DOM.createLabel());
            // label.for
            getElement().setAttribute("for", name);
            DOM.setInnerText(getElement(), text);
        }
    }
}
