/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.ui;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.nubotech.client.ui.mobile.TouchableFlowPanel;
import com.nubotech.client.ui.mobile.TouchableComposite;
import com.nubotech.client.ui.mobile.MobileSafariUtils;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.nubotech.client.LogUtil;
import com.nubotech.client.Utils;
import com.nubotech.client.resources.Resources;
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

            //container.setHeight("324px");
            LogUtil.log("header.h="+header.getOffsetHeight());
            LogUtil.log("header.h2="+header.getElement().getStyle().getHeight());
            //DOM.getStyleAttribute(header.getElement(), "height");
            container.setHeight((Window.getClientHeight()-header.getOffsetHeight())+"px");

            if (footer != null) {
                LogUtil.log("footer.h="+footer.getOffsetHeight());
                // float the footer
                int h = Window.getClientHeight()-footer.getOffsetHeight();
                footer.getElement().getStyle().setTop(h, Style.Unit.PX);
            }
            
            registerHandlers();
        }
        else {
            container.setHeight((Window.getClientHeight()-header.getOffsetHeight())+"px");
            container.getElement().getStyle().setOverflow(Style.Overflow.AUTO);

            //if (footer != null) {
                // float the footer
            //    int h = Window.getClientHeight()-footer.getOffsetHeight();
            //    footer.getElement().getStyle().setTop(h, Style.Unit.PX);
            //}

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
                    //LogUtil.log("onTouchMove.start");

                    // Current Y-point
                    int posY = e.touches().get(0).pageY();

                    // Set previous position
                    oldY = (oldY > -1 ? oldY : posY);

                    // Set a top value (if none exists)
                    Element this_element = contents_comp.getElement();
                    //Style style = this_element.getStyle();

                    // NEW
                    if (isNotAssigned(this_element)) {
                        MobileSafariUtils.setTranslateY(this_element, 0);
                    }

                    // TOREPLACE
                    //if (style.getTop() == null || style.getTop().length() == 0) {
                    //    style.setTop(0, Style.Unit.PX);
                    //}

                    // Make sure we don't scroll past boundaries
                    Element container_element = container.getElement();
                    double value;
                    int boundary = container_element.getOffsetHeight() - this_element.getOffsetHeight();

                    // If current position is greater than old position
                    if (posY > oldY) {

                        // TOREPLACE Value = current position + (Y-point - old position)
                        //double value_x = Utils.parseDouble(style.getTop()) + (posY - oldY);
                        
                        // NEW
                        value = MobileSafariUtils.getTranslateY(this_element) + (posY - oldY);

                        //LogUtil.log("value="+value+" value_x="+value_x);

                        // If value is negative
                        if (value <= 0) {

                            // We're good
                            // TOREPLACE this.style.top = value + "px";
                            //style.setTop(value, Style.Unit.PX);

                            // NEW
                            MobileSafariUtils.setTranslateY(this_element, value);


                            // Otherwise, we're over the limit
                        } else {

                            // TO REPLACE So mimic the 'snap' to top
                            //style.setTop(value * 0.9, Style.Unit.PX);

                            // NEW
                            MobileSafariUtils.setTranslateY(this_element, (value * 0.9));
                        }

                        // If current position is less than old position
                    } else if (posY < oldY) {

                        // TOREPLACE Value = current position - (old position - Y-point)
                        //double value_x = Utils.parseDouble(style.getTop()) - (oldY - posY);

                        // NEW
                        value  = MobileSafariUtils.getTranslateY(this_element) - (oldY - posY);

                        //LogUtil.log("value="+value+" value_x="+value_x);

                        // If value is greater than or equal to boundary
                        if (value >= boundary) {

                            // TOREPLACE We're good
                            //style.setTop(value, Style.Unit.PX);

                            // NEW
                            MobileSafariUtils.setTranslateY(this_element, value);
                        }
                    }

                    // Done with function, current position is now old
                    oldY = posY;

                    // Prevent default action
                    e.preventDefault();

                    //LogUtil.log("onTouchMove.end");
                }
            });
            contents_comp.addTouchEndHandler(new TouchEndHandler() {
                public void onTouchEnd(TouchEndEvent event) {
                    //LogUtil.log("onTouchEnd.start");

                    // no move, so ignore
                    if (oldY == -1) return;

                    // Log current Y-point
                    endY = event.changedTouches().get(0).getClientY();

                    // Log timestamp
                    endTime = event.timeStamp();

                    Element this_element = contents_comp.getElement();

                    // Log current Y offset
                    //Style style = this_element.getStyle();

                    // TOREPLACE
                    //double posY_x = Utils.parseDouble(style.getTop());

                    // NEW
                    double posY = MobileSafariUtils.getTranslateY(this_element);

                    //LogUtil.log("posY="+posY+",posY_x="+posY_x);

                    // If offset is greater than 0
                    if (posY > 0) {
                        // Scroll to 0
                        MobileSafariUtils.scrollToY(this_element, 0);
                    } else {

                        // Do all the math
                        int distance = startY - endY;
                        long time = endTime.getTime() - startTime.getTime();
                        long speed = Math.abs(distance / time);

                        // TOREPLACE y = current position - (distance * speed)
                        //double y_x = Utils.parseDouble(style.getTop()) - (distance * speed);

                        // NEW
                        double y = MobileSafariUtils.getTranslateY(this_element) - (distance * speed);
                        //LogUtil.log("y="+y+",y_x="+y_x);

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

                    //LogUtil.log("onTouchEnd.end");
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
