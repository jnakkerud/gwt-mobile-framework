/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nubotech.client.ui;

import com.google.gwt.core.client.Scheduler;
import com.nubotech.client.ui.mobile.MobileSafariUtils;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ScrollEvent;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.History;
import com.nubotech.client.Utils;
import com.nubotech.client.resources.Resources;
import com.nubotech.client.ui.mobile.Screen;
import com.nubotech.client.ui.mobile.ScreenOrientation;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jonnakkerud
 */
public class ApplicationContainer extends Composite {
    public static final String APPLICATION_ID = "application_id";
    //public static final String CONTENT_ID = "content";
    //public static final String CONTAINER_ID = "container";
    //public static final String HEADER_ID = "header";
    //public static final String FOOTER_ID = "footer";

    private static ApplicationContainer instance;

    private FlowPanel app_container_panel;
    private Map<String, ViewHandler> handlers = new HashMap<String, ViewHandler>();

    private ApplicationContainer() {
        initWidget(app_container_panel = new FlowPanel());

        // set the id
        app_container_panel.getElement().setId(APPLICATION_ID);

        if (Utils.isMobile()) {

            // Disable flick events
            MobileSafariUtils.disableScrollOnBody();

            Window.addWindowScrollHandler(new Window.ScrollHandler() {
                public void onWindowScroll(ScrollEvent event) {
                    //$i.utils.addClass(document.body, "scrolled");
                    Element body = RootPanel.getBodyElement();
                    body.addClassName(Resources.INSTANCE.appearanceCss().scrolled());

                    if (current_view != null) {
                        MobileSafariUtils.scrollToY(current_view.header_panel.getContents().getElement(), 11);
                    }

                    // TODO set footer if needed
                    //var h = document.getElementById(”footer”);
                    // where 50 is the height of the panel
                    //var h = (window.innerHeight + window.pageYOffset) – 50;
                    //h.style.top = h.toString() + “px”;

                }
            });


            // orientation
            final Screen screen = new Screen();
            screen.addHandler(new Screen.OrientationChangedHandler() {
                public void orientationChanged(ScreenOrientation newOrientation) {
                    Element body = RootPanel.getBodyElement();
                    body.removeClassName(Resources.INSTANCE.appearanceCss().scrolled());
                }
            });

            MobileSafariUtils.hideURLBar();
            screen.addHandler(new Screen.OrientationChangedHandler() {
                public void orientationChanged(ScreenOrientation newOrientation) {
                    MobileSafariUtils.hideURLBar();
                }
            });

            screen.updateOrientation();
            screen.addHandler(new Screen.OrientationChangedHandler() {
                public void orientationChanged(ScreenOrientation newOrientation) {
                    screen.updateOrientation();
                }
            });

        }

    }

    public static ApplicationContainer get() {
        if (instance == null) {
            instance = new ApplicationContainer();
        }
        return instance;
    }

    public void add(View panel) {
        panel.getElement().setId(panel.getShortTitle());
        app_container_panel.add(panel);
    }

    public void remove(View panel) {
        app_container_panel.remove(panel);
    }

    public void handle(String token) {
        // lookup the handler for the token
        ViewHandler handler = getHandler(token);
        if (handler != null) {
            Scheduler.get().scheduleDeferred(handler);
        }
        else {
            // get the default
            handler = getHandler(token, true);
            History.newItem(handler.name);
        }
    }
    
    public ViewHandler getHandler(String token) {
        return getHandler(token, false);
    }

    protected ViewHandler getHandler(String token, boolean get_default) {
        ViewHandler handler = null;

        if (token != null) {
            handler = handlers.get(token);
        }

        if (get_default && handler == null) {
            // get the default handler
            for (ViewHandler h : handlers.values()) {
                if (h.is_default) {
                    handler = h;
                    break;
                }
            }
        }

        return handler;
    }

    public void registerHandler(ViewHandler handler) {
        handlers.put(handler.name, handler);
    }


    View current_view;
    public void setCurrent(final View view) {
        // attach the view to the dom, if needed
        if (app_container_panel.getWidgetIndex(view) == -1) {
            add(view);
        }

        // execute the transition and set the current view
        final Transition trans = Transition.getInstance(view.transition_type);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            public void execute() {
                trans.perform(view, current_view, false);
                if (current_view != null) {
                    current_view.onSetCurrent(false);
                }
                current_view = view;
                current_view.onSetCurrent(true);
                Scheduler.get().scheduleDeferred(new ScrollToCommand(view));
            }
        });
    }


}
