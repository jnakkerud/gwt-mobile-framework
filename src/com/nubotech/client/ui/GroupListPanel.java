/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.ui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.nubotech.client.Utils;
import com.nubotech.client.resources.MobileResources;
import java.util.ArrayList;
import java.util.List;

/**
 * http://developer.apple.com/safari/library/documentation/InternetWeb/Conceptual/iPhoneWebAppHIG/MetricsLayout/MetricsLayout.html#//apple_ref/doc/uid/TP40007900-CH6-SW23
 *
 * @author jonnakkerud
 */
public class GroupListPanel extends Composite {

    FlowPanel main;
    FlowPanel listPanel;

    public GroupListPanel() {
        this(null);
    }

    public GroupListPanel(String title) {
        initWidget(main = new FlowPanel());
        addStyleName(MobileResources.INSTANCE.appearanceCss().groupedPanel());

        if (title != null) {
            UnsunkLabel groupedTitle = new UnsunkLabel(title);
            groupedTitle.addStyleName(MobileResources.INSTANCE.appearanceCss().groupedTitle());
            main.add(groupedTitle);
        }

        listPanel = new FlowPanel();
        listPanel.addStyleName(MobileResources.INSTANCE.appearanceCss().groupedList());
        main.add(listPanel);        
    }

    public void add(PanelLabel label) {
        label.removeStyleName(MobileResources.INSTANCE.appearanceCss().PanelLabel());
        label.addStyleName(MobileResources.INSTANCE.appearanceCss().groupedLabel());
        if (listPanel.getWidgetCount() > 0) {
            label.addStyleName(MobileResources.INSTANCE.appearanceCss().ChildLabel());
        }
        listPanel.add(label);
    }

    public void add(String label, String value) {
        add(new PanelLabel(label+ " " + Utils.createSpan(value, "font-weight: normal;"), true));
    }

    public void add(String text) {
        add(new PanelLabel(Utils.createSpan(text, "font-weight: normal;"), true));
    }

    public void add(Toggle toggle) {
        toggle.addStyleName(MobileResources.INSTANCE.appearanceCss().groupedLabel());
        if (listPanel.getWidgetCount() > 0) {
            toggle.addStyleName(MobileResources.INSTANCE.appearanceCss().ChildLabel());
        }
        listPanel.add(toggle);
    }

    public void add(SingleField field) {
        if (listPanel.getWidgetCount() > 0) {
            field.addStyleName(MobileResources.INSTANCE.appearanceCss().ChildLabel());
        }
        listPanel.add(field);
    }

    public List<Widget> getWidgets() {
        List<Widget> l = new ArrayList<Widget>();
        for (int i = 0; i < listPanel.getWidgetCount(); i++) {
            l.add(listPanel.getWidget(i));
        }
        return l;
    }
}
