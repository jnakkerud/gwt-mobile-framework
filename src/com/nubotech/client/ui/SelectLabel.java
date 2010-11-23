/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.nubotech.client.resources.Resources;

/**
 *
 * @author jonnakkerud
 */
public final class SelectLabel extends PanelLabel {
    SelectGroup grp;
    private String value;

    public SelectLabel(String text, SelectGroup grp) {
        this(text, true, false, grp);
    }

    public SelectLabel(String text, boolean selected, SelectGroup grp) {
        this(text, true, selected, grp);
    }

    public SelectLabel(String text, boolean html, boolean selected, SelectGroup grp) {
        super(text, html, null);
        addStyleName(Resources.INSTANCE.appearanceCss().selectLabel());

        this.grp = grp;
        this.grp.addSelectLabel(this);

        sinkEvents(Event.ONCLICK);
        if (selected) {
            select();
        }
    }

    @Override
    protected void onLoad() {
        super.onLoad();

        if (value != null && value.equals(grp.getSelectedValue())) {
            select();
        }
    }

    void select() {
        grp.setSelected(this);
        addStyleName(Resources.INSTANCE.appearanceCss().selected());
    }

    void unselect() {
        removeStyleName(Resources.INSTANCE.appearanceCss().selected());
    }

    @Override
    public void onBrowserEvent(Event e) {
        switch (DOM.eventGetType(e)) {
            case Event.ONCLICK:
                // remove selected label
                //removeSelected();
                //addStyleName(Resources.INSTANCE.appearanceCss().selected());
                select();
                grp.onSelect(this);

        }
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
}

