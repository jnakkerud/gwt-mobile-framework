/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.ui.mobile;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

/**
 *
 * @author jonnakkerud
 */
public class MobileSafariUtils {


    // prevent rubber-banding in mobile safari
    public static native void disableScrollOnBody() /*-{
        $doc.documentElement.addEventListener('touchmove',
                                             function(e) {
                                                  e.preventDefault();
                                              }
                                          );
    }-*/;

    public static void hideURLBar() {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            public void execute() {
                Window.scrollTo(0, 1);
            }
        });
    }

    public static native double getTranslateY(Element element) /*-{
        var transform = element.style.webkitTransform;
        if (transform && transform !== "") {
            var translateY = parseFloat((/translateY\((\-?.*)px\)/).exec(transform)[1]);
	}
	return translateY;
    }-*/;

    public static void setTranslateY(Element element, Number value) {
        setTransformProperty(element, "translateY(" + value + "px)");
    }

    public static native void setTransitionProperty(Element element, String property) /*-{
        element.style.webkitTransition = property;
    }-*/;

    public static native void setTransformProperty(Element element, String property) /*-{
        element.style.webkitTransform = property;
    }-*/;

    public static void scrollToY(final Element content, int y) {
        int ms = 350; // number of milliseconds

        double top = getTranslateY(content);

        // Convert negative to positive if need be
        double currentTop = (top < 0) ? -(top) : top;

        // Divide offset by 250 (more offset = more scroll time)
        double chunks = (currentTop / 100);

        // Calculate total time
        double totalTime = (ms * chunks);

        // Make sure time does not exceed 750ms
        totalTime = (totalTime > 750) ? 750 : totalTime;

        //content.style.webkitTransition = "-webkit-transform " + totalTime + "ms cubic-bezier(0.1, 0.25, 0.1, 1.0)";
        setTransitionProperty(content, "-webkit-transform " + totalTime + "ms cubic-bezier(0.1, 0.25, 0.1, 1.0)");

        setTranslateY(content, y);

        // Clean up after ourselves
        Timer t = new Timer() {
            @Override
            public void run() {
                setTransitionProperty(content, "none");
            }
        };

        // Schedule the timer to run once in 5 seconds.
        t.schedule((int) totalTime == 0 ? 1 : (int) totalTime);
    }

}
