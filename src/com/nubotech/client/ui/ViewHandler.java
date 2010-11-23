/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.ui;

import com.google.gwt.core.client.Scheduler;

/**
 *
 * @author jonnakkerud
 */
public abstract class ViewHandler implements Scheduler.ScheduledCommand {

    protected boolean is_default;

    protected String name;

    public ViewHandler(String name) {
        this(name, false);
    }

    public ViewHandler(String name, boolean is_default) {
        this.name = name;
        this.is_default = is_default;
    }

}
