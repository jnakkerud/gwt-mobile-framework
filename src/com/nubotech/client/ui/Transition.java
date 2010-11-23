/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.ui;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.Widget;
import com.nubotech.client.Utils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jonnakkerud
 */
public abstract class Transition {

    public enum TransitionType {
        PAGE_SLIDE,
        PAGE_FLIP;
    }

    List<TransitionHandler> handlers = new ArrayList<TransitionHandler>();

    public static Transition getInstance(TransitionType type) {
        // TODO deferred binding
        if (type == TransitionType.PAGE_SLIDE) {
            if (Utils.isWebKit()) {
                //return new SlideTransition();
                return new SwapTransition();
            } else {
                return new DefaultTransition();
            }
        } else {
            return new FlipTransition();
        }
    }

    public abstract void perform(View newView, View oldView, boolean isReverse);


    public void addTransitionHandler(TransitionHandler handler) {
        handlers.add(handler);
    }

    protected void fireTransitionEnd() {
        for (TransitionHandler h : handlers) {
            h.onTransitionEnded();
        }
    }

    public static class DefaultTransition extends Transition {
        @Override
        public void perform(View newView, View oldView, boolean isReverse) {
            //DeferredCommand.addPause();
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                public void execute() {
                    fireTransitionEnd();
                }
            });
        }

    }
}
