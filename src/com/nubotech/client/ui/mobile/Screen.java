package com.nubotech.client.ui.mobile;

import java.util.HashMap;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.user.client.ui.RootPanel;

public class Screen {

    private ScreenOrientation mOrientation;
    private HashMap<String, OrientationChangedHandler> mHandlers = new HashMap<String, OrientationChangedHandler>();
    private int mCurrentHandlerId = 1;

    private native void registerOrientationChangedHandler(OrientationChangedDomEvent handler) /*-{
        var callback = function(){
            handler.@com.nubotech.client.ui.mobile.Screen$OrientationChangedDomEvent::onOrientationChanged()();
        }
        $wnd.addEventListener("orientationchange", callback, false);
    }-*/;

    private native boolean calculateIsPortraitOrientation() /*-{
        var result = false;
        if ($wnd.orientation != null && $wnd.orientation == 0) {
            result = true;
        }
        return result;
    }-*/;

    private ScreenOrientation calculateScreenOrientation() {
        return calculateIsPortraitOrientation() ? ScreenOrientation.Portrait
                : ScreenOrientation.Landscape;
    }

    interface OrientationChangedDomEvent {
        void onOrientationChanged();
    }

    public Screen() {
        mOrientation = calculateScreenOrientation();
        try {
            registerOrientationChangedHandler(new OrientationChangedDomEvent() {
                @Override
                public void onOrientationChanged() {
                    mOrientation = calculateScreenOrientation();

                    for (OrientationChangedHandler handler : mHandlers.values()) {
                        handler.orientationChanged(mOrientation);
                    }
                }
            });
        } catch (JavaScriptException e) {
            // ignore b/c it may not work on some browsers
            mOrientation = ScreenOrientation.Landscape;
        }

    }

    public ScreenOrientation getScreenOrientation() {
        return mOrientation;
    }

    public int getScreenWidth() {
        return (mOrientation == ScreenOrientation.Portrait) ? 320 : 480;
    }

    public String addHandler(OrientationChangedHandler handler) {
        int newHandlerIdValue = mCurrentHandlerId++;
        String newHandlerId = String.valueOf(newHandlerIdValue);
        mHandlers.put(newHandlerId, handler);
        return newHandlerId;
    }

    public void removeHandler(String handlerId) {
        mHandlers.remove(handlerId);
    }

    public interface OrientationChangedHandler {
        void orientationChanged(ScreenOrientation newOrientation);
    }

    public void updateOrientation() {
        switch(mOrientation) {
            case Landscape: {
                RootPanel.getBodyElement().setAttribute("orient", "landscape");
                break;
            }
            case Portrait: {
                RootPanel.getBodyElement().setAttribute("orient", "portrait");
                break;

            }
        }
    }

}
