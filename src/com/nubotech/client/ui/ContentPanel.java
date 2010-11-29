/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.ui;

import com.google.gwt.core.client.Scheduler;
import com.nubotech.client.ui.mobile.TouchableFlowPanel;
import com.nubotech.client.ui.mobile.TouchableComposite;
import com.nubotech.client.ui.mobile.MobileSafariUtils;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.nubotech.client.LogUtil;
import com.nubotech.client.Utils;
import com.nubotech.client.ui.event.TouchEndEvent;
import com.nubotech.client.ui.event.TouchEndHandler;
import com.nubotech.client.ui.event.TouchMoveEvent;
import com.nubotech.client.ui.event.TouchMoveHandler;
import com.nubotech.client.ui.event.TouchStartEvent;
import com.nubotech.client.ui.event.TouchStartHandler;
import java.util.Date;

/**
 * TODO use deferred binding to load class
 *
 * @author jonnakkerud
 */
public class ContentPanel extends TouchableFlowPanel {

    TouchableComposite header;
    SimplePanel container;
    TouchableComposite footer;

    public void setHeader(Widget header) {
        this.header = new HeaderWrapper(header);
        add(this.header);
    }

    public void setFooter(Widget w_footer) {
        this.footer = new TouchableComposite(w_footer);
        this.footer.addStyleName("footer");
        add(footer);
    }

    public void setContainer(SimplePanel container) {
        add(container);
        container.addStyleName("container");
        container.getWidget().addStyleName("content");
        this.container = container;
    }

    public TouchableComposite getHeader() {
        return header;
    }

    public SimplePanel getContainer() {
        return container;
    }

    public TouchableComposite getContents() {
        return (TouchableComposite) container.getWidget();
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        if (Utils.isMobile()) {
            // set height of container for mobile

            int offset = header.getOffsetHeight() + (footer != null ? footer.getOffsetHeight() : 0);
            container.setHeight((Window.getClientHeight()-offset)+"px");

            //if (footer != null) {
            //    int h = Window.getClientHeight()-footer.getOffsetHeight();
            //    footer.getElement().getStyle().setTop(h, Style.Unit.PX);
            //}
            
            registerHandlers();
        }
        else {
            int offset = header.getOffsetHeight() + (footer != null ? footer.getOffsetHeight() : 0);

            container.setHeight((Window.getClientHeight()-offset)+"px");
            container.getElement().getStyle().setOverflow(Style.Overflow.AUTO);

            /*if (footer != null) {
                int h = Window.getClientHeight()-footer.getOffsetHeight();
                footer.getElement().getStyle().setTop(h, Style.Unit.PX);
            }*/

        }
    }

    @Override
    protected void onUnload() {
        super.onUnload();

        // TODO deregister listeners
    }

    private void registerHandlers() {
        Scheduler.get().scheduleDeferred(new HandlerRegistration());
    }

    class HandlerRegistration implements Scheduler.ScheduledCommand {

        boolean cancel;
        int startY;
        int endY;
        int oldY = -1;
        Date startTime;
        Date endTime;

        public void execute() {
            // header
            // Create an area to tap to return to top
            header.addTouchMoveHandler(new TouchMoveHandler() {
                public void onTouchMove(TouchMoveEvent event) {
                    // Cancel event if user drags finger off
                    cancel = true;
                }
            });
            header.addTouchEndHandler(new TouchEndHandler() {
                public void onTouchEnd(TouchEndEvent event) {
                    if (!cancel) {
                        MobileSafariUtils.scrollToY(getContents().getElement(), 0);
                    }

                    // Reset flag
                    cancel = false;
                }
            });

            final TouchableComposite contents_comp = getContents();
            contents_comp.addTouchStartHandler(new TouchStartHandler() {
                public void onTouchStart(TouchStartEvent event) {
                    startY = event.touches().get(0).getClientY();

                    startTime = event.timeStamp();
                }
            });
            contents_comp.addTouchMoveHandler(new TouchMoveHandler() {
                public void onTouchMove(TouchMoveEvent e) {

                    // Current Y-point
                    int posY = e.touches().get(0).pageY();

                    // Set previous position
                    oldY = (oldY > -1 ? oldY : posY);

                    // Set a top value (if none exists)
                    Element this_element = contents_comp.getElement();
                    //Style style = this_element.getStyle();

                    if (isNotAssigned(this_element)) {
                        MobileSafariUtils.setTranslateY(this_element, 0);
                    }

                    // Make sure we don't scroll past boundaries
                    Element container_element = container.getElement();
                    double value;
                    int boundary = container_element.getOffsetHeight() - this_element.getOffsetHeight();

                    // If current position is greater than old position
                    if (posY > oldY) {

                        value = MobileSafariUtils.getTranslateY(this_element) + (posY - oldY);


                        // If value is negative
                        if (value <= 0) {

                            // We're good
                            MobileSafariUtils.setTranslateY(this_element, value);


                            // Otherwise, we're over the limit
                        } else {

                            MobileSafariUtils.setTranslateY(this_element, (value * 0.9));
                        }

                        // If current position is less than old position
                    } else if (posY < oldY) {

                        value  = MobileSafariUtils.getTranslateY(this_element) - (oldY - posY);

                        // If value is greater than or equal to boundary
                        if (value >= boundary) {
                            MobileSafariUtils.setTranslateY(this_element, value);
                        }
                    }

                    // Done with function, current position is now old
                    oldY = posY;

                    // Prevent default action
                    e.preventDefault();
                }
            });
            contents_comp.addTouchEndHandler(new TouchEndHandler() {
                public void onTouchEnd(TouchEndEvent event) {

                    // no move, so ignore
                    if (oldY == -1) return;

                    // Log current Y-point
                    endY = event.changedTouches().get(0).getClientY();

                    // Log timestamp
                    endTime = event.timeStamp();

                    Element this_element = contents_comp.getElement();

                    // Log current Y offset
                    double posY = MobileSafariUtils.getTranslateY(this_element);

                    // If offset is greater than 0
                    if (posY > 0) {
                        // Scroll to 0
                        MobileSafariUtils.scrollToY(this_element, 0);
                    } else {

                        // Do all the math
                        int distance = startY - endY;
                        long time = endTime.getTime() - startTime.getTime();
                        long speed = Math.abs(distance / time);

                        double y = MobileSafariUtils.getTranslateY(this_element) - (distance * speed);

                        if ((time < 600) && distance > 50) {
                            // Flicks should go farther
                            y = y + (y * 0.2);
                        }

                        // Set boundary
                        Element container_element = container.getElement();
                        int boundary = (container_element.getOffsetHeight() - this_element.getOffsetHeight());

                        // Make sure y does not exceed boundaries
                        y = (y <= boundary) ? boundary : (y > 0) ? 0 : y;

                        // Scroll to specified point
                        MobileSafariUtils.scrollToY(this_element, (int) y);
                    }

                    // Clean up after ourselves
                    oldY = -1;
                }
            });
        }
    }

    private static native boolean isNotAssigned(Element element) /*-{
        return !element.style.webkitTransform;
    }-*/;

    class HeaderWrapper extends TouchableComposite {
        SimplePanel p;
        public HeaderWrapper(Widget wrapped) {
            initWidget(p = new SimplePanel());
            p.setWidget(wrapped);
            addStyleName("headerWrapper");
        }
    }

}
