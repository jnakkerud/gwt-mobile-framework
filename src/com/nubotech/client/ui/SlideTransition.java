/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.ui;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.nubotech.client.resources.Resources;

/**
 * TODO remove
 * @deprecated
 * @author jonnakkerud
 */
public class SlideTransition extends Transition {
    Widget newView;
    Widget oldView;
    int cnt = 0;

    JavaScriptObject oldCb;
    JavaScriptObject newCb;

    @Override
    public void perform(final View newView, final View oldView, boolean isReverse) {
        this.newView = newView;
        this.oldView = oldView;
        oldCb = registerDomTransitionEndedEvent(this,oldView.getElement());
        newCb = registerDomTransitionEndedEvent(this,newView.getElement());

        if (isReverse) {
            newView.removeStyleName(Resources.INSTANCE.appearanceCss().animate());
            newView.addStyleName(Resources.INSTANCE.appearanceCss().left());
            newView.removeStyleName(Resources.INSTANCE.appearanceCss().right());

            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                public void execute() {
                    oldView.addStyleName(Resources.INSTANCE.appearanceCss().animate());
                    oldView.removeStyleName(Resources.INSTANCE.appearanceCss().left());
                    oldView.addStyleName(Resources.INSTANCE.appearanceCss().right());

                    newView.addStyleName(Resources.INSTANCE.appearanceCss().animate());
                    newView.removeStyleName(Resources.INSTANCE.appearanceCss().left());
                    newView.removeStyleName(Resources.INSTANCE.appearanceCss().right());
                }
            });

        } else {
            newView.removeStyleName(Resources.INSTANCE.appearanceCss().animate());
            newView.removeStyleName(Resources.INSTANCE.appearanceCss().left());
            newView.addStyleName(Resources.INSTANCE.appearanceCss().right());

            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                public void execute() {
                    oldView.addStyleName(Resources.INSTANCE.appearanceCss().animate());
                    oldView.addStyleName(Resources.INSTANCE.appearanceCss().left());
                    oldView.removeStyleName(Resources.INSTANCE.appearanceCss().right());

                    newView.addStyleName(Resources.INSTANCE.appearanceCss().animate());
                    newView.removeStyleName(Resources.INSTANCE.appearanceCss().left());
                    newView.removeStyleName(Resources.INSTANCE.appearanceCss().right());
                }
            });
        }

    }

    public void onDomTransitionEnded() {
        if (cnt == 1) {
            cleanUp();
            fireTransitionEnd();
        }
        cnt++;
    }

    void cleanUp() {
        newView.removeStyleName(Resources.INSTANCE.appearanceCss().animate());
        oldView.removeStyleName(Resources.INSTANCE.appearanceCss().animate());

        removeDomTransitionEndedEvent(oldCb, oldView.getElement());
        removeDomTransitionEndedEvent(newCb, newView.getElement());

    }

    private native JavaScriptObject registerDomTransitionEndedEvent(SlideTransition instance, Element element) /*-{
        try {
            var callBack = function(e) {
                $wnd.console.log(e);
                instance.@com.nubotech.client.ui.SlideTransition::onDomTransitionEnded()();
            };

            element.addEventListener('webkitTransitionEnd', callBack, false);
            return callBack;
        }
        catch (err) {$wnd.console.log(err);}
        return null;
    }-*/;

    private native void removeDomTransitionEndedEvent(JavaScriptObject callBack, Element element) /*-{
        element.removeEventListener('webkitTransitionEnd', callBack, false);
    }-*/;

}

