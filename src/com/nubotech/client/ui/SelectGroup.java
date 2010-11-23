/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.ui;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jonnakkerud
 */
public class SelectGroup {
    private SelectLabel selected;

    List<SelectListener> listeners = new ArrayList<SelectListener>();;
    List<SelectLabel> labels = new ArrayList<SelectLabel>();

    public void onSelect(SelectLabel label) {
        for (SelectListener l : listeners) {
            l.onSelect(label);
        }
    }

    public SelectLabel getSelected() {
        return selected;
    }

    public void setSelected(SelectLabel label) {
        if (this.selected != null) {
            this.selected.unselect();
        }
        this.selected = label;
    }

    void addSelectLabel(SelectLabel l) {
        labels.add(l);
    }

    public void addSelectListener(SelectListener l) {
        listeners.add(l);
    }

    public void setSelectedValue(String selectedValue) {
        for (SelectLabel l : labels) {
            if (l.getValue().equals(selectedValue)) {
                l.select();
                break;
            }
        }

    }

    public String getSelectedValue() {
        String selected_value = null;
        if (selected != null) {
            selected_value = selected.getValue();
        }
        return selected_value;
    }

}
